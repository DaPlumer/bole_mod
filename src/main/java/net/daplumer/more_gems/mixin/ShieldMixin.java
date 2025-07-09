package net.daplumer.more_gems.mixin;

import net.daplumer.more_gems.MoreGems;
import net.daplumer.more_gems.entity.custom.Boulder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.waypoint.ServerWaypoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class ShieldMixin extends Entity implements Attackable, ServerWaypoint {
    @Shadow public abstract ItemStack getActiveItem();

    @Shadow public abstract Hand getActiveHand();

    public ShieldMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;getKnockback(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/damage/DamageSource;)Lit/unimi/dsi/fastutil/doubles/DoubleDoubleImmutablePair;"))
    void aVoid(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(source.getSource() instanceof Boulder boulder){
            BlocksAttacksComponent blocksAttacksComponent = getActiveItem().get(DataComponentTypes.BLOCKS_ATTACKS);
            if (blocksAttacksComponent != null) {
                getActiveItem().damage(10,(LivingEntity) (Object) this,getActiveHand());
                addVelocity(boulder.getVelocity().getHorizontal().multiply(5));
                boulder.dropStack(world, new ItemStack(MoreGems.RUBBLE, boulder.getRandom().nextBetween(1,2)));
            }

        }

    }
}
