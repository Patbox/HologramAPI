package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.InteractionType;
import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.interfaces.HologramHolder;
import eu.pb4.holograms.mixin.accessors.PlayerInteractEntityC2SPacketAccessor;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onPlayerInteractEntity", at = @At("TAIL"))
    private void interactWithHologram(PlayerInteractEntityC2SPacket packet, CallbackInfo ci) {
        if (packet.getEntity(this.player.world) == null) {
            int id = ((PlayerInteractEntityC2SPacketAccessor) packet).getEntityId();
            for (AbstractHologram hologram : ((HologramHolder) this.player).getHologramSet()) {
                if (hologram.getEntityIds().contains(id)) {

                    switch (packet.getType()) {
                        case INTERACT:
                            hologram.click(player,
                                    InteractionType.INTERACT,
                                    packet.getHand(),
                                    Vec3d.ZERO,
                                    id);
                            break;

                        case INTERACT_AT:
                            hologram.click(player,
                                    InteractionType.INTERACT,
                                    packet.getHand(),
                                    packet.getHitPosition(),
                                    id);
                            break;

                        case ATTACK:
                            hologram.click(player,
                                    InteractionType.ATTACK,
                                    Hand.MAIN_HAND,
                                    Vec3d.ZERO,
                                    id);
                    }
                    return;
                }
            }
        }
    }
}
