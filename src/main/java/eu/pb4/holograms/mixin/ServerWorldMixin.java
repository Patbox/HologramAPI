package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.interfaces.HologramHolder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(method = "tickChunk", at = @At("TAIL"))
    private void tickHolograms(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        for (AbstractHologram hologram : ((HologramHolder) chunk).getHologramSet()) {
            hologram.tick();
        }
    }
}
