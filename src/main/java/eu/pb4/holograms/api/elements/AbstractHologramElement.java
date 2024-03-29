package eu.pb4.holograms.api.elements;

import eu.pb4.holograms.api.InteractionType;
import eu.pb4.holograms.api.holograms.AbstractHologram;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

/**
 * @deprecated Use <a href="https://github.com/Patbox/polymer">Polymer</a>'s virtual entities instead.
 */
@Deprecated
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
    public abstract void createSpawnPackets(ServerPlayerEntity player, AbstractHologram hologram);

    @Override
    public void createRemovePackets(ServerPlayerEntity player, AbstractHologram hologram) {
        player.networkHandler.sendPacket(new EntitiesDestroyS2CPacket(this.entityIds));
    }

    @Override
    public void onTick(AbstractHologram hologram) {}

    @Override
    public void onClick(AbstractHologram hologram, ServerPlayerEntity player, InteractionType type, @Nullable Hand hand, @Nullable Vec3d vec, int entityId) {}
}
