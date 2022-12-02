package eu.pb4.holograms.api.elements.item;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.mixin.accessors.*;
import eu.pb4.holograms.impl.HologramHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StaticItemHologramElement extends AbstractItemHologramElement {
    protected int entityId;
    protected UUID uuid;

    public StaticItemHologramElement() {
        this(ItemStack.EMPTY);
    }

    public StaticItemHologramElement(ItemStack stack) {
        super(stack);
        this.height = 0.5;
        this.entityId = EntityAccessor.getMaxEntityId().incrementAndGet();
        this.entityIds.add(entityId);
        this.uuid = HologramHelper.getUUID();
    }

    @Override
    public void createSpawnPackets(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);

        player.networkHandler.sendPacket(new EntitySpawnS2CPacket(this.entityId, this.uuid, pos.x, pos.y, pos.z, 0, 0, EntityType.SNOWBALL, 0, Vec3d.ZERO, 0));

        List<DataTracker.SerializedEntry<?>> data = new ArrayList<>();
        data.add(DataTracker.SerializedEntry.of(EntityAccessor.getNoGravity(), true));
        data.add(DataTracker.SerializedEntry.of(ThrownItemEntityAccessor.getItem(), this.itemStack));
        player.networkHandler.sendPacket(new EntityTrackerUpdateS2CPacket(this.entityId, data));
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

    @Override
    public void onTick(AbstractHologram hologram) {
        if (this.isDirty) {


            List<DataTracker.SerializedEntry<?>> data = new ArrayList<>();
            data.add(DataTracker.SerializedEntry.of(ThrownItemEntityAccessor.getItem(), this.itemStack));

            var packet = new EntityTrackerUpdateS2CPacket(this.entityId, data);
            for (ServerPlayerEntity player : hologram.getPlayerSet()) {
                player.networkHandler.sendPacket(packet);
            }

            this.isDirty = false;
        }

        super.onTick(hologram);
    }
}
