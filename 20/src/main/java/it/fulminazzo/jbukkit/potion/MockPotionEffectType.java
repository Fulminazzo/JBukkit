package it.fulminazzo.jbukkit.potion;

import com.google.common.collect.BiMap;
import it.fulminazzo.fulmicollection.objects.Printable;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.StringUtils;
import it.fulminazzo.jbukkit.NotImplementedException;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

import static org.bukkit.potion.PotionEffectTypeCategory.*;

/**
 * Represents an implementation of {@link PotionEffectType}.
 */
@Getter
public class MockPotionEffectType extends PotionEffectType {
    private static final Supplier<MockPotionEffectType> SPEED = () -> new MockPotionEffectType("Speed", BENEFICIAL, "33EBFF", false);
    private static final Supplier<MockPotionEffectType> SLOWNESS = () -> new MockPotionEffectType("Slowness", HARMFUL, "5A6C81", false);
    private static final Supplier<MockPotionEffectType> HASTE = () -> new MockPotionEffectType("Haste", BENEFICIAL, "D9C043", false);
    private static final Supplier<MockPotionEffectType> MINING_FATIGUE = () -> new MockPotionEffectType("Mining Fatigue", HARMFUL, "4A4217", false);
    private static final Supplier<MockPotionEffectType> STRENGTH = () -> new MockPotionEffectType("Strength", BENEFICIAL, "FCC500", false);
    private static final Supplier<MockPotionEffectType> INSTANT_HEALTH = () -> new MockPotionEffectType("Instant Health", BENEFICIAL, "F82423", true);
    private static final Supplier<MockPotionEffectType> INSTANT_DAMAGE = () -> new MockPotionEffectType("Instant Damage", HARMFUL, "430A09", true);
    private static final Supplier<MockPotionEffectType> JUMP_BOOST = () -> new MockPotionEffectType("Jump Boost", BENEFICIAL, "23FC4D", false);
    private static final Supplier<MockPotionEffectType> NAUSEA = () -> new MockPotionEffectType("Nausea", HARMFUL, "551D4A", false);
    private static final Supplier<MockPotionEffectType> REGENERATION = () -> new MockPotionEffectType("Regeneration", BENEFICIAL, "CD5CAB", false);
    private static final Supplier<MockPotionEffectType> RESISTANCE = () -> new MockPotionEffectType("Resistance", BENEFICIAL, "8F45ED", false);
    private static final Supplier<MockPotionEffectType> FIRE_RESISTANCE = () -> new MockPotionEffectType("Fire Resistance", BENEFICIAL, "E49A3A", false);
    private static final Supplier<MockPotionEffectType> WATER_BREATHING = () -> new MockPotionEffectType("Water Breathing", BENEFICIAL, "96D7BE", false);
    private static final Supplier<MockPotionEffectType> INVISIBILITY = () -> new MockPotionEffectType("Invisibility", BENEFICIAL, "7F8392", false);
    private static final Supplier<MockPotionEffectType> BLINDNESS = () -> new MockPotionEffectType("Blindness", HARMFUL, "1F1F23", false);
    private static final Supplier<MockPotionEffectType> NIGHT_VISION = () -> new MockPotionEffectType("Night Vision", BENEFICIAL, "1F1FA1", false);
    private static final Supplier<MockPotionEffectType> HUNGER = () -> new MockPotionEffectType("Hunger", HARMFUL, "587653", false);
    private static final Supplier<MockPotionEffectType> WEAKNESS = () -> new MockPotionEffectType("Weakness", BENEFICIAL, "484D48", false);
    private static final Supplier<MockPotionEffectType> POISON = () -> new MockPotionEffectType("Poison", HARMFUL, "4E9331", false);
    private static final Supplier<MockPotionEffectType> WITHER = () -> new MockPotionEffectType("Wither", HARMFUL, "352A27", false);
    private static final Supplier<MockPotionEffectType> HEALTH_BOOST = () -> new MockPotionEffectType("Health Boost", BENEFICIAL, "F87D23", false);
    private static final Supplier<MockPotionEffectType> ABSORPTION = () -> new MockPotionEffectType("Absorption", BENEFICIAL, "2552A5", false);
    private static final Supplier<MockPotionEffectType> SATURATION = () -> new MockPotionEffectType("Saturation", BENEFICIAL, "F82421", false);
    private static final Supplier<MockPotionEffectType> GLOWING = () -> new MockPotionEffectType("Glowing", NEUTRAL, "94A061", false);
    private static final Supplier<MockPotionEffectType> LEVITATION = () -> new MockPotionEffectType("Levitation", HARMFUL, "CEFFFF", false);
    private static final Supplier<MockPotionEffectType> LUCK = () -> new MockPotionEffectType("Luck", BENEFICIAL, "339900", false);
    private static final Supplier<MockPotionEffectType> UNLUCK = () -> {
        MockPotionEffectType unluck = new MockPotionEffectType("Bad Luck", HARMFUL, "C0A44D", false);
        new Refl<>(unluck).setFieldObject("key", NamespacedKey.minecraft("unluck"));
        return unluck;
    };
    private static final Supplier<MockPotionEffectType> SLOW_FALLING = () -> new MockPotionEffectType("Slow Falling", BENEFICIAL, "FFEFD1", false);
    private static final Supplier<MockPotionEffectType> CONDUIT_POWER = () -> new MockPotionEffectType("Conduit Power", BENEFICIAL, "1DC2D1", false);
    private static final Supplier<MockPotionEffectType> DOLPHINS_GRACE = () -> new MockPotionEffectType("Dolphins Grace", BENEFICIAL, "88A3BE", false);
    private static final Supplier<MockPotionEffectType> BAD_OMEN = () -> new MockPotionEffectType("Bad Omen", NEUTRAL, "0B6138", false);
    private static final Supplier<MockPotionEffectType> HERO_OF_THE_VILLAGE = () -> new MockPotionEffectType("Hero of the Village", BENEFICIAL, "44FF44", false);
    private static final Supplier<MockPotionEffectType> DARKNESS = () -> new MockPotionEffectType("Darkness", HARMFUL, "292721", false);
    private static final Supplier<MockPotionEffectType> TRIAL_OMEN = () -> new MockPotionEffectType("Trial Omen", HARMFUL, "000000", false);
    private static final Supplier<MockPotionEffectType> RAID_OMEN = () -> new MockPotionEffectType("Raid Omen", HARMFUL, "000000", false);
    private static final Supplier<MockPotionEffectType> WIND_CHARGED = () -> new MockPotionEffectType("Wind Charged", HARMFUL, "000000", false);
    private static final Supplier<MockPotionEffectType> WEAVING = () -> new MockPotionEffectType("Weaving", HARMFUL, "000000", false);
    private static final Supplier<MockPotionEffectType> OOZING = () -> new MockPotionEffectType("Oozing", HARMFUL, "000000", false);
    private static final Supplier<MockPotionEffectType> INFESTED = () -> new MockPotionEffectType("Infested", HARMFUL, "000000", false);

