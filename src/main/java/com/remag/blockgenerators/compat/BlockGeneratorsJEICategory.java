package com.remag.blockgenerators.compat;

import com.remag.blockgenerators.BlockGenerators;
import com.remag.blockgenerators.block.ModBlocks;
import com.remag.blockgenerators.item.upgrades.TypeUpgradeItem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class BlockGeneratorsJEICategory implements IRecipeCategory<BlockGeneratorsJEICategory.Wrapper> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(BlockGenerators.MODID, "block_generator");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlockGenerators.MODID, "textures/gui/block_generator_jei.png");

    private final IDrawable background;
    private final IDrawable icon;

    public BlockGeneratorsJEICategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 180, 108);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BLOCK_GENERATOR.get()));
    }

    @Override
    public RecipeType<Wrapper> getRecipeType() {
        return new RecipeType<>(UID, Wrapper.class);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.block_generators.block_generator");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public int getWidth() {
        return background.getWidth();
    }

    @Override
    public int getHeight() {
        return background.getHeight();
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Wrapper recipe, IFocusGroup focuses) {
        int columns = 10;
        int rows = 6;
        int startX = 1;
        int startY = 1;
        int slotSize = 18;

        List<ItemStack> items = recipe.items; // use your Wrapper's item list
        for (int i = 0; i < items.size(); i++) {
            int col = i % columns;
            int row = i / columns;

            int x = startX + col * slotSize;
            int y = startY + row * slotSize;

            ItemStack stack = items.get(i); // current item for this slot

            builder.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                    .addItemStack(stack)
                    .addTooltipCallback((hoveredStack, tooltip) -> {
                        // Look up the upgrade for this specific item
                        TypeUpgradeItem upgrade = recipe.upgrades.get(stack.getItem());
                        if (upgrade != null) {
                            Component upgradeName = upgrade.getDefaultInstance().getDisplayName();
                            tooltip.add(Component.translatable("jei.block_generators.requires_upgrade", upgradeName)
                                    .withStyle(ChatFormatting.YELLOW));
                        } else {
                            tooltip.add(Component.translatable("jei.block_generators.misc_block")
                                    .withStyle(ChatFormatting.GRAY));
                        }
                    });
        }
    }

    public static class Wrapper {
        public final List<ItemStack> items;
        public final Map<Item, TypeUpgradeItem> upgrades; // store upgrade per item

        public Wrapper(List<ItemStack> items, Map<Item, TypeUpgradeItem> upgrades) {
            this.items = items;
            this.upgrades = upgrades;
        }
    }
}
