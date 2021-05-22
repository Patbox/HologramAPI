package eu.pb4.holograms;

import com.mojang.brigadier.context.CommandContext;
import eu.pb4.holograms.api.InteractionType;
import eu.pb4.holograms.api.elements.SpacingHologramElement;
import eu.pb4.holograms.api.elements.clickable.CubeHitboxHologramElement;
import eu.pb4.holograms.api.elements.clickable.EntityHologramElement;
import eu.pb4.holograms.api.holograms.AbstractHologram;
import eu.pb4.holograms.api.holograms.EntityHologram;
import eu.pb4.holograms.api.holograms.WorldHologram;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.server.command.CommandManager.literal;

public class TestMod implements ModInitializer {
    static int pos = -1;

    private static int test(CommandContext<ServerCommandSource> objectCommandContext) {
        try {
            ServerPlayerEntity player = objectCommandContext.getSource().getPlayer();

            WorldHologram hologram = new WorldHologram(player.getServerWorld(), player.getPos());

            hologram.addText(new LiteralText("hello"));
            hologram.addElement(new EntityHologramElement(getEntityType(false).create(player.world)));
            hologram.addText(new LiteralText("test"));
            hologram.addItemStack(Items.POTATO.getDefaultStack(), false);
            hologram.addItemStack(Items.DIAMOND.getDefaultStack(), true);
            hologram.addText(new LiteralText("« »"));
            hologram.addElement(new CubeHitboxHologramElement(2, new Vec3d(0, -0.2, 0)) {
                @Override
                public void onClick(AbstractHologram hologram, ServerPlayerEntity player, InteractionType type, @Nullable Hand hand, @Nullable Vec3d vec, int entityId) {
                    super.onClick(hologram, player, type, hand, vec, entityId);
                    hologram.setElement(1, new EntityHologramElement(getEntityType(type == InteractionType.ATTACK).create(player.world)));
                }
            });
            hologram.addText(new LiteralText("434234254234562653247y4575678rt").formatted(Formatting.AQUA));

            hologram.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static EntityType getEntityType(boolean previous) {
        if (previous) {
            pos--;
        } else {
            pos++;
        }

        EntityType type = Registry.ENTITY_TYPE.get(pos);

        if (type == null) {
            pos = 0;
            type = Registry.ENTITY_TYPE.get(pos);
        }

        System.out.println(type);

        return type;
    }

    private static int test2(CommandContext<ServerCommandSource> objectCommandContext) {
        try {
            ServerPlayerEntity player = objectCommandContext.getSource().getPlayer();

            WorldHologram hologram = new WorldHologram(player.getServerWorld(), player.getPos());

            hologram.addText(new LiteralText("hello"));
            hologram.addElement(new CubeHitboxHologramElement(1, new Vec3d(0, 0, 0)) {
                @Override
                public void onClick(AbstractHologram hologram, ServerPlayerEntity player, InteractionType type, @Nullable Hand hand, @Nullable Vec3d vec, int entityId) {
                    super.onClick(hologram, player, type, hand, vec, entityId);
                    hologram.setAlignment(AbstractHologram.VerticalAlign.TOP);
                }
            });
            hologram.addText(new LiteralText("test"));
            hologram.addItemStack(Items.POTATO.getDefaultStack(), false);
            hologram.addElement(new CubeHitboxHologramElement(1, new Vec3d(0, 0, 0)) {
                @Override
                public void onClick(AbstractHologram hologram, ServerPlayerEntity player, InteractionType type, @Nullable Hand hand, @Nullable Vec3d vec, int entityId) {
                    super.onClick(hologram, player, type, hand, vec, entityId);
                    hologram.setAlignment(AbstractHologram.VerticalAlign.CENTER);
                }
            });
            hologram.addItemStack(Items.DIAMOND.getDefaultStack(), true);
            hologram.addText(new LiteralText("« »"));
            hologram.addElement(new CubeHitboxHologramElement(1, new Vec3d(0, -0.2, 0)) {
                @Override
                public void onClick(AbstractHologram hologram, ServerPlayerEntity player, InteractionType type, @Nullable Hand hand, @Nullable Vec3d vec, int entityId) {
                    super.onClick(hologram, player, type, hand, vec, entityId);
                    hologram.setAlignment(AbstractHologram.VerticalAlign.BOTTOM);
                }
            });

            hologram.build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int test3(CommandContext<ServerCommandSource> objectCommandContext) {
        try {
            ServerPlayerEntity player = objectCommandContext.getSource().getPlayer();

            PigEntity pig = EntityType.PIG.create(player.world);
            pig.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), 0, 0);

            System.out.println(pig);
            EntityHologram hologram = new EntityHologram(pig, new Vec3d(2, 2, 2));

            hologram.addText(new LiteralText("Hello There"));
            hologram.addItemStack(Items.DIAMOND.getDefaultStack(), true);
            hologram.build();

            player.world.spawnEntity(pig);

            System.out.println(hologram.getEntityIds());
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
            hologram.addText(new LiteralText("(Static)"));
            hologram.addText(new LiteralText("Hello!"), false);
            hologram.addText(new LiteralText("(Non Static)"), false);

            hologram.addItemStack(Items.DIAMOND.getDefaultStack(), true);
            hologram.addItemStack(Items.IRON_AXE.getDefaultStack(), false);

            hologram.build();
            System.out.println(hologram.getEntityIds());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

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

}
