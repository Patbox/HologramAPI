package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.api.holograms.EntityHologram;
import eu.pb4.holograms.interfaces.EntityHologramHolder;
import eu.pb4.holograms.mixin.accessors.ThreadedAnvilChunkStorageAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityHologramHolder {
    @Shadow public World world;

    @Shadow public abstract World getEntityWorld();

    @Shadow private int entityId;
    @Unique
    private Set<EntityHologram> attachedHolograms = new HashSet<>();

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickHolograms(CallbackInfo ci) {
        for (EntityHologram hologram : this.attachedHolograms) {
            try {
                hologram.tick();
            } catch (Exception e) {
                e.printStackTrace();
                this.removeEntityHologram(hologram);
            }
        }

    }

    @Inject(method = "onStartedTrackingBy", at = @At("TAIL"))
    private void addToHolograms(ServerPlayerEntity player, CallbackInfo ci) {
        for (AbstractHologram hologram : this.attachedHolograms) {
            if (hologram.canAddPlayer(player)) {
                hologram.addPlayer(player);
            }
        }
    }

    @Inject(method = "onStoppedTrackingBy", at = @At("TAIL"))
    private void removeFromHolograms(ServerPlayerEntity player, CallbackInfo ci) {
        for (AbstractHologram hologram : this.attachedHolograms) {
            hologram.removePlayer(player);
        }
    }

    @Inject(method = "setPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;floor(D)I"))
    private void moveHologramsWithEntity(double x, double y, double z, CallbackInfo ci) {
        for (EntityHologram hologram : this.attachedHolograms) {
            hologram.syncPositionWithEntity();
        }
    }

    @Override
    public void addEntityHologram(EntityHologram hologram) {
        this.attachedHolograms.add(hologram);

        for (ServerPlayerEntity player : ((ThreadedAnvilChunkStorageAccessor) ((ServerWorld) this.world).getChunkManager().threadedAnvilChunkStorage)
                .getEntityTrackers().get(this.entityId).playersTracking) {
            hologram.addPlayer(player);
        }
    }

    @Override
    public void removeEntityHologram(EntityHologram hologram) {
        this.attachedHolograms.remove(hologram);
    }

    @Override
    public Set<EntityHologram> getEntityHologramSet() {
        return this.attachedHolograms;
    }
}
