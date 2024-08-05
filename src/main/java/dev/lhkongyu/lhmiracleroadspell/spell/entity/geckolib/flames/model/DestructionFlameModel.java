package dev.lhkongyu.lhmiracleroadspell.spell.entity.geckolib.flames.model;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import dev.lhkongyu.lhmiracleroadspell.spell.entity.geckolib.flames.destructionFlame.DestructionFlame;
import dev.lhkongyu.lhmiracleroadspell.spell.entity.geckolib.flames.fireball.Fireball;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DestructionFlameModel extends GeoModel<DestructionFlame> {
    public static final ResourceLocation modelResource = new ResourceLocation(LHMiracleRoadSpell.MODID, "geo/destruction_flame.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(LHMiracleRoadSpell.MODID, "textures/entity/spell/fire/destruction_flame.png");

    @Override
    public ResourceLocation getModelResource(DestructionFlame bigFireball) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(DestructionFlame bigFireball) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(DestructionFlame bigFireball) {
        return null;
    }

}
