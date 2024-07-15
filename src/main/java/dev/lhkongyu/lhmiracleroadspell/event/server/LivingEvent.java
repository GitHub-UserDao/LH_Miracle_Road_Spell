package dev.lhkongyu.lhmiracleroadspell.event.server;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LHMiracleRoadSpell.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingEvent {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity entity = event.getEntity();
        entity.invulnerableTime = 0;
    }

}
