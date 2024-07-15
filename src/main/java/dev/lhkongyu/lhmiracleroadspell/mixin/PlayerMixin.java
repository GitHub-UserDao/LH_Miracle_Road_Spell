package dev.lhkongyu.lhmiracleroadspell.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public class PlayerMixin {

    @ModifyVariable(method = "causeFoodExhaustion", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float causeFoodExhaustionUpdate(float amount) {
        return amount;
    }
}
