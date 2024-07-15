package dev.lhkongyu.lhmiracleroadspell.particle.fire;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FireTopParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    public FireTopParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xd, double yd, double zd) {

        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
//        this.rCol = 1.0F; // Red color (1.0是最亮)
//        this.gCol = 1.0F; // Green color (1.0是最亮)
//        this.bCol = 1.0F; // Blue color (1.0是最亮)
//        this.alpha = 0.75F; // 设置透明度，1.0是不透明

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.scale(this.random.nextFloat() * 1.75f + 1f);
        this.lifetime = 2 + (int) (Math.random() * 5);
        sprites = spriteSet;
        this.setSpriteFromAge(spriteSet);
        this.gravity = -0.015F;
    }

    @Override
    public void tick() {
        super.tick();
        this.xd += this.random.nextFloat() / 50.0F * (float) (this.random.nextBoolean() ? 1 : -1);
        this.yd += this.random.nextFloat() / 50.0F;
        this.zd += this.random.nextFloat() / 50.0F * (float) (this.random.nextBoolean() ? 1 : -1);
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new FireTopParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }

    @Override
    public int getLightColor(float p_107564_) {
        return 240;
    }
}
