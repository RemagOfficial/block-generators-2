package com.remag.blockgenerators.datagen;

import com.remag.blockgenerators.BlockGenerators;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider,
                               CompletableFuture<TagLookup<Block>> blockTagLookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockTagLookup, BlockGenerators.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Create your custom tag
        TagKey<Item> blockGenInputTag = ItemTags.create(ResourceLocation.fromNamespaceAndPath(BlockGenerators.MODID, "block_generator_input"));

        // Add vanilla ores tag to it
        tag(blockGenInputTag)
                .addTag(Tags.Items.ORES)                    // ores
                .addTag(Tags.Items.STONE)                   // all stone variants
                .addTag(Tags.Items.COBBLESTONE)             // cobble + variants
                .add(Blocks.ANDESITE.asItem(), Blocks.DIORITE.asItem(), Blocks.GRANITE.asItem(), Blocks.COBBLED_DEEPSLATE.asItem(), Blocks.DEEPSLATE.asItem())
                .add(Items.DIRT, Items.GRAVEL, Items.SAND, Items.RED_SAND, Items.CLAY) // extra vanilla non-renewables
                .add(Items.SOUL_SOIL, Items.NETHERRACK, Items.BASALT, Items.BLACKSTONE, Items.END_STONE) // nether/end materials
                .add(Items.ICE, Items.PACKED_ICE, Items.BLUE_ICE, Items.SNOW_BLOCK, Items.DRIPSTONE_BLOCK); // ice, snow, dripstone
    }
}
