package eu.pb4.holograms.mixin.accessors;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ArmorStandEntity.class)
public interface ArmorStandEntityAccessor {
    @Accessor("ARMOR_STAND_FLAGS")
    static TrackedData<Byte> getArmorStandFlags() {
        throw new AssertionError();
    }
}
