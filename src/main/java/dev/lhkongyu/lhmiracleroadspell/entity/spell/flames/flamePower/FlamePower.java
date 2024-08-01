package dev.lhkongyu.lhmiracleroadspell.entity.spell.flames.flamePower;

import dev.lhkongyu.lhmiracleroadspell.registry.EffectRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.LHMiracleRoadSpellTool;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FlamePower {

    public static void  playerAddFlamePower(Level level, LivingEntity livingEntity){
        if (!level.isClientSide) {
            livingEntity.addEffect(new MobEffectInstance(EffectRegistry.FLAME_POWER.get(), LHMiracleRoadSpellTool.getDuration(300), 1));
        }
    }
}
