package dev.lhkongyu.lhmiracleroadspell.registry;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import dev.lhkongyu.lhmiracleroadspell.items.SpellItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemsRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LHMiracleRoadSpell.MODID);

    public static final RegistryObject<Item> FIREBALL = ITEMS.register("fireball", () -> new SpellItem(new Item.Properties().rarity(Rarity.COMMON)));

    public static final RegistryObject<Item> BIG_FIREBALL = ITEMS.register("big_fireball", () -> new SpellItem(new Item.Properties().rarity(Rarity.COMMON)));

    public static final RegistryObject<Item> BLAZE_BOMB = ITEMS.register("blaze_bomb", () -> new SpellItem(new Item.Properties().rarity(Rarity.COMMON)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
