package net.daplumer.more_gems.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

import static net.daplumer.more_gems.MoreGems.REGISTERER;

public class ModBlockTags extends FabricTagProvider.BlockTagProvider {
    public ModBlockTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    /**
     * Implement this method and then use {@link FabricTagProvider#builder} to get and register new tag builders.
     *
     * @param wrapperLookup
     */
    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
    }
}
