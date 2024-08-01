package dev.lhkongyu.lhmiracleroadspell.event.server;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import dev.lhkongyu.lhmiracleroadspell.generator.SpellDamageTypeTagGenerator;
import dev.lhkongyu.lhmiracleroadspell.generator.SpellDamageTypes;
import dev.lhkongyu.lhmiracleroadspell.registry.EffectRegistry;
import dev.lhkongyu.lhmiracleroadspell.tool.damageRaise.FlameDamageRaise;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LHMiracleRoadSpell.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingEvent {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
//        Entity entity = event.getEntity();
//        entity.invulnerableTime = 0;
    }

    @SubscribeEvent
    public static void reduceDamage(LivingDamageEvent event) {
        FlameDamageRaise.damageRaise(event);
    }
}
