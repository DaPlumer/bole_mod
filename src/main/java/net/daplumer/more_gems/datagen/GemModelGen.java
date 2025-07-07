package net.daplumer.more_gems.datagen;

import net.daplumer.more_gems.MoreGems;
import net.daplumer.more_gems.gems.GemBlockSet;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;

public class GemModelGen extends FabricModelProvider {
    public GemModelGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        for (GemBlockSet set : GemBlockSet.REGISTRY){
            blockStateModelGenerator.registerSimpleCubeAll(set.block());
            blockStateModelGenerator.registerSimpleCubeAll(set.buddingGem());
            blockStateModelGenerator.registerAmethyst(set.crystals().tiny());
            blockStateModelGenerator.registerAmethyst(set.crystals().small());
            blockStateModelGenerator.registerAmethyst(set.crystals().medium());
            blockStateModelGenerator.registerAmethyst(set.crystals().big());
            set.stream().forEach(blockStateModelGenerator::registerItemModel);
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(MoreGems.BOLD_ARMOR_TRIM_SMITHING_TEMPLATE, Models.GENERATED);
    }
}
