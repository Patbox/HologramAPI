package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.api.holograms.WorldHologram;
import eu.pb4.holograms.impl.interfaces.HologramHolder;
import eu.pb4.holograms.impl.interfaces.WorldHologramHolder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;


@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin {

    @Shadow @Final ServerWorld world;

    @Inject(method = "handlePlayerAddedOrRemoved", at = @At("TAIL"))
    private void hologramApi$clearHolograms(ServerPlayerEntity player, boolean added, CallbackInfo ci) {
        if (!added) {
            for (AbstractHologram hologram : new HashSet<>(((HologramHolder<AbstractHologram>) player).hologramApi$getHologramSet())) {
                if (hologram instanceof WorldHologram worldHologram && worldHologram.getWorld() == this.world) {
                    hologram.removePlayer(player);
                }
            }
        }
    }

    @Inject(method = "method_17227", at = @At("TAIL"))
    private void hologramApi$onChunkLoad(ChunkHolder chunkHolder, Chunk protoChunk, CallbackInfoReturnable<Chunk> callbackInfoReturnable) {
        WorldChunk chunk = (WorldChunk) callbackInfoReturnable.getReturnValue();
        for (WorldHologram hologram : new HashSet<>(((WorldHologramHolder) this.world).hologramApi$getHologramSet(chunk.getPos()))) {
            ((HologramHolder<WorldHologram>) chunk).hologramApi$addHologram(hologram);
        }
    }
}
