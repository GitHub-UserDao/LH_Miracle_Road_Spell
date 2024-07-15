package dev.lhkongyu.lhmiracleroadspell.event.client;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.bigFireball.BigFireballRenderer;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.blazeBomb.BlazeBombRenderer;
import dev.lhkongyu.lhmiracleroadspell.entity.spell.burningFlames.destructionFlame.DestructionFlameRenderer;
import dev.lhkongyu.lhmiracleroadspell.particle.common.BlastWaveParticle;
import dev.lhkongyu.lhmiracleroadspell.particle.fire.FireBottomParticle;
import dev.lhkongyu.lhmiracleroadspell.particle.fire.FireParticle;
import dev.lhkongyu.lhmiracleroadspell.particle.fire.FireTopParticle;
import dev.lhkongyu.lhmiracleroadspell.particle.fire.FlameParticle;
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
        event.registerSpriteSet(ParticleRegistry.BLAST_WAVE_PARTICLE.get(), BlastWaveParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.FLAME_PARTICLE.get(), FlameParticle.Provider::new);

    }

    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        //test 练习
        event.registerEntityRenderer(EntityRegistry.FIREBALL_PROJECTILE_TEST.get(), ThrownItemRenderer::new);

        //火焰魔法注册
        event.registerEntityRenderer(EntityRegistry.BLAZE_BOMB_PROJECTILE.get(), BlazeBombRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BIG_FIREBALL_PROJECTILE.get(), (context) -> new BigFireballRenderer(context, 2f));
        event.registerEntityRenderer(EntityRegistry.FIREBALL_PROJECTILE.get(), (context) -> new BigFireballRenderer(context, 1f));
        event.registerEntityRenderer(EntityRegistry.SIN_BLAZE_PROJECTILE.get(), (context) -> new DestructionFlameRenderer(context, 0.25f));

        event.registerEntityRenderer(EntityRegistry.FIRE_FIELD.get(), NoopRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BigFireballRenderer.MODEL_LAYER_LOCATION, BigFireballRenderer::createBodyLayer);
        event.registerLayerDefinition(DestructionFlameRenderer.MODEL_LAYER_LOCATION, DestructionFlameRenderer::createBodyLayer);
    }
}
