package dev.lhkongyu.lhmiracleroadspell.generator;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class SpellDamageTypeTagGenerator extends TagsProvider<DamageType> {
    public SpellDamageTypeTagGenerator(PackOutput output, CompletableFuture<Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, LHMiracleRoadSpell.MODID, existingFileHelper);
    }

    private static TagKey<DamageType> create(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(LHMiracleRoadSpell.MODID, name));
    }
    public static final TagKey<DamageType> FLAME_MAGIC = create("flame_magic");

    protected void addTags(@NotNull Provider provider) {

        tag(FLAME_MAGIC).add(SpellDamageTypes.FLAME_MAGIC);
    }
}