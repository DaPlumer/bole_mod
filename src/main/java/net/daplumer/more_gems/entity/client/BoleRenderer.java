package net.daplumer.more_gems.entity.client;

import net.daplumer.more_gems.MoreGems;
import net.daplumer.more_gems.MoreGemsClient;
import net.daplumer.more_gems.entity.custom.BoleEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;


@Environment(EnvType.CLIENT)
public class BoleRenderer extends LivingEntityRenderer<BoleEntity,BoleRenderState,BoleEntityModel> {
    public BoleRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BoleEntityModel(ctx.getPart(MoreGemsClient.BOLE_LAYER)), .4F);
    }

    @Override
    public Identifier getTexture(BoleRenderState state) {
        return Identifier.of(MoreGems.MOD_ID, "textures/entity/bole.png");
    }

    @Override
    public BoleRenderState createRenderState() {
        return new BoleRenderState();
    }

    @Override
    protected @Nullable Text getDisplayName(BoleEntity entity) {
        return entity.shouldRenderName()? super.getDisplayName(entity):null;
    }

    @Override
    public void updateRenderState(BoleEntity livingEntity, BoleRenderState livingEntityRenderState, float tickProgress) {
        super.updateRenderState(livingEntity,livingEntityRenderState,tickProgress);
        livingEntityRenderState.boulders = livingEntity.getDataTracker().get(BoleEntity.BOULDERS);
        livingEntityRenderState.boulderCooldowns = livingEntity.getDataTracker().get(BoleEntity.BOULDER_DELAYS);
        livingEntityRenderState.tickProgress = tickProgress;
    }

}
