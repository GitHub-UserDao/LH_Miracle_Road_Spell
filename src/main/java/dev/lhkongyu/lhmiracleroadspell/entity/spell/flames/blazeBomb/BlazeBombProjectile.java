package dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.blazeBomb;

import dev.lhkongyu.lhmiracleroadspell.entity.spell.CommonMagicProjectile;
import dev.lhkongyu.lhmiracleroadspell.generator.SpellDamageTypes;
import dev.lhkongyu.lhmiracleroadspell.registry.EntityRegistry;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class BlazeBombProjectile extends CommonMagicProjectile {

    private int range = 0;

    private int rangeAdditional = 1;

    public void setRange(int range){
        this.range = range;
    }

    public void setRangeAdditional(int rangeAdditional){
        this.rangeAdditional = rangeAdditional;
    }

    private final int secondsOnFireTime = 1;

    public BlazeBombProjectile(EntityType<? extends BlazeBombProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public BlazeBombProjectile(Level levelIn, LivingEntity shooter) {
        this(EntityRegistry.BLAZE_BOMB_PROJECTILE.get(), levelIn);
        setOwner(shooter);
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {

        return Optional.of(SoundEvents.GENERIC_EXPLODE);
    }

    @Override
    public void doImpactSound(SoundEvent sound) {
        level().playSound(null, getX(), getY(), getZ(), sound, SoundSource.NEUTRAL, 1f, 1f);
    }

//    @Override
//    protected void onHitBlock(BlockHitResult blockHitResult) {
//        super.onHitBlock(blockHitResult);
//        if (!level().isClientSide) {
//            level().explode(getOwner(), getX(), getY(), getZ(), 1.75F, Level.ExplosionInteraction.NONE);
//        }
//        discard();
//    }
//
//    @Override
//    protected void onHitEntity(EntityHitResult entityHitResult) {
//        super.onHitEntity(entityHitResult);
//        var target = entityHitResult.getEntity();
//        target.setSecondsOnFire(5);
//        target.hurt(LHMiracleRoadSpellTool.getDamageSourceType(getOwner(), SpellDamageTypes.FLAME_MAGIC), getDamage());
//        if (!level().isClientSide) {
//            level().explode(getOwner(), getX(), getY(), getZ(), 1.75F, Level.ExplosionInteraction.NONE);
//        }
//        discard();
//    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!this.level().isClientSide) {
            impactParticles(xOld, yOld, zOld);
            float explosionRadius = range * rangeAdditional;
            double explosionRadiusSqr = explosionRadius * explosionRadius;
            var entities = level().getEntities(this, this.getBoundingBox().inflate(explosionRadius));
            for (Entity entity : entities) {
                double distanceSqr = entity.distanceToSqr(hitResult.getLocation());
                if (distanceSqr < explosionRadiusSqr && canHitEntity(entity)){
                    entity.setSecondsOnFire(secondsOnFireTime);
                    entity.hurt(LHMiracleRoadSpellTool.getDamageSourceType(getOwner(), SpellDamageTypes.FLAME_MAGIC), getDamage());
                }
                simulateKnockBack(entity, hitResult.getLocation(),explosionRadiusSqr,distanceSqr);
            }
//            playSound(SoundEvents.GENERIC_EXPLODE, 4.0F, (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F);
            getImpactSound().ifPresent(this::doImpactSound);
            this.discard();
        }
    }

    private void simulateKnockBack(Entity entity, Vec3 explosionCenter,double explosionRadiusSqr,double distanceSqr) {
        //获取当前实体的体积
        double volume = entity.getBbWidth() * entity.getBbHeight();

        //获得僵尸体积
        Zombie zombie = new Zombie(this.level());
        double zombieVolume = zombie.getBbWidth() * zombie.getBbHeight();

        double knockBackStrength = 2;
        if (volume > zombieVolume) {
            knockBackStrength = knockBackStrength * Math.max(1 - (volume - zombieVolume) / zombieVolume,0);
        }

        if (knockBackStrength > 0) knockBackStrength = knockBackStrength * Math.max(1 - distanceSqr / explosionRadiusSqr,0);

        Vec3 knockBackDirection = entity.position().subtract(explosionCenter).normalize();
        Vec3 knockBack = knockBackDirection.scale(knockBackStrength);
        entity.setDeltaMovement(knockBack);
        entity.hurtMarked = true;
    }


    @Override
    public void impactParticles(double x, double y, double z) {
        level().getServer().getPlayerList().getPlayers().forEach(player -> {
            var level = player.level();
            ((ServerLevel) level).sendParticles(player, ParticleRegistry.FIRE_BOTTOM_PARTICLE.get(), true, x, y, z, 15, .1, .1, .1,.25 );

            ((ServerLevel) level).sendParticles(player, ParticleTypes.EXPLOSION, true, x, y, z, 1, .1, .1, .1,.1);
        });
    }

    @Override
    public void trailParticles() {
        float yHeading = -((float) (Mth.atan2(getDeltaMovement().z, getDeltaMovement().x) * (double) (180F / (float) Math.PI)) + 90.0F);
        float radius = .25f;
        int steps = 3;
        for (int j = 0; j < steps; j++) {
            float offset = (1f / steps) * 0;
            double radians = ((tickCount + offset) / 7.5f) * 360 * Mth.DEG_TO_RAD;
            Vec3 swirl = new Vec3(Math.cos(radians) * radius, Math.sin(radians) * radius, 0).yRot(yHeading * Mth.DEG_TO_RAD);
            double x = getX() + swirl.x;
            double y = getY() + swirl.y + getBbHeight() / 2;
            double z = getZ() + swirl.z;
            Vec3 jitter = LHMiracleRoadSpellTool.getRandomVec3(.05f);
            level().addParticle(ParticleRegistry.FIRE_BOTTOM_PARTICLE.get(), x, y, z, jitter.x, jitter.y, jitter.z);
        }
    }

    @Override
    public boolean isOnFire() {
        return true;
    }

}
