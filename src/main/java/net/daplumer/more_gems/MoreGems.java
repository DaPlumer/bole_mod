package net.daplumer.more_gems;

import net.daplumer.data_modification_utils.block_set_generation.Shift;
import net.daplumer.data_modification_utils.block_set_generation.stone.KStoneSet;
import net.daplumer.data_modification_utils.block_set_generation.stone.KStoneSet.Variant;
import net.daplumer.data_modification_utils.mod_registries.GeneralDataRegisterer;
import net.daplumer.data_modification_utils.mod_registries.Registerer;
import net.daplumer.data_modification_utils.mod_registries.registering_functions.ItemsKt;
import net.daplumer.more_gems.Item.BoulderItem;
import net.daplumer.more_gems.entity.custom.BoleClientPacket;
import net.daplumer.more_gems.entity.custom.BoleEntity;
import net.daplumer.more_gems.entity.custom.Boulder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class MoreGems implements ModInitializer {
	public static final String MOD_ID = "more_gems";

	public static final Registerer REGISTERER = Registerer.of(MOD_ID);
	public static final GeneralDataRegisterer<Item, Item.Settings> ITEMS = REGISTERER.ITEMS;
	public static final SimpleParticleType BOULDER_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MoreGems.MOD_ID, "boulder_item"), new Boulder.SimpleParticleTypeWrapper(false));

	public static final EntityType<BoleEntity> BOLE = REGISTERER.ENTITY_TYPES.register(
			"bole",
			EntityType.Builder.create(BoleEntity::new, SpawnGroup.CREATURE)
					.dimensions(.75F,2F)
	);
	public static final EntityType<Boulder> BOULDER = REGISTERER.ENTITY_TYPES.register(
					"boulder",
					EntityType.Builder.<Boulder>create((Boulder::new), SpawnGroup.MISC)
			.dimensions(.5F,.5F)
			.trackingTickInterval(10)
			.maxTrackingRange(5)
	);
	public static final BoulderItem BOULDER_ITEM = ITEMS.register(
			"boulder",
			BoulderItem::new
	);
	public static final RegistryKey<DamageType> BOULDER_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(MOD_ID,"boulder"));
	@SuppressWarnings("deprecation")
    public static final KStoneSet RUBBLE_STONE = new KStoneSet.StoneSetBuilder(
			"rubble", REGISTERER.BLOCKS)
			.addVariants(Variant.POLISHED,Variant.BRICKS,Variant.MOSAIC)
			.hasCobbledVariant(true)
			.addChiselableVariants(Variant.MOSAIC)
			.addCrackableVariants(Variant.MOSAIC, Variant.BRICKS)
			.setDefaultSettings(AbstractBlock.Settings.copyShallow(Blocks.STONE))
			.build();
	public static final Item RUBBLE = ITEMS.register("rubble_item");
	public static final SpawnEggItem SPAWN_BOLE = ITEMS.register("bole_spawn_egg", ItemsKt.SPAWN_EGG(BOLE));
	public static final SmithingTemplateItem BOLD_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register(
			"bold_armor_trim_smithing_template", ItemsKt.ARMOR_TRIM(Rarity.UNCOMMON));
	public static final RegistryEntry<Potion> RESISTANCE_POTION =
			Registries.POTION.getEntry( Registry.register(
					Registries.POTION,
					Identifier.of(MOD_ID, "resistance"),
					new Potion("resistance",
							new StatusEffectInstance(
									StatusEffects.RESISTANCE,
									3600,
									0))));
	public static final RegistryEntry<Potion> LONG_RESISTANCE_POTION =
			Registries.POTION.getEntry( Registry.register(
					Registries.POTION,
					Identifier.of(MOD_ID, "long_resistance"),
					new Potion("resistance",
							new StatusEffectInstance(
									StatusEffects.RESISTANCE,
									9600,
									0))));
	public static final RegistryEntry<Potion> STRONG_RESISTANCE_POTION =
			Registries.POTION.getEntry( Registry.register(
					Registries.POTION,
					Identifier.of(MOD_ID, "strong_resistance"),
					new Potion("resistance",
							new StatusEffectInstance(
									StatusEffects.RESISTANCE,
									1800,
									1))));
	@Override
	public void onInitialize() {
		FabricDefaultAttributeRegistry.register(BOLE, BoleEntity.createAttributes());
		PayloadTypeRegistry.playS2C().register(BoleClientPacket.ID, BoleClientPacket.CODEC);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries ->
				RUBBLE_STONE.insertEntries(entries, Items.SANDSTONE, Shift.BEFORE)
		);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries ->{
				entries.addAfter(Items.BREEZE_ROD, BOULDER_ITEM, RUBBLE);
				entries.addAfter(Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, BOLD_ARMOR_TRIM_SMITHING_TEMPLATE);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries ->
				entries.addBefore(Items.BREEZE_SPAWN_EGG,SPAWN_BOLE));
		FabricBrewingRecipeRegistryBuilder.BUILD.register((builder -> {
			builder.registerRecipes(BOULDER_ITEM,RESISTANCE_POTION);
			builder.registerPotionRecipe(RESISTANCE_POTION,Items.GLOWSTONE_DUST, STRONG_RESISTANCE_POTION);
			builder.registerPotionRecipe(RESISTANCE_POTION,Items.REDSTONE, LONG_RESISTANCE_POTION);
		}));
	}
}