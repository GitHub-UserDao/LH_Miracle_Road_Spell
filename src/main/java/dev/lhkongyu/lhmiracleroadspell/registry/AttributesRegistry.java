package dev.lhkongyu.lhmiracleroadspell.registry;

import dev.lhkongyu.lhmiracleroadspell.LHMiracleRoadSpell;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class AttributesRegistry {

    private static Attribute create(String id, double base, double min, double max) {
        return new RangedAttribute(
                id,
                base,
                min,
                max
        );
    }

    public static final String FLAME_DAMAGE_ID = "attribute.name."+ LHMiracleRoadSpell.MODID+".flame_damage";
    public static final Attribute FLAME_DAMAGE = create(
            FLAME_DAMAGE_ID,
            1,
            0.0,
            Double.MAX_VALUE
    ).setSyncable(true);

    public static void register() {
        ForgeRegistries.ATTRIBUTES.register("flame_damage", FLAME_DAMAGE);
    }

    public static void registerPlayerAttribute(EntityAttributeModificationEvent event){
        event.add(EntityType.PLAYER, FLAME_DAMAGE);
    }
}
