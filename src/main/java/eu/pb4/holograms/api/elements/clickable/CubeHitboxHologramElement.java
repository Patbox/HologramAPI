package eu.pb4.holograms.api.elements.clickable;

import eu.pb4.holograms.api.elements.AbstractHologramElement;
import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.mixin.accessors.*;
import eu.pb4.holograms.impl.HologramHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @deprecated Use <a href="https://github.com/Patbox/polymer">Polymer</a>'s virtual entities instead.
 */
@Deprecated
public class CubeHitboxHologramElement extends AbstractHologramElement {
    protected final int entityId;
    protected final int size;
    protected final UUID uuid;

    public CubeHitboxHologramElement(int size, Vec3d offset) {
        this.height = 0;
        this.entityId = EntityAccessor.getMaxEntityId().incrementAndGet();
        this.entityIds.add(entityId);
        this.uuid = HologramHelper.getUUID();
        this.size = size;
        this.offset = offset;
    }

    @Override
    public void createSpawnPackets(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);


        player.networkHandler.sendPacket(new EntitySpawnS2CPacket(this.entityId, this.uuid, pos.x, pos.y, pos.z, 0, 0, EntityType.SLIME, 0, Vec3d.ZERO, 0));

        {
            List<DataTracker.SerializedEntry<?>> data = new ArrayList<>();
            data.add(DataTracker.SerializedEntry.of(EntityAccessor.getNoGravity(), true));
            data.add(DataTracker.SerializedEntry.of(SlimeEntityAccessor.getSlimeSize(), this.size));
            data.add(DataTracker.SerializedEntry.of(EntityAccessor.getFlags(), (byte) 0x20));


            player.networkHandler.sendPacket(new EntityTrackerUpdateS2CPacket(this.entityId, data));
        }
        player.networkHandler.sendPacket(TeamS2CPacket.changePlayerTeam(HologramHelper.getFakeTeam(), this.uuid.toString(), TeamS2CPacket.Operation.ADD));
    }

    @Override
    public void createRemovePackets(ServerPlayerEntity player, AbstractHologram hologram) {
        player.networkHandler.sendPacket(TeamS2CPacket.changePlayerTeam(HologramHelper.getFakeTeam(), this.uuid.toString(), TeamS2CPacket.Operation.REMOVE));
        super.createRemovePackets(player, hologram);
    }

    @Override
    public void updatePosition(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);

        var packet = HologramHelper.createUnsafe(EntityPositionS2CPacket.class);
        var accessor = (EntityPositionS2CPacketAccessor) packet;
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
