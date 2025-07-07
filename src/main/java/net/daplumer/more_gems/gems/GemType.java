package net.daplumer.more_gems.gems;

import com.mojang.serialization.Lifecycle;
import net.daplumer.more_gems.MoreGems;
import net.minecraft.block.MapColor;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public record GemType(String name, MapColor color) {
    public static final RegistryKey<Registry<GemType>> REGISTRY_KEY = RegistryKey.ofRegistry(Identifier.of(MoreGems.MOD_ID, "gem_types"));
    public static final Registry<GemType> REGISTRY = new SimpleRegistry<>(
            REGISTRY_KEY,
            Lifecycle.stable()
    );
    public static GemType of(Identifier id, MapColor color){
        GemType type = new GemType(id.getPath(), color);
        Registry.register(REGISTRY, id, type);
        return type;
    }
}
