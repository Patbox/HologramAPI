package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.InteractionType;
import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.interfaces.HologramHolder;
import eu.pb4.holograms.mixin.accessors.PlayerInteractEntityC2SPacketAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "onPlayerInteractEntity", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void interactWithHologram(PlayerInteractEntityC2SPacket packet, CallbackInfo ci, Entity entity) {
        if (entity == null) {
            int id = ((PlayerInteractEntityC2SPacketAccessor) packet).getEntityId();
            for (AbstractHologram hologram : ((HologramHolder) this.player).getHologramSet()) {
                if (hologram.getEntityIds().contains(id)) {
                    hologram.click(this.player,
                            packet.getType() == PlayerInteractEntityC2SPacket.InteractionType.ATTACK ? InteractionType.ATTACK : InteractionType.INTERACT,
                            packet.getHand(),
                            packet.getHitPosition(),
                            id);
                    return;
                }
            }
        }
    }
}
