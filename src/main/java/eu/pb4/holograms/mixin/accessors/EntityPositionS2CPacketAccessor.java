package eu.pb4.holograms.mixin.accessors;

import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPositionS2CPacket.class)
public interface EntityPositionS2CPacketAccessor {
    @Accessor("id")
    void setId(int id);

    @Accessor("x")
    void setX(double val);

    @Accessor("y")
    void setY(double val);

    @Accessor("z")
    void setZ(double val);

    @Accessor("yaw")
    void setYaw(byte val);

    @Accessor("pitch")
    void setPitch(byte val);

    @Accessor("onGround")
    void setOnGround(boolean val);
}
