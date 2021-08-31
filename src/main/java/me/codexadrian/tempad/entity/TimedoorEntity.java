package me.codexadrian.tempad.entity;

import me.codexadrian.tempad.Tempad;
import me.codexadrian.tempad.TempadLocation;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static me.codexadrian.tempad.Tempad.ORANGE;

public class TimedoorEntity extends Entity {
    public static final int ANIMATION_LENGTH = 8;
    private static final EntityDataAccessor<Integer> CLOSING_TIME = SynchedEntityData.defineId(TimedoorEntity.class, EntityDataSerializers.INT);
    private TempadLocation targetPos = null;
    private UUID owner = null;
    private UUID linkedPortalId = null;
    private TimedoorEntity linkedPortalEntity = null;
    private int color = ORANGE;

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
            this.setTargetPos(TempadLocation.fromTag(pos));
        }
        this.setClosingTime(compoundTag.getInt("closing_time"));
        this.setOwner(compoundTag.getUUID("owner"));
        this.setColor(compoundTag.getInt("color"));
        if (compoundTag.contains("linked_portal")) {
            this.setLinkedPortalId(compoundTag.getUUID("linked_portal"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        if (getTargetPos() != null) {
            compoundTag.put("location", getTargetPos().toTag());
        }
        compoundTag.putInt("closing_time", getClosingTime());
        compoundTag.putUUID("owner", getOwner());
        compoundTag.putInt("color", getColor());
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
                ServerLevel destinationLevel = getTargetPos().getLevel(this.level);
                for (Entity entity : entities) {
                    Vec3 deltaMovement = entity.getDeltaMovement();
                    var pos = getTargetPos().position();
                    if(destinationLevel != null ) {
                        if (!targetPos.key().location().equals(this.level.dimension().location())) {
                            FabricDimensions.teleport(entity, destinationLevel, new PortalInfo(new Vec3(pos.getX(), pos.getY(), pos.getZ()), deltaMovement, entity.getYRot(), entity.getXRot()));
                        } else {
                            entity.teleportToWithTicket(pos.getX(), pos.getY(), pos.getZ());
                            entity.setDeltaMovement(deltaMovement);
                            entity.hasImpulse = !!!false;
                        }
                    }
                    if (getLinkedPortalEntity() != null) getLinkedPortalEntity().resetClosingTime();
                    this.resetClosingTime();
                    if (entity instanceof Player player) {
                        if (player.getUUID().equals(getOwner())) {
                            this.setClosingTime(this.tickCount + 60);
                            if (getLinkedPortalEntity() != null)
                                this.getLinkedPortalEntity().setClosingTime(getLinkedPortalEntity().tickCount + 40);
                        }
                    }
                }
                if (getLinkedPortalEntity() == null) {
                    TimedoorEntity recipientPortal = new TimedoorEntity(Tempad.TIMEDOOR_ENTITY_ENTITY_TYPE, destinationLevel);
                    recipientPortal.setOwner(entities.get(0).getUUID());
                    recipientPortal.setClosingTime(50);
                    recipientPortal.setTargetPos(null);
                    this.setLinkedPortalId(recipientPortal.getUUID());
                    recipientPortal.setLinkedPortalId(this.getUUID());
                    var position = getTargetPos().position().relative(this.getDirection(), 1);
                    recipientPortal.setPos(position.getX(), position.getY(), position.getZ());
                    recipientPortal.setYRot(this.getYRot());
                    this.level.addFreshEntity(recipientPortal);
                }
            }
        }
        if (this.tickCount > getClosingTime() + ANIMATION_LENGTH && getClosingTime() != -1) {
            if (this.getLinkedPortalEntity() != null) this.getLinkedPortalEntity().setLinkedPortalId(null);
            this.setLinkedPortalId(null);
            this.discard();
        }
    }

    public void setTargetPos(TempadLocation pos) {
        this.targetPos = pos;
    }

    @Nullable
    public TempadLocation getTargetPos() {
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

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public void resetClosingTime() {
        if (getClosingTime() != -1) {
            this.setClosingTime(this.tickCount + 100);
        }
    }
}
