package com.remag.blockgenerators.compat;

import com.remag.blockgenerators.BlockGenerators;
import com.remag.blockgenerators.block.ModBlocks;
import com.remag.blockgenerators.util.ModTags;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class BlockGeneratorsJEIPlugin implements IModPlugin {
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BlockGenerators.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new BlockGeneratorsJEICategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<ItemStack> tagItems = new ArrayList<>();

        for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(ModTags.BLOCK_GEN_INPUTS)) {
            tagItems.add(new ItemStack(holder.value()));
        }

        // Split into sublists of 15
        for (int i = 0; i < tagItems.size(); i += 60) {
            List<ItemStack> sublist = tagItems.subList(i, Math.min(i + 60, tagItems.size()));
            registration.addRecipes(
                    new RecipeType<>(BlockGeneratorsJEICategory.UID, BlockGeneratorsJEICategory.Wrapper.class),
                    List.of(new BlockGeneratorsJEICategory.Wrapper(sublist))
            );
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BLOCK_GENERATOR.get()),
                new RecipeType<>(BlockGeneratorsJEICategory.UID, BlockGeneratorsJEICategory.Wrapper.class));
    }
}
