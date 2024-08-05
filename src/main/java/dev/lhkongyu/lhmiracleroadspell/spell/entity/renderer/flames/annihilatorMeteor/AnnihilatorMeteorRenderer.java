package dev.lhkongyu.lhmiracleroadspell.spell.entity.renderer.flames.annihilatorMeteor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class AnnihilatorMeteorRenderer extends EntityRenderer<Projectile> {

    private static final ResourceLocation BASE_TEXTURE = LHMiracleRoadSpellTool.resourceLocationId("textures/entity/spell/fire/meteor_summoning_array.png");

    public AnnihilatorMeteorRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(Projectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        poseStack.scale(6, 6, 6);
        PoseStack.Pose pose = poseStack.last();
//        Vec3 motion = entity.getDeltaMovement();
//        float xRot = -((float) (Mth.atan2(motion.horizontalDistance(), motion.y) * (double) (180F / (float) Math.PI)) - 180.0F);
//        float yRot = -((float) (Mth.atan2(motion.z, motion.x) * (double) (180F / (float) Math.PI)) + 180.0F);
//        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
//        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

        int maxRotation = 360; // 最大旋转角度
        float initialRotation = 0.0f; // 初始旋转角度
        float rotationIncrement = 15.0f; // 每次旋转增加的角度

        float yRot = rotate(maxRotation,initialRotation,rotationIncrement,entity.tickCount);
        poseStack.mulPose(Axis.YN.rotationDegrees(yRot));

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
        consumer.vertex(poseMatrix, -halfWidth, 0, -halfHeight).color(255, 255, 255, 255).uv(0f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, halfWidth, 0, -halfHeight).color(255, 255, 255, 255).uv(0f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, halfWidth, 0, halfHeight).color(255, 255, 255, 255).uv(1f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, -halfWidth, 0, halfHeight).color(255, 255, 255, 255).uv(1f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0f, 1f, 0f).endVertex();
    }

    private float rotate(int maxRotation,float initialRotation,float rotationIncrement,int tickCount){
        float rotateValue = initialRotation + rotationIncrement * tickCount;
        if (rotateValue > maxRotation){
            tickCount = (int) (tickCount - ((maxRotation - initialRotation) / rotationIncrement));
            return rotate(maxRotation,initialRotation,rotationIncrement,tickCount);
        }else return rotateValue;
    }

    @Override
    public ResourceLocation getTextureLocation(Projectile projectile) {
        return BASE_TEXTURE;
    }
}
