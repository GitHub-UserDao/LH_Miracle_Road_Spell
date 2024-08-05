package dev.lhkongyu.lhmiracleroadspell.spell.entity.geckolib.flames.model;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import dev.lhkongyu.lhmiracleroadspell.spell.entity.geckolib.flames.fireball.Fireball;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BigFireballModel extends GeoModel<Fireball> {

    public static final ResourceLocation modelResource = new ResourceLocation(LHMiracleRoadSpell.MODID, "geo/fireball.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(LHMiracleRoadSpell.MODID, "textures/entity/spell/fire/fireball.png");
    public static final ResourceLocation animationResource = new ResourceLocation(LHMiracleRoadSpell.MODID, "animations/fireball.animation.json");

    @Override
    public ResourceLocation getModelResource(Fireball bigFireball) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(Fireball bigFireball) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(Fireball bigFireball) {
        return animationResource;
    }
}
