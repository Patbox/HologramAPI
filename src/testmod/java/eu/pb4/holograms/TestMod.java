package eu.pb4.holograms;

import com.mojang.brigadier.context.CommandContext;
import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.api.holograms.EntityHologram;
import eu.pb4.holograms.api.holograms.WorldHologram;
import eu.pb4.holograms.api.elements.EmptyHologramElement;
import eu.pb4.holograms.api.elements.EntityHologramElement;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySetHeadYawS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.server.command.CommandManager.literal;

public class TestMod implements ModInitializer {
    static WorldHologram HOLOGRAM = null;
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(
                literal("test").executes(TestMod::test)
            );
            dispatcher.register(
                    literal("test2").executes(TestMod::test2)
            );
            dispatcher.register(
                    literal("test3").executes(TestMod::test3)
            );

            dispatcher.register(
                    literal("test4").executes(TestMod::test4)
            );
        });
    }


    private static int test(CommandContext<ServerCommandSource> objectCommandContext) {
        try {
            ServerPlayerEntity player = objectCommandContext.getSource().getPlayer();
            Entity entity = new CreeperEntity(EntityType.CREEPER, player.world);

            WorldHologram hologram = new WorldHologram(player.getServerWorld(), player.getPos());

            hologram.addText(new LiteralText("hello"));
            hologram.addElement(new EntityHologramElement(entity) {
                byte yaw = 0;

                @Override
                public void onTick(AbstractHologram hologram) {
                    super.onTick(hologram);
                    this.yaw++;
                    Packet packet = new EntitySetHeadYawS2CPacket(this.entity, yaw);
                    Packet packet2 = new EntityS2CPacket.Rotate(this.entity.getEntityId(), this.yaw, this.yaw, true);

                    for (ServerPlayerEntity player : hologram.getPlayerSet()) {
                        player.networkHandler.sendPacket(packet);
                        player.networkHandler.sendPacket(packet2);
                    }
                }
            });
            hologram.addText(new LiteralText("test"));
            hologram.addItemStack(Items.POTATO.getDefaultStack(), false);
            hologram.addItemStack(Items.DIAMOND.getDefaultStack(), true);
            hologram.addText(new LiteralText("2"));
            hologram.setElement(6, new EmptyHologramElement());
            hologram.addText(new LiteralText("434234254234562653247y4575678rt").formatted(Formatting.AQUA));

            hologram.create();

            HOLOGRAM = hologram;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int test2(CommandContext<ServerCommandSource> objectCommandContext) {
        try {
            ServerPlayerEntity player = objectCommandContext.getSource().getPlayer();

            HOLOGRAM.setPosition(player.getPos());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int test3(CommandContext<ServerCommandSource> objectCommandContext) {
        try {
            ServerPlayerEntity player = objectCommandContext.getSource().getPlayer();
            HOLOGRAM.removePlayer(player);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int test4(CommandContext<ServerCommandSource> objectCommandContext) {
        try {
            ServerPlayerEntity player = objectCommandContext.getSource().getPlayer();

            PigEntity pig = EntityType.PIG.create(player.world);
            pig.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), 0, 0);
            player.world.spawnEntity(pig);
            System.out.println(pig);

            EntityHologram hologram = new EntityHologram(pig, new Vec3d(2, 2, 2));

            hologram.addText(new LiteralText("Hello There"));
            hologram.addItemStack(Items.DIAMOND.getDefaultStack(), true);

            hologram.addPlayer(player);
            hologram.create();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
