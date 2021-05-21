package eu.pb4.holograms.api.elements.clickable;

import eu.pb4.holograms.api.elements.AbstractHologramElement;
import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.mixin.accessors.EntityAccessor;
import eu.pb4.holograms.mixin.accessors.EntityPositionS2CPacketAccessor;
import eu.pb4.holograms.mixin.accessors.EntityTrackerUpdateS2CPacketAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class EntityHologramElement extends AbstractHologramElement {
    protected final Entity entity;

    public EntityHologramElement(Entity entity) {
        this.height = entity.getHeight() + 0.1;
        this.entityIds.add(entity.getEntityId());
        this.entity = entity;
    }

    @Override
    public void createPackets(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);
        this.entity.setPos(pos.x, pos.y - 0.05, pos.z);

        player.networkHandler.sendPacket(this.entity.createSpawnPacket());

        EntityTrackerUpdateS2CPacket packet = new EntityTrackerUpdateS2CPacket();
        EntityTrackerUpdateS2CPacketAccessor accessor = (EntityTrackerUpdateS2CPacketAccessor) packet;

        accessor.setId(this.entity.getEntityId());
        List<DataTracker.Entry<?>> data = new ArrayList<>();
        data.addAll(this.entity.getDataTracker().getAllEntries());
        data.add(new DataTracker.Entry<>(EntityAccessor.getNoGravity(), true));
        accessor.setTrackedValues(data);

        player.networkHandler.sendPacket(packet);
    }

    @Override
    public void updatePosition(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);

        EntityPositionS2CPacket packet = new EntityPositionS2CPacket();
        EntityPositionS2CPacketAccessor accessor = (EntityPositionS2CPacketAccessor) packet;
        accessor.setId(this.entity.getEntityId());
        accessor.setX(pos.x);
        accessor.setY(pos.y - 0.05);
        accessor.setZ(pos.z);
        accessor.setOnGround(false);
        accessor.setPitch((byte) 0);
        accessor.setYaw((byte) 0);
        player.networkHandler.sendPacket(packet);
    }
}
