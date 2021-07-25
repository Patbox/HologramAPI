package eu.pb4.holograms.utils;

import eu.pb4.holograms.api.holograms.WorldHologram;
import eu.pb4.holograms.interfaces.HologramHolder;
import eu.pb4.holograms.interfaces.WorldHologramHolder;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;

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


    public static boolean attachToChunk(WorldHologram hologram, ChunkPos pos) {
        ((WorldHologramHolder) hologram.getWorld()).addHologram(hologram, pos);
        Chunk chunk = hologram.getWorld().getChunk(pos.x, pos.z, ChunkStatus.FULL, false);
        if (chunk instanceof HologramHolder holder) {
            holder.addHologram(hologram);
            return true;
        }
        return false;
    }

    public static boolean detachFromChunk(WorldHologram hologram, ChunkPos pos) {
        ((WorldHologramHolder) hologram.getWorld()).removeHologram(hologram, pos);
        Chunk chunk = hologram.getWorld().getChunk(pos.x, pos.z, ChunkStatus.FULL, false);
        if (chunk instanceof HologramHolder holder) {
            holder.removeHologram(hologram);
            hologram.clearPlayers();
            return true;
        }
        return false;
    }

}