    private static int LAST_USED_ID = 1;
    private final int id;
    private final @NotNull NamespacedKey key;
    private final @NotNull String name;
    private final @NotNull PotionEffectTypeCategory category;
    private final @NotNull Color color;
    private final boolean instant;

    /**
     * Instantiates a new Mock potion effect type.
     *
     * @param name     the name
     * @param category the category
     * @param color    the color
     * @param instant  true if its effects are applied instantly
     */
    public MockPotionEffectType(final @NotNull String name,
                                final @NotNull PotionEffectTypeCategory category,
                                final @NotNull String color, final boolean instant) {
        this(name, category, color(color), instant);
    }

    /**
     * Instantiates a new Mock potion effect type.
     *
     * @param name     the name
     * @param category the category
     * @param color    the color
     * @param instant  true if its effects are applied instantly
     */
    public MockPotionEffectType(final @NotNull String name,
                                final @NotNull PotionEffectTypeCategory category,
                                final @NotNull Color color, final boolean instant) {
        this.id = LAST_USED_ID++;
        this.name = Objects.requireNonNull(name);
        this.key = NamespacedKey.minecraft(StringUtils.decapitalize(name).toLowerCase());
        this.category = category;
        this.color = color;
        this.instant = instant;
        // Set to internal map
        BiMap<Integer, PotionEffectType> idMap = new Refl<>(PotionEffectType.class).getFieldObject("ID_MAP");
        Objects.requireNonNull(idMap).put(this.id, this);
    }

    @Override
    public @NotNull PotionEffect createEffect(int duration, int amplifier) {
        return new PotionEffect(this, duration, amplifier);
    }

    @Override
    public double getDurationModifier() {
        return 1.0;
    }

    @Override
    public @NotNull String getTranslationKey() {
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        return Printable.convertToJson(this);
    }

    /**
     * Gets all the vanilla {@link MockPotionEffectType}s.
     *
     * @return the mock potion effect types
     */
    public static MockPotionEffectType @NotNull [] values() {
        Refl<?> clazz = new Refl<>(MockPotionEffectType.class);
        return clazz.getStaticFields().stream()
                .filter(f -> f.getType().equals(MockPotionEffectType.class))
                .map(clazz::getFieldObject)
                .map(o -> (MockPotionEffectType) o)
                .toArray(MockPotionEffectType[]::new);
    }

    private static @NotNull Color color(final @NotNull String color) {
        if (color.length() != 6) throw new IllegalArgumentException("color must have 6 characters");
        String red = color.substring(0, 2);
        String green = color.substring(2, 4);
        String blue = color.substring(4, 6);
        return Color.fromRGB(Integer.parseInt(red, 16), Integer.parseInt(green, 16), Integer.parseInt(blue, 16));
    }

}
