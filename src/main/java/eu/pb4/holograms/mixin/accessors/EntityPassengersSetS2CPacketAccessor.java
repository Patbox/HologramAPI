package eu.pb4.holograms.mixin.accessors;

import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPassengersSetS2CPacket.class)
public interface EntityPassengersSetS2CPacketAccessor {
    @Mutable
    @Accessor("id")
    void setId(int id);

    @Mutable
    @Accessor("passengerIds")
    void setPassengers(int[] ids);
}
