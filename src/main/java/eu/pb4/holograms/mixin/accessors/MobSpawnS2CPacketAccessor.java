package eu.pb4.holograms.mixin.accessors;

import net.minecraft.network.packet.s2c.play.MobSpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(MobSpawnS2CPacket.class)
public interface MobSpawnS2CPacketAccessor {
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

    @Accessor("headYaw")
    void setHeadYaw(byte val);

    @Accessor("pitch")
    void setPitch(byte val);

    @Accessor("uuid")
    void setUUID(UUID val);

    @Accessor("entityTypeId")
    void setEntityType(int val);
}
