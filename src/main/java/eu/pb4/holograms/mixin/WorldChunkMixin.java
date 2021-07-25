package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.holograms.WorldHologram;
import eu.pb4.holograms.interfaces.HologramHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Set;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin implements HologramHolder<WorldHologram> {
    @Shadow public abstract World getWorld();

    @Shadow @Final private ChunkPos pos;
    @Unique
    private final Set<WorldHologram> holograms = new HashSet<>();

    public void addHologram(WorldHologram hologram) {
        this.holograms.add(hologram);
        ((ServerChunkManager) this.getWorld().getChunkManager()).threadedAnvilChunkStorage.getPlayersWatchingChunk(this.pos, false)
                .forEach((p) -> {
                    if (hologram.canAddPlayer(p)) {
                        hologram.addPlayer(p);
                    }
                });
    }

    public void removeHologram(WorldHologram hologram) {
        this.holograms.remove(hologram);
    }

    public Set<WorldHologram> getHologramSet() {
        return this.holograms;
    }
}
