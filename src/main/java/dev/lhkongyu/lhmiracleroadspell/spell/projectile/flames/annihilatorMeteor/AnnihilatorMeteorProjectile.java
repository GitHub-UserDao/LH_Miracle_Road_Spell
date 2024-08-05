package dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.annihilatorMeteor;

import dev.lhkongyu.lhmiracleroadspell.spell.projectile.CommonMagicProjectile;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.ReleaseFlamesSpellTool;
import dev.lhkongyu.lhmiracleroadspell.registry.EntityRegistry;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class AnnihilatorMeteorProjectile extends CommonMagicProjectile {

    private final float DELAY_TIME = 15f;

    private double playerTowardX;

    private double playerTowardZ;

    public void setPlayerTowardX(double playerTowardX) {
        this.playerTowardX = playerTowardX;
    }

    public void setPlayerTowardZ(double playerTowardZ) {
        this.playerTowardZ = playerTowardZ;
    }

    public AnnihilatorMeteorProjectile(EntityType<? extends AnnihilatorMeteorProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setExpireTime(1);
        this.setNoGravity(false);
    }

    public AnnihilatorMeteorProjectile(Level pLevel, LivingEntity pShooter) {
        this(EntityRegistry.ANNIHILATOR_METEOR_PROJECTILE.get(), pLevel);
        this.setOwner(pShooter);
    }

    @Override
    public void trailParticles() {
        Vec3 vec3 = getDeltaMovement();
        double d0 = this.getX() - vec3.x;
        double d1 = this.getY() - vec3.y;
        double d2 = this.getZ() - vec3.z;
        for (int i = 0; i < 1; i++) {
            Vec3 motion = LHMiracleRoadSpellTool.getRandomVec3(.2f).subtract(getDeltaMovement().scale(.2f));
            Vec3 pos = LHMiracleRoadSpellTool.getRandomVec3(2f);
            this.level().addParticle(ParticleRegistry.FIRE_BOTTOM_PARTICLE.get(),true, d0 + pos.x, d1, d2 + pos.z, motion.x, motion.y, motion.z);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > EXPIRE_TIME) {
            this.discard();
            return;
        }
        if (tickCount == DELAY_TIME && !level().isClientSide && getOwner() instanceof Player player){
            Vec3 lookVec = new Vec3(playerTowardX,  -0.85f, playerTowardZ);
            Vec3 startPosition = this.getPosition(0F).add(lookVec.scale(2));
            if (LHMiracleRoadSpellTool.percentageProbability(20)) ReleaseFlamesSpellTool.createSpellBigFireMeteor(level(),player,lookVec,startPosition);
            else ReleaseFlamesSpellTool.createSpellFireMeteor(level(),player,lookVec,startPosition);

        }
        if (level().isClientSide) {
            trailParticles();
        }
    }
}
