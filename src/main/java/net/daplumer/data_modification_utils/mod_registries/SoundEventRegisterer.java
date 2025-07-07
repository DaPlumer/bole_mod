package net.daplumer.data_modification_utils.mod_registries;

import kotlin.jvm.functions.Function1;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoundEventRegisterer extends ModDataRegisterer<SoundEvent,Unit, SoundEvent> {
    public SoundEventRegisterer(@NotNull String namespace) {
        super(namespace);
    }

    @Override
    public @NotNull RegistryKey<SoundEvent> getRegistryKey(@NotNull Identifier identifier) {
        return RegistryKey.of(RegistryKeys.SOUND_EVENT, identifier);
    }

    @Override
    public @Nullable SoundEvent getInstance(@NotNull Identifier identifier) {
        return SoundEvent.of(identifier);
    }

    @Override
    public <U extends SoundEvent> U register(@NotNull String name, @Nullable Unit instanceSettings, @Nullable Function1<? super Unit, ? extends U> instanceFactory) {
        return (U) Registry.register(Registries.SOUND_EVENT, Identifier.of(getNamespace(), name), SoundEvent.of(Identifier.of(getNamespace(),name)));
    }
}
