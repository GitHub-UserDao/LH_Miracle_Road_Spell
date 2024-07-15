package dev.lhkongyu.lhmiracleroadspell.registry;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
public class DamageTypesRegistry {

    public static final ResourceKey<DamageType> FLAME_MAGIC =  register("flame_damage");

    public static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(LHMiracleRoadSpell.MODID, name));
    }
}
