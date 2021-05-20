package eu.pb4.holograms.api.elements;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.mixin.accessors.EntityAccessor;
import eu.pb4.holograms.mixin.accessors.EntityPositionS2CPacketAccessor;
import eu.pb4.holograms.mixin.accessors.EntityTrackerUpdateS2CPacketAccessor;
import eu.pb4.holograms.mixin.accessors.ThrownItemEntityAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class StaticItemHologramElement extends AbstractItemHologramElement {

    public StaticItemHologramElement() {
        this(ItemStack.EMPTY);
    }

    public StaticItemHologramElement(ItemStack stack) {
        super(stack);
        this.height = 0.5;
        this.entityIds.add(this.createEntityId());
    }

    @Override
    public void createPackets(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);
        int itemId = this.getEntityIds().getInt(0);
        {
            player.networkHandler.sendPacket(new EntitySpawnS2CPacket(itemId, Util.NIL_UUID, pos.x, pos.y, pos.z, 0, 0, EntityType.SNOWBALL, 0, Vec3d.ZERO));

            EntityTrackerUpdateS2CPacket packet = new EntityTrackerUpdateS2CPacket();
            EntityTrackerUpdateS2CPacketAccessor accessor = (EntityTrackerUpdateS2CPacketAccessor) packet;

            accessor.setId(itemId);
            List<DataTracker.Entry<?>> data = new ArrayList<>();
            data.add(new DataTracker.Entry<>(EntityAccessor.getNoGravity(), true));
            data.add(new DataTracker.Entry<>(ThrownItemEntityAccessor.getItem(), this.itemStack));
            accessor.setTrackedValues(data);

            player.networkHandler.sendPacket(packet);
        }
    }

    @Override
    public void updatePosition(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);

        EntityPositionS2CPacket packet = new EntityPositionS2CPacket();
        EntityPositionS2CPacketAccessor accessor = (EntityPositionS2CPacketAccessor) packet;
        accessor.setId(this.getEntityIds().getInt(0));
        accessor.setX(pos.x);
        accessor.setY(pos.y);
        accessor.setZ(pos.z);
        accessor.setOnGround(false);
        accessor.setPitch((byte) 0);
        accessor.setYaw((byte) 0);
        player.networkHandler.sendPacket(packet);
    }

}
