package eu.pb4.holograms.mixin.accessors;

import net.minecraft.network.packet.s2c.play.MobSpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(MobSpawnS2CPacket.class)
public interface MobSpawnS2CPacketAccessor {
    @Mutable
    @Accessor("id")
    void setId(int id);

    @Mutable
    @Accessor("x")
    void setX(double val);

    @Mutable
    @Accessor("y")
    void setY(double val);

    @Mutable
    @Accessor("z")
    void setZ(double val);

    @Mutable
    @Accessor("yaw")
    void setYaw(byte val);

    @Mutable
    @Accessor("headYaw")
    void setHeadYaw(byte val);

    @Mutable
    @Accessor("pitch")
    void setPitch(byte val);

    @Mutable
    @Accessor("uuid")
    void setUUID(UUID val);

    @Mutable
    @Accessor("entityTypeId")
    void setEntityType(int val);
}
