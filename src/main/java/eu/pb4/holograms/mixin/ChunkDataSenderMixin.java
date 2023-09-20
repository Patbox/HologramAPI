package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.api.holograms.WorldHologram;
import eu.pb4.holograms.impl.interfaces.HologramHolder;
import net.minecraft.server.network.ChunkDataSender;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;

@Mixin(ChunkDataSender.class)
public class ChunkDataSenderMixin {
    @Inject(method = "sendChunkData", at = @At("TAIL"))
    private static void hologramApi$addToHolograms(ServerPlayNetworkHandler handler, ServerWorld world, WorldChunk chunk, CallbackInfo ci) {
        var player = handler.getPlayer();

        for (AbstractHologram hologram : ((HologramHolder<WorldHologram>) chunk).hologramApi$getHologramSet()) {
            if (hologram.canAddPlayer(player)) {
                hologram.addPlayer(player);
            }
        }
    }

    @Inject(method = "unload", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V"))
    private void hologramApi$clearHologramFromChunk(ServerPlayerEntity player, ChunkPos chunkPos, CallbackInfo ci) {
        var holograms = ((HologramHolder<?>) player).hologramApi$getHologramSet();

        for (AbstractHologram hologram : new HashSet<>(holograms)) {
            if (hologram instanceof WorldHologram && ((WorldHologram) hologram).getChunkPos().equals(chunkPos)) {
                hologram.removePlayer(player);
            }
        }
    }
}
