package eu.pb4.holograms.utils;

import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;

import java.util.UUID;

public class HologramHelper {
    private static long UP = 834995665469899l;
    private static long DOWN = 0;
    private static Team FAKE_TEAM;

    public static UUID getUUID() {
        UUID uuid = new UUID(UP, DOWN);
        DOWN++;
        return uuid;
    }

    public static Team getFakeTeam() {
        if (FAKE_TEAM == null) {
            Scoreboard scoreboard = new Scoreboard();
            FAKE_TEAM = new Team(scoreboard, "■HoloApiFakeTeam");
            FAKE_TEAM.setCollisionRule(AbstractTeam.CollisionRule.NEVER);
        }

        return FAKE_TEAM;
    }

}
