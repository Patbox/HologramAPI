package eu.pb4.holograms.impl.interfaces;

import eu.pb4.holograms.api.holograms.EntityHologram;

import java.util.Set;

public interface EntityHologramHolder {
    void holoapi_addEntityHologram(EntityHologram hologram);
    void holoapi_removeEntityHologram(EntityHologram hologram);
    Set<EntityHologram> holoapi_getEntityHologramSet();
}
