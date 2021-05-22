package eu.pb4.holograms.api.holograms;

import eu.pb4.holograms.interfaces.EntityHologramHolder;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class EntityHologram extends AbstractHologram {
    protected Vec3d offset;
    protected Entity entity;

    public EntityHologram(Entity entity, Vec3d offset) {
        super((ServerWorld) entity.world, entity.getPos().add(offset), VerticalAlign.TOP);
        this.offset = offset;
        this.entity = entity;
    }

    public EntityHologram(Entity entity, Vec3d offset, VerticalAlign alignment) {
        super((ServerWorld) entity.world, entity.getPos().add(offset), alignment);
        this.offset = offset;
        this.entity = entity;
    }

    public Vec3d getOffset() {
        return this.offset;
    }

    public void setOffset(Vec3d offset) {
        this.offset = offset;

        this.updatePosition(this.entity.getPos().add(this.offset));
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void setEntity(Entity entity) {
        if (this.isActive) {
            ((EntityHologramHolder) this.entity).removeEntityHologram(this);
            ((EntityHologramHolder) entity).addEntityHologram(this);
        }

        this.entity = entity;
    }

    @Override
    public void show() {
        ((EntityHologramHolder) this.entity).addEntityHologram(this);
        super.show();
    }

    @Override
    public void hide() {
        ((EntityHologramHolder) this.entity).removeEntityHologram(this);
        super.hide();
    }

    public void syncPositionWithEntity() {
        this.updatePosition(this.entity.getPos().add(this.offset));
    }
}
