package dev.lhkongyu.lhmiracleroadspell.registry;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.bigFireball.BigFireballProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.blazeBomb.BlazeBombProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.burningFlames.BurningFlamesProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.destructionFlame.DestructionFlameProjectile;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.textFireBall.FireballProjectileTest;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.fireball.FireballProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LHMiracleRoadSpell.MODID);

    public static final RegistryObject<EntityType<FireballProjectile>> FIREBALL_PROJECTILE =
            ENTITIES.register("fireball", () -> EntityType.Builder.<FireballProjectile>of(FireballProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "fireball").toString()));

    public static final RegistryObject<EntityType<BigFireballProjectile>> BIG_FIREBALL_PROJECTILE =
            ENTITIES.register("big_fireball", () -> EntityType.Builder.<BigFireballProjectile>of(BigFireballProjectile::new, MobCategory.MISC)
                    .sized(1.5f, 1.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "big_fireball").toString()));

    public static final RegistryObject<EntityType<BlazeBombProjectile>> BLAZE_BOMB_PROJECTILE =
            ENTITIES.register("blaze_bomb", () -> EntityType.Builder.<BlazeBombProjectile>of(BlazeBombProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "blaze_bomb").toString()));

    public static final RegistryObject<EntityType<DestructionFlameProjectile>> SIN_BLAZE_PROJECTILE =
            ENTITIES.register("sin_blaze", () -> EntityType.Builder.<DestructionFlameProjectile>of(DestructionFlameProjectile::new, MobCategory.MISC)
                    .sized(1.5f, 1.5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "sin_blaze").toString()));


    public static final RegistryObject<EntityType<BurningFlamesProjectile>> FIRE_FIELD =
            ENTITIES.register("fire_field", () -> EntityType.Builder.<BurningFlamesProjectile>of(BurningFlamesProjectile::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(LHMiracleRoadSpell.MODID, "fire_field").toString()));



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
