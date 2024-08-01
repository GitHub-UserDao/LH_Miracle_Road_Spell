package dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.blazeBomb;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BlazeBombRenderer extends EntityRenderer<Projectile> {

    private static final ResourceLocation FLAME_TEXTURE = new ResourceLocation("minecraft:textures/item/fire_charge.png");

    public BlazeBombRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(Projectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();

        PoseStack.Pose pose = poseStack.last();
        Vec3 motion = entity.getDeltaMovement();
        float xRot = -((float) (Mth.atan2(motion.horizontalDistance(), motion.y) * (double) (180F / (float) Math.PI)) - 180.0F);
        float yRot = -((float) (Mth.atan2(motion.z, motion.x) * (double) (180F / (float) Math.PI)) + 180.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        drawSlash(pose, entity, bufferSource, entity.getBbWidth() * 1.5f, true);

        poseStack.popPose();

        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    private void drawSlash(PoseStack.Pose pose, Projectile entity, MultiBufferSource bufferSource, float width, boolean mirrored) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
        float halfWidth = .5f;
        float halfHeight = .5f;
        // 设置顶点，使四边形竖直放置
        consumer.vertex(poseMatrix, 0, -halfWidth, -halfHeight).color(255, 255, 255, 255).uv(0f, mirrored ? 0f : 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0f, 0f, 1f).endVertex();
        consumer.vertex(poseMatrix, 0, halfWidth, -halfHeight).color(255, 255, 255, 255).uv(1f, mirrored ? 0f : 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0f, 0f, 1f).endVertex();
        consumer.vertex(poseMatrix, 0, halfWidth, halfHeight).color(255, 255, 255, 255).uv(1f, mirrored ? 1f : 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0f, 0f, 1f).endVertex();
        consumer.vertex(poseMatrix, 0, -halfWidth, halfHeight).color(255, 255, 255, 255).uv(0f, mirrored ? 1f : 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0f, 0f, 1f).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(Projectile projectile) {
        return FLAME_TEXTURE;
    }
}
