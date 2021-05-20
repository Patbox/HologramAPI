package eu.pb4.holograms.api.elements;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public interface HologramElement {
    double getHeight();
    Vec3d getOffset();
    IntList getEntityIds();
    void createPackets(ServerPlayerEntity player, AbstractHologram hologram);
    void updatePosition(ServerPlayerEntity player, AbstractHologram hologram);
    void onTick(AbstractHologram hologram);
}
