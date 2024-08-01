package dev.lhkongyu.lhmiracleroadspell;

import com.mojang.logging.LogUtils;
import dev.lhkongyu.lhmiracleroadspell.generator.RegistryDataGenerator;
import dev.lhkongyu.lhmiracleroadspell.generator.SpellDamageTypes;
import dev.lhkongyu.lhmiracleroadspell.registry.*;
import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mod(LHMiracleRoadSpell.MODID)
@Mod.EventBusSubscriber(modid = LHMiracleRoadSpell.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LHMiracleRoadSpell {

    public static final String MODID = "lhmiracleroadspell";
    private static final Logger LOGGER = LogUtils.getLogger();

    public LHMiracleRoadSpell(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        AttributesRegistry.register();
        bus.addListener(AttributesRegistry::registerPlayerAttribute);
        ItemsRegistry.register(bus);
        TabsRegistry.register(bus);
        ParticleRegistry.register(bus);
        EntityRegistry.register(bus);
        EffectRegistry.register(bus);
    }

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, SpellDamageTypes::bootstrap);

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        RegistryDataGenerator.addProviders(event.includeServer(), generator, output, provider, helper);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        
    }
}
