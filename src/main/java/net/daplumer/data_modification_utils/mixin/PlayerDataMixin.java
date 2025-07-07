package net.daplumer.data_modification_utils.mixin;

import net.daplumer.data_modification_utils.PlayerData.PlayerDataCallbacks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerDataMixin {
    @Inject(method = "readCustomData",at = @At("TAIL"))
    void readData(ReadView view, CallbackInfo ci){
        PlayerDataCallbacks.LOAD_DATA.getCallbacks().forEach(consumer -> consumer.accept(view, (PlayerEntity) (Object) this));
    }
    @Inject(method = "writeCustomData",at = @At("TAIL"))
    void writeData(WriteView view, CallbackInfo ci){
        PlayerDataCallbacks.STORE_DATA.getCallbacks().forEach(consumer -> consumer.accept(view,(PlayerEntity) (Object) this));
    }
}
