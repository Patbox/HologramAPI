package eu.pb4.holograms.impl.interfaces;

import eu.pb4.holograms.api.holograms.WorldHologram;
import net.minecraft.util.math.ChunkPos;

import java.util.Set;

public interface WorldHologramHolder {
    boolean holoapi_addHologram(WorldHologram hologram, ChunkPos pos);
    boolean holoapi_removeHologram(WorldHologram hologram, ChunkPos pos);
    Set<WorldHologram> holoapi_getHologramSet(ChunkPos pos);
}
