package eu.pb4.holograms.api.elements.clickable;

import eu.pb4.holograms.api.elements.AbstractHologramElement;
import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.mixin.accessors.EntityAccessor;
import eu.pb4.holograms.mixin.accessors.EntityPositionS2CPacketAccessor;
import eu.pb4.holograms.mixin.accessors.EntityTrackerUpdateS2CPacketAccessor;
import eu.pb4.holograms.utils.HologramHelper;
import eu.pb4.holograms.utils.PacketHelpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityHologramElement extends AbstractHologramElement {
    protected final Entity entity;

    public EntityHologramElement(Entity entity) {
        this.height = entity.getHeight() + 0.1;
        this.entityIds.add(entity.getId());
        this.entity = entity;

        if (this.entity.world.getEntityById(this.entity.getId()) != null) {
            throw new IllegalArgumentException("Entity can't exist in world!");
        }
        this.entity.setUuid(HologramHelper.getUUID());
    }

    @Override
    public void createSpawnPackets(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);
        this.entity.setPos(pos.x, pos.y - 0.05, pos.z);

        player.networkHandler.sendPacket(this.entity.createSpawnPacket());

        EntityTrackerUpdateS2CPacket packet = PacketHelpers.createEntityTrackerUpdate();
        EntityTrackerUpdateS2CPacketAccessor accessor = (EntityTrackerUpdateS2CPacketAccessor) packet;

        accessor.setId(this.entity.getId());
        List<DataTracker.Entry<?>> data = new ArrayList<>();
        data.addAll(this.entity.getDataTracker().getAllEntries());
        data.add(new DataTracker.Entry<>(EntityAccessor.getNoGravity(), true));
        accessor.setTrackedValues(data);

        player.networkHandler.sendPacket(packet);
        player.networkHandler.sendPacket(TeamS2CPacket.changePlayerTeam(HologramHelper.getFakeTeam(), this.entity.getUuidAsString(), TeamS2CPacket.Operation.ADD));
    }

    @Override
    public void createRemovePackets(ServerPlayerEntity player, AbstractHologram hologram) {
        player.networkHandler.sendPacket(TeamS2CPacket.changePlayerTeam(HologramHelper.getFakeTeam(), this.entity.getUuidAsString(), TeamS2CPacket.Operation.REMOVE));
        super.createRemovePackets(player, hologram);
    }

    @Override
    public void updatePosition(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);

        EntityPositionS2CPacket packet = PacketHelpers.createEntityPosition();
        EntityPositionS2CPacketAccessor accessor = (EntityPositionS2CPacketAccessor) packet;
        accessor.setId(this.entity.getId());
        accessor.setX(pos.x);
        accessor.setY(pos.y - 0.05);
        accessor.setZ(pos.z);
        accessor.setOnGround(false);
        accessor.setPitch((byte) 0);
        accessor.setYaw((byte) 0);
        player.networkHandler.sendPacket(packet);
    }
}
