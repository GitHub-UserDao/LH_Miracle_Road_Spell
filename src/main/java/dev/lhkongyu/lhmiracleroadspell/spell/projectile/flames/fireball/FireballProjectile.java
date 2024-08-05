package dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.fireball;

import dev.lhkongyu.lhmiracleroadspell.spell.entity.geckolib.flames.fireball.Fireball;
import dev.lhkongyu.lhmiracleroadspell.generator.SpellDamageTypes;
import dev.lhkongyu.lhmiracleroadspell.registry.EntityRegistry;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class FireballProjectile extends Fireball {

    private int range = 0;

    private final int secondsOnFireTime = 1;

    public void setRange(int range){
        this.range = range;
    }

    public FireballProjectile(EntityType<? extends FireballProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public FireballProjectile(Level levelIn, LivingEntity shooter) {
        this(EntityRegistry.FIREBALL_PROJECTILE.get(), levelIn);
        setOwner(shooter);
    }


    @Override
    public void trailParticles() {
        float shifting = shifting(0.75f);
        float yHeading = -((float) (Mth.atan2(getDeltaMovement().z, getDeltaMovement().x) * (double) (180F / (float) Math.PI)) + 90.0F);
        float radius = .25f;
        int steps = 6;
        for (int j = 0; j < steps; j++) {
            float offset = (1f / steps) * 0;
            double radians = ((tickCount + offset) / 7.5f) * 360 * Mth.DEG_TO_RAD;
            Vec3 swirl = new Vec3(Math.cos(radians) * radius, Math.sin(radians) * radius, 0).yRot(yHeading * Mth.DEG_TO_RAD);

            double x = getX() + swirl.x + shifting;
            double y = getY() + swirl.y + getBbHeight() / 2;
            double z = getZ() + swirl.z;
            Vec3 jitter = LHMiracleRoadSpellTool.getRandomVec3(.05f);
            level().addParticle(ParticleRegistry.FIRE_BOTTOM_PARTICLE.get(),true, x, y, z, jitter.x, jitter.y, jitter.z);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        level().getServer().getPlayerList().getPlayers().forEach(player -> {
            var level = player.level();
            ((ServerLevel) level).sendParticles(player, ParticleRegistry.FIRE_PARTICLE.get(), true, x, y, z, 50, .1, .1, .1,.125f);
        });
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.of(SoundEvents.DRAGON_FIREBALL_EXPLODE);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!this.level().isClientSide) {
            impactParticles(xOld, yOld, zOld);
            float explosionRadius = range;
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

    @Override
    public void doImpactSound(SoundEvent sound) {
        level().playSound(null, getX(), getY(), getZ(), sound, SoundSource.NEUTRAL, 1f, 1.5f);
    }
}