package dev.lhkongyu.lhmiracleroadspell.generator;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
public class SpellDamageTypes {

    public static final ResourceKey<DamageType> FLAME_MAGIC =  register("flame_magic");

    public static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(LHMiracleRoadSpell.MODID, name));
    }

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(FLAME_MAGIC, new DamageType(FLAME_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f));
    }
}
