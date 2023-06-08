package eu.pb4.holograms.api;

import eu.pb4.holograms.api.elements.HologramElement;
import eu.pb4.holograms.api.holograms.EntityHologram;
import eu.pb4.holograms.api.holograms.WorldHologram;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

/**
 * @deprecated Use <a href="https://github.com/Patbox/polymer">Polymer</a>'s virtual entities instead.
 */
@Deprecated
public class Holograms {
    public static WorldHologram create(ServerWorld world, Vec3d pos, Text... texts) {
        WorldHologram hologram = new WorldHologram(world, pos);
        for (Text text : texts) {
            hologram.addText(text);
        }
        return hologram;
    }

    public static WorldHologram create(ServerWorld world, Vec3d pos, HologramElement... elements) {
        WorldHologram hologram = new WorldHologram(world, pos);
        for (HologramElement element : elements) {
            hologram.addElement(element);
        }
        return hologram;
    }

    public static EntityHologram create(Entity entity, Vec3d offset, Text... texts) {
        EntityHologram hologram = new EntityHologram(entity, offset);
        for (Text text : texts) {
            hologram.addText(text);
        }
        return hologram;
    }

    public static EntityHologram create(Entity entity, Vec3d offset, HologramElement... elements) {
        EntityHologram hologram = new EntityHologram(entity, offset);
        for (HologramElement element : elements) {
            hologram.addElement(element);
        }
        return hologram;
    }
}
