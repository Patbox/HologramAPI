package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.holograms.WorldHologram;
import eu.pb4.holograms.impl.interfaces.HologramHolder;
import eu.pb4.holograms.impl.interfaces.WorldHologramHolder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements WorldHologramHolder {
    @Unique private Map<ChunkPos, Set<WorldHologram>> hologramsPerChunk = new HashMap<>();

    @Inject(method = "tickChunk", at = @At("TAIL"))
    private void tickHolograms(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        for (WorldHologram hologram : ((HologramHolder<WorldHologram>) chunk).holoapi_getHologramSet()) {
            hologram.tick();
        }
    }

    @Override
    public boolean holoapi_addHologram(WorldHologram hologram, ChunkPos pos) {
        return this.hologramsPerChunk.computeIfAbsent(pos, ServerWorldMixin::getSet).add(hologram);
    }

    @Override
    public boolean holoapi_removeHologram(WorldHologram hologram, ChunkPos pos) {
        Set<WorldHologram> set = this.hologramsPerChunk.get(pos);
        if (set != null) {
            boolean bool = set.remove(hologram);
            if (set.isEmpty()) {
                this.hologramsPerChunk.remove(pos);
            }
            return bool;
        }
        return false;
    }

    @Override
    public Set<WorldHologram> holoapi_getHologramSet(ChunkPos pos) {
        return this.hologramsPerChunk.getOrDefault(pos, Collections.emptySet());
    }


    private static HashSet<WorldHologram> getSet(ChunkPos pos) {
        return new HashSet<>();
    }
}
