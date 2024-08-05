package dev.lhkongyu.lhmiracleroadspell.spell.entity.geckolib.flames.fireball;

import dev.lhkongyu.lhmiracleroadspell.spell.projectile.CommonMagicProjectile;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class Fireball extends CommonMagicProjectile implements GeoEntity {

    public Fireball(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private final AnimationController animationDecoRotate = new AnimationController(this, "animation_deco_rotate", 0, this::decoRotatePredicate);

    private final RawAnimation DECO_ROTATE = RawAnimation.begin().thenPlay("deco_rotate");

    private PlayState decoRotatePredicate(software.bernie.geckolib.core.animation.AnimationState event) {
        var controller = event.getController();
        controller.setAnimation(DECO_ROTATE);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(animationDecoRotate);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected float shifting(float endValue){
        Vec3 vec3 = getDeltaMovement();
        float angle = (float) Mth.atan2(vec3.horizontalDistance(), vec3.y);
        float shifting = 0.0f;
        if (angle < 0.1f) {
            shifting = Mth.lerp(1.0f - (angle / 0.1f),0, endValue * -1);
        }
        else if (angle > 3.05f) {
            shifting =  Mth.lerp((angle - 3.05f) / (float) (Math.PI - 3.05f),0, endValue);
        }
        return shifting;
    }
}
