package dev.lhkongyu.lhmiracleroadspell.tool;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.burningFlames.BurningFlamesProjectile;
import dev.lhkongyu.lhmiracleroadspell.registry.DamageTypesRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.entity.PartEntity;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LHMiracleRoadSpellTool {

    public static RandomSource randomSource = RandomSource.createThreadSafe();

    public static double getRandomScaled(double scale) {
        return (2.0D * Math.random() - 1.0D) * scale;
    }

    public static Vec3 getRandomVec3(double scale) {
        return new Vec3(
                getRandomScaled(scale),
                getRandomScaled(scale),
                getRandomScaled(scale)
        );
    }

    public static ResourceLocation resourceLocationId(String path){

        return new ResourceLocation(LHMiracleRoadSpell.MODID,path);
    }

    public static Vector3f RGBChangeVector3f(int red,int green,int blue){
        // 将通道值归一化到范围[0, 1]
        float normalizedRed = (float) red / 255.0f;
        float normalizedGreen = (float) green / 255.0f;
        float normalizedBlue = (float) blue / 255.0f;

        return new Vector3f(normalizedRed, normalizedGreen, normalizedBlue);
    }

    public static double randomSpecifyValue(double... values){
        Random random = new Random();
        int randomIndex = random.nextInt(values.length);

        return values[randomIndex];
    }

    public static HitResult playerMousePointObject(Player player,double endDistance){
        Level level = player.level();
        Vec3 startPos = player.getEyePosition(1f);
        Vec3 endPos = player.getLookAngle().normalize().scale(endDistance).add(startPos);

        BlockHitResult blockHitResult = level.clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        endPos = blockHitResult.getLocation();
        AABB range = player.getBoundingBox().expandTowards(endPos.subtract(startPos));

        List<HitResult> hits = new ArrayList<>();
        List<? extends Entity> entities = level.getEntities(player, range,LHMiracleRoadSpellTool::canHitRayCast);
        for (Entity target : entities) {
            HitResult hit = checkEntityIntersecting(target, startPos, endPos, 0);
            if (hit != null)
                hits.add(hit);
        }

        if (!hits.isEmpty()) {
            hits.sort((o1, o2) -> o1.getLocation().distanceToSqr(startPos) < o2.getLocation().distanceToSqr(startPos) ? -1 : 1);
            return hits.get(0);
        }
//        Vec3 lookVector = player.getViewVector(1.0f);
//        Vec3 missEnd = player.getPosition(0f).add(lookVector.x * 6, 0, lookVector.z * 6);
        return BlockHitResult.miss(endPos, Direction.UP, BlockPos.containing(endPos));
//        Minecraft minecraft = Minecraft.getInstance();
//        if (minecraft.hitResult != null) {
//            switch (minecraft.hitResult.getType()) {
//                case ENTITY:
//                    Entity entity = ((EntityHitResult) minecraft.hitResult).getEntity();
//                    return entity.getPosition(0.0F);
//                case BLOCK:
//                        BlockHitResult blockHitResult = (BlockHitResult) minecraft.hitResult;
//                    return blockHitResult.getLocation();
//            }
//        }

//        // 进行射线检测，获取玩家鼠标指针指向的物品或生物
//        HitResult hitResult = player.level().clip(new ClipContext(startPos, endPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, player));
//        if (hitResult.getType() == HitResult.Type.ENTITY) {
//            Entity entity = ((EntityHitResult) hitResult).getEntity();
//            return entity.getPosition(0.0F);
//        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
//            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
//            return blockHitResult.getLocation();
//        }

//        Vec3 vec3 = player.getPosition(0);
//        return vec3;
    }

    private static boolean canHitRayCast(Entity entity) {
        return entity.isPickable() && entity.isAlive();
    }

    public static HitResult checkEntityIntersecting(Entity entity, Vec3 start, Vec3 end, float bbInflation) {
        Vec3 hitPos = null;
        if (entity.isMultipartEntity()) {
            for (PartEntity p : entity.getParts()) {
                var hit = p.getBoundingBox().inflate(bbInflation).clip(start, end).orElse(null);
                if (hit != null) {
                    hitPos = hit;
                    break;
                }
            }
        } else {
            hitPos = entity.getBoundingBox().inflate(bbInflation).clip(start, end).orElse(null);
                    }
                    if (hitPos != null) return new EntityHitResult(entity, hitPos);
                    else return null;

    }

    public static DamageSource getDamageSourceType(Player player, ResourceKey<DamageType> resourceKey){
        Holder<DamageType> option = player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(resourceKey);

        return new DamageSource(option,player,player,null);
    }
}
