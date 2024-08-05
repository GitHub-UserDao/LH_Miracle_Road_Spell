package dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.evilGodFlame;

import dev.lhkongyu.lhmiracleroadspell.spell.projectile.CommonMagicProjectile;
import dev.lhkongyu.lhmiracleroadspell.generator.SpellDamageTypes;
import dev.lhkongyu.lhmiracleroadspell.particle.CommonParticleOptions;
import dev.lhkongyu.lhmiracleroadspell.registry.EntityRegistry;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class EvilGodFlameProjectile extends CommonMagicProjectile {

    private Entity target;

    private static final int CHANGE_TIME = 60;

    private float baseSpeed = 0.2f;

    private float changeSpeedIncrease = 0;

    private int range = 0;

    private int rangeAdditional = 1;

    private final int secondsOnFireTime = 1;

    public void setRange(int range){
        this.range = range;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public void setBaseSpeed(float baseSpeed){
        this.baseSpeed = baseSpeed;
    }

    public void setRangeAdditional(int rangeAdditional){
        this.rangeAdditional = rangeAdditional;
    }

    public EvilGodFlameProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setExpireTime(8);
        this.setNoGravity(false);
    }

    public EvilGodFlameProjectile(Level pLevel, LivingEntity pShooter) {
        this(EntityRegistry.EVIL_GOD_FLAME.get(), pLevel);
        this.setOwner(pShooter);
    }

    @Override
    public void trailParticles() {
        double d0 = this.getX() ;
        double d1 = this.getY();
        double d2 = this.getZ();

        for (int i = 0; i < 10; i++) {
            Vec3 motion = LHMiracleRoadSpellTool.getRandomVec3(.35f).subtract(getDeltaMovement().scale(.35f));
            Vec3 pos = LHMiracleRoadSpellTool.getRandomVec3(.5f);
            this.level().addParticle(ParticleRegistry.FIRE_BOTTOM_PARTICLE.get(),true, d0 + pos.x, d1 + 1 + pos.y, d2 + pos.z, motion.x, motion.y, motion.z);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        level().getServer().getPlayerList().getPlayers().forEach(player -> {
            var level = player.level();
            ((ServerLevel) level).sendParticles(player, ParticleRegistry.FLAME_PARTICLE.get(), true, x, y, z, 100, .125, .125, .125,.225 );

            ((ServerLevel) level).sendParticles(player, ParticleTypes.CAMPFIRE_COSY_SMOKE, true, x, y, z, 25, .1, .1, .1,.2);

            ParticleType<CommonParticleOptions> particleOptionsParticleType = ParticleRegistry.BLAST_WAVE_PARTICLE.get();
            float growthRange = range;
            if (tickCount >= CHANGE_TIME){
                particleOptionsParticleType = ParticleRegistry.FIRE_EXPLOSION_PARTICLE.get();
                growthRange = range * 2;
            }
            ((ServerLevel) level).sendParticles(player, new CommonParticleOptions(LHMiracleRoadSpellTool.RGBChangeVector3f(255,163,37), growthRange * rangeAdditional,particleOptionsParticleType),true, xOld, yOld + 0.75D, zOld, 1, 0, 0, 0, 0);

            ((ServerLevel) level).sendParticles(player, new CommonParticleOptions(LHMiracleRoadSpellTool.RGBChangeVector3f(255,163,37), growthRange  * rangeAdditional,true,particleOptionsParticleType),true, xOld, yOld, zOld, 1, 0, 0, 0, 0);
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

    @Override
    public void tick() {
        super.tick();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (tickCount > EXPIRE_TIME) {
            onHit(hitresult);
            this.discard();
            return;
        }

        if (level().isClientSide) {
            trailParticles();
        }

        if (hitresult.getType() != HitResult.Type.MISS  && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            onHit(hitresult);
        }

        setPos(position().add(getDeltaMovement()));

        if (target != null && target.isAlive()) {
            // 获取投掷实体当前位置
            Vec3 currentPosition = position();

            // 计算到目标的方向向量
            Vec3 targetPosition = target.getEyePosition(target.getEyeHeight() / 2); // 以目标的眼睛位置为准
            Vec3 direction = targetPosition.subtract(currentPosition).normalize();

            // 设置实体的移动方向
            setDeltaMovement(direction.scale(baseSpeed + changeSpeedIncrease));
        }else if (this.isNoGravity()) {
            Vec3 vec34 = this.getDeltaMovement();
            this.setDeltaMovement(vec34.x, vec34.y - getGravity(), vec34.z);
        }

        if (tickCount == CHANGE_TIME && !level().isClientSide) {
            acceleratedParticles();
            setDamage(getDamage() * 1.5f);
            setRange(12);
            changeSpeedIncrease = 0.2f;
            setDeltaMovement(getDeltaMovement().scale(baseSpeed + changeSpeedIncrease + 1.5));
        }
    }

    private void acceleratedParticles() {
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.NEUTRAL, 1.5F, 1F);
        level().getServer().getPlayerList().getPlayers().forEach(player -> {
            var level = player.level();
            float growthRange = range * 2;
            ((ServerLevel) level).sendParticles(player, new CommonParticleOptions(LHMiracleRoadSpellTool.RGBChangeVector3f(255,163,37), growthRange * rangeAdditional,ParticleRegistry.FIRE_EXPLOSION_PARTICLE.get()),true, xOld, yOld + 0.75D, zOld, 1, 0, 0, 0, 0);
        });
    }
}
