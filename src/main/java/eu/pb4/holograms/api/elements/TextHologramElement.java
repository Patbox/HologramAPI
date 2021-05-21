package eu.pb4.holograms.api.elements;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.mixin.accessors.AreaEffectCloudEntityAccessor;
import eu.pb4.holograms.mixin.accessors.EntityAccessor;
import eu.pb4.holograms.mixin.accessors.EntityPositionS2CPacketAccessor;
import eu.pb4.holograms.mixin.accessors.EntityTrackerUpdateS2CPacketAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TextHologramElement extends AbstractHologramElement {
    protected Text text;
    protected int entityId;

    public TextHologramElement() {
        this(new LiteralText(""));
    }

    public TextHologramElement(Text text) {
        super();
        this.entityId = EntityAccessor.getMaxEntityId().incrementAndGet();
        this.getEntityIds().add(this.entityId);
        this.text = text;
        this.height = 0.26;
    }

    public Text getText() {
        return this.text;
    }

    public Text getTextFor(ServerPlayerEntity player) {
        return this.getText();
    }

    public void setText(Text text) {
        this.text = text;
    }

    @Override
    public void createPackets(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);
        player.networkHandler.sendPacket(new EntitySpawnS2CPacket(this.entityId, AbstractHologram.HOLOGRAM_ENTITY_UUID, pos.x, pos.y - 0.9, pos.z, 0, 0, EntityType.AREA_EFFECT_CLOUD, 0, Vec3d.ZERO));

        EntityTrackerUpdateS2CPacket packet = new EntityTrackerUpdateS2CPacket();
        EntityTrackerUpdateS2CPacketAccessor accessor = (EntityTrackerUpdateS2CPacketAccessor) packet;

        accessor.setId(this.entityId);

        List<DataTracker.Entry<?>> data = new ArrayList<>();
        data.add(new DataTracker.Entry<>(AreaEffectCloudEntityAccessor.getRadius(), 0f));
        data.add(new DataTracker.Entry<>(EntityAccessor.getCustomName(), Optional.of(this.getTextFor(player))));
        data.add(new DataTracker.Entry<>(EntityAccessor.getNameVisible(), true));
        accessor.setTrackedValues(data);

        player.networkHandler.sendPacket(packet);
    }

    @Override
    public void updatePosition(ServerPlayerEntity player, AbstractHologram hologram) {
        EntityPositionS2CPacket packet = new EntityPositionS2CPacket();
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
}
