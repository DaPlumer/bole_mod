package net.daplumer.data_modification_utils.mod_registries;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.IntProvider;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
public final class Registerer {
    private final String namespace;
    public static Registerer of(String namespace){
        return new Registerer(namespace);
    }
    private Registerer(String namespace){
        this.namespace = namespace;
        ITEMS              = new GeneralDataRegisterer<>(Registries.ITEM,namespace,Item.Settings::registryKey,Item::new,Item.Settings::new);
        BLOCKS             = new GeneralDataRegisterer<>(Registries.BLOCK,namespace,AbstractBlock.Settings::registryKey,Block::new,AbstractBlock.Settings::create);
        FLUIDS             = new GeneralDataRegisterer<>(Registries.FLUID,namespace,null,(fluid -> fluid),()->null);
        ITEM_GROUPS        = new GeneralDataRegisterer<>(Registries.ITEM_GROUP,namespace,null, ItemGroup.Builder::build, FabricItemGroup::builder);
        BLOCK_ENTITY_TYPES = new GeneralDataRegisterer<>(Registries.BLOCK_ENTITY_TYPE,namespace, null, FabricBlockEntityTypeBuilder::build,() -> null);
        STATS              = new ModStatTypeRegisterer(namespace);
        ENTITY_TYPES       = new ModEntityTypeRegisterer(namespace);
        ARMOR_MATERIALS    = new ModArmorMaterialRegisterer(namespace);
        SOUNDS             = new SoundEventRegisterer(namespace);
        SHERDS             = new ModSherdRegisterer(namespace);
    }
    public final GeneralDataRegisterer<Item, Item.Settings> ITEMS;
    public final GeneralDataRegisterer<Fluid, Fluid> FLUIDS;
    public final GeneralDataRegisterer<Block, AbstractBlock.Settings> BLOCKS;
    public final GeneralDataRegisterer<ItemGroup, ItemGroup.Builder> ITEM_GROUPS;
    public final GeneralDataRegisterer<BlockEntityType<?>, FabricBlockEntityTypeBuilder<?>> BLOCK_ENTITY_TYPES;
    public final ModStatTypeRegisterer STATS;
    public final ModEntityTypeRegisterer ENTITY_TYPES;
    public final SoundEventRegisterer SOUNDS;
    public final ModArmorMaterialRegisterer ARMOR_MATERIALS;
    public final ModSherdRegisterer SHERDS;
    public String getNamespace(){return this.namespace;}

    public static Function1<? super Item.Settings, ? extends Item> BLOCK_ITEM(Block block){
        return (settings -> new BlockItem(block, settings));
    }
    public static Function1<? super AbstractBlock.Settings, ? extends Block> EXP_DROPPER(IntProvider exp){
        return (settings -> new ExperienceDroppingBlock(exp,settings));
    }
    public static BlockItem registerBlockItem(Block block){
        return registerBlockItem(block,new Item.Settings());
    }
    public static BlockItem registerBlockItem(Block block, Item.Settings settings){
        BlockItem item = Registry.register(Registries.ITEM,Registries.BLOCK.getId(block),
                new BlockItem(block,settings.registryKey(RegistryKey.of(RegistryKeys.ITEM, Registries.BLOCK.getId(block))).translationKey(block.getTranslationKey())));
        Item.BLOCK_ITEMS.put(block,item);
        return item;
    }
    public static void registerBlockItems(@NotNull Block... blocks){
        Arrays.stream(blocks).forEach((Registerer::registerBlockItem));
    }
    public static Function1<? super AbstractBlock.Settings, ? extends  Block> FLUID(FlowableFluid fluid){
        return (settings -> new FluidBlock(fluid,settings));
    }
}
