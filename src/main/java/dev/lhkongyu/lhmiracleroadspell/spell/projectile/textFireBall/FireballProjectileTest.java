package dev.lhkongyu.lhmiracleroadspell.spell.projectile.textFireBall;

import dev.lhkongyu.lhmiracleroadspell.registry.EntityRegistry;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class FireballProjectileTest extends AbstractItemProjectile {
    public FireballProjectileTest(EntityType<? extends FireballProjectileTest> entityEntityType, Level world) {
        super(entityEntityType, world);
        this.setNoGravity(true);
    }

    public FireballProjectileTest(Level worldIn, LivingEntity throwerIn) {
        super(EntityRegistry.FIREBALL_PROJECTILE_TEST.get(), worldIn, throwerIn);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.FIRE_CHARGE;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.of(SoundEvents.FIREWORK_ROCKET_BLAST);
    }

    @Override
    protected void doImpactSound(SoundEvent sound) {
        level().playSound(null, getX(), getY(), getZ(), sound, SoundSource.NEUTRAL, 2, 1.2f + LHMiracleRoadSpellTool.randomSource.nextFloat() * .2f);

    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        var target = entityHitResult.getEntity();
        target.hurt(level().damageSources().playerAttack((Player) getOwner()), getDamage());
        discard();
    }

    @Override
    public void impactParticles(double x, double y, double z) {
    }

    @Override
    public void trailParticles() {
        float yHeading = -((float) (Mth.atan2(getDeltaMovement().z, getDeltaMovement().x) * (double) (180F / (float) Math.PI)) + 90.0F);
        float radius = .25f;
        int steps = 1;
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
}
