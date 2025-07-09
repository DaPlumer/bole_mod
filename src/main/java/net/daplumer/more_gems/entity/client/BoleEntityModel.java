package net.daplumer.more_gems.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import java.util.Arrays;


@Environment(EnvType.CLIENT)
public class BoleEntityModel extends EntityModel<BoleRenderState> {
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart[] rods;
	public static final int maxBoulders = 7;
	protected BoleEntityModel(ModelPart root) {
		super(root);
		this.bone = root.getChild("bone");
		this.bone2 = this.bone.getChild("bone2");
		this.rods = new ModelPart[maxBoulders];
		Arrays.setAll(this.rods, i -> root.getChild(getRodName(i)));
	}

	private static String getRodName(int index) {
		return "part" + index;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		for(int i = 0; i < maxBoulders; i++){
			modelPartData.addChild(
					getRodName(i),
					ModelPartBuilder.create()
							.uv(0, 16)
							.cuboid(-3.0F, 8.0F, -3.0F, 6.0F, 6.0F, 6.0F),
					ModelTransform.NONE
			);
		}

		ModelPartData bone = modelPartData.addChild(
				"bone",
				ModelPartBuilder.create()
						.uv(24, 16)
						.cuboid(-2.0F, 7.0F, -2.0F, 4.0F, 4.0F, 4.0F),
				ModelTransform.NONE);

		ModelPartData bone2 = bone.addChild(
				"bone2",
				ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 8.0F),
				ModelTransform.NONE);
		return TexturedModelData.of(modelData, 64, 64);
	}


	@Override
	public void setAngles(BoleRenderState state) {
		super.setAngles(state);
		float angle;
		for (int i = 0; i < maxBoulders; i++){
			if(i >= state.boulders){
				rods[i].hidden = true;
				continue;
			}
			angle = state.getBoulderAngle(i) * (float) Math.PI;
			rods[i].originX = (float) Math.cos(angle)*8;
			rods[i].originZ = (float) Math.sin(angle)*8;
			rods[i].originY = (float) Math.sin(state.getBoulderHeightAngle(i) * (float) Math.PI);
			rods[i].originY +=(float) Math.max(0, state.getDelayAtBoulderIndex(i) - state.tickProgress);
			rods[i].originY = Math.max(rods[i].originY,-1);
			rods[i].hidden  = (rods[i].originY > 17);
		}
		bone2.yaw = state.relativeHeadYaw * (float) Math.PI/180;
	}
}