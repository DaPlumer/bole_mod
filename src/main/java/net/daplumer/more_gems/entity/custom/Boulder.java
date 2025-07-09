package net.daplumer.more_gems.entity.custom;

import net.daplumer.more_gems.MoreGems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Boulder extends ThrownItemEntity {
    public static final double GRAVITY = .07;
    @Override
    protected Box calculateDefaultBoundingBox(Vec3d pos) {
        return super.calculateDefaultBoundingBox(pos);
    }

    public Boulder(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }
    public Boulder(World world, LivingEntity owner){
        super(MoreGems.BOULDER,owner,world, MoreGems.BOULDER_ITEM.getDefaultStack());
    }
    public Boulder(World world, Vec3d xyz){
        super(MoreGems.BOULDER,xyz.x,xyz.y,xyz.z,world, MoreGems.BOULDER_ITEM.getDefaultStack());
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        if(state.getBlock() instanceof FluidBlock) return;
        super.onBlockCollision(state);
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            dropStack(serverWorld, new ItemStack(MoreGems.RUBBLE, getRandom().nextBetween(1,2)));
            this.getWorld().sendEntityStatus(this, (byte)3);
            this.kill((ServerWorld) getWorld());
        }

    }

    @Environment(EnvType.CLIENT)
    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getStack();
        return (itemStack.isEmpty() ? MoreGems.BOULDER_PARTICLE : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
    }

    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) {
        if (status == 3) {
            ParticleEffect particleEffect = this.getParticleParameters();

            for(int i = 0; i < 8; ++i) {
                this.getWorld().addParticleClient(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    protected Item getDefaultItem() {
        return MoreGems.BOULDER_ITEM;
    }
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) { // called on entity hit.
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity(); // sets a new Entity instance as the EntityHitResult (victim)
        if (entity instanceof LivingEntity livingEntity) {
            DamageSource damageSource = getDamageSources().create(MoreGems.BOULDER_DAMAGE_TYPE, this, (getOwner() != null ? getOwner() : this));
            if(getWorld() instanceof ServerWorld serverWorld){
                livingEntity.addVelocity(getVelocity().multiply(.2));
                if(livingEntity.damage(serverWorld, damageSource,5)){
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 5, 2),getEffectCause()); // applies a status effect
                    this.discard();
                }
            }
        }
    }
    @Override
    protected double getGravity() {
        return GRAVITY;
    }
    public static class SimpleParticleTypeWrapper extends SimpleParticleType{
        public SimpleParticleTypeWrapper(boolean alwaysShow) {
            super(alwaysShow);
        }
    }
}
