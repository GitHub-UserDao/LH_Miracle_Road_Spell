package dev.lhkongyu.lhmiracleroadspell.particle;

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

public class CommonParticleOptions extends DustParticleOptionsBase {

    private ParticleType<CommonParticleOptions> particleOptionsParticleType;

    private final float scale;

    private boolean isPlane = false;

    public CommonParticleOptions(Vector3f color, float scale,ParticleType<CommonParticleOptions> particleOptionsParticleType) {
        super(color, scale);
        this.scale = scale;
        this.particleOptionsParticleType = particleOptionsParticleType;
    }

    public CommonParticleOptions(Vector3f color, float scale, boolean isPlane,ParticleType<CommonParticleOptions> particleOptionsParticleType) {
        super(color, scale);
        this.scale = scale;
        this.isPlane = isPlane;
        this.particleOptionsParticleType = particleOptionsParticleType;
    }

    public CommonParticleOptions(Vector3f color, float scale) {
        super(color,scale);
        this.scale = scale;
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

    public static final Codec<CommonParticleOptions> CODEC = RecordCodecBuilder.create((p_175793_) -> p_175793_.group(ExtraCodecs.VECTOR3F.fieldOf("color").forGetter((p_175797_) -> p_175797_.color), Codec.FLOAT.fieldOf("scale").forGetter((p_175795_) -> p_175795_.scale)).apply(p_175793_, CommonParticleOptions::new));
    @SuppressWarnings("deprecation")
    public static final Deserializer<CommonParticleOptions> DESERIALIZER = new Deserializer<>() {
        public CommonParticleOptions fromCommand(@NotNull ParticleType<CommonParticleOptions> p_123689_, @NotNull StringReader p_123690_) throws CommandSyntaxException {
            return null;
        }

        public CommonParticleOptions fromNetwork(@NotNull ParticleType<CommonParticleOptions> p_123692_, @NotNull FriendlyByteBuf p_123693_) {
            return null;
        }
    };

    public @NotNull ParticleType<CommonParticleOptions> getType() {
        return particleOptionsParticleType;
    }
}
