package me.codexadrian.tempad.entity;

import me.codexadrian.tempad.Tempad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class TimedoorEntity extends Entity {
    public static final int ANIMATION_LENGTH = 8;
    private static final EntityDataAccessor<Integer> CLOSING_TIME = SynchedEntityData.defineId(TimedoorEntity.class, EntityDataSerializers.INT);
    private BlockPos targetPos = null;
    private UUID owner = null;
    private UUID linkedPortalId = null;
    private TimedoorEntity linkedPortalEntity = null;

    public TimedoorEntity(EntityType<TimedoorEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(CLOSING_TIME, 100);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.contains("location")) {
            var pos = compoundTag.getCompound("location");
            this.setTargetPos(NbtUtils.readBlockPos(pos));
        }
        this.setClosingTime(compoundTag.getInt("closing_time"));
        this.setOwner(compoundTag.getUUID("owner"));
        if (compoundTag.contains("linked_portal")) {
            this.setLinkedPortalId(compoundTag.getUUID("linked_portal"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        if (getTargetPos() != null) {
            compoundTag.put("location", NbtUtils.writeBlockPos(getTargetPos()));
        }
        compoundTag.putInt("closing_time", getClosingTime());
        compoundTag.putUUID("owner", getOwner());
        if (getLinkedPortalId() != null) {
            compoundTag.putUUID("linked_portal", getLinkedPortalId());
        }
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
        if (getDirection() == Direction.EAST || getDirection() == Direction.WEST) {
            box = box.inflate(0, 0, 0.5);
        }
        if (getTargetPos() != null) {
            List<Entity> entities = this.level.getEntitiesOfClass(Entity.class, box, entity -> entity instanceof LivingEntity || entity instanceof ItemEntity);
            if (!entities.isEmpty() && !level.isClientSide()) {
                for (Entity entity : entities) {
                    entity.level = this.level;
                    Vec3 deltaMovement = entity.getDeltaMovement();
                    var pos = getTargetPos();
                    entity.teleportToWithTicket(pos.getX(), pos.getY(), pos.getZ());
                    entity.setDeltaMovement(deltaMovement);
                    entity.hasImpulse = true;
                    if(getLinkedPortalEntity() != null) getLinkedPortalEntity().resetClosingTime();
                    this.resetClosingTime();

                    //TODO do cool thing up and ash and gravy said to do
                    if (entity instanceof Player player) {
                        if (player.getUUID().equals(getOwner())) {
                            this.setClosingTime(this.tickCount + 60);
                            if(getLinkedPortalEntity() != null) this.getLinkedPortalEntity().setClosingTime(getLinkedPortalEntity().tickCount + 40);
                        }
                    }
                }
                if(getLinkedPortalEntity() == null) {
                    TimedoorEntity recipientPortal = new TimedoorEntity(Tempad.TIMEDOOR_ENTITY_ENTITY_TYPE, this.level);
                    recipientPortal.setOwner(entities.get(0).getUUID());
                    recipientPortal.setClosingTime(50);
                    recipientPortal.setTargetPos(null);
                    this.setLinkedPortalId(recipientPortal.getUUID());
                    recipientPortal.setLinkedPortalId(this.getUUID());
                    var position = getTargetPos().relative(this.getDirection(), 1);
                    recipientPortal.setPos(position.getX(), position.getY(), position.getZ());
                    recipientPortal.setYRot(this.getYRot());
                    level.addFreshEntity(recipientPortal);
                }
            }
        }
        if (this.tickCount > getClosingTime() + ANIMATION_LENGTH && getClosingTime() != -1) {
            if(this.getLinkedPortalEntity() != null) this.getLinkedPortalEntity().setLinkedPortalId(null);
            this.setLinkedPortalId(null);
            this.discard();
        }
    }

    public void setTargetPos(BlockPos pos) {
        this.targetPos = pos;
    }

    @Nullable
    public BlockPos getTargetPos() {
        return targetPos;
    }

    public int getClosingTime() {
        return entityData.get(CLOSING_TIME);
    }

    public void setClosingTime(int closingTime) {
        entityData.set(CLOSING_TIME, closingTime);
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public UUID getLinkedPortalId() {
        return linkedPortalId;
    }

    public TimedoorEntity getLinkedPortalEntity() {
        if (!level.isClientSide() && linkedPortalEntity == null) {
            ServerLevel serverLevel = (ServerLevel) level;
            linkedPortalEntity = (TimedoorEntity) serverLevel.getEntity(linkedPortalId);
        }
        return linkedPortalEntity;
    }

    public void setLinkedPortalId(UUID id) {
        this.linkedPortalId = id;
        this.linkedPortalEntity = null;
    }

    public void resetClosingTime() {
        if(getClosingTime() != -1) {
            this.setClosingTime(this.tickCount + 100);
        }
    }
}
