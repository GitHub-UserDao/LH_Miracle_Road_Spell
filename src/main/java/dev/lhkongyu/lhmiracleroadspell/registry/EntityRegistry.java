package dev.lhkongyu.lhmiracleroadspell.registry;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.fireball.BigFireballProjectile;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.CommonAoeProjectile;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.annihilatorMeteor.AnnihilatorMeteorProjectile;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.fireMeteor.FireMeteorProjectile;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.fireMeteor.BigFireMeteorProjectile;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.blazeBomb.BlazeBombProjectile;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.burningFlames.BurningFlamesProjectile;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.destructionFlame.DestructionFlameProjectile;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.evilGodFlame.EvilGodFlameProjectile;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.flameDevouring.FlameDevouringProjectile;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.textFireBall.FireballProjectileTest;
import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.fireball.FireballProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LHMiracleRoadSpell.MODID);

    //火焰法术实体

    //火球术实体
    public static final RegistryObject<EntityType<FireballProjectile>> FIREBALL_PROJECTILE =
            ENTITIES.register("fireball", () -> EntityType.Builder.<FireballProjectile>of(FireballProjectile::new, MobCategory.MISC)
                    .sized(0.6f, 0.6f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "fireball").toString()));

    //大火球术实体 测试版本
    public static final RegistryObject<EntityType<BigFireballProjectile>> TEST_FIREBALL =
            ENTITIES.register("test_fireball", () -> EntityType.Builder.<BigFireballProjectile>of(BigFireballProjectile::new, MobCategory.MISC)
                    .sized(1.5f, 1.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "test_fireball").toString()));

    //火焰流星实体
    public static final RegistryObject<EntityType<FireMeteorProjectile>> FIRE_METEOR_PROJECTILE =
            ENTITIES.register("fire_meteor", () -> EntityType.Builder.<FireMeteorProjectile>of(FireMeteorProjectile::new, MobCategory.MISC)
                    .sized(1.5f, 1.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "fire_meteor").toString()));

    //大火焰流星实体
    public static final RegistryObject<EntityType<BigFireMeteorProjectile>> BIG_FIRE_METEOR_PROJECTILE =
            ENTITIES.register("big_fire_meteor", () -> EntityType.Builder.<BigFireMeteorProjectile>of(BigFireMeteorProjectile::new, MobCategory.MISC)
                    .sized(1.5f, 1.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "big_fire_meteor").toString()));

    //烈焰爆弹实体
    public static final RegistryObject<EntityType<BlazeBombProjectile>> BLAZE_BOMB_PROJECTILE =
            ENTITIES.register("blaze_bomb", () -> EntityType.Builder.<BlazeBombProjectile>of(BlazeBombProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "blaze_bomb").toString()));

    //毁灭烈火实体
    public static final RegistryObject<EntityType<DestructionFlameProjectile>> DESTRUCTION_FLAME =
            ENTITIES.register("destruction_flame", () -> EntityType.Builder.<DestructionFlameProjectile>of(DestructionFlameProjectile::new, MobCategory.MISC)
                    .sized(1.5f, 1.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "sin_blaze").toString()));

    //爆裂火海实体
    public static final RegistryObject<EntityType<BurningFlamesProjectile>> BURNING_FLAMES =
            ENTITIES.register("burning_flames", () -> EntityType.Builder.<BurningFlamesProjectile>of(BurningFlamesProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "burning_flames").toString()));

    //火焰吞噬实体
    public static final RegistryObject<EntityType<FlameDevouringProjectile>> FLAME_DEVOURING =
            ENTITIES.register("flame_devouring", () -> EntityType.Builder.<FlameDevouringProjectile>of(FlameDevouringProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "flame_devouring").toString()));

    //恶神火焰实体
    public static final RegistryObject<EntityType<EvilGodFlameProjectile>> EVIL_GOD_FLAME =
            ENTITIES.register("evil_god_flame", () -> EntityType.Builder.<EvilGodFlameProjectile>of(EvilGodFlameProjectile::new, MobCategory.MISC)
                    .sized(2f, 2f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "evil_god_flame").toString()));

    //送葬者的流星
    public static final RegistryObject<EntityType<AnnihilatorMeteorProjectile>> ANNIHILATOR_METEOR_PROJECTILE =
            ENTITIES.register("annihilator_meteor", () -> EntityType.Builder.<AnnihilatorMeteorProjectile>of(AnnihilatorMeteorProjectile::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "annihilator_meteor").toString()));





/** 通用实体 */

    //aoe 实体
    public static final RegistryObject<EntityType<CommonAoeProjectile>> COMMON_AOE =
            ENTITIES.register("common_aoe", () -> EntityType.Builder.<CommonAoeProjectile>of(CommonAoeProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "common_aoe").toString()));

    //测试
    public static final RegistryObject<EntityType<FireballProjectileTest>> FIREBALL_PROJECTILE_TEST =
            ENTITIES.register("wither_skull", () -> EntityType.Builder.<FireballProjectileTest>of(FireballProjectileTest::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .setTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "wither_skull").toString()));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
