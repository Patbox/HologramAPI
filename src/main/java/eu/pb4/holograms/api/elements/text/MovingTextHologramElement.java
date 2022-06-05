package eu.pb4.holograms.api.elements.text;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.impl.HologramHelper;
import eu.pb4.holograms.mixin.accessors.ArmorStandEntityAccessor;
import eu.pb4.holograms.mixin.accessors.EntityAccessor;
import eu.pb4.holograms.mixin.accessors.EntityPositionS2CPacketAccessor;
import eu.pb4.holograms.mixin.accessors.EntityTrackerUpdateS2CPacketAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovingTextHologramElement extends AbstractTextHologramElement {

    public MovingTextHologramElement() {
        this(Text.empty());
    }

    public MovingTextHologramElement(Text text) {
        super(text);
        this.entityId = EntityAccessor.getMaxEntityId().incrementAndGet();
        this.getEntityIds().add(this.entityId);
        this.uuid = HologramHelper.getUUID();
    }

    @Override
    public void createSpawnPackets(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);

        player.networkHandler.sendPacket(new EntitySpawnS2CPacket(this.entityId, this.uuid, pos.x, pos.y, pos.z, 0, 0, EntityType.ARMOR_STAND, 0, Vec3d.ZERO, 0));

        {
            var packet = HologramHelper.createUnsafe(EntityTrackerUpdateS2CPacket.class);
            var accessor = (EntityTrackerUpdateS2CPacketAccessor) packet;

            accessor.setId(this.entityId);
            List<DataTracker.Entry<?>> data = new ArrayList<>();
            data.add(new DataTracker.Entry<>(EntityAccessor.getNoGravity(), true));
            data.add(new DataTracker.Entry<>(EntityAccessor.getFlags(), (byte) 0x20));
            data.add(new DataTracker.Entry<>(EntityAccessor.getCustomName(), Optional.of(this.getTextFor(player))));
            data.add(new DataTracker.Entry<>(EntityAccessor.getNameVisible(), true));
            data.add(new DataTracker.Entry<>(ArmorStandEntityAccessor.getArmorStandFlags(), (byte) 0x19));

            accessor.setTrackedValues(data);

            player.networkHandler.sendPacket(packet);
        }
    }

    @Override
    public void updatePosition(ServerPlayerEntity player, AbstractHologram hologram) {
        var packet = HologramHelper.createUnsafe(EntityPositionS2CPacket.class);
        var accessor = (EntityPositionS2CPacketAccessor) packet;
        accessor.setId(this.entityId);
        Vec3d pos = hologram.getElementPosition(this).add(this.getOffset());
        accessor.setX(pos.x);
        accessor.setY(pos.y - 0.40);
        accessor.setZ(pos.z);
        accessor.setOnGround(false);
        accessor.setPitch((byte) 0);
        accessor.setYaw((byte) 0);

        player.networkHandler.sendPacket(packet);
    }

    @Override
    public void onTick(AbstractHologram hologram) {
        if (this.isDirty) {
            for (ServerPlayerEntity player : hologram.getPlayerSet()) {
                var packet = HologramHelper.createUnsafe(EntityTrackerUpdateS2CPacket.class);
                var accessor = (EntityTrackerUpdateS2CPacketAccessor) packet;

                accessor.setId(this.entityId);
                List<DataTracker.Entry<?>> data = new ArrayList<>();
                data.add(new DataTracker.Entry<>(EntityAccessor.getCustomName(), Optional.of(this.getTextFor(player))));
                accessor.setTrackedValues(data);

                player.networkHandler.sendPacket(packet);
            }

            this.isDirty = false;
        }

        super.onTick(hologram);
    }
}
