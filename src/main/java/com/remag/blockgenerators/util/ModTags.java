package com.remag.blockgenerators.util;

import com.remag.blockgenerators.BlockGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.remag.blockgenerators.BlockGenerators.MODID;

public class ModTags {
    // public static final TagKey<Item> BLOCK_GEN_INPUTS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "block_generator_input"));
    public static final TagKey<Item> BLOCK_GEN_MISC = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "block_generator_misc"));
}
