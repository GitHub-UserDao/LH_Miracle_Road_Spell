package dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.flameDevouring;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class FlameDevouring {

    public static void releaseFlameDevouring(Level level, LivingEntity livingEntity){
        if (!level.isClientSide) {
            Vec3 position = livingEntity.getPosition(0F);

            List<FlameDevouringProjectile> flameDevouringProjectiles = new ArrayList<>();
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(-1.5,0,-0.5)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(2.5,0,-1)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(-2.5,0,1.5)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(2.5,0,2.5)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(0,0,-2)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(0,0,-4)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(-0.5,0,2.5)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(2,0,-3.5)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(-1.5,0,-3.5)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(3.5,0,0.5)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(-3.5,0,-1)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(-4,0,-3)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(4,0,-2.5)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(4,0,3)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(1,0,4)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(-3,0,4)));
            flameDevouringProjectiles.add(createFlameDevouringProjectile(level,livingEntity,position.add(-4,0,2)));

            Collections.shuffle(flameDevouringProjectiles);

            Queue<FlameDevouringProjectile> queue = new LinkedList<>(flameDevouringProjectiles);

            addFlameDevouringProjectile(level,livingEntity,position.add(1,0,0.5),queue);
        }
    }

    private static void addFlameDevouringProjectile(Level level, LivingEntity livingEntity,Vec3 position,Queue<FlameDevouringProjectile> queue){
        FlameDevouringProjectile flameDevouring = new FlameDevouringProjectile(level,position,livingEntity,12,2f,6,queue);
        flameDevouring.setDuration(200);
        level.addFreshEntity(flameDevouring);

    }

    private static FlameDevouringProjectile createFlameDevouringProjectile(Level level, LivingEntity livingEntity,Vec3 position){
        FlameDevouringProjectile flameDevouring = new FlameDevouringProjectile(level,position,livingEntity,12,2f,6);
        flameDevouring.setDuration(200);
        return flameDevouring;
    }
}
