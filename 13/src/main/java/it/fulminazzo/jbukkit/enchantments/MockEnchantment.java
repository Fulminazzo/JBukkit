package it.fulminazzo.jbukkit.enchantments;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import it.fulminazzo.fulmicollection.utils.StringUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents an implementation of {@link Enchantment}.
 */
@Getter
public class MockEnchantment extends Enchantment {
    private static final MockEnchantment PROTECTION_ENVIRONMENTAL = new MockEnchantment("protection", 1, 4, EnchantmentTarget.ARMOR);
    private static final MockEnchantment PROTECTION_FIRE = new MockEnchantment("fire_protection", 1, 4, EnchantmentTarget.ARMOR);
    private static final MockEnchantment PROTECTION_EXPLOSIONS = new MockEnchantment("blast_protection", 1, 4, EnchantmentTarget.ARMOR);
    private static final MockEnchantment PROTECTION_PROJECTILE = new MockEnchantment("projectile_protection", 1, 4, EnchantmentTarget.ARMOR);
    private static final MockEnchantment PROTECTION_FALL = new MockEnchantment("feather_falling", 1, 4, EnchantmentTarget.ARMOR_FEET);
    private static final MockEnchantment OXYGEN = new MockEnchantment("respiration", 1, 3, EnchantmentTarget.ARMOR_HEAD);
    private static final MockEnchantment WATER_WORKER = new MockEnchantment("aqua_affinity", 1, 1, EnchantmentTarget.ARMOR_HEAD);
    private static final MockEnchantment THORNS = new MockEnchantment("thorns", 1, 3, EnchantmentTarget.ARMOR);
    private static final MockEnchantment DEPTH_STRIDER = new MockEnchantment("depth_strider", 1, 3, EnchantmentTarget.ARMOR_FEET);
    private static final MockEnchantment FROST_WALKER = new MockEnchantment("frost_walker", 1, 2, EnchantmentTarget.ARMOR_FEET);
    private static final MockEnchantment BINDING_CURSE = new MockEnchantment("binding_curse", 1, 1, EnchantmentTarget.WEARABLE);
    private static final MockEnchantment DAMAGE_ALL = new MockEnchantment("sharpness", 1, 5, EnchantmentTarget.WEAPON);
    private static final MockEnchantment DAMAGE_UNDEAD = new MockEnchantment("smite", 1, 5, EnchantmentTarget.WEAPON);
    private static final MockEnchantment DAMAGE_ARTHROPODS = new MockEnchantment("bane_of_arthropods", 1, 5, EnchantmentTarget.WEAPON);
    private static final MockEnchantment KNOCKBACK = new MockEnchantment("knockback", 1, 2, EnchantmentTarget.WEAPON);
    private static final MockEnchantment FIRE_ASPECT = new MockEnchantment("fire_aspect", 1, 2, EnchantmentTarget.WEAPON);
    private static final MockEnchantment LOOT_BONUS_MOBS = new MockEnchantment("looting", 1, 3, EnchantmentTarget.WEAPON);
    private static final MockEnchantment SWEEPING_EDGE = new MockEnchantment("sweeping", 1, 3, EnchantmentTarget.WEAPON);
    private static final MockEnchantment DIG_SPEED = new MockEnchantment("efficiency", 1, 5, EnchantmentTarget.TOOL);
    private static final MockEnchantment SILK_TOUCH = new MockEnchantment("silk_touch", 1, 1, EnchantmentTarget.TOOL);
    private static final MockEnchantment DURABILITY = new MockEnchantment("unbreaking", 1, 3, EnchantmentTarget.BREAKABLE);
    private static final MockEnchantment LOOT_BONUS_BLOCKS = new MockEnchantment("fortune", 1, 3, EnchantmentTarget.TOOL);
    private static final MockEnchantment ARROW_DAMAGE = new MockEnchantment("power", 1, 5, EnchantmentTarget.BOW);
    private static final MockEnchantment ARROW_KNOCKBACK = new MockEnchantment("punch", 1, 2, EnchantmentTarget.BOW);
    private static final MockEnchantment ARROW_FIRE = new MockEnchantment("flame", 1, 1, EnchantmentTarget.BOW);
    private static final MockEnchantment ARROW_INFINITE = new MockEnchantment("infinity", 1, 1, EnchantmentTarget.BOW);
    private static final MockEnchantment LUCK = new MockEnchantment("luck_of_the_sea", 1, 3, EnchantmentTarget.FISHING_ROD);
    private static final MockEnchantment LURE = new MockEnchantment("lure", 1, 3, EnchantmentTarget.FISHING_ROD);
    private static final MockEnchantment LOYALTY = new MockEnchantment("loyalty", 1, 3, EnchantmentTarget.TRIDENT);
    private static final MockEnchantment IMPALING = new MockEnchantment("impaling", 1, 5, EnchantmentTarget.TRIDENT);
    private static final MockEnchantment RIPTIDE = new MockEnchantment("riptide", 1, 3, EnchantmentTarget.TRIDENT);
    private static final MockEnchantment CHANNELING = new MockEnchantment("channeling", 1, 1, EnchantmentTarget.TRIDENT);
    private static final MockEnchantment MENDING = new MockEnchantment("mending", 1, 1, EnchantmentTarget.BREAKABLE);
    private static final MockEnchantment VANISHING_CURSE = new MockEnchantment("vanishing_curse", 1, 1, EnchantmentTarget.ALL);

