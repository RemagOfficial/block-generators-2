package com.remag.blockgenerators.compat;

import com.remag.blockgenerators.BlockGenerators;
import com.remag.blockgenerators.block.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

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
        // Layout the items in a 3x5 grid
        int columns = 10;
        int rows = 6;
        int startX = 1; // x-offset for the grid
        int startY = 1;  // y-offset for the grid
        int slotSize = 18;

        List<ItemStack> items = recipe.outputs;
        for (int i = 0; i < items.size(); i++) {
            int col = i % columns;
            int row = i / columns;

            int x = startX + col * slotSize;
            int y = startY + row * slotSize;

            builder.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                    .addItemStack(items.get(i));
        }
    }

    public static class Wrapper {
        public final List<ItemStack> outputs;
        public Wrapper(List<ItemStack> outputs) {
            this.outputs = new ArrayList<>(outputs);
        }
    }
}
