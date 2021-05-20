package eu.pb4.holograms.api.holograms;

import com.google.common.collect.Lists;
import eu.pb4.holograms.api.elements.*;
import eu.pb4.holograms.interfaces.HologramHolder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractHologram {
    protected final ServerWorld world;
    protected List<HologramElement> elements = new ArrayList<>();
    protected Vec3d position;
    protected Set<ServerPlayerEntity> players = new HashSet<>();
    protected boolean isActive = false;

    public AbstractHologram(ServerWorld world, Vec3d position) {
        this.world = world;
        this.position = new Vec3d(position.x, position.y, position.z);
    }

    protected void updatePosition(Vec3d position) {
        this.position = new Vec3d(position.x, position.y, position.z);
        this.sendPositionUpdate();
    }

    protected void sendPositionUpdate() {
        if (isActive) {
            for (HologramElement element : this.elements) {
                if (element.getEntityIds().size() == 0) {
                    continue;
                }

                for (ServerPlayerEntity player : this.players) {
                    element.updatePosition(player, this);
                }
            }
        }
    }

    public int addText(Text text) {
        return this.addElement(new TextHologramElement(text));
    }

    public int addText(int pos, Text text) {
        return this.addElement(pos, new TextHologramElement(text));
    }

    public int setText(int pos, Text text) {
        return this.setElement(pos, new TextHologramElement(text));
    }

    public int addItemStack(ItemStack stack, boolean isStatic) {
        return this.addElement(isStatic ? new StaticItemHologramElement(stack) : new SpinningItemHologramElement(stack));
    }

    public int addItemStack(int pos, ItemStack stack, boolean isStatic) {
        return this.addElement(pos, isStatic ? new StaticItemHologramElement(stack) : new SpinningItemHologramElement(stack));
    }

    public int setItemStack(int pos, ItemStack stack, boolean isStatic) {
        return this.setElement(pos, isStatic ? new StaticItemHologramElement(stack) : new SpinningItemHologramElement(stack));
    }

    public int addElement(HologramElement element) {
        this.elements.add(element);
        if (isActive) {
            for (ServerPlayerEntity player : this.players) {
                element.createPackets(player, this);
            }
        }
        this.sendPositionUpdate();

        return this.elements.indexOf(element);
    }

    public int addElement(int pos, HologramElement element) {
        this.elements.add(pos, element);
        if (isActive) {
            for (ServerPlayerEntity player : this.players) {
                element.createPackets(player, this);
            }
        }
        this.sendPositionUpdate();

        return this.elements.indexOf(element);
    }

    public int setElement(int pos, HologramElement element) {
        if (pos >= 0) {
            if (pos >= this.elements.size()) {
                int needed = pos - this.elements.size();
                for (int x = 0; x < needed; x++) {
                    this.elements.add(new EmptyHologramElement());
                }
                this.elements.add(element);
            } else {
                int[] ids = this.elements.get(pos).getEntityIds().toIntArray();
                this.elements.set(pos, element);

                if (isActive) {
                    if (ids.length > 0) {
                        Packet packet = new EntitiesDestroyS2CPacket(ids);
                        for (ServerPlayerEntity player : this.players) {
                            player.networkHandler.sendPacket(packet);
                        }
                    }

                    for (ServerPlayerEntity player : this.players) {
                        element.createPackets(player, this);
                    }

                    this.sendPositionUpdate();
                }
            }

            return pos;
        }

        return -1;
    }

    @Nullable
    public HologramElement getElement(int pos) {
        if (pos >= 0 && pos < this.elements.size()) {
            return this.elements.get(pos);
        }

        return null;
    }

    public int getElementIndex(HologramElement element) {
        return this.elements.indexOf(element);
    }

    @Nullable
    public Vec3d getElementPosition(HologramElement element) {
        double height = 0;
        for (HologramElement other : Lists.reverse(elements)) {
            if (other == element) {
                return new Vec3d(this.position.x, this.position.y + height, this.position.z);
            }
            height += other.getHeight();
        }

        return null;
    }

    public void tick() {
        if (isActive && this.players.size() > 0) {
            for (HologramElement element : this.elements) {
                element.onTick(this);
            }
        }
    }

    public void create() {
        if (!isActive) {
            this.isActive = true;
            for (ServerPlayerEntity player : this.players) {
                for (HologramElement element : this.elements) {
                    if (element.getEntityIds().size() == 0) {
                        continue;
                    }
                    element.createPackets(player, this);
                }
            }
        }
    }

    public void remove() {
        if (isActive) {
            this.isActive = false;
            Packet packet = this.getDestroyPacket();

            for (ServerPlayerEntity player : this.players) {
                if (!player.isDisconnected()) {
                    player.networkHandler.sendPacket(packet);
                }
            }
        }
    }

    protected Packet<?> getDestroyPacket() {
        IntList list = new IntArrayList();

        for (HologramElement element : this.elements) {
            if (element.getEntityIds().size() == 0) {
                continue;
            }
            list.addAll(element.getEntityIds());
        }

        return new EntitiesDestroyS2CPacket(list.toIntArray());
    }

    public void addPlayer(ServerPlayerEntity player) {
        if (!this.players.contains(player)) {
            this.players.add(player);
            ((HologramHolder) player).addHologram(this);

            if (isActive) {
                for (HologramElement element : this.elements) {
                    if (element.getEntityIds().size() == 0) {
                        continue;
                    }
                    element.createPackets(player, this);
                }
            }
        }
    }

    public void removePlayer(ServerPlayerEntity player) {
        if (this.players.contains(player)) {
            this.players.remove(player);
            ((HologramHolder) player).removeHologram(this);
            if (isActive) {
                if (!player.isDisconnected()) {
                    player.networkHandler.sendPacket(this.getDestroyPacket());
                }
            }
        }
    }

    public Set<ServerPlayerEntity> getPlayerSet() {
        return Collections.unmodifiableSet(this.players);
    }

    public boolean canAddPlayer(ServerPlayerEntity player) {
        return true;
    }
}
