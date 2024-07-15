package dev.lhkongyu.lhmiracleroadspell;

import com.mojang.logging.LogUtils;
import dev.lhkongyu.lhmiracleroadspell.registry.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(LHMiracleRoadSpell.MODID)
public class LHMiracleRoadSpell {

    public static final String MODID = "lhmiracleroadspell";
    private static final Logger LOGGER = LogUtils.getLogger();

    public LHMiracleRoadSpell(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemsRegistry.register(bus);
        TabsRegistry.register(bus);
        ParticleRegistry.register(bus);
        EntityRegistry.register(bus);
        EffectRegistry.register(bus);
    }
}
