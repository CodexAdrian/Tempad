package me.codexadrian.tempad.items;

import me.codexadrian.tempad.Tempad;
import me.codexadrian.tempad.TempadLocation;
import me.codexadrian.tempad.client.gui.MainTempadScreenDesc;
import me.codexadrian.tempad.client.gui.TempadInterfaceGui;
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

import static me.codexadrian.tempad.Tempad.ORANGE;

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

    public static void summonTimeDoor(TempadLocation pos, Player player) {
        TimedoorEntity timedoor = new TimedoorEntity(Tempad.TIMEDOOR_ENTITY_ENTITY_TYPE, player.level);
        var dir = player.getDirection();
        timedoor.setTargetPos(pos);
        timedoor.setOwner(player.getUUID());
        var position = player.blockPosition().relative(dir, 3);
        timedoor.setPos(position.getX(), position.getY(), position.getZ());
        timedoor.setYRot(dir.getOpposite().toYRot());
        timedoor.setClosingTime(-1);
        player.level.addFreshEntity(timedoor);
    }

    @Environment(EnvType.CLIENT)
    private void openScreen(Player player, ItemStack stack, InteractionHand interactionHand) {
        int color = ORANGE;
        if (stack.hasTag()) {
            color = stack.getTag().contains("color") ? stack.getTag().getInt("color") : ORANGE;
        }
        Minecraft.getInstance().setScreen(new TempadInterfaceGui(new MainTempadScreenDesc(color, player, interactionHand)));
        //Minecraft.getInstance().setScreen(new CottonClientScreen(new TempadGuiDescription(interactionHand, player.getItemInHand(interactionHand))));
    }
}
