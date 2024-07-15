package dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.destructionFlame;

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
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class DestructionFlameProjectile extends AbstractMagicProjectile {

    private static final int SPEED_BOOST_INTERVAL = 30;

    private static final double SPEED_BOOST_PERCENTAGE = 2;

    private int speedBoostTimer = 0;

    private int range = 0;

    private int rangeAdditional = 1;

    public void setRange(int range){
        this.range = range;
    }

    public void setRangeAdditional(int rangeAdditional){
        this.rangeAdditional = rangeAdditional;
    }

    public DestructionFlameProjectile(EntityType<? extends DestructionFlameProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(false);
        this.setExpireTime(8);
    }

    public DestructionFlameProjectile(Level pLevel, LivingEntity pShooter) {
        this(EntityRegistry.SIN_BLAZE_PROJECTILE.get(), pLevel);
        this.setOwner(pShooter);
    }

    @Override
    public void trailParticles() {
//        Vec3 vec3 = getDeltaMovement();
        double d0 = this.getX() ;
        double d1 = this.getY();
        double d2 = this.getZ();

        int growthParticles = (tickCount / 20);

        double growthRange = ((double) tickCount / 20 / 4);

        for (int i = 0; i < 16 + (growthParticles * 2); i++) {
            Vec3 motion = LHMiracleRoadSpellTool.getRandomVec3(.125f + growthRange * 0.75).subtract(getDeltaMovement().scale(.125f + growthRange * 0.75));
            Vec3 pos = LHMiracleRoadSpellTool.getRandomVec3(.35f + growthRange * 0.75);
            this.level().addParticle(ParticleRegistry.FIRE_BOTTOM_PARTICLE.get(),true, d0 + pos.x, d1 + 0.75D + pos.y, d2 + pos.z, motion.x, motion.y, motion.z);
        }
//        double posX = getX();
//        double posY = getY();
//        double posZ = getZ();
//        int count = 20 + growthParticles;
//        double radius = 1.5 + growthRange * 1.2;
//
//        for (int i = 0; i < count; i++) {
//            double angle = (Math.PI * 2 / count) * i;
//            double offsetX = Math.cos(angle) * radius;
//            double offsetZ = Math.sin(angle) * radius;
//            Vec3 pos = LHMiracleRoadSpellTool.getRandomVec3(.35f);
//            level().addParticle(ParticleRegistry.FIRE_TOP_PARTICLE.get(),true, posX + offsetX + pos.x, posY + 1 + pos.y, posZ + offsetZ + pos.z, 0, 0, 0);
//        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        level().getServer().getPlayerList().getPlayers().forEach(player -> {
            var level = player.level();
            int growthParticles = tickCount / 20;
            float growthRange = (float) (tickCount / 20  * 2.5);
            ((ServerLevel) level).sendParticles(player, ParticleRegistry.FIRE_PARTICLE.get(), true, x, y, z, 40 + (growthParticles * 35), .1, .1, .1,0.1 + ((double) growthParticles * 0.05));

            ((ServerLevel) level).sendParticles(player, ParticleTypes.CAMPFIRE_COSY_SMOKE, true, x, y, z, 30 + (growthParticles * 20), .1, .1, .1,0.1 + ((double) growthParticles * 0.055));

            ((ServerLevel) level).sendParticles(player, new BlastWaveParticleOptions(LHMiracleRoadSpellTool.RGBChangeVector3f(255,163,37), (range + growthRange) * rangeAdditional,true),true, xOld, yOld + 0.75D, zOld, 1, 0, 0, 0, 0);

            ((ServerLevel) level).sendParticles(player, new BlastWaveParticleOptions(LHMiracleRoadSpellTool.RGBChangeVector3f(255,163,37), (range + growthRange) * rangeAdditional),true, xOld, yOld + 0.75D, zOld, 1, 0, 0, 0, 0);
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
            float growthRange = (float) (tickCount / 20  * 2.5);
            float damage = (float) (getDamage() * (1 + (growthRange / 4 * 0.4)));
            float explosionRadius = (range + growthRange) * rangeAdditional;
            var explosionRadiusSqr = explosionRadius * explosionRadius;
            var entities = level().getEntities(this, this.getBoundingBox().inflate(explosionRadius));
            for (Entity entity : entities) {
                double distanceSqr = entity.distanceToSqr(hitResult.getLocation());
                if (distanceSqr < explosionRadiusSqr && canHitEntity(entity)){
                    entity.setSecondsOnFire(5);
                    entity.hurt(level().damageSources().playerAttack((Player) getOwner()), damage);
                }
            }
            getImpactSound().ifPresent(this::doImpactSound);
            this.discard();
        }
    }

    @Override
    public void tick() {
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (tickCount > EXPIRE_TIME) {
            onHit(hitresult);
            if (this.level().isClientSide){
                this.discard();
            }
            return;
        }

        if (level().isClientSide) {
            trailParticles();
        }

        if (hitresult.getType() != HitResult.Type.MISS  && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            onHit(hitresult);
        }
        setPos(position().add(getDeltaMovement()));
        ProjectileUtil.rotateTowardsMovement(this, 0.5f);

        if (this.isNoGravity()) {
            Vec3 vec34 = this.getDeltaMovement();
            this.setDeltaMovement(vec34.x, vec34.y - getGravity(), vec34.z);
        }

        speedBoostTimer++;
        if (speedBoostTimer >= SPEED_BOOST_INTERVAL) {
            if (!level().isClientSide){
                acceleratedParticles();
            }
            speedBoostTimer = 0;
            double speedMultiplier = 1 + SPEED_BOOST_PERCENTAGE;
            setDeltaMovement(getDeltaMovement().scale(speedMultiplier));
        }
    }

    private void acceleratedParticles() {
        level().getServer().getPlayerList().getPlayers().forEach(player -> {
            var level = player.level();
            ((ServerLevel) level).sendParticles(player, ParticleRegistry.FLAME_PARTICLE.get(), true, xOld, yOld + 0.75D, zOld, 30, .1, .1, .1,0.1);

            float growthRange = (float) (tickCount / 20 / 4);
            ((ServerLevel) level).sendParticles(player, new BlastWaveParticleOptions(LHMiracleRoadSpellTool.RGBChangeVector3f(255,163,37), 1.5f + growthRange),true, xOld, yOld + 0.75D, zOld, 1, 0, 0, 0, 0);
        });
    }
}
