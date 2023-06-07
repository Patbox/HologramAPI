package eu.pb4.holograms.api.elements.clickable;

import eu.pb4.holograms.api.elements.AbstractHologramElement;
import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.mixin.accessors.EntityAccessor;
import eu.pb4.holograms.impl.HologramHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class EntityHologramElement extends AbstractHologramElement {
    protected final Entity entity;

    public EntityHologramElement(Entity entity) {
        this.height = entity.getHeight() + 0.1;
        this.entityIds.add(entity.getId());
        this.entity = entity;

        if (this.entity.getWorld().getEntityById(this.entity.getId()) != null) {
            throw new IllegalArgumentException("Entity can't exist in world!");
        }
        this.entity.setUuid(HologramHelper.getUUID());
    }

    @Override
    public void createSpawnPackets(ServerPlayerEntity player, AbstractHologram hologram) {
        Vec3d pos = hologram.getElementPosition(this).add(this.offset);
        this.entity.setPos(pos.x, pos.y - 0.05, pos.z);

        player.networkHandler.sendPacket(this.entity.createSpawnPacket());

        List<DataTracker.SerializedEntry<?>> data = new ArrayList<>();
        data.addAll(this.entity.getDataTracker().getChangedEntries());
        data.add(DataTracker.SerializedEntry.of(EntityAccessor.getNoGravity(), true));

        player.networkHandler.sendPacket(new EntityTrackerUpdateS2CPacket(this.entity.getId(), data));
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
        this.entity.setPos(pos.x, pos.y - 0.05, pos.z);

        this.createRemovePackets(player, hologram);
        this.createSpawnPackets(player, hologram);

    }
}
