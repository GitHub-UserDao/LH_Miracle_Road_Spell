package dev.lhkongyu.lhmiracleroadspell.entity.spell;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

import java.util.List;

public abstract class AoeProjectile extends Projectile {

    protected AoeProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.blocksBuilding = false;
    }

    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(AoeProjectile.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> HIGH = SynchedEntityData.defineId(AoeProjectile.class, EntityDataSerializers.FLOAT);

    protected float damage = 1;
    protected int duration = 200;
    protected int attackInterval = 5;

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getRadius() {
        return this.getEntityData().get(RADIUS);
    }

    public void setRadius(float radius) {
        this.getEntityData().set(RADIUS, radius);
    }

    public float getHigh() {
        return this.getEntityData().get(HIGH);
    }

    public void setHigh(float high) {
        this.getEntityData().set(HIGH, high);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > duration) {
            discard();
            return;
        }
        if (!level().isClientSide) {
            if (tickCount % attackInterval == 0) {
                var radiusSqr = getRadius();
                List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0, getHigh(),0));
                radiusSqr *= radiusSqr;
                for (LivingEntity target : targets) {
                    if (canHitEntity(target)) {
                        if (target.distanceToSqr(this) <= radiusSqr || Math.abs(target.getY() - this.getY()) <= getHigh()) {
                            damageMethod(target);
                        }
                    }
                }
            }
        } else {
            rangeParticles();
        }
    }

    protected abstract void damageMethod(LivingEntity target);

    protected abstract void rangeParticles();

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return (getOwner() != null && pTarget != getOwner() && !getOwner().isAlliedTo(pTarget)) && super.canHitEntity(pTarget);
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2, this.getHigh());
    }

    protected void defineSynchedData() {
        this.getEntityData().define(RADIUS, 1F);
        this.getEntityData().define(HIGH, 1F);
    }

    /**
     * 添加需要持久化的数据，避免退出在进入刷新存在时间等问题
     * @param pCompound
     */
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("TickCount", this.tickCount);
        pCompound.putInt("Duration", this.duration);
        pCompound.putInt("AttackInterval", this.attackInterval);
        pCompound.putFloat("Radius", this.getRadius());
        pCompound.putFloat("Damage", this.getDamage());
        pCompound.putFloat("High", this.getHigh());
        super.addAdditionalSaveData(pCompound);
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.tickCount = pCompound.getInt("TickCount");
        this.duration = pCompound.getInt("Duration");
        this.attackInterval = pCompound.getInt("AttackInterval");
        this.setRadius(pCompound.getFloat("Radius"));
        this.setRadius(pCompound.getFloat("High"));
        this.setDamage(pCompound.getFloat("Damage"));
        super.readAdditionalSaveData(pCompound);
    }

    //不让流体推动法术实体
    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    //同步更新实体数据状态
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (RADIUS.equals(pKey)) {
            this.refreshDimensions();
            if (getRadius() < .1f)
                this.discard();
        }

        super.onSyncedDataUpdated(pKey);
    }
}
