package it.fulminazzo.jbukkit.inventory.meta;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class MockPotionMeta extends MockItemMeta implements PotionMeta {
    private final Set<PotionEffect> customEffects;
    private PotionEffectType mainEffect;

    public MockPotionMeta() {
        this.customEffects = new HashSet<>();
    }

    @Override
    public boolean hasCustomEffects() {
        return !this.customEffects.isEmpty();
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        return new ArrayList<>(this.customEffects);
    }

    @Override
    public boolean addCustomEffect(PotionEffect effect, boolean overwrite) {
        if (hasCustomEffect(effect.getType()) && !overwrite) return false;
        return this.customEffects.add(effect);
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType type) {
        return this.customEffects.remove(type);
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        return this.customEffects.stream().anyMatch(a -> a.getType().equals(type));
    }

    @Override
    public boolean clearCustomEffects() {
        if (hasCustomEffects()) return false;
        this.customEffects.clear();
        return true;
    }

    @Override
    public MockPotionMeta clone() {
        return (MockPotionMeta) super.clone();
    }

}
