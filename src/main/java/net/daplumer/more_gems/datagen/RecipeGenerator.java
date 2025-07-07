package net.daplumer.more_gems.datagen;

import net.daplumer.data_modification_utils.block_set_generation.stone.KStoneSet;
import net.daplumer.more_gems.MoreGems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class RecipeGenerator extends FabricRecipeProvider {
    public RecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }
    @Override
    protected net.minecraft.data.recipe.RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new net.minecraft.data.recipe.RecipeGenerator(registryLookup,exporter) {
            @Override
            public void generate() {
                createShapeless(RecipeCategory.COMBAT, MoreGems.BOULDER_ITEM)
                        .input(MoreGems.RUBBLE, 2)
                        .criterion("get_rubble", Criteria.INVENTORY_CHANGED.create(InventoryChangedCriterion.Conditions.items(MoreGems.RUBBLE ).conditions()))
                        .offerTo(exporter,"boulder_from_rubble");
                offer2x2CompactingRecipe(RecipeCategory.BUILDING_BLOCKS,MoreGems.RUBBLE_STONE.getVariant(KStoneSet.Variant.COBBLED).getDefaultBlock(), MoreGems.RUBBLE);
                offerSmithingTemplateCopyingRecipe(MoreGems.BOLD_ARMOR_TRIM_SMITHING_TEMPLATE, Blocks.RED_SANDSTONE);
            }
        };
    }

    @Override
    public String getName() {
        return "MoreGemsRecipeGenerator";
    }
}
