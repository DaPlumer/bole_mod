package net.daplumer.more_gems.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;

@Environment(EnvType.CLIENT)
public class BoleRenderState extends LivingEntityRenderState {
    byte boulders;
    long boulderCooldowns;
    float tickProgress;
    //uses offset
    public float getBoulderAngle(int position){
        return (((float) position)*2F/((float) boulders) + (((float) 2*age)/BoleRenderConstants.boulderRotationCycleTime)) + this.bodyYaw/180;
    }
    public float getBoulderHeightAngle(int position){
        return (((float) position)*2F/((float) boulders) + (((float) 2*age)/BoleRenderConstants.boulderVerticalTime));
    }

    public byte getDelayAtBoulderIndex(int index){
        return (byte) ((boulderCooldowns >> (8 * index)) & 0xff);
    }
}
