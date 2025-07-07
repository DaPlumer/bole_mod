package net.daplumer.more_gems.gems.growing_crystals;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.daplumer.more_gems.gems.GemBlock;
import net.daplumer.more_gems.gems.GemCrystals;
import net.daplumer.more_gems.gems.GemType;
import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

public class BuddingGemBlock extends GemBlock {
	public static final MapCodec<BuddingGemBlock> CODEC =
        RecordCodecBuilder.mapCodec(instance -> instance.group(
			Settings.CODEC.fieldOf("settings").forGetter(Block::getSettings),
            GemType.REGISTRY.getCodec().fieldOf("type").forGetter(GemBlock::getType),
			GemCrystals.CODEC.fieldOf("crystals").forGetter((block) -> block.crystals)
        ).apply(instance, BuddingGemBlock::new));
	public static final int GROW_CHANCE = 5;
	private static final Direction[] DIRECTIONS = Direction.values();

	@Override
	public MapCodec<BuddingGemBlock> getCodec() {
		return CODEC;
	}
	public final GemCrystals crystals;
	public BuddingGemBlock(AbstractBlock.Settings settings, GemType type, GemCrystals crystals) {
		super(settings, type);
		this.crystals = crystals;
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (random.nextInt(5) == 0) {
			Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
			BlockPos blockPos = pos.offset(direction);
			BlockState blockState = world.getBlockState(blockPos);
			Block block = null;
			if (canGrowIn(blockState)) {
				block = crystals.tiny();
			} else if (blockState.isOf(crystals.tiny()) && blockState.get(AmethystClusterBlock.FACING) == direction) {
				block = crystals.small();
			} else if (blockState.isOf(crystals.small()) && blockState.get(AmethystClusterBlock.FACING) == direction) {
				block = crystals.medium();
			} else if (blockState.isOf(crystals.medium()) && blockState.get(AmethystClusterBlock.FACING) == direction) {
				block = crystals.big();
			}

			if (block != null) {
				BlockState blockState2 = block.getDefaultState()
					.with(AmethystClusterBlock.FACING, direction)
					.with(AmethystClusterBlock.WATERLOGGED, blockState.getFluidState().getFluid() == Fluids.WATER);
				world.setBlockState(blockPos, blockState2);
			}
		}
	}

	public static boolean canGrowIn(BlockState state) {
		return state.isAir() || state.isOf(Blocks.WATER) && state.getFluidState().getLevel() == 8;
	}
}
