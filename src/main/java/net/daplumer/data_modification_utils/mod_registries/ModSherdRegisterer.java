package net.daplumer.data_modification_utils.mod_registries;

import kotlin.jvm.functions.Function1;
import net.minecraft.block.DecoratedPotPattern;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class ModSherdRegisterer extends ModDataRegisterer<Item,Item.Settings, DecoratedPotPattern>{
    public ModSherdRegisterer(@NotNull String namespace) {
        super(namespace);
    }

    @Override
    public @NotNull RegistryKey<DecoratedPotPattern> getRegistryKey(@NotNull Identifier identifier) {
        return RegistryKey.of(RegistryKeys.DECORATED_POT_PATTERN, identifier);
    }

    @Override
    public @Nullable Item getInstance(@NotNull Identifier identifier) {
        return Registries.ITEM.get(identifier);
    }

    @Override
    public @Nullable Item getInstance(@NotNull String name) {
        return super.getInstance(name+"_pottery_sherd");
    }

    @Override
    public <U extends Item> U register(@NotNull String name, @Nullable Item.Settings instanceSettings, @Nullable Function1<? super Item.Settings, ? extends U> instanceFactory) {
        Item sherd = Registry.register(
                Registries.ITEM,
                getIdentifier(name + "_pottery_sherd"),
                new Item(
                        new Item.Settings()
                                .rarity(Rarity.UNCOMMON)
                                .registryKey(RegistryKey.of(RegistryKeys.ITEM, getIdentifier(name + "_pottery_sherd")))
                )
        );
        Registry.register(Registries.DECORATED_POT_PATTERN,getRegistryKey(name),new DecoratedPotPattern(getIdentifier(name+"_pottery_pattern")));

        return (U) sherd;
    }


}
