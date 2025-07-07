package net.daplumer.more_gems;

import net.daplumer.more_gems.datagen.GemModelGen;
import net.daplumer.more_gems.datagen.ModItemTags;
import net.daplumer.more_gems.datagen.RecipeGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.tag.BlockTags;

public class MoreGemsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		MoreGems.RUBBLE_STONE.provideDataTo(pack, BlockTags.NEEDS_STONE_TOOL);
		pack.addProvider(RecipeGenerator::new);
		pack.addProvider(GemModelGen::new);
		pack.addProvider(ModItemTags::new);
	}
}
