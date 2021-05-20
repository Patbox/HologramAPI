package eu.pb4.holograms.api.elements;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.mixin.accessors.EntityAccessor;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public abstract class AbstractHologramElement implements HologramElement {
    protected final IntList entityIds = new IntArrayList();
    protected double height;
    protected Vec3d offset;

    protected AbstractHologramElement() {
        this.offset = Vec3d.ZERO;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public Vec3d getOffset() { return this.offset; }

    @Override
    public IntList getEntityIds() {
        return this.entityIds;
    }

    @Override
    public abstract void createPackets(ServerPlayerEntity player, AbstractHologram hologram);

    protected int createEntityId() {
        return EntityAccessor.getMaxEntityId().incrementAndGet();
    }

    @Override
    public void onTick(AbstractHologram hologram) {}
}
