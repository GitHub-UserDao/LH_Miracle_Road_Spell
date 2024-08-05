package dev.lhkongyu.lhmiracleroadspell.spell.entity.renderer.flames.destructionFlame;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.lhkongyu.lhmiracleroadspell.spell.entity.geckolib.flames.destructionFlame.DestructionFlame;
import dev.lhkongyu.lhmiracleroadspell.spell.entity.geckolib.flames.model.DestructionFlameModel;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.projectile.Projectile;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DestructionFlameRenderer extends GeoEntityRenderer<DestructionFlame> {

    protected final float scale;

    private float additional;

    private static final int SPEED_BOOST_INTERVAL = 31;

    public DestructionFlameRenderer(EntityRendererProvider.Context renderManager,float scale) {
        super(renderManager, new DestructionFlameModel());
        this.scale = scale;
        this.additional = 0;
    }

    @Override
    public void render(DestructionFlame entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        if (entity.tickCount % SPEED_BOOST_INTERVAL == 0){
            additional = (0.225f * ((float) entity.tickCount / 20));
        }

        float scale = this.scale + additional;
        poseStack.pushPose();
        poseStack.translate(0, entity.getBoundingBox().getYsize() * .5f, 0);
        poseStack.scale(scale, scale, scale);

        int maxRotation = 360; // 最大旋转角度
        float initialRotation = 0.0f; // 初始旋转角度
        float rotationIncrement = 45.0f; // 每次旋转增加的角度

        float yRot = rotate(maxRotation,initialRotation,rotationIncrement,entity.tickCount);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));

        super.render(entity, yaw, partialTicks, poseStack, bufferSource, LightTexture.FULL_BRIGHT);
        poseStack.popPose();
    }

    private float rotate(int maxRotation,float initialRotation,float rotationIncrement,int tickCount){
        float rotateValue = initialRotation + rotationIncrement * tickCount;
        if (rotateValue > maxRotation){
            tickCount = (int) (tickCount - ((maxRotation - initialRotation) / rotationIncrement));
            return rotate(maxRotation,initialRotation,rotationIncrement,tickCount);
        }else return rotateValue;
    }
}
