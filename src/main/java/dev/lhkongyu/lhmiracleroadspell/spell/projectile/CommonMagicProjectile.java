package dev.lhkongyu.lhmiracleroadspell.spell.projectile;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.Optional;

public abstract class CommonMagicProjectile extends Projectile {
    protected int EXPIRE_TIME = 10 * 20;

    protected float damage;

    private double gravity = 0.05;

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    public void setGravity(double gravity){
        this.gravity = gravity;
    }

    public double getGravity(){
        return this.gravity;
    }

    public void setExpireTime(int time){
        this.EXPIRE_TIME = time * 20;
    }

    //飞行拖尾特效
    public abstract void trailParticles();

    //实体碰撞后的结束特效
    public abstract void impactParticles(double x, double y, double z);

    public abstract Optional<SoundEvent> getImpactSound();

    public CommonMagicProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return super.canHitEntity(pTarget) && pTarget != getOwner();
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > EXPIRE_TIME) {
            discard();
            return;
        }
        if (level().isClientSide) {
            trailParticles();
        }
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS  && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            onHit(hitresult);
        }
        setPos(position().add(getDeltaMovement()));
//        ProjectileUtil.rotateTowardsMovement(this, 1);
        if (this.isNoGravity()) {
            Vec3 vec34 = this.getDeltaMovement();
            this.setDeltaMovement(vec34.x, vec34.y - gravity, vec34.z);
        }
    }

    @Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);

        if (!level().isClientSide) {
            impactParticles(getX(), getY(), getZ());
            getImpactSound().ifPresent(this::doImpactSound);
        }
    }

    @Override
    public boolean shouldBeSaved() {
        return super.shouldBeSaved() && !Objects.equals(getRemovalReason(), RemovalReason.UNLOADED_TO_CHUNK);
    }

    protected void doImpactSound(SoundEvent sound) {
        level().playSound(null, getX(), getY(), getZ(), sound, SoundSource.NEUTRAL, 2, 1);
    }

    @Override
    protected void defineSynchedData() {
        System.err.println();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!shouldPierceShields()) {
            this.onHitBlock(new BlockHitResult(pResult.getEntity().position(), Direction.fromYRot(this.getYRot()), pResult.getEntity().blockPosition(), false));
        }
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    protected boolean shouldPierceShields() {
        return false;
    }
}
