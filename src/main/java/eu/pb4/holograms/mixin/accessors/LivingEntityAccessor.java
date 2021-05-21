package eu.pb4.holograms.mixin.accessors;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("LIVING_FLAGS")
    static TrackedData<Byte> getLivingFlags() { throw new AssertionError(); }

    @Accessor("HEALTH")
    static TrackedData<Float> getHealth() { throw new AssertionError(); }

    @Accessor("POTION_SWIRLS_COLOR")
    static TrackedData<Integer> getPotionColor() { throw new AssertionError(); }

    @Accessor("POTION_SWIRLS_AMBIENT")
    static TrackedData<Boolean> getPotionAmbient() { throw new AssertionError(); }

    @Accessor("STUCK_ARROW_COUNT")
    static TrackedData<Integer> getArrowCount() { throw new AssertionError(); }

    @Accessor("STINGER_COUNT")
    static TrackedData<Integer> getStingerCount() { throw new AssertionError(); }

    @Accessor("SLEEPING_POSITION")
    static TrackedData<Optional<BlockPos>> getSleepingPos() { throw new AssertionError(); }
}
