// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package dev.lhkongyu.lhmiracleroadspell.spell.entity.renderer.flames.evilGodFlame;


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
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.NotNull;

public class EvilGodFlameRenderer extends EntityRenderer<Projectile> {
	public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(LHMiracleRoadSpell.MODID, "evil_god_flame"), "main");

	private static final ResourceLocation BASE_TEXTURE = LHMiracleRoadSpellTool.resourceLocationId("textures/entity/spell/fire/evil_god_flame.png");

	private static final ResourceLocation COMPLETE_BASE_TEXTURES = LHMiracleRoadSpellTool.resourceLocationId("textures/entity/spell/fire/evil_god_flame_complete.png");

	private static final int CHANGE_TIME = 60;

	protected final ModelPart body;

	protected final ModelPart outline;

	public EvilGodFlameRenderer(EntityRendererProvider.Context context) {
		super(context);
		ModelPart modelpart = context.bakeLayer(MODEL_LAYER_LOCATION);
		this.body = modelpart.getChild("bone");
		this.outline = modelpart.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 22.0F, 6.0F, 0.0F, 0.0F, -3.1416F));
		bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-7.0F, -14.0F, -8.0F, 14.0F, 14.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, -0.2618F, 0.0F, -0.0436F));
		bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 46).addBox(-8.0F, -14.0F, -8.0F, 14.0F, 14.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 15.0F, -7.0F, 0.4363F, 0.0F, -0.0436F));
		bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(52, 20).addBox(-6.0F, -14.0F, -7.0F, 14.0F, 14.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 14.0F, -5.0F, 0.0F, 0.0F, 0.3927F));
		bone.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 60).addBox(-6.0F, -14.0F, -7.0F, 14.0F, 14.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 15.0F, -5.0F, 0.0F, 0.0F, -0.3054F));
		partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void render(Projectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
		VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));

		if (entity.tickCount >= CHANGE_TIME){
			consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(COMPLETE_BASE_TEXTURES));
		}

		poseStack.pushPose();
		int maxRotation = 360; // 最大旋转角度
		float initialRotation = 0.0f; // 初始旋转角度
		float rotationIncrement = 15.0f; // 每次旋转增加的角度

		float yRot = rotate(maxRotation,initialRotation,rotationIncrement,entity.tickCount);

		poseStack.mulPose(Axis.YN.rotationDegrees(yRot));
		body.render(poseStack,consumer,LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
		outline.render(poseStack,consumer,LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
		poseStack.popPose();
	}

	private float rotate(int maxRotation,float initialRotation,float rotationIncrement,int tickCount){
		float rotateValue = initialRotation + rotationIncrement * tickCount;
		if (rotateValue > maxRotation){
			tickCount = (int) (tickCount - ((maxRotation - initialRotation) / rotationIncrement));
			return rotate(maxRotation,initialRotation,rotationIncrement,tickCount);
		}else return rotateValue;
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull Projectile entity) {
		return BASE_TEXTURE;
	}
}