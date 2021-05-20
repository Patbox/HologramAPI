package eu.pb4.holograms.mixin.accessors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("MAX_ENTITY_ID")
    static AtomicInteger getMaxEntityId() {
        throw new AssertionError();
    }

    @Accessor("CUSTOM_NAME")
    static TrackedData<Optional<Text>> getCustomName() {
        throw new AssertionError();
    }

    @Accessor("NAME_VISIBLE")
    static TrackedData<Boolean> getNameVisible() {
        throw new AssertionError();
    }

    @Accessor("NO_GRAVITY")
    static TrackedData<Boolean> getNoGravity() {
        throw new AssertionError();
    }

    @Accessor("SILENT")
    static TrackedData<Boolean> getSilent() {
        throw new AssertionError();
    }
}
