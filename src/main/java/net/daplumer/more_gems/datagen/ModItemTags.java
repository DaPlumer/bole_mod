package net.daplumer.more_gems.datagen;

import net.daplumer.more_gems.MoreGems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.daplumer.more_gems.MoreGems.REGISTERER;

public class ModItemTags extends FabricTagProvider.ItemTagProvider {
    public ModItemTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture, null);
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
