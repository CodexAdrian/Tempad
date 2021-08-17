package me.codexadrian.tempad.entity;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class TimedoorEntity extends Entity {
    public long birthTime = Util.getMillis();
    protected BlockPos targetPos = BlockPos.ZERO;
    public TimedoorEntity(EntityType<TimedoorEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        var pos = compoundTag.getIntArray("location");
        this.setLocation(new BlockPos(pos[0], pos[1], pos[2]));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putIntArray("location", new int[]{targetPos.getX(), targetPos.getY(), targetPos.getZ()});
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.getType(), this.getDirection().get3DDataValue(), this.blockPosition());
    }

    @Override
    public void tick() {
        AABB box = getBoundingBox();
        if (getDirection() == Direction.NORTH || getDirection() == Direction.SOUTH) {
            box = box.inflate(0.5, 0, 0);
        }
        if(getDirection() == Direction.EAST || getDirection() == Direction.WEST) {
            box = box.inflate(0, 0, 0.5);
        }
        if(getLocation() != BlockPos.ZERO) {
            for (Entity entity : this.level.getEntitiesOfClass(Entity.class, box, entity -> entity instanceof LivingEntity || entity instanceof ItemEntity)) {
                entity.level = this.level;
                var pos = getLocation();
                entity.teleportTo(pos.getX(), pos.getY(), pos.getZ());
            }
        }
    }

    public void setLocation(BlockPos pos) {
        this.targetPos = pos;
    }

    public BlockPos getLocation() {
        return targetPos;
    }
}
