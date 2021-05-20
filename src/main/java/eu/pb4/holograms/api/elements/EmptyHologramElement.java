package eu.pb4.holograms.api.elements;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class EmptyHologramElement implements HologramElement {
    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public Vec3d getOffset() {
        return Vec3d.ZERO;
    }

    public IntList getEntityIds() {
        return IntLists.EMPTY_LIST;
    }

    @Override
    public void createPackets(ServerPlayerEntity player, AbstractHologram hologram) {}

    @Override
    public void updatePosition(ServerPlayerEntity player, AbstractHologram hologram) {}

    @Override
    public void onTick(AbstractHologram hologram) {}
}
