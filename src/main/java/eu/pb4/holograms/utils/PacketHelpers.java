package eu.pb4.holograms.utils;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.MobSpawnS2CPacket;

public class PacketHelpers {
    public static EntityTrackerUpdateS2CPacket createEntityTrackerUpdate() {
        try {
            return (EntityTrackerUpdateS2CPacket) UnsafeAccess.UNSAFE.allocateInstance(EntityTrackerUpdateS2CPacket.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MobSpawnS2CPacket createMobSpawn() {
        try {
            return (MobSpawnS2CPacket) UnsafeAccess.UNSAFE.allocateInstance(MobSpawnS2CPacket.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static EntityPositionS2CPacket createEntityPosition() {
        try {
            return (EntityPositionS2CPacket) UnsafeAccess.UNSAFE.allocateInstance(EntityPositionS2CPacket.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static EntityPassengersSetS2CPacket createEntityPassengersSet() {
        try {
            return (EntityPassengersSetS2CPacket) UnsafeAccess.UNSAFE.allocateInstance(EntityPassengersSetS2CPacket.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
