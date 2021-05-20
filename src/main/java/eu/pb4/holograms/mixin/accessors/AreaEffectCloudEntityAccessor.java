package eu.pb4.holograms.mixin.accessors;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.data.TrackedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AreaEffectCloudEntity.class)
public interface AreaEffectCloudEntityAccessor {
    @Accessor("RADIUS")
    static TrackedData<Float> getRadius() {
        throw new AssertionError();
    }
}
