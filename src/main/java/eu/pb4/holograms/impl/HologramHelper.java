package eu.pb4.holograms.impl;

import eu.pb4.holograms.api.holograms.WorldHologram;
import eu.pb4.holograms.impl.interfaces.HologramHolder;
import eu.pb4.holograms.impl.interfaces.WorldHologramHolder;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
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

    public static <T> T createUnsafe(Class<T> tClass) {
        try {
            return (T) UnsafeAccess.UNSAFE.allocateInstance(tClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean attachToChunk(WorldHologram hologram, ChunkPos pos) {
        ((WorldHologramHolder) hologram.getWorld()).hologramApi$addHologram(hologram, pos);
        Chunk chunk = hologram.getWorld().getChunk(pos.x, pos.z, ChunkStatus.FULL, false);
        if (chunk instanceof HologramHolder holder) {
            holder.hologramApi$addHologram(hologram);
            return true;
        }
        return false;
    }

    public static boolean detachFromChunk(WorldHologram hologram, ChunkPos pos) {
        ((WorldHologramHolder) hologram.getWorld()).hologramApi$removeHologram(hologram, pos);
        Chunk chunk = hologram.getWorld().getChunk(pos.x, pos.z, ChunkStatus.FULL, false);
        if (chunk instanceof HologramHolder holder) {
            holder.hologramApi$removeHologram(hologram);
            hologram.clearPlayers();
            return true;
        }
        return false;
    }

}
