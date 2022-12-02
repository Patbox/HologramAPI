package eu.pb4.holograms.impl.interfaces;

import eu.pb4.holograms.api.holograms.AbstractHologram;

import java.util.Set;

public interface HologramHolder<T extends AbstractHologram> {
    void hologramApi$addHologram(T hologram);
    void hologramApi$removeHologram(T hologram);
    Set<T> hologramApi$getHologramSet();
}
