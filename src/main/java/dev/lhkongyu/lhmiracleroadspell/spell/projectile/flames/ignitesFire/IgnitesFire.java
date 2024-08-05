package dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.ignitesFire;

import dev.lhkongyu.lhmiracleroadspell.generator.SpellDamageTypes;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;

import java.util.List;

public class IgnitesFire {

    private static final float damage = 10;

    private static final int secondsOnFireTime = 1;

    public static void releaseIgnitesFire(Level level, LivingEntity livingEntity){
        if (!level.isClientSide) {
            HitResult hitResult = LHMiracleRoadSpellTool.playerMousePointBlock(livingEntity, 5);
            BlockHitResult blockHitResult = null;
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                blockHitResult = (BlockHitResult) hitResult;

                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockPos ignitePos = blockPos.above();
                BlockState fireBlockState = Blocks.FIRE.defaultBlockState();
                level.setBlock(ignitePos, fireBlockState, 10);
            }

            // 获取玩家的视线方向
            Vec3 lookVec = livingEntity.getViewVector(1.0F);
            // 计算出发射位置：在玩家眼睛的位置基础上，沿着视线方向前进一定距离（例如 1.0 个单位）
            Vec3 startPosition = livingEntity.getEyePosition(1.0F).add(lookVec.scale(2));

            level.playSound(null, startPosition.x, startPosition.y, startPosition.z, SoundEvents.BLAZE_SHOOT, SoundSource.NEUTRAL, 1F, 1F);

            level.getServer().getPlayerList().getPlayers().forEach(serverPlayer -> {
                ((ServerLevel) level).sendParticles(serverPlayer, ParticleRegistry.FIRE_PARTICLE.get(), true,  startPosition.x, startPosition.y, startPosition.z, 30, .05, .05, .05,.1);
            });

            Vec3 aabbPosition = livingEntity.getPosition(0f).add(lookVec.scale(2));
            AABB aabb = new AABB(aabbPosition, aabbPosition).inflate(2,3,2);  // 创建一个小的AABB用于碰撞检测
            List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class,aabb);
            for (LivingEntity target : targets) {
                if (target != livingEntity) {
                    target.setSecondsOnFire(secondsOnFireTime);
                    target.hurt(LHMiracleRoadSpellTool.getDamageSourceType(livingEntity, SpellDamageTypes.FLAME_MAGIC), damage);
                }
            }
        }
    }
}
