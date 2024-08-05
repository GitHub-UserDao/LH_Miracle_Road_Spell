package dev.lhkongyu.lhmiracleroadspell.spell.entity.renderer.flames.fireball;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.lhkongyu.lhmiracleroadspell.spell.entity.geckolib.flames.fireball.Fireball;
import dev.lhkongyu.lhmiracleroadspell.spell.entity.geckolib.flames.model.BigFireballModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FireballRenderer extends GeoEntityRenderer<Fireball> {

    private final float scale;

    public FireballRenderer(EntityRendererProvider.Context renderManager,float scale) {
        super(renderManager, new BigFireballModel());
        this.scale = scale;
    }

    @Override
    public void render(@NotNull Fireball entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        Vec3 motion = entity.getDeltaMovement();


        float angle = (float) Mth.atan2(motion.horizontalDistance(), motion.y);
        float xRot = -((float) (angle * (double) (180F / (float) Math.PI)) - 90.0F);
        float yRot = -((float) (Mth.atan2(motion.z, motion.x) * (double) (180F / (float) Math.PI)) + 270.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XN.rotationDegrees(xRot));


        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);
        poseStack.popPose();
    }
}
