package dev.lhkongyu.lhmiracleroadspell.spell.projectile.textFireBall;

import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.Optional;

public abstract class AbstractItemProjectile extends ThrowableItemProjectile {
    protected static final int EXPIRE_TIME = 15 * 20;

    protected float damage;

    private double gravity = 0.05;

    public AbstractItemProjectile(EntityType<? extends ThrowableItemProjectile> entityEntityType, Level world) {
        super(entityEntityType, world);
    }

    public AbstractItemProjectile(EntityType<? extends ThrowableItemProjectile> entityEntityType,Level worldIn, LivingEntity throwerIn) {
        super(entityEntityType, throwerIn, worldIn);
    }

    /**
     * 客户端端与 tick()方法一起调用
     */
    public abstract void trailParticles();
    /**
     * 服务端与 onHit()方法一起调用
     */
    public abstract void impactParticles(double x, double y, double z);

    public abstract Optional<SoundEvent> getImpactSound();

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    public void setGravity(double gravity){
        this.gravity = gravity;
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
        this.setSecondsOnFire(1);
        if (level().isClientSide) {
            trailParticles();
        }
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS  && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            onHit(hitresult);
        }
        setPos(position().add(getDeltaMovement()));
        ProjectileUtil.rotateTowardsMovement(this, 1);
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
        level().playSound(null, getX(), getY(), getZ(), sound, SoundSource.NEUTRAL, 2, .9f + LHMiracleRoadSpellTool.randomSource.nextFloat() * .2f);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        this.onHitBlock(new BlockHitResult(pResult.getEntity().position(), Direction.fromYRot(this.getYRot()), pResult.getEntity().blockPosition(), false));
    }
}
