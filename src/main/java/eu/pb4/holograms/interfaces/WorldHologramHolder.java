package eu.pb4.holograms.interfaces;

import eu.pb4.holograms.api.holograms.WorldHologram;
import net.minecraft.util.math.ChunkPos;

import java.util.Set;

public interface WorldHologramHolder {
    boolean addHologram(WorldHologram hologram, ChunkPos pos);
    boolean removeHologram(WorldHologram hologram, ChunkPos pos);
    Set<WorldHologram> getHologramSet(ChunkPos pos);
}
