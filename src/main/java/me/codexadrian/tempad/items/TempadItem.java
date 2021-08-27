package me.codexadrian.tempad.items;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import me.codexadrian.tempad.Tempad;
import me.codexadrian.tempad.client.gui.MainTempadScreenDesc;
import me.codexadrian.tempad.entity.TimedoorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
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
        int color = ORANGE;
        if(stack.hasTag()) {
            color = stack.getTag().contains("color") ? stack.getTag().getInt("color") : ORANGE;
        }

        if(level.isClientSide) {
            Minecraft.getInstance().setScreen(new CottonClientScreen(new MainTempadScreenDesc(color, player, interactionHand)));
            //Minecraft.getInstance().setScreen(new CottonClientScreen(new TempadGuiDescription(interactionHand, player.getItemInHand(interactionHand))));
        }

        return super.use(level, player, interactionHand);

    }

    public static void summonTimeDoor(BlockPos pos, Player player/*, ResourceKey<Level> dimension*/) {
        TimedoorEntity timedoor = new TimedoorEntity(Tempad.TIMEDOOR_ENTITY_ENTITY_TYPE, player.level);
        var dir = player.getDirection();
        timedoor.setTargetPos(pos);
        timedoor.setOwner(player.getUUID());
        var position = player.blockPosition().relative(dir, 3);
        timedoor.setPos(position.getX(), position.getY(), position.getZ());
        timedoor.setYRot(dir.getOpposite().toYRot());
        player.level.addFreshEntity(timedoor);
    }
}
