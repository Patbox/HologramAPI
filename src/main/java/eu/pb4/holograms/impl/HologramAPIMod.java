package eu.pb4.holograms.impl;

import eu.pb4.holograms.api.InteractionType;
import eu.pb4.holograms.api.holograms.AbstractHologram;
import net.fabricmc.api.ModInitializer;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class HologramAPIMod implements ModInitializer {
    public static final PlayerInteractEntityC2SPacket.Handler interactHelper(AbstractHologram hologram, PlayerInteractEntityC2SPacket packet, int id, ServerPlayerEntity player) {
        return new PlayerInteractEntityC2SPacket.Handler() {
            @Override
            public void interact(Hand hand) {
                hologram.click(player,
                        InteractionType.INTERACT,
                        hand,
                        Vec3d.ZERO,
                        id);
            }

            @Override
            public void interactAt(Hand hand, Vec3d pos) {
                hologram.click(player,
                        InteractionType.INTERACT,
                        hand,
                        pos,
                        id);
            }

            @Override
            public void attack() {
                hologram.click(player,
                        InteractionType.ATTACK,
                        Hand.MAIN_HAND,
                        Vec3d.ZERO,
                        id);
            }
        };
    }

    @Override
    public void onInitialize() {
    }
}
