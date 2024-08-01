package dev.lhkongyu.lhmiracleroadspell.event.client;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.annihilatorMeteor.AnnihilatorMeteorRenderer;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.bigFireball.BigFireballRenderer;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.blazeBomb.BlazeBombRenderer;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.destructionFlame.DestructionFlameRenderer;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.evilGodFlame.EvilGodFlameRenderer;
import dev.lhkongyu.lhmiracleroadspell.particle.common.BlastWaveParticle;
import dev.lhkongyu.lhmiracleroadspell.particle.fire.*;
import dev.lhkongyu.lhmiracleroadspell.registry.EntityRegistry;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LHMiracleRoadSpell.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegistryEvent {

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.FIRE_TOP_PARTICLE.get(), FireTopParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.FIRE_BOTTOM_PARTICLE.get(), FireBottomParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.FIRE_PARTICLE.get(), FireParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.FLAME_PARTICLE.get(), FlameParticle.Provider::new);

        event.registerSpriteSet(ParticleRegistry.BLAST_WAVE_PARTICLE.get(), BlastWaveParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.FIRE_EXPLOSION_PARTICLE.get(), FireExplosion.Provider::new);

    }

    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        //test 练习
        event.registerEntityRenderer(EntityRegistry.FIREBALL_PROJECTILE_TEST.get(), ThrownItemRenderer::new);

        //火焰魔法实体注册
        event.registerEntityRenderer(EntityRegistry.BLAZE_BOMB_PROJECTILE.get(), BlazeBombRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BIG_FIREBALL_PROJECTILE.get(), (context) -> new BigFireballRenderer(context, 2f));
        event.registerEntityRenderer(EntityRegistry.FIREBALL_PROJECTILE.get(), (context) -> new BigFireballRenderer(context, 1f));
        event.registerEntityRenderer(EntityRegistry.SUPER_BIG_FIREBALL_PROJECTILE.get(), (context) -> new BigFireballRenderer(context, 3f));
        event.registerEntityRenderer(EntityRegistry.DESTRUCTION_FLAME.get(), (context) -> new DestructionFlameRenderer(context, 0.25f));
        event.registerEntityRenderer(EntityRegistry.BURNING_FLAMES.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.FLAME_DEVOURING.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.EVIL_GOD_FLAME.get(), EvilGodFlameRenderer::new);
        event.registerEntityRenderer(EntityRegistry.ANNIHILATOR_METEOR_PROJECTILE.get(), AnnihilatorMeteorRenderer::new);

        //通用实体注册
        event.registerEntityRenderer(EntityRegistry.COMMON_AOE.get(), NoopRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BigFireballRenderer.MODEL_LAYER_LOCATION, BigFireballRenderer::createBodyLayer);
        event.registerLayerDefinition(DestructionFlameRenderer.MODEL_LAYER_LOCATION, DestructionFlameRenderer::createBodyLayer);
        event.registerLayerDefinition(EvilGodFlameRenderer.MODEL_LAYER_LOCATION, EvilGodFlameRenderer::createBodyLayer);
    }
}
