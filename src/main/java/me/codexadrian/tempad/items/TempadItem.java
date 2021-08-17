package me.codexadrian.tempad.items;

import me.codexadrian.tempad.Tempad;
import me.codexadrian.tempad.entity.TimedoorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static net.minecraft.core.Direction.*;

public class TempadItem extends Item {

    public TempadItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if (player.isShiftKeyDown()) {
            stack.getOrCreateTag().putIntArray("target_location", new int[]{(int) player.getX(), (int) player.getY(), (int) player.getZ()});
        }
        else if (stack.hasTag()) {
            var pos = stack.getTag().getIntArray("target_location");
            TimedoorEntity timedoor = new TimedoorEntity(Tempad.TIMEDOOR_ENTITY_ENTITY_TYPE, level);
            var dir = player.getDirection();
            if (dir == WEST) {
                timedoor.setPos(player.getX() - 3, player.getY(), player.getZ());
            }
            if (dir ==  NORTH) {
                timedoor.setPos(player.getX(), player.getY(), player.getZ() - 3);
            }
            if (dir ==  EAST) {
                timedoor.setPos(player.getX() + 3, player.getY(), player.getZ());
            }
            if (dir == SOUTH) {
                timedoor.setPos(player.getX(), player.getY(), player.getZ() + 3);
            }

            timedoor.setLocation(new BlockPos(pos[0], pos[1], pos[2]));
            timedoor.setYRot(dir.get2DDataValue() * 90F + 180F);
            level.addFreshEntity(timedoor);
        }
        return super.use(level, player, interactionHand);
    }
}