    private final String name;
    private final int startLevel;
    private final int maxLevel;
    private final EnchantmentTarget itemTarget;
    private final Set<Enchantment> conflicts;
    private boolean treasure;
    private boolean cursed;

    /**
     * Instantiates a new Mock enchantment.
     * It will copy each value from the given enchantment.
     *
     * @param enchantment the enchantment
     */
    public MockEnchantment(final @NotNull Enchantment enchantment) {
        this(enchantment.getKey(), enchantment.getName(), enchantment.getStartLevel(),
                enchantment.getMaxLevel(), enchantment.getItemTarget());
        setTreasure(enchantment.isTreasure()).setCursed(enchantment.isCursed());
    }

    /**
     * Instantiates a new Mock enchantment.
     *
     * @param name       the name
     * @param startLevel the start level
     * @param maxLevel   the max level
     * @param itemTarget the item target
     */
    public MockEnchantment(final @NotNull String name, final int startLevel,
                           final int maxLevel, final @NotNull EnchantmentTarget itemTarget) {
        this(NamespacedKey.minecraft(StringUtils.decapitalize(name).toLowerCase()), name, startLevel, maxLevel, itemTarget);
    }

    /**
     * Instantiates a new Mock enchantment.
     *
     * @param key        the key
     * @param name       the name
     * @param startLevel the start level
     * @param maxLevel   the max level
     * @param itemTarget the item target
     */
    public MockEnchantment(final @NotNull NamespacedKey key, final @NotNull String name, final int startLevel,
                           final int maxLevel, final @NotNull EnchantmentTarget itemTarget) {
        super(key);
        this.name = name;
        this.startLevel = startLevel;
        this.maxLevel = maxLevel;
        this.itemTarget = itemTarget;
        this.conflicts = new HashSet<>();
    }

    /**
     * Flags this enchantment as a treasure.
     * <br>
     * Treasure enchantments can only be received via looting, trading, or
     * fishing.
     *
     * @param treasure the flag
     * @return this enchantment
     */
    public @NotNull MockEnchantment setTreasure(boolean treasure) {
        this.treasure = treasure;
        return this;
    }

    /**
     * Flags this enchantment as cursed.
     * <br>
     * Cursed enchantments are found the same way treasure enchantments are.
     *
     * @param cursed the flag
     * @return this enchantment
     */
    public @NotNull MockEnchantment setCursed(boolean cursed) {
        this.cursed = cursed;
        return this;
    }

    /**
     * Explicitly declare the enchantments with which this enchantment will conflict.
     *
     * @param conflicts the enchantments
     * @return this enchantment
     */
    public @NotNull MockEnchantment conflictsWith(final Enchantment @NotNull ... conflicts) {
        this.conflicts.addAll(Arrays.asList(conflicts));
        return this;
    }

    @Override
    public boolean conflictsWith(final @NotNull Enchantment other) {
        return this.conflicts.contains(other);
    }

    @Override
    public boolean canEnchantItem(final @NotNull ItemStack item) {
        return this.itemTarget.includes(item);
    }

    /**
     * Sets up the default vanilla enchantments by checking the {@link Enchantment} static fields.
     */
    @SneakyThrows
    public static void setupEnchantments() {
        Refl<Class<Enchantment>> enchantmentClass = new Refl<>(Enchantment.class);
        Map<NamespacedKey, Enchantment> byKey = enchantmentClass.getFieldObject("byKey");
        Map<String, Enchantment> byName = enchantmentClass.getFieldObject("byName");
        byKey.clear();
        byName.clear();
        for (Field field : enchantmentClass.getStaticFields())
            if (field.getType().equals(Enchantment.class)) {
                Enchantment enchantment = (Enchantment) ReflectionUtils.setAccessibleOrThrow(field)
                        .get(Enchantment.class);
                MockEnchantment mock = new MockEnchantment(enchantment);
                field.set(Enchantment.class, mock);
                byKey.put(mock.getKey(), enchantment);
                byName.put(mock.getName(), enchantment);
            }
    }

}
