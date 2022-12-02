package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.api.holograms.EntityHologram;
import eu.pb4.holograms.impl.interfaces.EntityHologramHolder;
import eu.pb4.holograms.mixin.accessors.ThreadedAnvilChunkStorageAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.EntityTrackingListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
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

    @Shadow public abstract String toString();

    @Shadow private int id;
    @Unique
    private final Set<EntityHologram> hologramApi$attachedHolograms = new HashSet<>();

    @Unique
    private boolean hologramApi$addPlayersOnFirstTick = false;

    @Inject(method = "tick", at = @At("TAIL"))
    private void hologramApi$tickHolograms(CallbackInfo ci) {
        if (this.hologramApi$addPlayersOnFirstTick) {
            ThreadedAnvilChunkStorage.EntityTracker tracker = ((ThreadedAnvilChunkStorageAccessor) ((ServerWorld) this.world).getChunkManager().threadedAnvilChunkStorage)
                    .getEntityTrackers().get(this.id);

            if (tracker != null) {
                for (EntityTrackingListener listener : tracker.listeners) {
                    for (EntityHologram hologram : this.hologramApi$attachedHolograms) {
                        if (hologram.canAddPlayer(listener.getPlayer())) {
                            hologram.addPlayer(listener.getPlayer());
                        }
                    }
                }

                this.hologramApi$addPlayersOnFirstTick = false;

            }
        }

        for (EntityHologram hologram : this.hologramApi$attachedHolograms) {
            try {
                hologram.tick();
            } catch (Exception e) {
                e.printStackTrace();
                this.hologramApi$removeEntityHologram(hologram);
            }
        }
    }

    @Inject(method = "onStartedTrackingBy", at = @At("TAIL"))
    private void hologramApi$addToHolograms(ServerPlayerEntity player, CallbackInfo ci) {
        for (AbstractHologram hologram : this.hologramApi$attachedHolograms) {
            if (hologram.canAddPlayer(player)) {
                hologram.addPlayer(player);
            }
        }
    }

    @Inject(method = "onStoppedTrackingBy", at = @At("TAIL"))
    private void hologramApi$removeFromHolograms(ServerPlayerEntity player, CallbackInfo ci) {
        for (AbstractHologram hologram : this.hologramApi$attachedHolograms) {
            hologram.removePlayer(player);
        }
    }

    @Inject(method = "setPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;floor(D)I"))
    private void hologramApi$moveHologramsWithEntity(double x, double y, double z, CallbackInfo ci) {
        for (EntityHologram hologram : this.hologramApi$attachedHolograms) {
            hologram.syncPositionWithEntity();
        }
    }

    @Override
    public void hologramApi$addEntityHologram(EntityHologram hologram) {
        this.hologramApi$attachedHolograms.add(hologram);

        ThreadedAnvilChunkStorage.EntityTracker tracker = ((ThreadedAnvilChunkStorageAccessor) ((ServerWorld) this.world).getChunkManager().threadedAnvilChunkStorage)
                .getEntityTrackers().get(this.id);

        if (tracker != null) {
            for (EntityTrackingListener listener : tracker.listeners) {
                if (hologram.canAddPlayer(listener.getPlayer())) {
                    hologram.addPlayer(listener.getPlayer());
                }
            }
        } else {
            this.hologramApi$addPlayersOnFirstTick = true;
        }
    }

    @Override
    public void hologramApi$removeEntityHologram(EntityHologram hologram) {
        this.hologramApi$attachedHolograms.remove(hologram);
    }

    @Override
    public Set<EntityHologram> hologramApi$getEntityHologramSet() {
        return this.hologramApi$attachedHolograms;
    }
}
