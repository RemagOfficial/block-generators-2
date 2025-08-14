package com.remag.blockgenerators.block;

import com.remag.blockgenerators.BlockGenerators;
import com.remag.blockgenerators.block.custom_blocks.BlockGeneratorBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BlockGenerators.MODID);

    public static final RegistryObject<Block> BLOCK_GENERATOR = BLOCKS.register("block_generator",
            () -> new BlockGeneratorBlock(Block.Properties.of().strength(3.5f).sound(SoundType.METAL).mapColor(MapColor.METAL)));

    public static final RegistryObject<Block> GENERATOR_BASE = BLOCKS.register("generator_base",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SMOOTH_STONE).sound(SoundType.STONE)));
}
