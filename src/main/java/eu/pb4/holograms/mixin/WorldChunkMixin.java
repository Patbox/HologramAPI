package eu.pb4.holograms.mixin;

import eu.pb4.holograms.api.holograms.WorldHologram;
import eu.pb4.holograms.interfaces.HologramHolder;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.TickScheduler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Set;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin extends Chunk implements HologramHolder<WorldHologram> {
    public WorldChunkMixin(ChunkPos chunkPos, UpgradeData upgradeData, HeightLimitView heightLimitView, Registry<Biome> registry, long l, @Nullable ChunkSection[] chunkSections, TickScheduler<Block> tickScheduler, TickScheduler<Fluid> tickScheduler2) {
        super(chunkPos, upgradeData, heightLimitView, registry, l, chunkSections, tickScheduler, tickScheduler2);
    }

    @Shadow public abstract World getWorld();

    @Unique
    private final Set<WorldHologram> holograms = new HashSet<>();

    public void addHologram(WorldHologram hologram) {
        this.holograms.add(hologram);
        ((ServerChunkManager) this.getWorld().getChunkManager()).threadedAnvilChunkStorage.getPlayersWatchingChunk(this.getPos(), false)
                .forEach((p) -> {
                    if (hologram.canAddPlayer(p)) {
                        hologram.addPlayer(p);
                    }
                });
    }

    public void removeHologram(WorldHologram hologram) {
        this.holograms.remove(hologram);
    }

    public Set<WorldHologram> getHologramSet() {
        return this.holograms;
    }
}
