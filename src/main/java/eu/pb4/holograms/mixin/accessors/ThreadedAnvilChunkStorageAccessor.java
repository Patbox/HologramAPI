package eu.pb4.holograms.mixin.accessors;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ThreadedAnvilChunkStorage.class)
public interface ThreadedAnvilChunkStorageAccessor {
    @Accessor("entityTrackers")
    Int2ObjectMap<ThreadedAnvilChunkStorage.EntityTracker> getEntityTrackers();
}
