package eu.pb4.holograms.impl.interfaces;

import eu.pb4.holograms.api.holograms.EntityHologram;

import java.util.Set;

public interface EntityHologramHolder {
    void hologramApi$addEntityHologram(EntityHologram hologram);
    void hologramApi$removeEntityHologram(EntityHologram hologram);
    Set<EntityHologram> hologramApi$getEntityHologramSet();
}
