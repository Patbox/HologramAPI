package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.impl.interfaces.HologramHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements HologramHolder<AbstractHologram> {
    @Shadow @Final public MinecraftServer server;
    @Unique
    Set<AbstractHologram> holograms = new HashSet<>();

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void hologramApi$removeFromHologramsOnDisconnect(CallbackInfo ci) {
        for (AbstractHologram hologram : new HashSet<>(this.holograms)) {
            hologram.removePlayer(this.asPlayer());
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void hologramApi$removeOnDeath(DamageSource source, CallbackInfo ci) {
        for (AbstractHologram hologram : new HashSet<>(this.holograms)) {
            hologram.removePlayer(this.asPlayer());
        }
    }

    @Inject(method = "moveToWorld", at = @At("HEAD"))
    private void hologramApi$removeOnWorldChange(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
        for (AbstractHologram hologram : new HashSet<>(this.holograms)) {
            hologram.removePlayer(this.asPlayer());
        }
    }

    @Inject(method = "teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getServerWorld()Lnet/minecraft/server/world/ServerWorld;"))
    private void hologramApi$removeOnWorldChange2(ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch, CallbackInfo ci) {
        for (AbstractHologram hologram : new HashSet<>(this.holograms)) {
            hologram.removePlayer(this.asPlayer());
        }
    }

    @Unique
    private ServerPlayerEntity asPlayer() {
        return (ServerPlayerEntity) (Object) this;
    }

    @Override
    public void hologramApi$addHologram(AbstractHologram hologram) {
        this.holograms.add(hologram);
    }

    @Override
    public void hologramApi$removeHologram(AbstractHologram hologram) {
        this.holograms.remove(hologram);
    }

    @Override
    public Set<AbstractHologram> hologramApi$getHologramSet() {
        return this.holograms;
    }
}
