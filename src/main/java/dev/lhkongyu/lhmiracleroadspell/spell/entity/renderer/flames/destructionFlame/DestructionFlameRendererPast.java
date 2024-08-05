package dev.lhkongyu.lhmiracleroadspell.spell.entity.renderer.flames.destructionFlame;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;

public class DestructionFlameRendererPast extends EntityRenderer<Projectile> {

    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(LHMiracleRoadSpell.MODID, "sin_blaze_model"), "main");
    private static final ResourceLocation BASE_TEXTURE = LHMiracleRoadSpellTool.resourceLocationId("textures/entity/spell/fire/big_fireball.png");


    protected final ModelPart body;
    protected final float scale;

    private float additional;

    private static final int SPEED_BOOST_INTERVAL = 31;

    public DestructionFlameRendererPast(Context context, float scale) {
        super(context);
        ModelPart modelpart = context.bakeLayer(MODEL_LAYER_LOCATION);
        this.body = modelpart.getChild("body");
        this.scale = scale;
        this.additional = 0;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void render(Projectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        if (entity.tickCount % SPEED_BOOST_INTERVAL == 0){
            additional = (0.225f * ((float) entity.tickCount / 20));
        }

        float scale = (float) ((this.scale + additional) *  LHMiracleRoadSpellTool.randomSpecifyValue(0.75,1,1.25));
        poseStack.pushPose();
        poseStack.translate(0, entity.getBoundingBox().getYsize() * .5f, 0);
        poseStack.scale(scale, scale, scale);

        int maxRotation = 360; // 最大旋转角度
        float initialRotation = 0.0f; // 初始旋转角度
        float rotationIncrement = 15.0f; // 每次旋转增加的角度

        float yRot = rotate(maxRotation,initialRotation,rotationIncrement,entity.tickCount);
        float xRot = rotate(maxRotation,initialRotation,rotationIncrement,entity.tickCount);

        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
        this.body.render(poseStack, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();

        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    private float rotate(int maxRotation,float initialRotation,float rotationIncrement,int tickCount){
        float rotateValue = initialRotation + rotationIncrement * tickCount;
        if (rotateValue > maxRotation){
            tickCount = (int) (tickCount - ((maxRotation - initialRotation) / rotationIncrement));
            return rotate(maxRotation,initialRotation,rotationIncrement,tickCount);
        }else return rotateValue;
    }

    @Override
    public ResourceLocation getTextureLocation(Projectile entity) {
        return BASE_TEXTURE;
    }
}