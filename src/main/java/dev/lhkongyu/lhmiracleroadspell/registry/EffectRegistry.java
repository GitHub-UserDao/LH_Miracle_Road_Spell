package dev.lhkongyu.lhmiracleroadspell.registry;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import dev.lhkongyu.lhmiracleroadspell.entity.effect.BurningArmorEffect;
import dev.lhkongyu.lhmiracleroadspell.entity.effect.FlamePowerEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class EffectRegistry {

    public static final DeferredRegister<MobEffect> EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, LHMiracleRoadSpell.MODID);

    public static final RegistryObject<MobEffect> BURNING_ARMOR = EFFECT_DEFERRED_REGISTER.register("burning_armor", () -> new BurningArmorEffect(MobEffectCategory.BENEFICIAL, 0xffef95,-0.1).addAttributeModifier(Attributes.ARMOR,"43f7da30-da20-90d4-4a85-4c2934dd6e56",-.25,AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<MobEffect> FLAME_POWER = EFFECT_DEFERRED_REGISTER.register("flame_power", () -> new FlamePowerEffect(MobEffectCategory.BENEFICIAL, 0xc3242f,0.05)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE,"3f779efa-c307-e83b-f9e6-3ec12fbd0ce7",0.15,AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(AttributesRegistry.FLAME_DAMAGE,"a574b075-f9b2-2242-809e-8ba67cffacb1",0.15,AttributeModifier.Operation.MULTIPLY_TOTAL));


    public static void register(IEventBus eventBus) {
        EFFECT_DEFERRED_REGISTER.register(eventBus);
    }
}
