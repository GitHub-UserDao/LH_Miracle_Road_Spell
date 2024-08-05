package dev.lhkongyu.lhmiracleroadspell.spell.projectile;

import dev.lhkongyu.lhmiracleroadspell.spell.projectile.flames.ReleaseFlamesSpellTool;
import dev.lhkongyu.lhmiracleroadspell.registry.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class CommonAoeProjectile extends AoeMagicProjectile {

    private int spacing;

    private List<Vec3> meteorPositions = new ArrayList<>();
    private Queue<Vec3> currentRoundPositions = new LinkedList<>();

    private int maxRounds;

    private String spellEntityType;

    private double playerTowardX;

    private double playerTowardZ;

    public int getSpacing() {
        return spacing;
    }

    public String getSpellEntityType() {
        return spellEntityType;
    }

    public double getPlayerTowardX() {
        return playerTowardX;
    }

    public double getPlayerTowardZ() {
        return playerTowardZ;
    }

    public List<Vec3> getMeteorPositions() {
        return meteorPositions;
    }

    public Queue<Vec3> getCurrentRoundPositions() {
        return currentRoundPositions;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public void setSpellEntityType(String spellEntityType) {
        this.spellEntityType = spellEntityType;
    }

    public void setPlayerTowardX(double playerTowardX) {
        this.playerTowardX = playerTowardX;
    }

    public void setPlayerTowardZ(double playerTowardZ) {
        this.playerTowardZ = playerTowardZ;
    }

    public void setMeteorPositions(List<Vec3> meteorPositions) {
        this.meteorPositions = meteorPositions;
    }

    public void setCurrentRoundPositions(Queue<Vec3> currentRoundPositions) {
        this.currentRoundPositions = currentRoundPositions;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public CommonAoeProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public CommonAoeProjectile(Level pLevel) {
        super(EntityRegistry.COMMON_AOE.get(), pLevel);
    }

    @Override
    protected void continuedSound() {
    }

    @Override
    protected void damageMethod(LivingEntity target) {
    }

    @Override
    protected void rangeParticles() {
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > duration) {
            discard();
            return;
        }

        if (!level().isClientSide) {

            if (spellEntityType != null && spellEntityType.equals(EntityRegistry.ANNIHILATOR_METEOR_PROJECTILE.getId().toString()) && getOwner() instanceof LivingEntity livingEntity){
                if (currentRoundPositions.isEmpty()) {
                    Vec3 currentPosition = this.position();
                    double startX = currentPosition.x - getRadius();
                    double startZ = currentPosition.z - getRadius();

                    // 计算所有流星的位置
                    meteorPositions.clear();
                    for (double x = startX; x <= startX + getRadius() * 2; x += spacing) {
                        for (double z = startZ; z <= startZ + getRadius() * 2; z += spacing) {
                            Vec3 spawnPosition = new Vec3(x, currentPosition.y, z);
                            meteorPositions.add(spawnPosition);
                        }
                    }

                    // 每轮前打乱位置列表
                    Collections.shuffle(meteorPositions);
                    currentRoundPositions.addAll(meteorPositions);
                }

                // 每1个tick生成一个流星
                if (!currentRoundPositions.isEmpty()) {
                    int count = getRadius() / getSpacing() >= 10 ? 2 : 1;
                    for (int i = 0;i < count; i++) {
                        Vec3 spawnPosition = currentRoundPositions.poll();
                        if (spawnPosition != null) {
                            ReleaseFlamesSpellTool.createMeteor(level(), livingEntity, spawnPosition, playerTowardX, playerTowardZ);
                        }
                    }

                }
            }

        }

    }

    /**
     * 添加需要持久化的数据，避免退出在进入刷新存在时间等问题
     * @param pCompound
     */
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("TickCount", this.tickCount);
        pCompound.putInt("Duration", this.duration);
        pCompound.putInt("AttackInterval", this.attackInterval);
        pCompound.putFloat("Radius", this.getRadius());
        pCompound.putFloat("Damage", this.getDamage());
        pCompound.putFloat("High", this.getHigh());
        pCompound.putString("SpellEntityType",this.getSpellEntityType());
        pCompound.putDouble("PlayerTowardX",this.getPlayerTowardX());
        pCompound.putDouble("PlayerTowardZ",this.getPlayerTowardZ());

        pCompound.putInt("MaxRounds",this.getMaxRounds());
        pCompound.putInt("Spacing",this.getSpacing());

        super.addAdditionalSaveData(pCompound);
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.tickCount = pCompound.getInt("TickCount");
        this.duration = pCompound.getInt("Duration");
        this.attackInterval = pCompound.getInt("AttackInterval");
        this.setRadius(pCompound.getFloat("Radius"));
        this.setDamage(pCompound.getFloat("Damage"));
        this.setHigh(pCompound.getFloat("High"));
        this.setSpellEntityType(pCompound.getString("SpellEntityType"));
        this.setPlayerTowardX(pCompound.getDouble("PlayerTowardX"));
        this.setPlayerTowardZ(pCompound.getDouble("PlayerTowardZ"));

        this.setMaxRounds(pCompound.getInt("MaxRounds"));
        this.setSpacing(pCompound.getInt("Spacing"));

        super.readAdditionalSaveData(pCompound);
    }

}
