package me.codexadrian.tempad;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WPanel;
import me.codexadrian.tempad.entity.TimedoorEntity;
import me.codexadrian.tempad.items.TempadItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Arrays;

public class Tempad implements ModInitializer {
	public static final int ORANGE = 0xFF_ff8400;
	public static final String MODID = "tempad";
	public static final EntityType<TimedoorEntity> TIMEDOOR_ENTITY_ENTITY_TYPE = FabricEntityTypeBuilder.create(MobCategory.MISC, TimedoorEntity::new).dimensions(EntityDimensions.scalable(.4F,2.3F)).build();
	public static final TempadItem TEMPAD = new TempadItem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TOOLS));
	public static final Logger LOGGER = LogManager.getLogger();
	public static final ResourceLocation TIMEDOOR_PACKET = new ResourceLocation(MODID, "timedoor");
	public static final ResourceLocation LOCATION_PACKET = new ResourceLocation(MODID, "location");
	public static final ResourceLocation SET_COLOR_PACKET = new ResourceLocation(MODID, "color");
	@Override
	public void onInitialize() {
		Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(MODID, "timedoor"), TIMEDOOR_ENTITY_ENTITY_TYPE);
		Registry.register(Registry.ITEM, new ResourceLocation(MODID, "tempad"), TEMPAD);

		ServerPlayNetworking.registerGlobalReceiver(SET_COLOR_PACKET, (server, player, handler, buf, responseSender) -> {
			int color = buf.readInt();
			InteractionHand hand = buf.readEnum(InteractionHand.class);
			server.execute(() -> {
				ItemStack stack = player.getItemInHand(hand);
				stack.getOrCreateTag().putInt("color", color);
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(TIMEDOOR_PACKET, (server, player, handler, buf, responseSender) -> {
			BlockPos target = buf.readBlockPos();
			server.execute(() -> TempadItem.summonTimeDoor(target, player));
		});

		ServerPlayNetworking.registerGlobalReceiver(LOCATION_PACKET, (server, player, handler, buf, responseSender) -> {
			long longPos = player.blockPosition().asLong();
			InteractionHand hand = buf.readEnum(InteractionHand.class);
			server.execute(() -> {
				ItemStack stack = player.getItemInHand(hand);
				long[] positions = new long[0];
				if(stack.hasTag()) {
					positions = stack.getTag().getLongArray("locations");
				}
				positions = Arrays.copyOf(positions, positions.length + 1);
				positions[positions.length - 1] = longPos;
				stack.getOrCreateTag().putLongArray("locations", positions);
			});
		});
	}

	public static void drawUnifiedBackground(WPanel root, int color) {
		root.setBackgroundPainter((matrices, left, top, panel) -> {
			ScreenDrawing.coloredRect(matrices, left - 2, top - 2, 484, 260, color);
			ScreenDrawing.texturedRect(matrices, left, top, 480, 256, new ResourceLocation(MODID, "textures/widget/tempad_grid.png"), 0, 0, 30, 16, blend(Color.getColor("orange", color), Color.gray).getRGB());
		});
	}
	public static Color blend(Color c0, Color c1) {
		double totalAlpha = c0.getAlpha() + c1.getAlpha();
		double weight0 = c0.getAlpha() / totalAlpha;
		double weight1 = c1.getAlpha() / totalAlpha;

		double r = weight0 * c0.getRed() + weight1 * c1.getRed();
		double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
		double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
		double a = Math.max(c0.getAlpha(), c1.getAlpha());

		return new Color((int) r, (int) g, (int) b, (int) a);
	}

}
