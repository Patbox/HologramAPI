package eu.pb4.holograms.interfaces;

import eu.pb4.holograms.api.holograms.AbstractHologram;

import java.util.Set;

public interface HologramHolder {
    void addHologram(AbstractHologram hologram);
    void removeHologram(AbstractHologram hologram);
    Set<AbstractHologram> getHologramSet();
}
