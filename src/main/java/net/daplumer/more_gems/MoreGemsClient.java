package net.daplumer.more_gems;

import net.daplumer.more_gems.entity.client.BoleEntityModel;
import net.daplumer.more_gems.entity.client.BoleRenderer;
import net.daplumer.more_gems.entity.custom.BoleClientPacket;
import net.daplumer.more_gems.entity.custom.BoleEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MoreGemsClient implements ClientModInitializer {
    public static final EntityModelLayer BOLE_LAYER = new EntityModelLayer(Identifier.of(MoreGems.MOD_ID, "bole"), "main");
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(MoreGems.BOLE, BoleRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(BOLE_LAYER, BoleEntityModel::getTexturedModelData);


        ClientPlayNetworking.registerGlobalReceiver(BoleClientPacket.ID, (payload, context) -> {
            context.client().execute(() -> {
            });
        });
        EntityRendererRegistry.register(MoreGems.BOULDER,(FlyingItemEntityRenderer::new));
    }
}