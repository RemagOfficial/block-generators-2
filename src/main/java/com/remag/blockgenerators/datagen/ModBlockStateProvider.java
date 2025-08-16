package com.remag.blockgenerators.datagen;

import com.remag.blockgenerators.BlockGenerators;
import com.remag.blockgenerators.block.ModBlocks;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BlockGenerators.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        cubeColumnBlock(ModBlocks.BLOCK_GENERATOR.get(),
                modLoc("block/block_generator_side"),
                modLoc("block/block_generator_top"),
                modLoc("block/block_generator_bottom"));

        cubeColumnBlockNoRotation(ModBlocks.GENERATOR_BASE.get(),
                modLoc("block/block_generator_base_side"),
                modLoc("block/block_generator_base_top"),
                modLoc("block/block_generator_base_top"));
    }

    public void cubeColumnBlock(Block block, ResourceLocation sideTexture, ResourceLocation topTexture, ResourceLocation bottomTexture) {
        String name = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();

        // Use the custom parent model
        models().withExistingParent(name, modLoc("block/block_generator_model"))
                .texture("side", sideTexture)
                .texture("end", topTexture)          // top face
                .texture("end_bottom", bottomTexture); // bottom face

        // Blockstate rotation for horizontal facing
        getVariantBuilder(block).forAllStates(state -> {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

            int yRot = switch (facing) {
                case NORTH -> 0;
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };

            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc("block/" + name)))
                    .rotationY(yRot)
                    .build();
        });

        // Item model
        simpleBlockItem(block, models().getExistingFile(modLoc("block/" + name)));
    }

    public void cubeColumnBlockNoRotation(Block block, ResourceLocation sideTexture, ResourceLocation topTexture, ResourceLocation bottomTexture) {
        String name = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();

        // Create the block model with separate textures
        models().withExistingParent(name, modLoc("block/block_generator_model"))
                .texture("side", sideTexture)
                .texture("end", topTexture)          // top face
                .texture("end_bottom", bottomTexture); // bottom face

        // Blockstate without rotation: just one model for all states
        getVariantBuilder(block).forAllStates(state ->
                ConfiguredModel.builder()
                        .modelFile(models().getExistingFile(modLoc("block/" + name)))
                        .build()
        );

        // Item model
        simpleBlockItem(block, models().getExistingFile(modLoc("block/" + name)));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

}
