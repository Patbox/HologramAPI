package eu.pb4.holograms.api.elements.text;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.mixin.accessors.AreaEffectCloudEntityAccessor;
import eu.pb4.holograms.mixin.accessors.EntityAccessor;
import eu.pb4.holograms.mixin.accessors.EntityPositionS2CPacketAccessor;
import eu.pb4.holograms.impl.HologramHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaticTextHologramElement extends AbstractTextHologramElement {

    public StaticTextHologramElement() {
        this(Text.empty());
    }

    public StaticTextHologramElement(Text text) {
        super(text);
        this.entityId = EntityAccessor.getMaxEntityId().incrementAndGet();
        this.uuid = HologramHelper.getUUID();
        this.getEntityIds().add(this.entityId);
    }

    @Override
    public void createSpawnPackets(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);
        player.networkHandler.sendPacket(new EntitySpawnS2CPacket(this.entityId, this.uuid, pos.x, pos.y - 0.9, pos.z, 0, 0, EntityType.AREA_EFFECT_CLOUD, 0, Vec3d.ZERO, 0));

        List<DataTracker.SerializedEntry<?>> data = new ArrayList<>();
        data.add(DataTracker.SerializedEntry.of(AreaEffectCloudEntityAccessor.getRadius(), 0f));
        data.add(DataTracker.SerializedEntry.of(EntityAccessor.getCustomName(), Optional.of(this.getTextFor(player))));
        data.add(DataTracker.SerializedEntry.of(EntityAccessor.getNameVisible(), true));

        player.networkHandler.sendPacket(new EntityTrackerUpdateS2CPacket(this.entityId, data));
    }

    @Override
    public void updatePosition(ServerPlayerEntity player, AbstractHologram hologram) {
        var packet = HologramHelper.createUnsafe(EntityPositionS2CPacket.class);
        EntityPositionS2CPacketAccessor accessor = (EntityPositionS2CPacketAccessor) packet;
        accessor.setId(this.entityId);
        Vec3d pos = hologram.getElementPosition(this).add(this.getOffset());
        accessor.setX(pos.x);
        accessor.setY(pos.y - 0.9);
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
                List<DataTracker.SerializedEntry<?>> data = new ArrayList<>();
                data.add(DataTracker.SerializedEntry.of(EntityAccessor.getCustomName(), Optional.of(this.getTextFor(player))));

                player.networkHandler.sendPacket(new EntityTrackerUpdateS2CPacket(this.entityId, data));
            }

            this.isDirty = false;
        }

        super.onTick(hologram);
    }
}
