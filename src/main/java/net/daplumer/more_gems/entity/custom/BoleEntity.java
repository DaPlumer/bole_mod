package net.daplumer.more_gems.entity.custom;

import net.daplumer.more_gems.MoreGems;
import net.daplumer.more_gems.entity.client.BoleEntityModel;
import net.daplumer.more_gems.entity.client.BoleRenderConstants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class BoleEntity extends HostileEntity implements RangedAttackMob {
    public static final TrackedData<Byte> BOULDERS = DataTracker.registerData(BoleEntity.class, TrackedDataHandlerRegistry.BYTE);
    public static final TrackedData<Long> BOULDER_DELAYS = DataTracker.registerData(BoleEntity.class, TrackedDataHandlerRegistry.LONG);
    public int cooldown = 0;
    public static final byte MAX_BOULDER_DELAY = 127;

    public static long indexShifted(int index, byte byteValue){
        return ((long)byteValue) << (8 * index);
    }
    public byte getDelayAtBoulderIndex(int index){
        return (byte) ((dataTracker.get(BOULDER_DELAYS) >> (8 * index)) & 0xff);
    }
    public void setDelayAtBoulderIndex(int index, byte delay){
        long data = dataTracker.get(BOULDER_DELAYS);
        data ^=  indexShifted(index, (byte) (delay ^ getDelayAtBoulderIndex(index)));
        dataTracker.set(BOULDER_DELAYS,data);
    }
    public boolean hasBoulderAt(int index){
        return getDelayAtBoulderIndex(index) == 0 && index < dataTracker.get(BOULDERS);
    }
    public void removeBoulder(int index){
        setDelayAtBoulderIndex(index, MAX_BOULDER_DELAY);
    }

    public void decrementBoulderIndices(){
        for(int i = 0; i < dataTracker.get(BOULDERS); i++){
            if(!hasBoulderAt(i)){
                setDelayAtBoulderIndex(i, (byte) (getDelayAtBoulderIndex(i) - 1));
            }
        }
    }
    public float getAngleOfBoulder(int index){
        return (((float) index)/((float) dataTracker.get(BOULDERS)) * 360F + 360*age / BoleRenderConstants.boulderRotationCycleTime);
    }

    public int getIndexOfBestBoulderForAngle(float targetAngle){
        int indexOfBestBoulder = -1;
        float angleOfBestBoulder = Float.MAX_VALUE;
        float currentAngle;
        float currentAngleDistance;
        targetAngle = targetAngle % 360;

        for (int i = 0; i < dataTracker.get(BOULDERS); i++){
            if(!hasBoulderAt(i)) continue;
            currentAngle = getAngleOfBoulder(i);
            currentAngleDistance = Math.abs(((Math.abs(currentAngle - targetAngle) + 180) % 360) - 180);
            if(currentAngleDistance < angleOfBestBoulder){
                indexOfBestBoulder = i;
                angleOfBestBoulder = currentAngleDistance;
            }
        }
        return indexOfBestBoulder;
    }

    public BoleEntity(EntityType<? extends Entity> entityType, World world) {
        super(MoreGems.BOLE, world);
    }
    public static DefaultAttributeContainer.Builder createAttributes(){
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.MAX_HEALTH, 30)
                .add(EntityAttributes.ATTACK_DAMAGE, 0)
                .add(EntityAttributes.MOVEMENT_SPEED,.1);
    }
    @Override
    protected void initGoals() {
        super.initGoals();
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.goalSelector.add(0, new BoleAttackGoal());
        this.goalSelector.add(2,new LookAtEntityGoal(this, PlayerEntity.class,10F));
    }
    public float getAngleOfTarget(Entity target){
        Vec3d vec3d = target.getPos().subtract(getPos());
        return (float)(MathHelper.atan2(vec3d.x, vec3d.z) * 180.0F / (float)Math.PI);
    }
    public float getBoulderHeightAngle(int position){
        return (((float) position)*360F/((float) dataTracker.get(BOULDERS)) + (((float) 360*age)/BoleRenderConstants.boulderVerticalTime));
    }
    public Vec3d getBoulderPosition(int index){
        float angle = getAngleOfBoulder(index);
        return new Vec3d(
            getX() + .5 * Math.cos(angle),
            getY() + (16 + Math.sin(getBoulderHeightAngle(index)))/16,
            getZ() + .5 * Math.sin(angle)
        );
    }
    @Override
    public void shootAt(LivingEntity target, float pullProgress) {shootAt(target);}
    public void shootAt (Entity target){
        int bestBoulderIndex = getIndexOfBestBoulderForAngle(getAngleOfTarget(target));
        if(bestBoulderIndex != -1 && hasBoulderAt(bestBoulderIndex)) {
            Vec3d vec3d = getTargetVelocity(target, Boulder.GRAVITY, 2, bestBoulderIndex);
            if (vec3d != null) {
                removeBoulder(bestBoulderIndex);
                Boulder boulder = new Boulder(getWorld(), this);
                boulder.setPosition(getBoulderPosition(bestBoulderIndex));
                Boulder.spawnWithVelocity(boulder, (ServerWorld) getWorld(), MoreGems.BOULDER_ITEM.getDefaultStack(), vec3d.getX(), vec3d.getY(), vec3d.getZ(), 1F, 0);
                cooldown = 10;
            }
        }
    }
    boolean canShootAt(@Nullable Entity target){
        if(target == null || cooldown > 0) return false;
        int bestBoulderIndex = getIndexOfBestBoulderForAngle(getAngleOfTarget(target));
        if(bestBoulderIndex == -1) return false;
        return hasBoulderAt(bestBoulderIndex);

    }

    public class BoleAttackGoal extends Goal{
        private final MobEntity mob;
        private LivingEntity target;

        public BoleAttackGoal() {
            this.mob = BoleEntity.this;
            this.setControls(EnumSet.of(Goal.Control.LOOK));
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) {
                return false;
            } else {
                this.target = livingEntity;
                return true;
            }
        }

        @Override
        public boolean shouldContinue() {
            if (!this.target.isAlive()) {
                return false;
            } else {
                return !(this.mob.squaredDistanceTo(this.target) > 225.0) && (!this.mob.getNavigation().isIdle() || this.canStart());
            }
        }

        @Override
        public void stop() {
            this.target = null;
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }


        @Override
        public void tick() {
            getLookControl().lookAt(this.target, 30.0F, 30.0F);
            cooldown = Math.max(cooldown - 1, 0);
            if(canShootAt(this.target)){
                shootAt(this.target);
            }
        }
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        dataTracker.set(BOULDERS, view.getByte("boulders",(byte) random.nextBetween(3, BoleEntityModel.maxBoulders)));
        dataTracker.set(BOULDER_DELAYS, view.getLong("boulder_cooldowns",0L));
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putByte("boulders",dataTracker.get(BOULDERS));
        view.putLong("boulder_cooldowns",dataTracker.get(BOULDER_DELAYS));
    }
    public @Nullable Vec3d getTargetVelocity(Entity targetEntity, double gravity, double targetSpeed, int boulderIndex){
        Vec3d offset = (targetEntity.getEyePos().subtract(getBoulderPosition(boulderIndex)));
        double horizLenSq = offset.horizontalLengthSquared();
        double tsq = targetSpeed * targetSpeed;//target speed squared
        double discriminant = tsq*tsq - gravity*(gravity*horizLenSq + 2*offset.y*tsq);
        if(discriminant < 0) return null;
        Vec3d newPos = new Vec3d(
                offset.x * gravity,
                tsq - Math.sqrt(discriminant),
                offset.z * gravity
        );
        return (newPos.normalize().multiply(targetSpeed));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(BOULDERS, (byte) getRandom().nextBetween(3,BoleEntityModel.maxBoulders));
        builder.add(BOULDER_DELAYS, 0L);
    }

    @Override
    public void tick() {
        super.tick();
        if(getWorld().isClient()){
            spawnSprintingParticles();
        }
        if(getTarget() != null && distanceTo(getTarget()) > 10)
            this.addVelocity(getTarget().getPos().subtract(getPos()).normalize().multiply(.05));
        decrementBoulderIndices();
    }
}
