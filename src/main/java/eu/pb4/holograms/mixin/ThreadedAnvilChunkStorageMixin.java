package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.interfaces.HologramHolder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin {
    @Shadow private volatile Long2ObjectLinkedOpenHashMap<ChunkHolder> chunkHolders;

    @Shadow @Final private LongSet loadedChunks;

    @Shadow @Nullable protected abstract ChunkHolder getChunkHolder(long pos);

    @Shadow @Final private Int2ObjectMap<ThreadedAnvilChunkStorage.EntityTracker> entityTrackers;

    @Inject(method = "sendChunkDataPackets", at = @At("TAIL"))
    private void addToHolograms(ServerPlayerEntity player, Packet<?>[] packets, WorldChunk chunk, CallbackInfo ci) {
        for (AbstractHologram hologram : ((HologramHolder) chunk).getHologramSet()) {
            if (hologram.canAddPlayer(player)) {
                hologram.addPlayer(player);
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickHolograms(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        for (long chunkPos : this.loadedChunks) {
            ChunkHolder holder = this.getChunkHolder(chunkPos);
            if (holder != null) {
                WorldChunk chunk = holder.getWorldChunk();
                if (chunk != null) {
                    for (AbstractHologram hologram : ((HologramHolder) chunk).getHologramSet()) {
                        hologram.tick();
                    }
                }
            }
        }
    }
}
