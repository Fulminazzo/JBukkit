package it.fulminazzo.jbukkit.utils;

import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Utility class for {@link Registry}.
 */
@NoArgsConstructor
public final class RegistryUtils {

    /**
     * Sets up the registries.
     */
    public static void setupRegistries() {
        when(Bukkit.getServer().getRegistry(any())).thenAnswer(a -> {
            Class<Keyed> clazz = a.getArgument(0);
            if (clazz == null) return null;
            else return getRegistry(clazz);
        });
    }

    /**
     * Gets the most appropriate registry from the given class.
     *
     * @param <T>   the type of the class
     * @param clazz the class
     * @return the registry
     */
    public static <T extends Keyed> Registry<T> getRegistry(final @NotNull Class<T> clazz) {

    }

}