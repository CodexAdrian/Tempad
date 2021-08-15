package net.minecraft.world.entity.projectile;

import net.minecraft.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class TimedoorEntity extends Projectile {
    public long birthTime = Util.getMillis();
    public TimedoorEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {

    }

}
