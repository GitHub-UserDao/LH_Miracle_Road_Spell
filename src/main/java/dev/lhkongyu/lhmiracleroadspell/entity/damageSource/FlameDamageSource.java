package dev.lhkongyu.lhmiracleroadspell.entity.damageSource;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class FlameDamageSource extends DamageSource{

    public FlameDamageSource(Holder<DamageType> damageType) {
        super(damageType);
    }


}
