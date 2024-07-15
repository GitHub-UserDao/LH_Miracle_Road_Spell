package dev.lhkongyu.lhmiracleroadspell.items;

import dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.bigFireball.BigFireballProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.blazeBomb.BlazeBombProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.blazeBurn.BlazeBurn;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.burningFlames.BurningFlamesProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.destructionFlame.DestructionFlameProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.fireball.FireballProjectile;
import dev.lhkongyu.lhmiracleroadspell.registry.ItemsRegistry;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

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
//        spellDestructionFlame(level,player);
//        createFireField(level,player);
//        releaseBlazeBurn(level,player);
        return InteractionResultHolder.sidedSuccess(itemStack,level.isClientSide());
    }

    private static void fireProjectile(Level level, Player player, String itemId){
        String fireball = ItemsRegistry.FIREBALL.get().getDescriptionId();
        if (itemId.equals(fireball)){
            spellFireball(level,player);
        }

        String blaze_bomb = ItemsRegistry.BLAZE_BOMB.get().getDescriptionId();
        if (itemId.equals(blaze_bomb)){
            spellBlazeBomb(level,player);
        }

        String big_fireball = ItemsRegistry.BIG_FIREBALL.get().getDescriptionId();
        if (itemId.equals(big_fireball)){
            spellBigFireball(level,player);
        }
    }

    /**
     * 发射火球术
     * @param level
     * @param player
     */
    private static void spellFireball(Level level, Player player){
        FireballProjectile fireball = new FireballProjectile(level,player);
        // 获取玩家的视线方向
        Vec3 lookVec = player.getViewVector(1.0F);
        // 计算出发射位置：在玩家眼睛的位置基础上，沿着视线方向前进一定距离（例如 1.0 个单位）
        Vec3 startPosition = player.getEyePosition(1.0F).add(lookVec.scale(1.5)).subtract(0, 0.4, 0);
        // 将火球的位置设置到这个起始位置
        fireball.setPos(startPosition);
        // 设置火球的射击方向
        fireball.shoot(lookVec.x, lookVec.y, lookVec.z, 1.5f, 0.0f); // 第五个参数为发射速度，第六个参数为发射精度
        // 设置火球的伤害值
        fireball.setDamage(10);
        //设置伤害范围
        fireball.setRange(2);
        // 设置重量值
        fireball.setGravity(0.03);
        level.addFreshEntity(fireball);
    }

    /**
     * 发射烈焰爆弹
     * @param level
     * @param player
     */
    private static void spellBlazeBomb(Level level, Player player){
        BlazeBombProjectile blazeBomb = new BlazeBombProjectile(level,player);
        // 获取玩家的视线方向
        Vec3 lookVec = player.getViewVector(1.0F);
        // 计算出发射位置：在玩家眼睛的位置基础上，沿着视线方向前进一定距离（例如 1.0 个单位）
        Vec3 startPosition = player.getEyePosition(1.0F).add(lookVec.scale(1.0)).subtract(0, 0.4, 0);
        // 设置起始位置
        blazeBomb.setPos(startPosition);
        // 设置射击方向
        blazeBomb.shoot(lookVec.x, lookVec.y, lookVec.z, 1.5f, 0.0f); // 第五个参数为发射速度，第六个参数为发射精度
        // 设置基础伤害值
        blazeBomb.setDamage(10);
        // 设置重量值
        blazeBomb.setGravity(0.03);
        level.addFreshEntity(blazeBomb);
    }

    /**
     * 发射大火球术
     * @param level
     * @param player
     */
    private static void spellBigFireball(Level level, Player player){
        BigFireballProjectile bigFireball = new BigFireballProjectile(level,player);
        // 获取玩家的视线方向
        Vec3 lookVec = player.getViewVector(1.0F);
        // 计算出发射位置：在玩家眼睛的位置基础上，沿着视线方向前进一定距离（例如 1.0 个单位）
        Vec3 startPosition = player.getEyePosition(1.0F).add(lookVec.scale(2.5)).subtract(0, 0.4, 0);
        // 设置起始位置
        bigFireball.setPos(startPosition);
        // 设置射击方向
        bigFireball.shoot(lookVec.x, lookVec.y, lookVec.z, 1.4f, 0.0f); // 第五个参数为发射速度，第六个参数为发射精度
        // 设置基础伤害值
        bigFireball.setDamage(25);
        //设置伤害范围
        bigFireball.setRange(6);
        // 设置重量值
        bigFireball.setGravity(0.035);
        level.addFreshEntity(bigFireball);
    }

    /**
     * 发射毁灭烈火
     * @param level
     * @param player
     */
    private static void spellDestructionFlame(Level level, Player player){
        DestructionFlameProjectile destructionFlame = new DestructionFlameProjectile(level,player);
        // 获取玩家的视线方向
        Vec3 lookVec = player.getViewVector(1.0F);
        // 计算出发射位置：在玩家眼睛的位置基础上，沿着视线方向前进一定距离（例如 1.0 个单位）
        Vec3 startPosition = player.getEyePosition(1.0F).add(lookVec.scale(1.0)).subtract(0, 0.4, 0);
        // 设置起始位置
        destructionFlame.setPos(startPosition);
        // 设置射击方向
        destructionFlame.shoot(lookVec.x, lookVec.y, lookVec.z, 0.1f, 0.0f); // 第五个参数为发射速度，第六个参数为发射精度
        // 设置基础伤害值
        destructionFlame.setDamage(25);
        //设置伤害范围
        destructionFlame.setRange(3);
        level.addFreshEntity(destructionFlame);
    }

    /**
     * 释放 爆裂火海
     * @param level
     * @param player
     */
    private static void createFireField(Level level,Player player){
        if (!level.isClientSide) {
            // 获取玩家的视线命中的目标
            HitResult hitResult = LHMiracleRoadSpellTool.playerMousePointObject(player,32);

            Vec3 lookVec = null;
            if (hitResult.getType() == HitResult.Type.ENTITY){
                lookVec = hitResult.getLocation().add(0,-1,0);
            }else {
                lookVec = hitResult.getLocation();
            }
            BurningFlamesProjectile fire = new BurningFlamesProjectile(level,lookVec,player,12,5,3,4);
            fire.setDuration(240);
            level.addFreshEntity(fire);
        }
    }

    /**
     * 释放 烈火焚净
     * @param level
     * @param player
     */
    private static void releaseBlazeBurn(Level level,Player player){
        if (!level.isClientSide) {
            // 获取玩家的视线命中的目标
            HitResult hitResult = LHMiracleRoadSpellTool.playerMousePointObject(player,32);
            EntityHitResult entityHitResult = null;
            if (hitResult.getType() == HitResult.Type.ENTITY){
                entityHitResult = (EntityHitResult) hitResult;
            }else {
                player.sendSystemMessage(Component.translatable("lhmiracleroadspell.spell.blaze_burn.prompt").withStyle(ChatFormatting.RED));
                return;
            }
            BlazeBurn.burnTarget((ServerLevel) level, (ServerPlayer) player,entityHitResult,8f,6);
        }
    }
}
