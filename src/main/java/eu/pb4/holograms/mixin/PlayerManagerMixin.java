package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.holograms.AbstractHologram;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Unique private Team fakeTeam = null;

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void sendFakeTeam(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        player.networkHandler.sendPacket(new TeamS2CPacket(this.getFakeTeam(), 0));
    }

    @Unique
    private Team getFakeTeam() {
        if (this.fakeTeam == null) {
            Scoreboard scoreboard = new Scoreboard();
            this.fakeTeam = new Team(scoreboard, "■HoloApiFakeTeam");
            scoreboard.addPlayerToTeam(AbstractHologram.HOLOGRAM_ENTITY_UUID.toString(), this.fakeTeam);
            this.fakeTeam.setCollisionRule(AbstractTeam.CollisionRule.NEVER);
        }

        return fakeTeam;
    }


}
