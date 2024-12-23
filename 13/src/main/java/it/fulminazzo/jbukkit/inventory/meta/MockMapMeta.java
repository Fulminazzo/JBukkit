package it.fulminazzo.jbukkit.inventory.meta;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a mock implementation for {@link MapMeta}.
 */
@Getter
@Setter
public class MockMapMeta extends MockItemMeta implements MapMeta {
    private boolean scaling;
    private String locationName;
    private Color color;
    private Integer mapId;
    private MapView mapView;

    @Override
    public @NotNull MockMapMeta clone() {
        return (MockMapMeta) super.clone();
    }

    @Override
    public boolean hasMapId() {
        return this.mapId != null;
    }

    @Override
    public void setMapId(int id) {
        this.mapId = id;
    }

    @Override
    public int getMapId() {
        return this.mapId;
    }

    @Override
    public boolean hasMapView() {
        return this.mapView != null;
    }

    @Override
    public boolean hasLocationName() {
        return this.locationName != null;
    }

    @Override
    public boolean hasColor() {
        return this.color != null;
    }

}
