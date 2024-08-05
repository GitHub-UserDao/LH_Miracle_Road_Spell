package dev.lhkongyu.lhmiracleroadspell.spell.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;

import java.util.List;

public abstract class AoeMagicProjectile extends Projectile {

    protected DamageSource damageSource;

    protected AoeMagicProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.blocksBuilding = false;
    }

    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(AoeMagicProjectile.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> HIGH = SynchedEntityData.defineId(AoeMagicProjectile.class, EntityDataSerializers.FLOAT);

    protected float damage = 1;
    protected int duration = 200;
    protected int attackInterval = 10;

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

            if (tickCount % 20 == 0 && tickCount < duration - 20){
                continuedSound();
            }

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

    //自定义声音
    protected abstract void continuedSound();

    //自定义攻击类型
    protected abstract void damageMethod(LivingEntity target);

    //自定义法术实体存在期间的粒子特效
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
        this.setDamage(pCompound.getFloat("Damage"));
        this.setHigh(pCompound.getFloat("High"));
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
