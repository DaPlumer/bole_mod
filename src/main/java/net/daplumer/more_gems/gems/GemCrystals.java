package net.daplumer.more_gems.gems;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.daplumer.more_gems.gems.growing_crystals.GemClusterBlock;

public record GemCrystals(GemClusterBlock tiny, GemClusterBlock small, GemClusterBlock medium, GemClusterBlock big) {
    public static final MapCodec<GemCrystals> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            GemClusterBlock.CODEC.fieldOf("tiny_crystal").forGetter(GemCrystals::tiny),
            GemClusterBlock.CODEC.fieldOf("small_crystal").forGetter(GemCrystals::small),
            GemClusterBlock.CODEC.fieldOf("medium_crystal").forGetter(GemCrystals::medium),
            GemClusterBlock.CODEC.fieldOf("big_crystal").forGetter(GemCrystals::big)
    ).apply(instance, GemCrystals::new));
}
