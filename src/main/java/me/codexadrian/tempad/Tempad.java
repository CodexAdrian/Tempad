package me.codexadrian.tempad;

import me.codexadrian.tempad.entity.TimedoorEntity;
import me.codexadrian.tempad.items.TempadItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
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

import java.util.Arrays;

public class Tempad implements ModInitializer {
	public static final String MODID = "tempad";
	public static final EntityType<TimedoorEntity> TIMEDOOR_ENTITY_ENTITY_TYPE = FabricEntityTypeBuilder.create(MobCategory.MISC, TimedoorEntity::new).dimensions(EntityDimensions.scalable(.4F,2.3F)).build();
	public static final TempadItem TEMPAD = new TempadItem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TOOLS));
	public static final Logger LOGGER = LogManager.getLogger();
	public static final ResourceLocation TIMEDOOR_PACKET = new ResourceLocation(MODID, "timedoor");
	public static final ResourceLocation LOCATION_PACKET = new ResourceLocation(MODID, "location");
	@Override
	public void onInitialize() {
		Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(MODID, "timedoor"), TIMEDOOR_ENTITY_ENTITY_TYPE);
		Registry.register(Registry.ITEM, new ResourceLocation(MODID, "tempad"), TEMPAD);
		ServerPlayNetworking.registerGlobalReceiver(TIMEDOOR_PACKET, (server, player, handler, buf, responseSender) -> {
			InteractionHand hand = buf.readEnum(InteractionHand.class);
			ItemStack stack = player.getItemInHand(hand);
			int index = buf.readInt();
			if(stack.hasTag()) {
				var positions = stack.getOrCreateTag().getLongArray("locations");
				if(index < positions.length) {
					BlockPos target = BlockPos.of(positions[index]);
					server.execute(() -> TempadItem.summonTimeDoor(target, player));
				} else {
					server.execute(() -> player.displayClientMessage(new TranslatableComponent("tempad.timedoorfailure"), true));
				}
			} else {
				server.execute(() -> player.displayClientMessage(new TranslatableComponent("tempad.timedoorfailure"), true));
			}
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
}
