package net.daplumer.more_gems.entity.client;

import com.google.common.primitives.UnsignedBytes;
import net.daplumer.more_gems.entity.custom.BoleEntity;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;

import java.util.ArrayList;
import java.util.List;

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
