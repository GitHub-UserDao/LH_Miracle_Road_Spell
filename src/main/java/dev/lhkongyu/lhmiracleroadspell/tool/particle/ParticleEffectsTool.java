package dev.lhkongyu.lhmiracleroadspell.tool.particle;

import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class ParticleEffectsTool {

    public static void igniteAndExplode(ServerPlayer serverPlayer, ServerLevel serverLevel, Vec3 position){
        serverLevel.playSound(null, position.x, position.y, position.z, SoundEvents.BLAZE_SHOOT, SoundSource.NEUTRAL, 2F, 1.3F);
        serverLevel.playSound(null, position.x, position.y, position.z, SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 0.8F, 1.5F);
        serverLevel.getServer().getPlayerList().getPlayers().forEach(p -> {
            serverLevel.sendParticles(serverPlayer, ParticleRegistry.FLAME_PARTICLE.get(), true, position.x, position.y, position.z, 50, .3, .3, .3,.35 );
            serverLevel.sendParticles(serverPlayer, ParticleTypes.CAMPFIRE_COSY_SMOKE, true, position.x, position.y, position.z, 30, .3, .3, .3,.15 );
        });
    }
}
