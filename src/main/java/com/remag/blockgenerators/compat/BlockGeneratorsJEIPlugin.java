package com.remag.blockgenerators.compat;

import com.remag.blockgenerators.BlockGenerators;
import com.remag.blockgenerators.block.ModBlocks;
import com.remag.blockgenerators.item.ModItems;
import com.remag.blockgenerators.item.upgrades.TypeUpgradeItem;
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
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<ItemStack> allStacks = new ArrayList<>();
        Map<Item, TypeUpgradeItem> upgradeMap = new HashMap<>(); // <-- use Item as key

        // Items allowed by type upgrades
        for (TypeUpgradeItem upgrade : ModItems.ALL_TYPE_UPGRADES) {
            TagKey<Item> allowedTag = upgrade.getAllowedBlocks();
            for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(allowedTag)) {
                Item item = holder.value();
                ItemStack stack = new ItemStack(item);
                allStacks.add(stack);
                upgradeMap.put(item, upgrade); // <-- store Item not ItemStack
            }
        }

        // Items in the misc tag (no upgrade required)
        for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(ModTags.BLOCK_GEN_MISC)) {
            Item item = holder.value();
            ItemStack stack = new ItemStack(item);
            allStacks.add(stack);
            upgradeMap.put(item, null); // <-- store Item not ItemStack
        }

        // Split into sublists of 60
        int groupSize = 60;
        for (int i = 0; i < allStacks.size(); i += groupSize) {
            List<ItemStack> sublist = allStacks.subList(i, Math.min(i + groupSize, allStacks.size()));
            Map<Item, TypeUpgradeItem> subMap = new HashMap<>();
            for (ItemStack stack : sublist) {
                subMap.put(stack.getItem(), upgradeMap.get(stack.getItem())); // <-- use Item key
            }

            registration.addRecipes(
                    new RecipeType<>(BlockGeneratorsJEICategory.UID, BlockGeneratorsJEICategory.Wrapper.class),
                    List.of(new BlockGeneratorsJEICategory.Wrapper(sublist, subMap))
            );
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BLOCK_GENERATOR.get()),
                new RecipeType<>(BlockGeneratorsJEICategory.UID, BlockGeneratorsJEICategory.Wrapper.class));
    }
}
