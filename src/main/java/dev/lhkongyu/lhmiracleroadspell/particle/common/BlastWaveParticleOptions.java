package dev.lhkongyu.lhmiracleroadspell.particle.common;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.lhkongyu.lhmiracleroadspell.registry.ParticleRegistry;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class BlastWaveParticleOptions extends DustParticleOptionsBase {
    private final float scale;

    private boolean isPlane = false;

    public BlastWaveParticleOptions(Vector3f color, float scale) {
        super(color, scale);
        this.scale = scale;
    }

    public BlastWaveParticleOptions(Vector3f color, float scale,boolean isPlane) {
        super(color, scale);
        this.scale = scale;
        this.isPlane = isPlane;
    }

    @Override
    public float getScale() {
        return scale;
    }

    public boolean getIsPlane(){

        return isPlane;
    }

    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeFloat(this.color.x());
        pBuffer.writeFloat(this.color.y());
        pBuffer.writeFloat(this.color.z());
        pBuffer.writeFloat(this.scale);
    }

    public static final Codec<BlastWaveParticleOptions> CODEC = RecordCodecBuilder.create((p_175793_) -> p_175793_.group(ExtraCodecs.VECTOR3F.fieldOf("color").forGetter((p_175797_) -> p_175797_.color), Codec.FLOAT.fieldOf("scale").forGetter((p_175795_) -> p_175795_.scale)).apply(p_175793_, BlastWaveParticleOptions::new));
    @SuppressWarnings("deprecation")
    public static final Deserializer<BlastWaveParticleOptions> DESERIALIZER = new Deserializer<>() {
        public @NotNull BlastWaveParticleOptions fromCommand(@NotNull ParticleType<BlastWaveParticleOptions> p_123689_, @NotNull StringReader p_123690_) throws CommandSyntaxException {
            Vector3f vector3f = DustParticleOptionsBase.readVector3f(p_123690_);
            p_123690_.expect(' ');
            float f = p_123690_.readFloat();
            return new BlastWaveParticleOptions(vector3f, f);
        }

        public @NotNull BlastWaveParticleOptions fromNetwork(@NotNull ParticleType<BlastWaveParticleOptions> p_123692_, @NotNull FriendlyByteBuf p_123693_) {
            return new BlastWaveParticleOptions(DustParticleOptionsBase.readVector3f(p_123693_), p_123693_.readFloat());
        }
    };

    public @NotNull ParticleType<BlastWaveParticleOptions> getType() {
        return ParticleRegistry.BLAST_WAVE_PARTICLE.get();
    }
}
