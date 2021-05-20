package eu.pb4.holograms.interfaces;

import eu.pb4.holograms.api.holograms.EntityHologram;

import java.util.Set;

public interface EntityHologramHolder {
    void addEntityHologram(EntityHologram hologram);
    void removeEntityHologram(EntityHologram hologram);
    Set<EntityHologram> getEntityHologramSet();
}
