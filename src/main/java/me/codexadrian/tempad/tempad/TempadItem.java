package me.codexadrian.tempad.tempad;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import me.codexadrian.tempad.Tempad;
import me.codexadrian.tempad.client.gui.MainTempadScreenDesc;
import me.codexadrian.tempad.entity.TimedoorEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TempadItem extends Item {

    public TempadItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if (level.isClientSide) openScreen(player, stack, interactionHand);
        return InteractionResultHolder.success(stack);
    }

    public static void summonTimeDoor(LocationData locationData, Player player) {
        TimedoorEntity timedoor = new TimedoorEntity(Tempad.TIMEDOOR_ENTITY_ENTITY_TYPE, player.level);
        var dir = player.getDirection();
        timedoor.setColor(ColorDataComponent.COLOR_DATA.get(player).getColor());
        timedoor.setLocation(locationData);
        timedoor.setOwner(player.getUUID());
        var position = player.blockPosition().relative(dir, 3);
        timedoor.setPos(position.getX(), position.getY(), position.getZ());
        timedoor.setYRot(dir.getOpposite().toYRot());
        //timedoor.setClosingTime(-1);
        player.level.addFreshEntity(timedoor);
    }

    @Environment(EnvType.CLIENT)
    private void openScreen(Player player, ItemStack stack, InteractionHand interactionHand) {
        int color = ColorDataComponent.COLOR_DATA.get(player).getColor();
        Minecraft.getInstance().setScreen(new CottonClientScreen(new MainTempadScreenDesc(color, player, interactionHand)){
            @Override
            public boolean isPauseScreen() {
                return false;
            }
        });
        //Minecraft.getInstance().setScreen(new CottonClientScreen(new TempadGuiDescription(interactionHand, player.getItemInHand(interactionHand))));
    }
}
