package dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.flameDevouring;

import dev.lhkongyu.lhmiracleroadspell.spell.projectile.AoeMagicProjectile;
import dev.lhkongyu.lhmiracleroadspell.generator.SpellDamageTypes;
import dev.lhkongyu.lhmiracleroadspell.registry.EntityRegistry;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FlameDevouringProjectile extends AoeMagicProjectile {

    private final int secondsOnFireTime = 1;

    private Queue<FlameDevouringProjectile> queue;

    public FlameDevouringProjectile(Level pLevel, Vec3 position, LivingEntity livingEntity, float damage, float radius, float high) {
        super(EntityRegistry.FLAME_DEVOURING.get(), pLevel);
        this.moveTo(position);
        this.setOwner(livingEntity);
        this.setDamage(damage);
        this.setRadius(radius);
        this.setHigh(high);
        this.queue = new LinkedList<>();
    }

    public FlameDevouringProjectile(Level pLevel, Vec3 position, LivingEntity livingEntity, float damage, float radius, float high,Queue<FlameDevouringProjectile> queue) {
        super(EntityRegistry.FLAME_DEVOURING.get(), pLevel);
        this.moveTo(position);
        this.setOwner(livingEntity);
        this.setDamage(damage);
        this.setRadius(radius);
        this.setHigh(high);
        this.queue = queue;

        pLevel.playSound(null, position.x, position.y, position.z, SoundEvents.BLAZE_SHOOT, SoundSource.NEUTRAL, 1.5F, 1F);
    }

    public FlameDevouringProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void continuedSound() {
        level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLAZE_BURN, SoundSource.NEUTRAL, 1.5F, (float) (.9f + LHMiracleRoadSpellTool.getRandomScaled(.5f) * .2f));
    }

    @Override
    protected void damageMethod(LivingEntity target) {
        if (damageSource == null) {
            damageSource = LHMiracleRoadSpellTool.getDamageSourceType(getOwner(), SpellDamageTypes.FLAME_MAGIC);
        }
        target.setSecondsOnFire(secondsOnFireTime);
        target.hurt(damageSource, getDamage());
    }

    @Override
    protected void rangeParticles() {
        for (int i = 0; i < 2; i++) {
            Vec3 motion = new Vec3(
                    LHMiracleRoadSpellTool.getRandomScaled(.03f),
                    this.random.nextDouble() * .45f,
                    LHMiracleRoadSpellTool.getRandomScaled(.03f)
            );

            level().addParticle(ParticleRegistry.FLAME_PARTICLE.get(),true, getX(), getY(), getZ(), motion.x, motion.y, motion.z);
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius(), this.getHigh());
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > duration) {
            discard();
            return;
        }
        if (!level().isClientSide) {

            if (queue != null && !queue.isEmpty() && tickCount > 0 && tickCount % 10 == 0){

                for (int i = 0;i < LHMiracleRoadSpellTool.randomNumber(1,3); i++) {
                    FlameDevouringProjectile flameDevouringProjectile = queue.poll();
                    if (flameDevouringProjectile != null) {
                        this.level().addFreshEntity(flameDevouringProjectile);
                    }
                }
                level().playSound(null, getX(), getY(), getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.NEUTRAL, 1.5F, 1F);
            }

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
}
