package dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.bigFireball;

import dev.lhkongyu.lhmiracleroadspell.entity.spell.AbstractMagicProjectile;
import dev.lhkongyu.lhmiracleroadspell.particle.common.BlastWaveParticleOptions;
import dev.lhkongyu.lhmiracleroadspell.registry.EntityRegistry;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class BigFireballProjectile extends AbstractMagicProjectile {
    public BigFireballProjectile(EntityType<? extends BigFireballProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public BigFireballProjectile(Level pLevel, LivingEntity pShooter) {
        this(EntityRegistry.BIG_FIREBALL_PROJECTILE.get(), pLevel);
        this.setOwner(pShooter);
    }

    private int range = 0;

    private int rangeAdditional = 1;

    public void setRange(int range){
        this.range = range;
    }

    public void setRangeAdditional(int rangeAdditional){
        this.rangeAdditional = rangeAdditional;
    }

    @Override
    public void trailParticles() {
//        Vec3 vec3 = getDeltaMovement();
//        double d0 = this.getX() - vec3.x;
//        double d1 = this.getY() - vec3.y;
//        double d2 = this.getZ() - vec3.z;
//        for (int i = 0; i < 8; i++) {
//            double radius = 0.65; // 旋转半径
//            double angle = i * (2 * Math.PI / 10); // 计算每个粒子的角度，使其均匀分布在圆周上
//            // 计算粒子在圆周上的位置
//            double offsetX = radius * Math.cos(angle);
//            double offsetY = 0.35;
//            double offsetZ = radius * Math.sin(angle);
//            // 计算粒子的旋转运动
//            double rotationSpeed = 0.5; // 旋转速度
//            double rotationAngle = (double) this.tickCount * rotationSpeed;
//            double rotatedX = offsetX * Math.cos(rotationAngle) - offsetZ * Math.sin(rotationAngle);
//            double rotatedZ = offsetX * Math.sin(rotationAngle) + offsetZ * Math.cos(rotationAngle);
//            // 在围绕实体的位置生成粒子
//            this.level().addParticle(ParticleRegistry.FIRE_PARTICLE.get(), d0 + rotatedX, d1 + offsetY, d2 + rotatedZ,0, 0, 0);
//        }

        Vec3 vec3 = getDeltaMovement();
        double d0 = this.getX() - vec3.x;
        double d1 = this.getY() - vec3.y;
        double d2 = this.getZ() - vec3.z;
        for (int i = 0; i < 10; i++) {
            Vec3 motion = LHMiracleRoadSpellTool.getRandomVec3(.125f).subtract(getDeltaMovement().scale(.125f));
            Vec3 pos = LHMiracleRoadSpellTool.getRandomVec3(.35f);
            this.level().addParticle(ParticleRegistry.FIRE_BOTTOM_PARTICLE.get(),true, d0 + pos.x, d1 + 0.5D + pos.y, d2 + pos.z, motion.x, motion.y, motion.z);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        level().getServer().getPlayerList().getPlayers().forEach(player -> {
            var level = player.level();
            ((ServerLevel) level).sendParticles(player, ParticleRegistry.FLAME_PARTICLE.get(), true, x, y, z, 100, .125, .125, .125,.225 );

            ((ServerLevel) level).sendParticles(player, ParticleTypes.CAMPFIRE_COSY_SMOKE, true, x, y, z, 30, .1, .1, .1,.2);

            ((ServerLevel) level).sendParticles(player, new BlastWaveParticleOptions(LHMiracleRoadSpellTool.RGBChangeVector3f(255,163,37), range,true),true, xOld, yOld, zOld, 1, 0, 0, 0, 0);
        });
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.of(SoundEvents.GENERIC_EXPLODE);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!this.level().isClientSide) {
            impactParticles(xOld, yOld, zOld);
            float explosionRadius = range * rangeAdditional;
            var explosionRadiusSqr = explosionRadius * explosionRadius;
            var entities = level().getEntities(this, this.getBoundingBox().inflate(explosionRadius));
            for (Entity entity : entities) {
                double distanceSqr = entity.distanceToSqr(hitResult.getLocation());
                if (distanceSqr < explosionRadiusSqr && canHitEntity(entity)){
                    entity.setSecondsOnFire(5);
                    float damage = distanceSqr < explosionRadiusSqr / 2 ? getDamage() : getDamage() / 2;
                    entity.hurt(level().damageSources().playerAttack((Player) getOwner()), damage);
                }
            }
//            playSound(SoundEvents.GENERIC_EXPLODE, 4.0F, (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F);
            getImpactSound().ifPresent(this::doImpactSound);
            this.discard();
        }
    }
}
