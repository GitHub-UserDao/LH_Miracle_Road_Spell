package dev.lhkongyu.lhmiracleroadspell.registry;

import com.mojang.serialization.Codec;
import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import dev.lhkongyu.lhmiracleroadspell.particle.CommonParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleRegistry {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, LHMiracleRoadSpell.MODID);

    public static final RegistryObject<SimpleParticleType> FIRE_TOP_PARTICLE = PARTICLE_TYPES.register("fire_top", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> FIRE_BOTTOM_PARTICLE = PARTICLE_TYPES.register("fire_bottom", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> FIRE_PARTICLE = PARTICLE_TYPES.register("fire", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> FLAME_PARTICLE = PARTICLE_TYPES.register("flame", () -> new SimpleParticleType(false));

    public static final RegistryObject<ParticleType<CommonParticleOptions>> BLAST_WAVE_PARTICLE = PARTICLE_TYPES.register("blast_wave", () -> new ParticleType<CommonParticleOptions>(true, CommonParticleOptions.DESERIALIZER) {
        public Codec<CommonParticleOptions> codec() {
            return CommonParticleOptions.CODEC;
        }
    });

    public static final RegistryObject<ParticleType<CommonParticleOptions>> FIRE_EXPLOSION_PARTICLE = PARTICLE_TYPES.register("fire_explosion", () -> new ParticleType<CommonParticleOptions>(true, CommonParticleOptions.DESERIALIZER) {
        public Codec<CommonParticleOptions> codec() {
            return CommonParticleOptions.CODEC;
        }
    });

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
