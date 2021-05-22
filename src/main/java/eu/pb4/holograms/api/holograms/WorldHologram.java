package eu.pb4.holograms.api.holograms;

import eu.pb4.holograms.interfaces.HologramHolder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;

public class WorldHologram extends AbstractHologram {
    protected WorldChunk chunk;
    protected ChunkPos chunkPos;

    public WorldHologram(ServerWorld world, Vec3d position) {
        super(world, position, VerticalAlign.TOP);
        this.chunk = world.getChunk(((int) position.x) >> 4, ((int) position.z) >> 4);
        this.chunkPos = this.chunk.getPos();
    }

    public WorldHologram(ServerWorld world, Vec3d position, VerticalAlign alignment) {
        super(world, position, alignment);
        this.chunk = world.getChunk(((int) position.x) >> 4, ((int) position.z) >> 4);
        this.chunkPos = this.chunk.getPos();
    }


    public void setPosition(Vec3d vec) {
        this.updatePosition(vec);
        WorldChunk chunk = world.getChunk(((int) position.x) >> 4, ((int) position.z) >> 4);

        if (chunk != this.chunk) {
            ((HologramHolder) this.chunk).removeHologram(this);
            if (chunk != null) {
                if (this.isActive) {
                    ((HologramHolder) chunk).addHologram(this);
                }
                this.chunk = chunk;
                this.chunkPos = this.chunk.getPos();
            } else {
                this.chunk = null;
                this.chunkPos = null;
            }
        }
    }

    @Override
    public void build() {
        this.world.getChunkManager().threadedAnvilChunkStorage.getPlayersWatchingChunk(this.chunkPos, false)
                .forEach( player -> this.addPlayer(player));
        ((HologramHolder) chunk).addHologram(this);
        super.build();
    }

    @Override
    public void remove() {
        super.remove();
        ((HologramHolder) chunk).removeHologram(this);
    }

    public Vec3d getPosition() {
        return this.position;
    }

    public ChunkPos getChunkPos() {
        return this.chunkPos;
    }

    public void onChunkUnload() {

    }
}
