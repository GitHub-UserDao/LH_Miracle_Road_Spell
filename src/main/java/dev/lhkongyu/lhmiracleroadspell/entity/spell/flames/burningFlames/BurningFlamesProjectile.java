package dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.burningFlames;

import dev.lhkongyu.lhmiracleroadspell.entity.spell.AoeMagicProjectile;
import dev.lhkongyu.lhmiracleroadspell.generator.SpellDamageTypes;
import dev.lhkongyu.lhmiracleroadspell.registry.EntityRegistry;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import dev.lhkongyu.lhmiracleroadspell.tool.particle.ParticleEffectsTool;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BurningFlamesProjectile extends AoeMagicProjectile {

    private final int secondsOnFireTime = 1;

    public BurningFlamesProjectile(Level pLevel,Vec3 position,LivingEntity livingEntity,float blast,float damage,float radius,float high) {
        super(EntityRegistry.BURNING_FLAMES.get(), pLevel);
        this.moveTo(position);
        this.setOwner(livingEntity);
        this.setDamage(damage);
        this.setRadius(radius);
        this.setHigh(high);
        igniteAndExplode(pLevel,position,livingEntity,blast,Math.max(radius,high));
    }

    public BurningFlamesProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void continuedSound() {
        level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLAZE_BURN, SoundSource.NEUTRAL, 1.5F, (float) (.9f + LHMiracleRoadSpellTool.getRandomScaled(.5f) * .2f));
    }


    //自定义攻击方式
    @Override
    protected void damageMethod(LivingEntity target){
        if (damageSource == null) {
            damageSource = LHMiracleRoadSpellTool.getDamageSourceType(getOwner(), SpellDamageTypes.FLAME_MAGIC);
        }
        target.setSecondsOnFire(secondsOnFireTime);
        target.hurt(damageSource, getDamage());
    }

    /**
     * 法术持续时间的范围粒子效果
     */
    @Override
    public void rangeParticles() {
        float f = (float) (getRadius() * getRadius() * 0.75);
        for (int i = 0; i < f; i++) {
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

        for (int i = 0; i < f * .8; i++) {
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

            Vec3 motion = new Vec3(
                    LHMiracleRoadSpellTool.getRandomScaled(.1f),
                    this.random.nextDouble() * .15f,
                    LHMiracleRoadSpellTool.getRandomScaled(.1f)
            );

            level().addParticle(ParticleRegistry.FIRE_PARTICLE.get(),true, getX() + pos.x, getY() + pos.y, getZ() + pos.z, motion.x, motion.y, motion.z);
        }

//        double centerX = getX();
//        double centerY = getY() + 0.75D;  // 假设圆心在粒子所在位置上方
//        double centerZ = getZ();
//
//        for (int i = 0; i < f; i++) {
//            if (f - i < 1 && random.nextFloat() > f - i)
//                return;
//            var r = getRadius();
//            Vec3 pos;
//            var distance = r * (1 - this.random.nextFloat() * this.random.nextFloat());
//            var theta = this.random.nextFloat() * Math.PI * 2;
//            pos = new Vec3(
//                    distance * Mth.cos((float) theta),
//                    .2f,
//                    distance * Mth.sin((float) theta)
//            );
//
//            Vec3 relativePos = new Vec3(centerX + pos.x - getX(), centerY + pos.y - getY(), centerZ + pos.z - getZ());
//            relativePos = relativePos.normalize();
//            Vec3 motion = new Vec3(
//                    relativePos.x * LHMiracleRoadSpellTool.getRandomScaled(.1f),
//                    this.random.nextDouble() * .1f,
//                    relativePos.z * LHMiracleRoadSpellTool.getRandomScaled(.1f)
//            );
//
//            level().addParticle(ParticleRegistry.FIRE_PARTICLE.get(),true, getX() + pos.x, getY() + pos.y, getZ() + pos.z, motion.x, motion.y, motion.z);
//        }
    }

    private void igniteAndExplode(Level pLevel,Vec3 position,LivingEntity livingEntity,float blast,float explosionRadius){
        if (!pLevel.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer) livingEntity;
            ServerLevel serverLevel = (ServerLevel) pLevel;
            ParticleEffectsTool.igniteAndExplode(serverPlayer,serverLevel,position);

            var explosionRadiusSqr = explosionRadius * explosionRadius;
            var entities = level().getEntities(this, this.getBoundingBox().inflate(explosionRadius));
            for (Entity entity : entities) {
                double distanceSqr = entity.distanceToSqr(position);
                if (distanceSqr < explosionRadiusSqr && canHitEntity(entity)){
                    entity.setSecondsOnFire(secondsOnFireTime);
//                    entity.hurt(pLevel.damageSources().mobAttack(livingEntity), blast);
                    entity.hurt(LHMiracleRoadSpellTool.getDamageSourceType(livingEntity, SpellDamageTypes.FLAME_MAGIC), blast);
                }
            }
        }
    }
}
