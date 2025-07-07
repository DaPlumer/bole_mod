package net.daplumer.data_modification_utils.mixin;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FabricTagProvider.class)
public interface FabricTagProviderAccessor {
    @Invoker("builder")
    <T> ProvidedTagBuilder<RegistryKey<T>,T> builder(TagKey<T> key);
}
