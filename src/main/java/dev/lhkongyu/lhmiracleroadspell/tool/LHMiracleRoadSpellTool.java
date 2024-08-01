package dev.lhkongyu.lhmiracleroadspell.tool;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import dev.lhkongyu.lhmiracleroadspell.generator.SpellDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.entity.PartEntity;
import org.joml.Vector3f;

import java.util.*;

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

    public static HitResult playerMousePointObject(LivingEntity livingEntity, double endDistance, float expandAmount){
        Level level = livingEntity.level();
        Vec3 startPos = livingEntity.getEyePosition(1f);
        Vec3 endPos = livingEntity.getLookAngle().normalize().scale(endDistance).add(startPos);

        BlockHitResult blockHitResult = level.clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity));
        endPos = blockHitResult.getLocation();
        AABB range = livingEntity.getBoundingBox().expandTowards(endPos.subtract(startPos));
        range = range.inflate(expandAmount);

        List<HitResult> hits = new ArrayList<>();
        List<? extends Entity> entities = level.getEntities(livingEntity, range,LHMiracleRoadSpellTool::canHitRayCast);
        for (Entity target : entities) {
            HitResult hit = checkEntityIntersecting(target, startPos, endPos, expandAmount);
            if (hit != null)
                hits.add(hit);
        }

        if (!hits.isEmpty()) {
            hits.sort((o1, o2) -> o1.getLocation().distanceToSqr(startPos) < o2.getLocation().distanceToSqr(startPos) ? -1 : 1);
            return hits.get(0);
        }

        return BlockHitResult.miss(endPos, Direction.UP, BlockPos.containing(endPos));
    }

    private static boolean canHitRayCast(Entity entity) {
        return entity.isPickable() && entity.isAlive();
    }

    public static HitResult checkEntityIntersecting(Entity entity, Vec3 start, Vec3 end, float bbInflation) {
        Vec3 hitPos = null;
        if (entity.isMultipartEntity()) {
            for (PartEntity p : entity.getParts()) {
                if (p == null) continue;
                AABB boundingBox = entity.getBoundingBox();
                hitPos = boundingBox.clip(start, end).orElse(null);
                if (hitPos != null) break;

                if (bbInflation > 0) {
                    boundingBox = boundingBox.inflate(bbInflation);
                    Vec3 direction = end.subtract(start).normalize();
                    Vec3[] offsets = {
                            direction.scale(bbInflation),
                            direction.scale(-bbInflation),
                            new Vec3(bbInflation, 0, 0),
                            new Vec3(-bbInflation, 0, 0),
                            new Vec3(0, bbInflation, 0),
                            new Vec3(0, -bbInflation, 0)
                    };

                    for (Vec3 offset : offsets) {
                        hitPos = boundingBox.clip(start.add(offset), end.add(offset)).orElse(null);

                        if (hitPos != null) {
                            break;
                        }
                    }
                }

            }
        } else {

            AABB boundingBox = entity.getBoundingBox();
            hitPos = boundingBox.clip(start, end).orElse(null);
            if (hitPos == null && bbInflation > 0) {
                boundingBox = boundingBox.inflate(bbInflation);
                Vec3 direction = end.subtract(start).normalize();
                Vec3[] offsets = {
                        direction.scale(bbInflation),
                        direction.scale(-bbInflation),
                        new Vec3(bbInflation, 0, 0),
                        new Vec3(-bbInflation, 0, 0),
                        new Vec3(0, bbInflation, 0),
                        new Vec3(0, -bbInflation, 0)
                };

                for (Vec3 offset : offsets) {
                    hitPos = boundingBox.clip(start.add(offset), end.add(offset)).orElse(null);

                    if (hitPos != null) {
                        break;
                    }
                }
            }
        }

        if (hitPos != null) return new EntityHitResult(entity, hitPos);
        else return null;

    }

    public static DamageSource getDamageSourceType(Entity entity, ResourceKey<DamageType> resourceKey){
        var option = entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(resourceKey);
        if (option.isPresent()) {
            Set<TagKey<DamageType>> tagSet = new HashSet<>();
            option.get().tags().map(tagSet::add);
            tagSet.add(DamageTypeTags.BYPASSES_ARMOR);
            option.get().bindTags(tagSet);
            return option.map(damageTypeReference -> new DamageSource(damageTypeReference, entity)).orElseGet(() -> new DamageSource(entity.level().damageSources().genericKill().typeHolder(), entity));
        }
        return new DamageSource(entity.level().damageSources().genericKill().typeHolder(), entity);
    }

    public static int getDuration(int duration){
        return duration * 20;
    }

    /**
     * 范围随机
     * @param min
     * @param max
     * @return
     */
    public static int randomNumber(int min,int max){

        return Math.min(new Random().nextInt(max) + min,max);
    }

    /**
     * 范围随机
     * @param min
     * @param max
     * @return
     */
    public static float randomNumber(float min,float max){

        return Math.min(new Random().nextFloat(max - min) + min,max);
    }

    /**
     * 概率计算
     *
     * @param probability
     * @return
     */
    public static boolean percentageProbability(int probability) {
        if (probability >= 100) return true;
        else if (probability < 1) return false;

        return new Random().nextInt(100) < probability;
    }

    /**
     * 概率计算
     *
     * @param probability
     * @return
     */
    public static boolean percentageProbability(double probability) {
        if (probability >= 100.0) return true;
        else if (probability <= 0.0) return false;

        return new Random().nextDouble() * 100.0 < probability;
    }

}
