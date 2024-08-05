package dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.fireball;

import dev.lhkongyu.lhmiracleroadspell.spell.entity.geckolib.flames.fireball.Fireball;
import dev.lhkongyu.lhmiracleroadspell.generator.SpellDamageTypes;
import dev.lhkongyu.lhmiracleroadspell.particle.CommonParticleOptions;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class BigFireballProjectile extends Fireball {

    public BigFireballProjectile(EntityType<? extends BigFireballProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public BigFireballProjectile(Level pLevel, LivingEntity pShooter) {
        this(EntityRegistry.TEST_FIREBALL.get(), pLevel);
        this.setOwner(pShooter);
    }

    private final int flameParticleAmount = 75;

    private final int trailParticlesAmount = 15;

    private int range = 0;

    private int rangeAdditional = 1;

    private boolean isSmog;

    public void setRange(int range){
        this.range = range;
    }

    public void setRangeAdditional(int rangeAdditional){
        this.rangeAdditional = rangeAdditional;
    }

    public void setSmog(boolean smog) {
        isSmog = smog;
    }

    private final int secondsOnFireTime = 1;

    @Override
    public void trailParticles() {
        Vec3 vec3 = getDeltaMovement();
        float shifting = shifting(1.5f);

        double d0 = this.getX() - vec3.x + shifting;
        double d1 = this.getY() - vec3.y;
        double d2 = this.getZ() - vec3.z;
        for (int i = 0; i < trailParticlesAmount; i++) {
            Vec3 motion = LHMiracleRoadSpellTool.getRandomVec3(.15f).subtract(getDeltaMovement().scale(.15f));
            Vec3 pos = LHMiracleRoadSpellTool.getRandomVec3(.4f);
            this.level().addParticle(ParticleRegistry.FIRE_BOTTOM_PARTICLE.get(),true, d0 + pos.x, d1 + 1 + pos.y, d2 + pos.z, motion.x, motion.y, motion.z);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        level().getServer().getPlayerList().getPlayers().forEach(player -> {
            var level = player.level();
            ((ServerLevel) level).sendParticles(player, ParticleRegistry.FLAME_PARTICLE.get(), true, x, y, z, flameParticleAmount, .125, .125, .125,.225 );

            if (isSmog) {
                ((ServerLevel) level).sendParticles(player, ParticleTypes.CAMPFIRE_COSY_SMOKE, true, x, y, z, 20, .1, .1, .1, .2);
            }

            ((ServerLevel) level).sendParticles(player, new CommonParticleOptions(LHMiracleRoadSpellTool.RGBChangeVector3f(255,163,37), range * rangeAdditional,true,ParticleRegistry.BLAST_WAVE_PARTICLE.get()),true, xOld, yOld, zOld, 1, 0, 0, 0, 0);
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
                    entity.setSecondsOnFire(secondsOnFireTime);
                    entity.hurt(LHMiracleRoadSpellTool.getDamageSourceType(getOwner(), SpellDamageTypes.FLAME_MAGIC), getDamage());
                }
            }
            getImpactSound().ifPresent(this::doImpactSound);
            this.discard();
        }
    }
}
