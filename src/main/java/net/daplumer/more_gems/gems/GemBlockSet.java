package net.daplumer.more_gems.gems;

import com.jcraft.jorbis.Block;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.daplumer.data_modification_utils.mod_registries.GeneralDataRegisterer;
import net.daplumer.data_modification_utils.mod_registries.Registerer;
import net.daplumer.more_gems.MoreGems;
import net.daplumer.more_gems.gems.growing_crystals.BuddingGemBlock;
import net.daplumer.more_gems.gems.growing_crystals.GemClusterBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.stream.Stream;

public record GemBlockSet(GemBlock block, GemCrystals crystals, BuddingGemBlock buddingGem) {
    public static final MapCodec<GemBlockSet> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            GemBlock.CODEC.fieldOf("base_block").forGetter(GemBlockSet::block),
            GemCrystals.CODEC.fieldOf("crystals").forGetter(GemBlockSet::crystals),
            BuddingGemBlock.CODEC.fieldOf("budding_gem_block").forGetter(GemBlockSet::buddingGem)
    ).apply(instance,GemBlockSet::new));

    public static final RegistryKey<Registry<GemBlockSet>> REGISTRY_KEY = RegistryKey.ofRegistry(Identifier.of(MoreGems.MOD_ID,"gem_block_sets"));
    public static final Registry<GemBlockSet> REGISTRY = new SimpleRegistry<>(REGISTRY_KEY, Lifecycle.stable());
    public static GemBlockSet of(Registerer registerer, GemType type){
        GemBlock defaultBlock = registerer.BLOCKS.register(
                (type.name() + "_block"),
                AbstractBlock.Settings.create()
                        .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                        .strength(1.5F)
                        .mapColor(type.color())
                        .requiresTool(),
                settings -> (new GemBlock(settings, type))
        );
        GemClusterBlock small_bud = registerer.BLOCKS.register(
                ("small_" + type.name() + "_bud"),
                AbstractBlock.Settings.create()
                        .sounds(BlockSoundGroup.SMALL_AMETHYST_BUD)
                        .strength(1.5F)
                        .mapColor(type.color())
                        .solid()
                        .nonOpaque()
                        .pistonBehavior(PistonBehavior.DESTROY)
                        .luminance(state -> 1),
                settings -> (new GemClusterBlock(3F,8F,settings, type))
        );
        GemClusterBlock medium_bud = registerer.BLOCKS.register(
                ("medium_" + type.name() + "_bud"),
                AbstractBlock.Settings.create()
                        .sounds(BlockSoundGroup.MEDIUM_AMETHYST_BUD)
                        .strength(1.5F)
                        .mapColor(type.color())
                        .solid()
                        .nonOpaque()
                        .pistonBehavior(PistonBehavior.DESTROY)
                        .luminance(state -> 2),
                settings -> (new GemClusterBlock(4F,10F,settings, type))
        );
        GemClusterBlock large_bud = registerer.BLOCKS.register(
                ("large_" + type.name() + "_bud"),
                AbstractBlock.Settings.create()
                        .sounds(BlockSoundGroup.LARGE_AMETHYST_BUD)
                        .strength(1.5F)
                        .mapColor(type.color())
                        .solid()
                        .nonOpaque()
                        .pistonBehavior(PistonBehavior.DESTROY)
                        .luminance(state -> 4),
                settings -> (new GemClusterBlock(5F,10F,settings, type))
        );
        GemClusterBlock cluster = registerer.BLOCKS.register(
                (type.name() + "_cluster"),
                AbstractBlock.Settings.create()
                        .sounds(BlockSoundGroup.AMETHYST_CLUSTER)
                        .strength(1.5F)
                        .mapColor(type.color())
                        .solid()
                        .nonOpaque()
                        .pistonBehavior(PistonBehavior.DESTROY)
                        .luminance(state -> 5),
                settings -> (new GemClusterBlock(7F,10F,settings, type))
        );

        GemCrystals crystals = new GemCrystals(small_bud,medium_bud,large_bud,cluster);
        BuddingGemBlock budding_block = registerer.BLOCKS.register(
                "budding_"+type.name(),
                AbstractBlock.Settings.create()
                        .mapColor(type.color())
                        .ticksRandomly()
                        .strength(1.5F)
                        .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                        .requiresTool()
                        .pistonBehavior(PistonBehavior.DESTROY),
                settings -> new BuddingGemBlock(settings,type,crystals)
        );
        GemBlockSet set = new GemBlockSet(defaultBlock, crystals, budding_block);
        Registry.register(REGISTRY, Identifier.of(registerer.getNamespace(), type.name()),set);
        return set;
    }
    public Stream<GemBlock> stream(){
        return Stream.of(block,buddingGem,crystals.tiny(),crystals.small(),crystals.medium(),crystals.big());
    }


}
