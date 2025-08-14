package com.remag.blockgenerators.block.entities;

import com.remag.blockgenerators.BlockGenerators;
import com.remag.blockgenerators.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BlockGenerators.MODID);

    public static final RegistryObject<BlockEntityType<BlockGeneratorBlockEntity>> BLOCK_GENERATOR =
            BLOCK_ENTITIES.register("block_generator",
                    () -> BlockEntityType.Builder.of(BlockGeneratorBlockEntity::new, ModBlocks.BLOCK_GENERATOR.get()).build(null));
}
