package eu.pb4.holograms.api.elements.item;

import eu.pb4.holograms.api.elements.item.AbstractItemHologramElement;
import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.mixin.accessors.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class SpinningItemHologramElement extends AbstractItemHologramElement {
    protected int itemId;
    protected int helperId;

    public SpinningItemHologramElement() {
        this(ItemStack.EMPTY);
    }

    public SpinningItemHologramElement(ItemStack stack) {
        super(stack);
        this.height = 0.45;
        this.itemId = this.createEntityId();
        this.helperId = this.createEntityId();

        this.entityIds.add(itemId);
        this.entityIds.add(helperId);
    }

    @Override
    public void createPackets(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);
        {
            player.networkHandler.sendPacket(new EntitySpawnS2CPacket(itemId, Util.NIL_UUID, pos.x, pos.y, pos.z, 0, 0, EntityType.ITEM, 0, Vec3d.ZERO));

            EntityTrackerUpdateS2CPacket packet = new EntityTrackerUpdateS2CPacket();
            EntityTrackerUpdateS2CPacketAccessor accessor = (EntityTrackerUpdateS2CPacketAccessor) packet;

            accessor.setId(itemId);
            List<DataTracker.Entry<?>> data = new ArrayList<>();
            data.add(new DataTracker.Entry<>(EntityAccessor.getNoGravity(), true));
            data.add(new DataTracker.Entry<>(ItemEntityAccessor.getStack(), this.itemStack));
            accessor.setTrackedValues(data);

            player.networkHandler.sendPacket(packet);
        }

        {
            player.networkHandler.sendPacket(new EntitySpawnS2CPacket(helperId, Util.NIL_UUID, pos.x, pos.y - 0.6, pos.z, 0, 0, EntityType.AREA_EFFECT_CLOUD, 0, Vec3d.ZERO));

            EntityTrackerUpdateS2CPacket packet = new EntityTrackerUpdateS2CPacket();
            EntityTrackerUpdateS2CPacketAccessor accessor = (EntityTrackerUpdateS2CPacketAccessor) packet;

            accessor.setId(helperId);
            List<DataTracker.Entry<?>> data = new ArrayList<>();
            data.add(new DataTracker.Entry<>(AreaEffectCloudEntityAccessor.getRadius(), 0f));
            accessor.setTrackedValues(data);

            player.networkHandler.sendPacket(packet);
        }

        {
            EntityPassengersSetS2CPacket packet = new EntityPassengersSetS2CPacket();
            EntityPassengersSetS2CPacketAccessor accessor = (EntityPassengersSetS2CPacketAccessor) packet;
            accessor.setId(helperId);
            accessor.setPassengers(new int[]{itemId});

            player.networkHandler.sendPacket(packet);
        }
    }

    @Override
    public void updatePosition(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);

        EntityPositionS2CPacket packet = new EntityPositionS2CPacket();
        EntityPositionS2CPacketAccessor accessor = (EntityPositionS2CPacketAccessor) packet;
        accessor.setId(this.helperId);
        accessor.setX(pos.x);
        accessor.setY(pos.y - 0.6);
        accessor.setZ(pos.z);
        accessor.setOnGround(false);
        accessor.setPitch((byte) 0);
        accessor.setYaw((byte) 0);
        player.networkHandler.sendPacket(packet);
    }

}
