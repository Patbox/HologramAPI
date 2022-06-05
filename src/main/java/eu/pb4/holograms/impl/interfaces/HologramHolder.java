package eu.pb4.holograms.impl.interfaces;

import eu.pb4.holograms.api.holograms.AbstractHologram;

import java.util.Set;

public interface HologramHolder<T extends AbstractHologram> {
    void holoapi_addHologram(T hologram);
    void holoapi_removeHologram(T hologram);
    Set<T> holoapi_getHologramSet();
}
