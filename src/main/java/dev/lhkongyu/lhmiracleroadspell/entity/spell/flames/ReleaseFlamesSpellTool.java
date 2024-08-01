package dev.lhkongyu.lhmiracleroadspell.entity.spell.flames;

import dev.lhkongyu.lhmiracleroadspell.entity.spell.CommonAoeProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.annihilatorMeteor.AnnihilatorMeteorProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.bigFireball.BigFireballProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.bigFireball.SuperBigFireballProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.blazeBomb.BlazeBombProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.blazePurification.BlazePurification;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.burningFlames.BurningFlamesProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.destructionFlame.DestructionFlameProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.evilGodFlame.EvilGodFlameProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.fireball.FireballProjectile;
import dev.lhkongyu.lhmiracleroadspell.registry.EntityRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.DirectionUtils;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ReleaseFlamesSpellTool {

    /**
     * 发射火球术
     * @param level
     * @param livingEntity
     */
    public static void spellFireball(Level level,LivingEntity livingEntity){
        if (level.isClientSide) return;
        FireballProjectile fireball = new FireballProjectile(level,livingEntity);
        // 获取玩家的视线方向
        Vec3 lookVec = livingEntity.getViewVector(1.0F);
        // 计算出发射位置：在玩家眼睛的位置基础上，沿着视线方向前进一定距离（例如 1.0 个单位）
        Vec3 startPosition = livingEntity.getEyePosition(1.0F).add(lookVec.scale(2)).subtract(0, 0.4, 0);
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
     * @param livingEntity
     */
    public static void spellBlazeBomb(Level level, LivingEntity livingEntity){
        if (level.isClientSide) return;
        BlazeBombProjectile blazeBomb = new BlazeBombProjectile(level,livingEntity);
        // 获取玩家的视线方向
        Vec3 lookVec = livingEntity.getViewVector(1.0F);
        // 计算出发射位置：在玩家眼睛的位置基础上，沿着视线方向前进一定距离（例如 1.0 个单位）
        Vec3 startPosition = livingEntity.getEyePosition(1.0F).add(lookVec.scale(2)).subtract(0, 0.4, 0);
        // 设置起始位置
        blazeBomb.setPos(startPosition);
        // 设置射击方向
        blazeBomb.shoot(lookVec.x, lookVec.y, lookVec.z, 1.5f, 0.0f); // 第五个参数为发射速度，第六个参数为发射精度
        // 设置基础伤害值
        blazeBomb.setDamage(10);
        // 设置重量值
        blazeBomb.setGravity(0.03);
        // 设置范围
        blazeBomb.setRange(3);
        level.addFreshEntity(blazeBomb);
    }

    /**
     * 发射大火球术
     * @param level
     * @param livingEntity
     */
    public static void spellBigFireball(Level level, LivingEntity livingEntity){
        if (level.isClientSide) return;
        // 获取玩家的视线方向
        Vec3 lookVec = livingEntity.getViewVector(1.0F);
        // 计算出发射位置：在玩家眼睛的位置基础上，沿着视线方向前进一定距离（例如 1.0 个单位）
        Vec3 startPosition = livingEntity.getEyePosition(1.0F).add(lookVec.scale(3.5)).subtract(0, 0.4, 0);
        createSpellBigFireball(level,livingEntity,lookVec,startPosition,true);
    }

    //创建大火球
    public static void createSpellBigFireball(Level level, LivingEntity livingEntity, Vec3 lookVec, Vec3 startPosition,boolean isSmog){
        BigFireballProjectile bigFireball = new BigFireballProjectile(level,livingEntity);
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
        bigFireball.setSmog(isSmog);
        level.addFreshEntity(bigFireball);
    }

    //创建超大火球
    public static void createSpellSuperBigFireball(Level level, LivingEntity livingEntity, Vec3 lookVec, Vec3 startPosition,boolean isSmog){
        SuperBigFireballProjectile superBigFireball = new SuperBigFireballProjectile(level,livingEntity);
        // 设置起始位置
        superBigFireball.setPos(startPosition);
        // 设置射击方向
        superBigFireball.shoot(lookVec.x, lookVec.y, lookVec.z, 1.4f, 0.0f); // 第五个参数为发射速度，第六个参数为发射精度
        // 设置基础伤害值
        superBigFireball.setDamage(37.5f);
        //设置伤害范围
        superBigFireball.setRange(10);
        // 设置重量值
        superBigFireball.setGravity(0.04);
        superBigFireball.setSmog(isSmog);
        level.addFreshEntity(superBigFireball);
    }

    /**
     * 发射毁灭烈火
     * @param level
     * @param livingEntity
     */
    public static void spellDestructionFlame(Level level, LivingEntity livingEntity){
        if (level.isClientSide) return;
        DestructionFlameProjectile destructionFlame = new DestructionFlameProjectile(level, livingEntity);
        // 获取玩家的视线方向
        Vec3 lookVec = livingEntity.getViewVector(1.0F);
        // 计算出发射位置：在玩家眼睛的位置基础上，沿着视线方向前进一定距离（例如 1.0 个单位）
        Vec3 startPosition = livingEntity.getEyePosition(1.0F).add(lookVec.scale(1)).subtract(0, 0.4, 0);
        // 设置起始位置
        destructionFlame.setPos(startPosition);
        // 设置射击方向
        destructionFlame.shoot(lookVec.x, lookVec.y, lookVec.z, 0.1f, 0.0f); // 第五个参数为发射速度，第六个参数为发射精度
        // 设置基础伤害值
        destructionFlame.setDamage(25);
        //设置伤害范围
        destructionFlame.setRange(6);
        level.addFreshEntity(destructionFlame);
    }

    /**
     * 释放 爆裂火海
     * @param level
     * @param livingEntity
     */
    public static void releaseBurningFlames(Level level,LivingEntity livingEntity){
        if (level.isClientSide) return;

        // 获取玩家的视线命中的目标
        HitResult hitResult = LHMiracleRoadSpellTool.playerMousePointObject(livingEntity,32,1);

        Vec3 lookVec = null;
        if (hitResult.getType() == HitResult.Type.ENTITY){
            lookVec = hitResult.getLocation().add(0,-1,0);
        }else {
            lookVec = hitResult.getLocation();
        }
        BurningFlamesProjectile fire = new BurningFlamesProjectile(level,lookVec,livingEntity,12,5,3,4);
        fire.setDuration(240);
        level.addFreshEntity(fire);
    }

    /**
     * 释放 烈火焚净
     * @param level
     * @param livingEntity
     */
    public static void releaseBlazePurification(Level level,LivingEntity livingEntity){
        if (level.isClientSide) return;

        // 获取玩家的视线命中的目标
        HitResult hitResult = LHMiracleRoadSpellTool.playerMousePointObject(livingEntity,32,0.5f);
        EntityHitResult entityHitResult = null;
        if (hitResult.getType() == HitResult.Type.ENTITY){
            entityHitResult = (EntityHitResult) hitResult;
        }else {
            livingEntity.sendSystemMessage(Component.translatable("lhmiracleroadspell.spell.blaze_burn.prompt").withStyle(ChatFormatting.RED));
            return;
        }
        BlazePurification.burnTarget((ServerLevel) level, (ServerPlayer) livingEntity,entityHitResult,8f,6);
    }

    /**
     * 发射恶神火焰
     * @param level
     * @param livingEntity
     */
    public static void spellEvilGodFlame(Level level, LivingEntity livingEntity){
        if (level.isClientSide) return;

        // 获取玩家的视线命中的目标
        HitResult hitResult = LHMiracleRoadSpellTool.playerMousePointObject(livingEntity, 64, 1);
        EntityHitResult entityHitResult = null;
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            entityHitResult = (EntityHitResult) hitResult;
        }

        float baseSpeed = 0.2f;

        EvilGodFlameProjectile evilGodFlame = new EvilGodFlameProjectile(level, livingEntity);
        // 获取玩家的视线方向
        Vec3 lookVec = livingEntity.getViewVector(1.0F);
        // 计算出发射位置：在玩家眼睛的位置基础上，沿着视线方向前进一定距离（例如 1.0 个单位）
        Vec3 startPosition = livingEntity.getEyePosition(1.0F).add(lookVec.scale(3.5)).subtract(0, 0.4, 0);
        // 设置起始位置
        evilGodFlame.setPos(startPosition);
        // 设置射击方向
        evilGodFlame.shoot(lookVec.x, lookVec.y, lookVec.z, baseSpeed, 0.0f); // 第五个参数为发射速度，第六个参数为发射精度
        //设置初始速度
        evilGodFlame.setBaseSpeed(baseSpeed);
        // 设置基础伤害值
        evilGodFlame.setDamage(30);
        //设置伤害范围
        evilGodFlame.setRange(9);
        //设置跟踪目标
        if (entityHitResult != null) {
            evilGodFlame.setTarget(entityHitResult.getEntity());
        }
        level.addFreshEntity(evilGodFlame);
    }

    /**
     * 释放 送葬者的流星
     * @param level
     * @param livingEntity
     */
    public static void releaseSpellAnnihilatorMeteor(Level level, LivingEntity livingEntity){
        if (level.isClientSide) return;

        // 获取玩家的视线命中的目标
        HitResult hitResult = LHMiracleRoadSpellTool.playerMousePointObject(livingEntity,32,1);

        Vec3 hitVec = null;
        if (hitResult.getType() == HitResult.Type.ENTITY){
            hitVec = hitResult.getLocation().add(0,0,0);
        }else {
            hitVec = hitResult.getLocation();
        }

        CommonAoeProjectile commonAoe = new CommonAoeProjectile(level);
        // 获取玩家的视线方向
        Vec3 lookVec = livingEntity.getViewVector(1.0F);
        Vec3 direction = DirectionUtils.getMeteorTransmitDirection(lookVec);
        Vec3 startPosition = hitVec.add(0,50,0);
        commonAoe.moveTo(startPosition);
        commonAoe.setOwner(livingEntity);
        commonAoe.setRadius(24);
        commonAoe.setMaxRounds(6);
        commonAoe.setHigh(1);
        int duration = (int) Math.pow(commonAoe.getRadius() / commonAoe.getSpacing() * 2 + 1,2) * commonAoe.getMaxRounds() + 10;
        commonAoe.setDuration(duration);
        commonAoe.setPlayerTowardX(direction.x);
        commonAoe.setPlayerTowardZ(direction.z);
        commonAoe.setSpellEntityType(EntityRegistry.ANNIHILATOR_METEOR_PROJECTILE.getId().toString());
        level.addFreshEntity(commonAoe);
    }

    /**
     * 创建 流星
     * @param level
     * @param livingEntity
     */
    public static void createMeteor(Level level, LivingEntity livingEntity, Vec3 position, double playerTowardX, double playerTowardZ){
        if (level.isClientSide) return;

        AnnihilatorMeteorProjectile annihilatorMeteor = new AnnihilatorMeteorProjectile(level,livingEntity);
        annihilatorMeteor.setPos(position);
        annihilatorMeteor.setPlayerTowardX(playerTowardX);
        annihilatorMeteor.setPlayerTowardZ(playerTowardZ);
        level.addFreshEntity(annihilatorMeteor);
    }
}
