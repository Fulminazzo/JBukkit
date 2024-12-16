package it.fulminazzo.jbukkit;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.jbukkit.annotations.After1_;
import it.fulminazzo.jbukkit.annotations.Before1_;
import it.fulminazzo.jbukkit.enchantments.MockEnchantment;
import it.fulminazzo.jbukkit.inventory.MockInventory;
import it.fulminazzo.jbukkit.inventory.MockItemFactory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BukkitUtils {
    private static final Logger LOGGER = Logger.getLogger("Bukkit");
    private static final List<Player> PLAYERS = new LinkedList<>();
    private static final List<OfflinePlayer> OFFLINE_PLAYERS = new LinkedList<>();
    private static final List<Recipe> RECIPES = new LinkedList<>();
    private static final String VERSION_FORMAT = "1\\.(\\d+)(?:\\.(\\d+))?-R\\d+\\.\\d+-SNAPSHOT";
    private static final String DEFAULT_VERSION = "1.20.4-R0.1-SNAPSHOT";
    private static final Map<Class<?>, Object> REGISTRIES = new HashMap<>();
    static final String VERSION_NAME = "MINECRAFT_VERSION";
    @Getter
    static double numericalVersion;

    @BeforeEach
    protected void setUp() {
        setupVersion();
        check();
    }

    public static void setupVersion() {
        String version = System.getenv(VERSION_NAME);
        if (version == null) {
            LOGGER.warning(String.format("Could not find set version '%s'. Defaulting to value '%s'", VERSION_NAME, DEFAULT_VERSION));
            version = DEFAULT_VERSION;
        }
        Matcher matcher = Pattern.compile(VERSION_FORMAT).matcher(version);
        if (!matcher.matches())
            throw new IllegalArgumentException(String.format("Version '%s' did not match format '%s'", version, VERSION_FORMAT));
        String patch = matcher.group(2);
        if (patch == null) patch = "0";
        numericalVersion = Double.parseDouble(matcher.group(1) + "." + patch);
        LOGGER.info(String.format("Using version '1.%s'", numericalVersion));
    }

    public static void setupServer() {
        setupVersion();
        Server server = mock(Server.class);
        if (Arrays.stream(server.getClass().getDeclaredMethods()).anyMatch(m -> m.getName().equals("getRecipe")))
            when(server.getRecipe(any())).thenAnswer(r -> {
                Object key = r.getArgument(0);
                Iterator<Recipe> iterator = Bukkit.recipeIterator();
                while (iterator.hasNext()) {
                    Recipe recipe = iterator.next();
                    if (Objects.equals(new Refl<>(recipe).invokeMethod("getKey"), key)) return recipe;
                }
                return null;
            });
        when(server.getLogger()).thenReturn(LOGGER);
        when(server.getBukkitVersion()).thenReturn(String.format("1.%s-R0.1-SNAPSHOT", numericalVersion));
        when(server.addRecipe(any())).thenAnswer(r -> RECIPES.add(r.getArgument(0)));
        when(server.recipeIterator()).thenAnswer(r -> RECIPES.iterator());
        when(server.getItemFactory()).thenReturn(new MockItemFactory());
        // Inventories
        when(server.createInventory(any(), anyInt())).thenAnswer(a -> {
            MockInventory inventory = new MockInventory(a.getArgument(1));
            inventory.setHolder(a.getArgument(0));
            return inventory;
        });
        when(server.createInventory(any(), any(InventoryType.class))).thenAnswer(a -> {
            InventoryType type = a.getArgument(1);
            MockInventory inventory = new MockInventory(type.getDefaultSize());
            inventory.setHolder(a.getArgument(0));
            return inventory;
        });
        when(server.createInventory(any(), anyInt(), anyString())).thenAnswer(a -> {
            MockInventory inventory = new MockInventory(a.getArgument(1));
            inventory.setTitle(a.getArgument(2));
            inventory.setHolder(a.getArgument(0));
            return inventory;
        });
        when(server.createInventory(any(), any(InventoryType.class), anyString())).thenAnswer(a -> {
            InventoryType type = a.getArgument(1);
            MockInventory inventory = new MockInventory(type.getDefaultSize());
            inventory.setTitle(a.getArgument(2));
            inventory.setHolder(a.getArgument(0));
            return inventory;
        });
        // Players
        when(server.getPlayer(any(UUID.class))).thenAnswer(a -> getPlayer((UUID) a.getArgument(0)));
        when(server.getPlayer(any(String.class))).thenAnswer(a -> getPlayer((String) a.getArgument(0)));
        when(server.getOnlinePlayers()).thenAnswer(a -> PLAYERS);
        when(server.getOfflinePlayer(any(UUID.class))).thenAnswer(a -> getOfflinePlayer((UUID) a.getArgument(0)));
        when(server.getOfflinePlayer(any(String.class))).thenAnswer(a -> getOfflinePlayer((String) a.getArgument(0)));
        when(server.getOfflinePlayers()).thenAnswer(a -> OFFLINE_PLAYERS.toArray(new OfflinePlayer[0]));
        // Registries
        try {
            when(new Refl<>(server).invokeMethod("getRegistry", any(Class.class))).thenAnswer(a -> {
                Class<?> clazz = a.getArgument(0);
                return REGISTRIES.computeIfAbsent(clazz, c ->
                        mock(ReflectionUtils.getClass("org.bukkit.Registry")));
            });
        } catch (IllegalArgumentException ignored) {
            // Older versions
        }
        new Refl<>(Bukkit.class).setFieldObject("server", server);
    }

    public static void setupEnchantments() {
        MockEnchantment.setupEnchantments();
    }

    /**
     * Allows to check the current method or class.
     * If they are annotated with {@link After1_} or with {@link Before1_},
     * and {@link #numericalVersion} does not match, then it is skipped.
     */
    public static void check() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        // The first should be getStackTrace, the second this method.
        // The third is the actual method of interest.
        StackTraceElement actualTrace = trace[2];
        // If BukkitUtils is being extended by another class
        if (actualTrace.getClassName().equals(BukkitUtils.class.getCanonicalName()))
            actualTrace = trace[3];

        final Class<?> clazz = ReflectionUtils.getClass(actualTrace.getClassName());
        check(clazz);

        final String methodName = actualTrace.getMethodName();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods)
            if (method.getName().equals(methodName)) check(method);
    }

    public static @Nullable Player getPlayer(final @NotNull UUID uuid) {
        return PLAYERS.stream().filter(p -> p.getUniqueId().equals(uuid)).findAny().orElse(null);
    }

    public static @Nullable Player getPlayer(final @NotNull String name) {
        return PLAYERS.stream().filter(p -> p.getName().equals(name)).findAny().orElse(null);
    }

    public static @NotNull Player addPlayer(final @NotNull UUID uuid, final @NotNull String name) {
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(uuid);
        when(player.getName()).thenReturn(name);
        PLAYERS.add(player);
        return player;
    }

    public static @Nullable Player removePlayer(final @NotNull Player player) {
        return removePlayer(player.getUniqueId());
    }

    public static @Nullable Player removePlayer(final @NotNull String uuid) {
        Player player = getPlayer(uuid);
        if (player != null) PLAYERS.remove(player);
        return player;
    }

    public static @Nullable Player removePlayer(final @NotNull UUID uuid) {
        Player player = getPlayer(uuid);
        if (player != null) PLAYERS.remove(player);
        return player;
    }

    public static @Nullable OfflinePlayer getOfflinePlayer(final @NotNull UUID uuid) {
        return OFFLINE_PLAYERS.stream().filter(p -> p.getUniqueId().equals(uuid)).findAny().orElse(null);
    }

    public static @Nullable OfflinePlayer getOfflinePlayer(final @NotNull String name) {
        return OFFLINE_PLAYERS.stream().filter(p -> p.getName().equals(name)).findAny().orElse(null);
    }

    public static @NotNull OfflinePlayer addOfflinePlayer(final @NotNull UUID uuid, final @NotNull String name) {
        OfflinePlayer offlinePlayer = mock(OfflinePlayer.class);
        when(offlinePlayer.getUniqueId()).thenReturn(uuid);
        when(offlinePlayer.getName()).thenReturn(name);
        OFFLINE_PLAYERS.add(offlinePlayer);
        return offlinePlayer;
    }

    public static @Nullable OfflinePlayer removeOfflinePlayer(final @NotNull OfflinePlayer offlinePlayer) {
        return removeOfflinePlayer(offlinePlayer.getUniqueId());
    }

    public static @Nullable OfflinePlayer removeOfflinePlayer(final @NotNull String uuid) {
        OfflinePlayer offlinePlayer = getOfflinePlayer(uuid);
        if (offlinePlayer != null) OFFLINE_PLAYERS.remove(offlinePlayer);
        return offlinePlayer;
    }

    public static @Nullable OfflinePlayer removeOfflinePlayer(final @NotNull UUID uuid) {
        OfflinePlayer offlinePlayer = getOfflinePlayer(uuid);
        if (offlinePlayer != null) OFFLINE_PLAYERS.remove(offlinePlayer);
        return offlinePlayer;
    }

    private static void check(AnnotatedElement element) {
        if (element.isAnnotationPresent(Before1_.class)) {
            double value = element.getAnnotation(Before1_.class).value();
            final String message = String.format("Skipping tests because of version higher than 1.%s", value);
            if (numericalVersion > value) {
                LOGGER.info(message);
                assumeTrue(numericalVersion <= value, message);
            }
        }
        if (element.isAnnotationPresent(After1_.class)) {
            double value = element.getAnnotation(After1_.class).value();
            final String message = String.format("Skipping tests because of version lower than 1.%s", value);
            if (numericalVersion < value) {
                LOGGER.info(message);
                assumeTrue(numericalVersion >= value, message);
            }
        }
    }
}