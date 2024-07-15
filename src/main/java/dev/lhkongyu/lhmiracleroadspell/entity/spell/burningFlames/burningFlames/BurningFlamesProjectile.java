package dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.burningFlames;

import dev.lhkongyu.lhmiracleroadspell.entity.spell.AoeProjectile;
import dev.lhkongyu.lhmiracleroadspell.registry.EntityRegistry;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import dev.lhkongyu.lhmiracleroadspell.tool.particle.ParticleEffectsTool;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BurningFlamesProjectile extends AoeProjectile {

    private DamageSource damageSource;

    public BurningFlamesProjectile(Level pLevel,Vec3 position,Player player,float blast,float damage,float radius,float high) {
        super(EntityRegistry.FIRE_FIELD.get(), pLevel);
        this.moveTo(position);
        this.setOwner(player);
        this.setDamage(damage);
        this.setRadius(radius);
        this.setHigh(high);
        igniteAndExplode(pLevel,position,player,blast,Math.max(radius,high));
    }

    public BurningFlamesProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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

    private void continuedSound() {
        level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLAZE_BURN, SoundSource.NEUTRAL, 1.5F, (float) (.9f + LHMiracleRoadSpellTool.getRandomScaled(.5f) * .2f));
    }

    //自定义攻击方式
    public void damageMethod(LivingEntity target){
        if (damageSource == null) {
            damageSource = target.damageSources().playerAttack((Player) getOwner());
        }
        target.hurt(damageSource, getDamage());
        target.setSecondsOnFire(5);
    }

    /**
     * 法术持续时间的范围粒子效果
     */
    public void rangeParticles() {
        float f = getRadius() * getRadius();
        for (int i = 0; i < f * .8; i++) {
            if (f - i < 1 && random.nextFloat() > f - i)
                return;
            var r = getRadius() / 2;
            Vec3 pos;
            var distance = r * (1 - this.random.nextFloat() * this.random.nextFloat());
            var theta = this.random.nextFloat() * Math.PI * 2;
            pos = new Vec3(
                    distance * Mth.cos((float) theta),
                    .2f,
                    distance * Mth.sin((float) theta)
            );
            Vec3 motion = new Vec3(
                    LHMiracleRoadSpellTool.getRandomScaled(.03f),
                    this.random.nextDouble() * .25f,
                    LHMiracleRoadSpellTool.getRandomScaled(.03f)
            );

            level().addParticle(ParticleRegistry.FLAME_PARTICLE.get(),true, getX() + pos.x, getY() + pos.y, getZ() + pos.z, motion.x, motion.y, motion.z);
        }

        double centerX = getX();
        double centerY = getY() + 0.75D;  // 假设圆心在粒子所在位置上方
        double centerZ = getZ();

        for (int i = 0; i < f; i++) {
            if (f - i < 1 && random.nextFloat() > f - i)
                return;
            var r = getRadius();
            Vec3 pos;
            var distance = r * (1 - this.random.nextFloat() * this.random.nextFloat());
            var theta = this.random.nextFloat() * Math.PI * 2;
            pos = new Vec3(
                    distance * Mth.cos((float) theta),
                    .2f,
                    distance * Mth.sin((float) theta)
            );

            Vec3 relativePos = new Vec3(centerX + pos.x - getX(), centerY + pos.y - getY(), centerZ + pos.z - getZ());
            relativePos = relativePos.normalize();
            Vec3 motion = new Vec3(
                    relativePos.x * LHMiracleRoadSpellTool.getRandomScaled(.1f),
                    this.random.nextDouble() * .1f,
                    relativePos.z * LHMiracleRoadSpellTool.getRandomScaled(.1f)
            );

            level().addParticle(ParticleRegistry.FIRE_PARTICLE.get(),true, getX() + pos.x, getY() + pos.y, getZ() + pos.z, motion.x, motion.y, motion.z);
        }
    }

    private void igniteAndExplode(Level pLevel,Vec3 position,Player player,float blast,float explosionRadius){
        if (!pLevel.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            ServerLevel serverLevel = (ServerLevel) pLevel;
            ParticleEffectsTool.igniteAndExplode(serverPlayer,serverLevel,position);

            var explosionRadiusSqr = explosionRadius * explosionRadius;
            var entities = level().getEntities(this, this.getBoundingBox().inflate(explosionRadius));
            for (Entity entity : entities) {
                double distanceSqr = entity.distanceToSqr(position);
                if (distanceSqr < explosionRadiusSqr && canHitEntity(entity)){
                    entity.setSecondsOnFire(5);
                    entity.hurt(pLevel.damageSources().playerAttack(player), blast);
                }
            }
        }
    }
}
