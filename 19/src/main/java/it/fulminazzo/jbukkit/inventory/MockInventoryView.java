package it.fulminazzo.jbukkit.inventory;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a mock implementation of {@link InventoryView}.
 */
@Getter
public class MockInventoryView extends InventoryView {
    private final @NotNull Inventory topInventory;
    private final @NotNull Player player;
    private final @NotNull String originalTitle;
    @Setter
    private @NotNull String title;

    /**
     * Instantiates a new Mock inventory view.
     *
     * @param topInventory the inventory displayed
     * @param player       the player viewing it
     */
    public MockInventoryView(final @NotNull Inventory topInventory, final @NotNull Player player,
                             final @NotNull String title) {
        this.topInventory = topInventory;
        this.player = player;
        this.title = title;
        this.originalTitle = title;
    }

    @Override
    public @NotNull Inventory getBottomInventory() {
        return this.player.getInventory();
    }

    @Override
    public @NotNull InventoryType getType() {
        return this.topInventory.getType();
    }

}