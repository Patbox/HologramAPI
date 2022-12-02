package eu.pb4.holograms.impl.interfaces;

import eu.pb4.holograms.api.holograms.WorldHologram;
import net.minecraft.util.math.ChunkPos;

import java.util.Set;

public interface WorldHologramHolder {
    boolean hologramApi$addHologram(WorldHologram hologram, ChunkPos pos);
    boolean hologramApi$removeHologram(WorldHologram hologram, ChunkPos pos);
    Set<WorldHologram> hologramApi$getHologramSet(ChunkPos pos);
}
