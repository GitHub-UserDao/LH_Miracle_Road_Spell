package dev.lhkongyu.lhmiracleroadspell.registry;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import dev.lhkongyu.lhmiracleroadspell.entity.effect.BurnEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class EffectRegistry {

    public static final DeferredRegister<MobEffect> EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, LHMiracleRoadSpell.MODID);

    public static final RegistryObject<MobEffect> BURN = EFFECT_DEFERRED_REGISTER.register("burn", () -> new BurnEffect(MobEffectCategory.BENEFICIAL, 0xffef95) );

    public static void register(IEventBus eventBus) {
        EFFECT_DEFERRED_REGISTER.register(eventBus);
    }
}
