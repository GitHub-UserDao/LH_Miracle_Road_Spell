package dev.lhkongyu.lhmiracleroadspell.particle.fire;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.lhkongyu.lhmiracleroadspell.particle.CommonParticleOptions;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class FireExplosion extends TextureSheetParticle {
    private static final Vector3f ROTATION_VECTOR = Util.make(new Vector3f(0.5F, 0.5F, 0.5F), Vector3f::normalize);
    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0F, -1.0F, 0.0F);
    private final float targetSize;
    private final SpriteSet sprites;

    private final boolean isPlane;

    FireExplosion(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double xd, double yd, double zd, CommonParticleOptions options) {
        super(pLevel, pX, pY, pZ, 0, 0, 0);

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;

        this.targetSize = options.getScale();
        this.quadSize = 1;
        this.lifetime = 8;
        this.gravity = .1f;
        sprites = spriteSet;

        float f = random.nextFloat() * 0.14F + 0.85F;
        this.rCol = options.getColor().x() * f;
        this.gCol = options.getColor().y() * f;
        this.bCol = options.getColor().z() * f;
        this.friction = 1;

        this.isPlane = options.getIsPlane();
    }

    @Override
    public float getQuadSize(float partialTick) {
        var f = (partialTick + this.age) / (float) this.lifetime;
        return this.quadSize * Mth.lerp(1 - (1 - f) * (1 - f), targetSize * .75f, targetSize);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(sprites);
            this.move(this.xd, this.yd, this.zd);
            this.yd *= .85f;
            this.xd *= .94f;
            this.zd *= .94f;
        }
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialticks) {
        if (isPlane) {
            this.renderRotatedParticle(buffer, camera, partialticks, (p_234005_) -> {
                p_234005_.mul(Axis.YP.rotation(0));
                p_234005_.mul(Axis.XP.rotation(-(float) (Math.PI / 2f)));
            });
            this.renderRotatedParticle(buffer, camera, partialticks, (p_234000_) -> {
                p_234000_.mul(Axis.YP.rotation(-(float) Math.PI));
                p_234000_.mul(Axis.XP.rotation((float) (Math.PI / 2f)));
            });
        }else {
            super.render(buffer,camera,partialticks);
        }
    }

    private void renderRotatedParticle(VertexConsumer pConsumer, Camera camera, float partialTick, Consumer<Quaternionf> pQuaternion) {
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp(partialTick, this.xo, this.x) - vec3.x());
        float f1 = (float) (Mth.lerp(partialTick, this.yo, this.y) - vec3.y());
        float f2 = (float) (Mth.lerp(partialTick, this.zo, this.z) - vec3.z());
        Quaternionf quaternion = (new Quaternionf()).setAngleAxis(0.0F, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());

        pQuaternion.accept(quaternion);
        quaternion.transform(TRANSFORM_VECTOR);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f3 = this.getQuadSize(partialTick);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternion);
            vector3f.mul(f3);
            vector3f.add(f, f1, f2);
        }

        int j = this.getLightColor(partialTick);
        this.makeCornerVertex(pConsumer, avector3f[0], this.getU1(), this.getV1(), j);
        this.makeCornerVertex(pConsumer, avector3f[1], this.getU1(), this.getV0(), j);
        this.makeCornerVertex(pConsumer, avector3f[2], this.getU0(), this.getV0(), j);
        this.makeCornerVertex(pConsumer, avector3f[3], this.getU0(), this.getV1(), j);
    }

    private void makeCornerVertex(VertexConsumer pConsumer, Vector3f pVec3f, float p_233996_, float p_233997_, int p_233998_) {
        pConsumer.vertex(pVec3f.x(), pVec3f.y() + .08f, pVec3f.z()).uv(p_233996_, p_233997_).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(p_233998_).endVertex();
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float p_107564_) {
        return 240;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<CommonParticleOptions> {
        private final SpriteSet sprite;

        public Provider(SpriteSet pSprite) {
            this.sprite = pSprite;
        }

        public Particle createParticle(@NotNull CommonParticleOptions options, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            FireExplosion fireExplosion = new FireExplosion(pLevel, pX, pY, pZ, sprite, pXSpeed, pYSpeed, pZSpeed, options);
            fireExplosion.setSpriteFromAge(this.sprite);
            fireExplosion.setAlpha(1.0F);
            return fireExplosion;
        }
    }

}