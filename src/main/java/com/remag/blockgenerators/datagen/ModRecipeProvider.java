package com.remag.blockgenerators.datagen;

import com.remag.blockgenerators.block.ModBlocks;
import com.remag.blockgenerators.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.GENERATOR_BASE.get())
                .pattern("IRI")
                .pattern("CSC")
                .pattern("IRI")
                .define('I', ModItems.REDSTONE_INFUSED_IRON.get())
                .define('R', Items.COBBLESTONE)
                .define('S', Items.SMOOTH_STONE)
                .define('C', Items.COBBLESTONE)
                .unlockedBy("has_redstone_infused_iron", has(ModItems.REDSTONE_INFUSED_IRON.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.REDSTONE_INFUSED_IRON.get())
                .pattern(" R ")
                .pattern("RIR")
                .pattern(" R ")
                .define('R', Items.REDSTONE)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BLOCK_GENERATOR.get())
                .pattern("ISI")
                .pattern("SBS")
                .pattern("ISI")
                .define('I', Items.IRON_BLOCK)
                .define('S', Items.SMOOTH_STONE)
                .define('B', ModItems.GENERATOR_BASE_BLOCK_ITEM.get())
                .unlockedBy("has_generator_base", has(ModItems.GENERATOR_BASE_BLOCK_ITEM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.UPGRADE_BASE.get())
                .pattern("IRI")
                .pattern("RGR")
                .pattern("IRI")
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('G', Items.GOLD_INGOT)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SPEED_UPGRADE.get())
                .pattern("SRC")
                .pattern("RBR")
                .pattern("CRS")
                .define('S', Items.SUGAR)
                .define('C', Items.CLOCK)
                .define('R', Items.REDSTONE)
                .define('B', ModItems.UPGRADE_BASE.get())
                .unlockedBy(getHasName(ModItems.UPGRADE_BASE.get()), has(ModItems.UPGRADE_BASE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VERTICAL_OFFSET_UPGRADE.get())
                .pattern("QEQ")
                .pattern("EBE")
                .pattern("QEQ")
                .define('Q', Items.QUARTZ)
                .define('E', Items.ENDER_PEARL)
                .define('B', ModItems.UPGRADE_BASE.get())
                .unlockedBy(getHasName(ModItems.UPGRADE_BASE.get()), has(ModItems.UPGRADE_BASE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.INVENTORY_OUTPUT_UPGRADE.get())
                .pattern("IDI")
                .pattern("RBR")
                .pattern("IPI")
                .define('I', Items.IRON_INGOT)
                .define('P', Items.PISTON)
                .define('D', Items.DIAMOND_PICKAXE)
                .define('B', ModItems.UPGRADE_BASE.get())
                .define('R', Items.REDSTONE)
                .unlockedBy(getHasName(ModItems.UPGRADE_BASE.get()), has(ModItems.UPGRADE_BASE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ORE_TYPE_UPGRADE.get())
                .pattern("GOG")
                .pattern("OBO")
                .pattern("GOG")
                .define('O', Tags.Items.ORES)
                .define('G', Items.GOLD_INGOT)
                .define('B', ModItems.UPGRADE_BASE.get())
                .unlockedBy("has_block_generator", has(ModItems.BLOCK_GENERATOR_BLOCK_ITEM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COBBLESTONE_TYPE_UPGRADE.get())
                .pattern("GCG")
                .pattern("CBC")
                .pattern("GCG")
                .define('C', Tags.Items.COBBLESTONE)
                .define('G', Items.GOLD_INGOT)
                .define('B', ModItems.UPGRADE_BASE.get())
                .unlockedBy("has_block_generator", has(ModItems.BLOCK_GENERATOR_BLOCK_ITEM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STONE_TYPE_UPGRADE.get())
                .pattern("GSG")
                .pattern("SBS")
                .pattern("GSG")
                .define('S', Tags.Items.STONE)
                .define('G', Items.GOLD_INGOT)
                .define('B', ModItems.UPGRADE_BASE.get())
                .unlockedBy("has_block_generator", has(ModItems.BLOCK_GENERATOR_BLOCK_ITEM.get()))
                .save(consumer);
    }
}
