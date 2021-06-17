package eu.pb4.holograms.mixin.accessors;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ThreadedAnvilChunkStorage.EntityTracker.class)
public interface EntityTrackerAccessor {

    @Accessor("playersTracking")
    Set<ServerPlayerEntity> getTrackedPlayers();
}
