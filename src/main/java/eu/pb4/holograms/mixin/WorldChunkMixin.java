package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.interfaces.HologramHolder;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Set;

@Mixin(WorldChunk.class)
public class WorldChunkMixin implements HologramHolder {
    @Unique
    private final Set<AbstractHologram> holograms = new HashSet<>();

    public void addHologram(AbstractHologram hologram) {
        this.holograms.add(hologram);
    }

    public void removeHologram(AbstractHologram hologram) {
        this.holograms.remove(hologram);
    }

    public Set<AbstractHologram> getHologramSet() {
        return this.holograms;
    }
}
