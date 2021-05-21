package eu.pb4.holograms.api.elements.clickable;

import eu.pb4.holograms.api.elements.AbstractHologramElement;
import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.mixin.accessors.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.MobSpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class CubeHitboxHologramElement extends AbstractHologramElement {
    protected final int entityId;
    protected final int size;

    public CubeHitboxHologramElement(int size, Vec3d offset) {
        this.height = 0;
        this.entityId = EntityAccessor.getMaxEntityId().incrementAndGet();
        this.entityIds.add(entityId);
        this.size = size;
        this.offset = offset;
    }

    @Override
    public void createPackets(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);

        {
            MobSpawnS2CPacket packet = new MobSpawnS2CPacket();
            MobSpawnS2CPacketAccessor accessor = (MobSpawnS2CPacketAccessor) packet;
            accessor.setId(this.entityId);
            accessor.setPitch((byte) 0);
            accessor.setYaw((byte) 0);
            accessor.setHeadYaw((byte) 0);
            accessor.setX(pos.x);
            accessor.setY(pos.y);
            accessor.setZ(pos.z);
            accessor.setEntityType(Registry.ENTITY_TYPE.getRawId(EntityType.SLIME));
            accessor.setUUID(Util.NIL_UUID);

            player.networkHandler.sendPacket(packet);
        }
        {
            EntityTrackerUpdateS2CPacket packet = new EntityTrackerUpdateS2CPacket();
            EntityTrackerUpdateS2CPacketAccessor accessor = (EntityTrackerUpdateS2CPacketAccessor) packet;

            accessor.setId(this.entityId);
            List<DataTracker.Entry<?>> data = new ArrayList<>();
            data.add(new DataTracker.Entry<>(EntityAccessor.getNoGravity(), true));
            data.add(new DataTracker.Entry<>(SlimeEntityAccessor.getSlimeSize(), this.size));
            data.add(new DataTracker.Entry<>(EntityAccessor.getFlags(), (byte) 0x20));

            accessor.setTrackedValues(data);

            player.networkHandler.sendPacket(packet);
        }
    }

    @Override
    public void updatePosition(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);

        EntityPositionS2CPacket packet = new EntityPositionS2CPacket();
        EntityPositionS2CPacketAccessor accessor = (EntityPositionS2CPacketAccessor) packet;
        accessor.setId(this.entityId);
        accessor.setX(pos.x);
        accessor.setY(pos.y);
        accessor.setZ(pos.z);
        accessor.setOnGround(false);
        accessor.setPitch((byte) 0);
        accessor.setYaw((byte) 0);
        player.networkHandler.sendPacket(packet);
    }
}
