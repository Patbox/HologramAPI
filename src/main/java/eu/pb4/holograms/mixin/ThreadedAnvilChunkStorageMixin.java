package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.interfaces.HologramHolder;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin {

    @Inject(method = "sendChunkDataPackets", at = @At("TAIL"))
    private void addToHolograms(ServerPlayerEntity player, Packet<?>[] packets, WorldChunk chunk, CallbackInfo ci) {
        for (AbstractHologram hologram : ((HologramHolder) chunk).getHologramSet()) {
            if (hologram.canAddPlayer(player)) {
                hologram.addPlayer(player);
            }
        }
    }
}
