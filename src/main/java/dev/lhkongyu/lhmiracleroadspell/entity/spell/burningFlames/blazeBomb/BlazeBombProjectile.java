package dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.blazeBomb;

import dev.lhkongyu.lhmiracleroadspell.entity.spell.AbstractMagicProjectile;
import dev.lhkongyu.lhmiracleroadspell.registry.EntityRegistry;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class BlazeBombProjectile extends AbstractMagicProjectile {
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

        return Optional.of(SoundEvents.FIREWORK_ROCKET_BLAST);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (!level().isClientSide) {
            level().explode(getOwner(), getX(), getY(), getZ(), 1.75F, Level.ExplosionInteraction.NONE);
        }
        discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        var target = entityHitResult.getEntity();
        target.setSecondsOnFire(5);
        target.hurt(level().damageSources().playerAttack((Player) getOwner()), getDamage());
        if (!level().isClientSide) {
            level().explode(getOwner(), getX(), getY(), getZ(), 1.75F, Level.ExplosionInteraction.NONE);
        }
        discard();
    }


    @Override
    public void impactParticles(double x, double y, double z) {
        level().getServer().getPlayerList().getPlayers().forEach(player -> ((ServerLevel) level()).sendParticles(player, ParticleRegistry.FIRE_BOTTOM_PARTICLE.get(), true, x, y, z, 5, .1, .1, .1,.25 ));
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
