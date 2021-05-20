package eu.pb4.holograms.mixin.accessors;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ThrownItemEntity.class)
public interface ThrownItemEntityAccessor {
    @Accessor("ITEM")
    static TrackedData<ItemStack> getItem() {
        throw new AssertionError();
    }
}
