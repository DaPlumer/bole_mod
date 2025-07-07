package net.daplumer.data_modification_utils.PlayerData;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class PlayerDataCallbacks<T> {
    public static PlayerDataCallbacks<WriteView> STORE_DATA = new PlayerDataCallbacks<>();
    public static PlayerDataCallbacks<ReadView> LOAD_DATA   = new PlayerDataCallbacks<>();

    public void addCallback(BiConsumer<T, PlayerEntity> callback){
        this.callbacks.add(callback);
    }

    private final Set<BiConsumer<T, PlayerEntity>> callbacks = new HashSet<>();

    public Set<BiConsumer<T, PlayerEntity>> getCallbacks() {
        return callbacks;
    }
}
