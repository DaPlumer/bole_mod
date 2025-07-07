package net.daplumer.more_gems.gems;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.BiFunction;

public class GemBlock extends Block {
    public static <T extends GemBlock> MapCodec<T> generateCodec(BiFunction<Settings, GemType, T> constructor){
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            Settings.CODEC.fieldOf("settings").forGetter(Block::getSettings),
            GemType.REGISTRY.getCodec().fieldOf("type").forGetter(GemBlock::getType)
        ).apply(instance, constructor));
    }
    public static final MapCodec<GemBlock> CODEC = generateCodec(GemBlock::new);
    protected final GemType type;
    public GemBlock(Settings settings, GemType type) {
        super(settings);
        this.type = type;
    }
    public GemType getType(){
        return type;
    }

    @Override
    protected void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!world.isClient) {
            BlockPos blockPos = hit.getBlockPos();
            world.playSound(null, blockPos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
        }
    }
}
