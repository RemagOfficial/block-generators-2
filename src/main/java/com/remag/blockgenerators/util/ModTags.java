package com.remag.blockgenerators.util;

import com.remag.blockgenerators.BlockGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static final TagKey<Item> BLOCK_GEN_INPUTS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(BlockGenerators.MODID, "block_generator_input"));
}
