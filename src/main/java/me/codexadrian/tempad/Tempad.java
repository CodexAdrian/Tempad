package me.codexadrian.tempad;

import me.codexadrian.tempad.entity.TimedoorEntity;
import me.codexadrian.tempad.tempad.ColorDataComponent;
import me.codexadrian.tempad.tempad.LocationData;
import me.codexadrian.tempad.tempad.TempadComponent;
import me.codexadrian.tempad.tempad.TempadItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Tempad implements ModInitializer {
    public static final int ORANGE = 0xFF_ff6f00;
    public static final String MODID = "tempad";
    public static final EntityType<TimedoorEntity> TIMEDOOR_ENTITY_ENTITY_TYPE = FabricEntityTypeBuilder.create(MobCategory.MISC, TimedoorEntity::new).dimensions(EntityDimensions.scalable(.4F, 2.3F)).build();
    public static final TempadItem TEMPAD = new TempadItem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TOOLS));
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation TIMEDOOR_PACKET = new ResourceLocation(MODID, "timedoor");
    public static final ResourceLocation LOCATION_PACKET = new ResourceLocation(MODID, "location");
    public static final ResourceLocation SET_COLOR_PACKET = new ResourceLocation(MODID, "color");
    public static final ResourceLocation DELETE_LOCATION_PACKET = new ResourceLocation(MODID, "delete_location");

    public static final int[] colors = {
            0xFFFFFF,
            0xF51302,
            0xF77B05,
            0xF89506,
            0xFAB306,
            0xFBCF01,
            0xFEF304,
            0xEBFE05,
            0xCBFD03,
            0x82FE01,
            0x53FE00,
            0x53FE84,
            0x53FEB1,
            0x52FEDF,
            0x52FEF8,
            0x45DAFE,
            0x3ABDFE,
            0x2A93FB,
            0x165EFB,
            0x061AFB,
            0x471AFC,
            0x6519FC,
            0x7C19FC,
            0x9019FE,
            0xB319FD,
            0xD618FC,
            0xF418FC,
            0xEE28B0,
            0xEC3785,
            0xEB3860,
    };

    @Override
    public void onInitialize() {
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(MODID, "timedoor"), TIMEDOOR_ENTITY_ENTITY_TYPE);
        Registry.register(Registry.ITEM, new ResourceLocation(MODID, "tempad"), TEMPAD);
        ServerPlayNetworking.registerGlobalReceiver(SET_COLOR_PACKET, (server, player, handler, buf, responseSender) -> {
            int color = buf.readInt();
            server.execute(() -> {
                ColorDataComponent.COLOR_DATA.get(player).setColor(color);
                ColorDataComponent.COLOR_DATA.sync(player);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(TIMEDOOR_PACKET, (server, player, handler, buf, responseSender) -> {
            ResourceKey<Level> resourceKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, buf.readResourceLocation());
            BlockPos target = buf.readBlockPos();
            InteractionHand hand = buf.readEnum(InteractionHand.class);
            player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 3600);
            server.execute(() -> TempadItem.summonTimeDoor(new LocationData("", resourceKey, target), player));
        });

        ServerPlayNetworking.registerGlobalReceiver(LOCATION_PACKET, (server, player, handler, buf, responseSender) -> {
            int index = buf.readInt();
            String name = (String) buf.readCharSequence(index, StandardCharsets.UTF_8);
            InteractionHand hand = buf.readEnum(InteractionHand.class);
            server.execute(() -> {
                ItemStack stack = player.getItemInHand(hand);
                var tempadLocation = new LocationData(name, player.level.dimension(), player.blockPosition());

                TempadComponent.addStackLocation(stack, tempadLocation);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(DELETE_LOCATION_PACKET, (server, player, handler, buf, responseSender) -> {
            InteractionHand hand = buf.readEnum(InteractionHand.class);
            UUID locationId = buf.readUUID();
            ItemStack stack = player.getItemInHand(hand);
            server.execute(() -> TempadComponent.deleteStackLocation(stack, locationId));
        });
    }
}
