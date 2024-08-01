package dev.lhkongyu.lhmiracleroadspell.tool.damageRaise;

import dev.lhkongyu.lhmiracleroadspell.generator.SpellDamageTypes;
import dev.lhkongyu.lhmiracleroadspell.registry.AttributesRegistry;
import dev.lhkongyu.lhmiracleroadspell.registry.EffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class FlameDamageRaise {

    public static void damageRaise(LivingDamageEvent event){
        if (event.getSource().is(SpellDamageTypes.FLAME_MAGIC)) {
            Entity directEntity = event.getSource().getEntity();
            if (directEntity instanceof Player player) {
                //火焰伤害属性提升伤害
                AttributeInstance flameDamageAttribute = player.getAttribute(AttributesRegistry.FLAME_DAMAGE);
                if (flameDamageAttribute != null){
                    float damage = (float) (event.getAmount() * flameDamageAttribute.getValue());
                    event.setAmount(damage);
                }
            }

            //药水影响的伤害提升
            effectDamageRaise(event, event.getEntity());
        }
    }

    /**
     * 药水影响的伤害提升
     * @param event
     * @param hurtEntity
     */
    public static void effectDamageRaise(LivingDamageEvent event, LivingEntity hurtEntity){
        MobEffectInstance mobEffectInstance = null;
        mobEffectInstance = hurtEntity.getEffect(EffectRegistry.BURNING_ARMOR.get());
        if (mobEffectInstance == null) return;
        int amplifier = mobEffectInstance.getAmplifier();

        float amount = (float) (event.getAmount() * (1.1 + (amplifier * 0.05)));
        event.setAmount(amount);
    }
}
