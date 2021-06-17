package eu.pb4.holograms.api.elements;

import eu.pb4.holograms.api.InteractionType;
import eu.pb4.holograms.api.holograms.AbstractHologram;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public interface HologramElement {
    double getHeight();
    Vec3d getOffset();
    IntList getEntityIds();
    void createSpawnPackets(ServerPlayerEntity player, AbstractHologram hologram);
    void createRemovePackets(ServerPlayerEntity player, AbstractHologram hologram);
    void updatePosition(ServerPlayerEntity player, AbstractHologram hologram);
    void onTick(AbstractHologram hologram);
    void onClick(AbstractHologram hologram, ServerPlayerEntity player, InteractionType type, @Nullable Hand hand, @Nullable Vec3d vec, int entityId);
}
