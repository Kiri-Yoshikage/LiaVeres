package com.algorithmlx.liaveres.common.entity;

import com.algorithmlx.liaveres.common.setup.Constants;
import com.algorithmlx.liaveres.common.setup.Registration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

@SuppressWarnings("ALL")
public class AmdanorMob extends AbstractSkeleton {
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);

    public AmdanorMob(EntityType<? extends AbstractSkeleton> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.WITHER_SKELETON_STEP;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true ));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(ZombifiedPiglin.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, SnowGolem.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.WITHER_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_SKELETON_DEATH;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 1.3F;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.33F;
    }

//    @Override
//    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
//        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
//        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Registration.MATTER_SHARD.get()));
//    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource p_218949_, DifficultyInstance p_218950_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Registration.MATTER_SHARD.get()));
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason,
                                        @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        SpawnGroupData spawnGroupData = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.reassessWeaponGoal();
        return spawnGroupData;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (!super.doHurtTarget(entityIn)) {
            return false;
        } else {
            if (entityIn instanceof LivingEntity entity) {
                entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 200));
            }

            return true;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof AmdanorMob)
            amount *= -Float.MAX_VALUE;
        return !this.isInvulnerableTo(source) && super.hurt(source, amount);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance mobEffectInstance) {
        return mobEffectInstance.getEffect() != MobEffects.WITHER && super.canBeAffected(mobEffectInstance);
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 20000000000.0d)
                .add(Attributes.MAX_HEALTH, Double.MAX_VALUE)
                .add(Attributes.FOLLOW_RANGE, 400.0d)
                .add(Attributes.MOVEMENT_SPEED, 1.0d)
                .add(Attributes.ATTACK_KNOCKBACK, 3.5d);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource pDamageSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pDamageSource, pLooting, pRecentlyHit);
        ItemEntity itemEntity = this.spawnAtLocation(Registration.MATTER_SHARD.get(), 4);
        if (itemEntity != null) {
            itemEntity.setExtendedLifetime();
        }
    }

    @Override
    protected void dropAllDeathLoot(DamageSource pDamageSource) {
        super.dropAllDeathLoot(pDamageSource);
    }

    @Override
    public void tick() {
        super.tick();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public BossEvent.BossBarColor getBarColor() {
        return BossEvent.BossBarColor.BLUE;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        if (player.gameMode.isCreative() || player.gameMode.isSurvival()) {
            player.gameMode.changeGameModeForPlayer(GameType.ADVENTURE);
        }

        if (player.getAbilities().mayfly) {
            player.getAbilities().mayfly = false;
            player.sendSystemMessage(Component.translatable("msg." + Constants.ModId + ".amdanor.blocking"));
        }
        this.bossInfo.setColor(getBarColor());
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        if (player.gameMode.getGameModeForPlayer() == GameType.ADVENTURE) {
            player.gameMode.changeGameModeForPlayer(GameType.SURVIVAL);
        }
        this.bossInfo.removePlayer(player);
    }
}
