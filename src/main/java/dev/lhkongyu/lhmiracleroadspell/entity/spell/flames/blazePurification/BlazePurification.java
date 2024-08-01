package dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.blazePurification;

import dev.lhkongyu.lhmiracleroadspell.generator.SpellDamageTypes;
import dev.lhkongyu.lhmiracleroadspell.particle.CommonParticleOptions;
import dev.lhkongyu.lhmiracleroadspell.registry.EffectRegistry;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;

public class BlazePurification {

    private static final int secondsOnFireTime = 1;

    public static void burnTarget(ServerLevel pLevel, ServerPlayer player, EntityHitResult entityHitResult, float damage, float range){
        if (!pLevel.isClientSide) {
            Entity entity = entityHitResult.getEntity();
            LivingEntity hitEntity = null;
            if (entity instanceof LivingEntity livingEntity) hitEntity = livingEntity;
            else return;

            pLevel.playSound(null, hitEntity.getX(), hitEntity.getY(), hitEntity.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.NEUTRAL, 1F, 1.4F);
            if (livingEntity.getEffect(EffectRegistry.BURNING_ARMOR.get()) != null) {
                diffuseSpell(pLevel,player,entity,damage,range);
            }else {
                int particleCount = (int) (20 * (hitEntity.getBbWidth() * hitEntity.getBbHeight()));
                pLevel.getServer().getPlayerList().getPlayers().forEach(p -> {
                    pLevel.sendParticles(player, ParticleRegistry.FIRE_BOTTOM_PARTICLE.get(), true,
                            entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
                            particleCount, entity.getBbWidth() / 4, entity.getBbHeight() / 4, entity.getBbWidth() / 4,0.15);
                });
            }
            addBurnEffect(livingEntity);
            hitEntity.setSecondsOnFire(secondsOnFireTime);
            hitEntity.hurt(LHMiracleRoadSpellTool.getDamageSourceType(player, SpellDamageTypes.FLAME_MAGIC), damage);
        }
    }

    private static void diffuseSpell(ServerLevel pLevel,ServerPlayer serverPlayer,Entity hitEntity,float damage,float range){
        pLevel.playSound(null, hitEntity.getX(), hitEntity.getY(), hitEntity.getZ(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.NEUTRAL, 1.5F, 1.5F);
        pLevel.getServer().getPlayerList().getPlayers().forEach(player -> {
            var level = player.level();
            ((ServerLevel) level).sendParticles(player, ParticleRegistry.FIRE_PARTICLE.get(), true, hitEntity.getX(), hitEntity.getY(), hitEntity.getZ(), 75, .125, .125, .125,.25 );

            ((ServerLevel) level).sendParticles(player, new CommonParticleOptions(LHMiracleRoadSpellTool.RGBChangeVector3f(255,163,37), range,true,ParticleRegistry.BLAST_WAVE_PARTICLE.get()),true, hitEntity.getX(), hitEntity.getY() + hitEntity.getBbHeight() / 2, hitEntity.getZ(), 1, 0, 0, 0, 0);
        });

        var explosionRadiusSqr = range * range;
        var entities = pLevel.getEntities(hitEntity, hitEntity.getBoundingBox().inflate(range));
        for (Entity entity : entities) {
            double distanceSqr = entity.distanceToSqr(hitEntity);
            if (distanceSqr < explosionRadiusSqr && canHitEntity(serverPlayer,entity)){
                if (entity instanceof LivingEntity living) addBurnEffect(living);
                entity.setSecondsOnFire(secondsOnFireTime);
                entity.hurt(LHMiracleRoadSpellTool.getDamageSourceType(serverPlayer, SpellDamageTypes.FLAME_MAGIC), damage);
            }
        }


    }

    private static void addBurnEffect(LivingEntity livingEntity){
        MobEffectInstance mobEffectInstance = livingEntity.getEffect(EffectRegistry.BURNING_ARMOR.get());
        int amplifier = -1;
        if (mobEffectInstance != null) {
            amplifier = mobEffectInstance.getAmplifier();
        }
        livingEntity.addEffect(new MobEffectInstance(EffectRegistry.BURNING_ARMOR.get(), LHMiracleRoadSpellTool.getDuration(15), Math.min(amplifier + 1,4)));
    }

    public static boolean canHitEntity(Player player,Entity pTarget) {
        return (player != null && pTarget != player && !player.isAlliedTo(pTarget)) && canHit(player,pTarget);
    }

    public static boolean canHit(Player player,Entity pTarget) {
        if (!pTarget.canBeHitByProjectile()) {
            return false;
        } else {
            return player == null || !player.isPassengerOfSameVehicle(pTarget);
        }
    }
}
