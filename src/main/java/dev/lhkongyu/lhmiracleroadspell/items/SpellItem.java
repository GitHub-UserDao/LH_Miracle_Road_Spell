package dev.lhkongyu.lhmiracleroadspell.items;

import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.ReleaseFlamesSpellTool;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.flameDevouring.FlameDevouring;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.flamePower.FlamePower;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.ignitesFire.IgnitesFire;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpellItem extends Item {
    public SpellItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
//        // 发射粒子逻辑
//        // 设置粒子的起始位置和方向
//        Vec3 startVec = player.getEyePosition(1.0F);
//        Vec3 lookVec = player.getViewVector(1.0F);
//        // 发射粒子和检测实体
//        double t = 3;
//        while (t <= 30) {
//            Vec3 particlePos = startVec.add(lookVec.scale(t));
//            level.addParticle(ParticleRegistry.FIRE_PARTICLE.get(), particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
//
//            // 检查这个点附近是否有实体
//            AABB aabb = new AABB(particlePos, particlePos).inflate(0.5);  // 创建一个小的AABB用于碰撞检测
//            List<Entity> entities = level.getEntities(player, aabb);  // 查找AABB区域内的所有实体
//            for (Entity entity : entities) {
//                if (entity instanceof LivingEntity && entity != player) {  // 避免自我伤害
//                    ((LivingEntity)entity).hurt(level.damageSources().magic(), 5.0F);  // 造成5点魔法伤害
//                    return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
//                }
//            }
//
//            t += 1;  // 这里可以调整粒子之间的距离，影响精确度和性能
//        }

//        int numParticles = 20; // 圆环粒子数量
//        double angleIncrement = 2 * Math.PI / numParticles;
//
//        for (int i = 0; i < numParticles; i++) {
//            double angle = i * angleIncrement;
//
//            double yLowA = 0.1; // 底部A圆环的y轴位置
//            double radiusLowA = 0.75; // 底部A 圆环半径
//            double LowXA = radiusLowA * Math.cos(angle);
//            double LowZA = radiusLowA * Math.sin(angle);
//
//            double yLowB = 0.5; // 底部B圆环的y轴位置
//            double radiusLowB = 1; // 底部B 圆环半径
//            double LowXB = radiusLowB * Math.cos(angle);
//            double LowZB = radiusLowB * Math.sin(angle);
//
//            double yMiddleA = 1; // 中间A圆环的y轴位置
//            double radiusMiddleA = 1.25; // 中间A圆环半径
//            double XMiddleA = radiusMiddleA * Math.cos(angle);
//            double ZMiddleA = radiusMiddleA * Math.sin(angle);
//
//            double yUpperA = 1.5; // 上方A圆环的y轴位置
//            double radiusUpperA = 1; // 上方A 圆环半径
//            double XUpperA = radiusUpperA * Math.cos(angle);
//            double ZUpperA = radiusUpperA * Math.sin(angle);
//
//            double yUpperB = 2; // 上方B圆环的y轴位置
//            double radiusUpperB = 0.75; // 上方B 圆环半径
//            double XUpperB = radiusUpperB * Math.cos(angle);
//            double ZUpperB = radiusUpperB * Math.sin(angle);
//            level.addParticle(ParticleRegistry.FIRE_PARTICLE.get(), player.getX() + LowXA , player.getY() + yLowA, player.getZ() + LowZA,0,0, 0);
//
//            level.addParticle(ParticleRegistry.FIRE_PARTICLE.get(), player.getX() + LowXB , player.getY() + yLowB, player.getZ() + LowZB,0,0, 0);
//
//            level.addParticle(ParticleRegistry.FIRE_PARTICLE.get(), player.getX() + XMiddleA , player.getY() + yMiddleA, player.getZ() + ZMiddleA,0,0, 0);
//
//            level.addParticle(ParticleRegistry.FIRE_PARTICLE.get(), player.getX() + XUpperA , player.getY() + yUpperA, player.getZ() + ZUpperA,0,0, 0);
//
//            level.addParticle(ParticleRegistry.FIRE_PARTICLE.get(), player.getX() + XUpperB , player.getY() + yUpperB, player.getZ() + ZUpperB,0,0, 0);
//
//        }

        fireProjectile(level,player,itemStack.getDescriptionId());
        return InteractionResultHolder.sidedSuccess(itemStack,level.isClientSide());
    }

    private static void fireProjectile(Level level, Player player, String itemId){
        switch (itemId) {
            case "item.lhmiracleroadspell.fireball" -> ReleaseFlamesSpellTool.spellFireball(level,player);
            case "item.lhmiracleroadspell.blaze_bomb" -> ReleaseFlamesSpellTool.spellBlazeBomb(level,player);
            case "item.lhmiracleroadspell.big_fireball" -> ReleaseFlamesSpellTool.spellBigFireball(level,player);
            case "item.lhmiracleroadspell.blaze_purification" -> ReleaseFlamesSpellTool.releaseBlazePurification(level,player);
            case "item.lhmiracleroadspell.burning_flames" -> ReleaseFlamesSpellTool.releaseBurningFlames(level,player);
            case "item.lhmiracleroadspell.destruction_flame" -> ReleaseFlamesSpellTool.spellDestructionFlame(level,player);
            case "item.lhmiracleroadspell.flame_power" -> FlamePower.playerAddFlamePower(level,player);
            case "item.lhmiracleroadspell.ignites_fire" -> IgnitesFire.releaseIgnitesFire(level,player);
            case "item.lhmiracleroadspell.flame_devouring" -> FlameDevouring.releaseFlameDevouring(level,player);
            case "item.lhmiracleroadspell.evil_god_flame" -> ReleaseFlamesSpellTool.spellEvilGodFlame(level,player);
            case "item.lhmiracleroadspell.annihilator_meteor" -> ReleaseFlamesSpellTool.releaseSpellAnnihilatorMeteor(level,player);
        }
    }
}
