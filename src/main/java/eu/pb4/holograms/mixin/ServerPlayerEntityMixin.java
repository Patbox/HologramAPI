package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.api.holograms.WorldHologram;
import eu.pb4.holograms.interfaces.HologramHolder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements HologramHolder {
    @Shadow @Final public MinecraftServer server;
    @Unique
    Set<AbstractHologram> holograms = new HashSet<>();

    @Inject(method = "sendUnloadChunkPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"))
    private void clearHologramFromChunk(ChunkPos chunkPos, CallbackInfo ci) {
        for (AbstractHologram hologram : new HashSet<>(this.holograms)) {
            if (hologram instanceof WorldHologram && ((WorldHologram) hologram).getChunkPos().equals(chunkPos)) {
                hologram.removePlayer(this.asPlayer());
            }
        }
    }

    @Inject(method = "onDisconnect", at = @At("TAIL"))
    private void removeFromHologramsOnDisconnect(CallbackInfo ci) {
        for (AbstractHologram hologram : new HashSet<>(this.holograms)) {
            hologram.removePlayer(this.asPlayer());
        }
    }

    @Unique
    private ServerPlayerEntity asPlayer() {
        return (ServerPlayerEntity) (Object) this;
    }

    @Override
    public void addHologram(AbstractHologram hologram) {
        this.holograms.add(hologram);
    }

    @Override
    public void removeHologram(AbstractHologram hologram) {
        this.holograms.remove(hologram);
    }

    @Override
    public Set<AbstractHologram> getHologramSet() {
        return this.holograms;
    }
}
