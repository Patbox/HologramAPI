package eu.pb4.holograms.api.elements.text;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.mixin.accessors.*;
import eu.pb4.holograms.utils.HologramHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.MobSpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovingTextHologramElement extends AbstractTextHologramElement {

    public MovingTextHologramElement() {
        this(new LiteralText(""));
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
        {
            MobSpawnS2CPacket packet = new MobSpawnS2CPacket();
            MobSpawnS2CPacketAccessor accessor = (MobSpawnS2CPacketAccessor) packet;
            accessor.setId(this.entityId);
            accessor.setPitch((byte) 0);
            accessor.setYaw((byte) 0);
            accessor.setHeadYaw((byte) 0);
            accessor.setX(pos.x);
            accessor.setY(pos.y - 0.40);
            accessor.setZ(pos.z);
            accessor.setEntityType(Registry.ENTITY_TYPE.getRawId(EntityType.ARMOR_STAND));
            accessor.setUUID(this.uuid);

            player.networkHandler.sendPacket(packet);
        }
        {
            EntityTrackerUpdateS2CPacket packet = new EntityTrackerUpdateS2CPacket();
            EntityTrackerUpdateS2CPacketAccessor accessor = (EntityTrackerUpdateS2CPacketAccessor) packet;

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
        EntityPositionS2CPacket packet = new EntityPositionS2CPacket();
        EntityPositionS2CPacketAccessor accessor = (EntityPositionS2CPacketAccessor) packet;
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
                EntityTrackerUpdateS2CPacket packet = new EntityTrackerUpdateS2CPacket();
                EntityTrackerUpdateS2CPacketAccessor accessor = (EntityTrackerUpdateS2CPacketAccessor) packet;

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
