package com.remag.blockgenerators.gui;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class UpgradeSlot extends SlotItemHandler {
    private final Class<?> allowedItemClass;

    public UpgradeSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Class<?> allowedItemClass) {
        super(itemHandler, index, xPosition, yPosition);
        this.allowedItemClass = allowedItemClass;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return allowedItemClass.isInstance(stack.getItem());
    }
}
