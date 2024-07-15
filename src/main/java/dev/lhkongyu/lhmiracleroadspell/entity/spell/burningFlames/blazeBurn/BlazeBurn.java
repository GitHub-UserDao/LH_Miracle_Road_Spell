package dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.blazeBurn;

import dev.lhkongyu.lhmiracleroadspell.registry.DamageTypesRegistry;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import dev.lhkongyu.lhmiracleroadspell.tool.particle.ParticleEffectsTool;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BlazeBurn {

    public static void burnTarget(ServerLevel pLevel, ServerPlayer player, EntityHitResult entityHitResult, float damage, float radius){
        if (!pLevel.isClientSide) {
            Entity hitEntity = entityHitResult.getEntity();
            int particleCount = (int) (20 * (hitEntity.getBbWidth() * hitEntity.getBbHeight()));
            pLevel.playSound(null, hitEntity.getX(), hitEntity.getY(), hitEntity.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.NEUTRAL, 1F, 1.4F);
            pLevel.getServer().getPlayerList().getPlayers().forEach(p -> {
                pLevel.sendParticles(player, ParticleRegistry.FIRE_BOTTOM_PARTICLE.get(), true,
                        hitEntity.getX(), hitEntity.getY() + hitEntity.getBbHeight() * 0.5, hitEntity.getZ(),
                        particleCount, hitEntity.getBbWidth() / 4, hitEntity.getBbHeight() / 4, hitEntity.getBbWidth() / 4,0.15);
            });
            hitEntity.hurt(LHMiracleRoadSpellTool.getDamageSourceType(player, DamageTypesRegistry.FLAME_MAGIC), damage);

//            var explosionRadiusSqr = radius * radius;
//            var entities = pLevel.getEntities(hitEntity, hitEntity.getBoundingBox().inflate(radius));
//            for (Entity entity : entities) {
//                double distanceSqr = entity.distanceToSqr(position);
//                if (distanceSqr < explosionRadiusSqr && canHitEntity(player,entity)){
//                    entity.setSecondsOnFire(5);
//                    entity.hurt(pLevel.damageSources().playerAttack(player), damage);
//                }
//            }
        }
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
