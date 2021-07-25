package eu.pb4.holograms.api.holograms;

import eu.pb4.holograms.utils.HologramHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

public class WorldHologram extends AbstractHologram {
    protected ChunkPos chunkPos;

    public WorldHologram(ServerWorld world, Vec3d position) {
        super(world, position, VerticalAlign.TOP);
        this.chunkPos = new ChunkPos(((int) position.x) >> 4, ((int) position.z) >> 4);
    }

    public WorldHologram(ServerWorld world, Vec3d position, VerticalAlign alignment) {
        super(world, position, alignment);
        this.chunkPos = new ChunkPos(((int) position.x) >> 4, ((int) position.z) >> 4);
    }

    public void setPosition(Vec3d vec) {
        this.updatePosition(vec);
        ChunkPos newChunkPos = new ChunkPos(((int) position.x) >> 4, ((int) position.z) >> 4);
        if (!newChunkPos.equals(this.chunkPos)) {
            if (this.isActive) {
                HologramHelper.detachFromChunk(this, this.chunkPos);
                HologramHelper.attachToChunk(this, newChunkPos);
            }
            this.chunkPos = newChunkPos;
        }
    }

    @Override
    public void show() {
        HologramHelper.attachToChunk(this, this.chunkPos);
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
        HologramHelper.detachFromChunk(this, this.chunkPos);
    }

    public Vec3d getPosition() {
        return this.position;
    }

    public ChunkPos getChunkPos() {
        return this.chunkPos;
    }


}
