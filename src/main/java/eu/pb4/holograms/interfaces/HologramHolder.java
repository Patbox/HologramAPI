package eu.pb4.holograms.interfaces;

import eu.pb4.holograms.api.holograms.AbstractHologram;

import java.util.Set;

public interface HologramHolder<T extends AbstractHologram> {
    void addHologram(T hologram);
    void removeHologram(T hologram);
    Set<T> getHologramSet();
}
