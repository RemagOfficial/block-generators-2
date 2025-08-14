package com.remag.blockgenerators.gui;

import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TagRestrictedSlot extends SlotItemHandler {
    private final TagKey<Item> allowedTag;

    public TagRestrictedSlot(IItemHandler itemHandler, int slotIndex, int xPosition, int yPosition, TagKey<Item> allowedTag) {
        super(itemHandler, slotIndex, xPosition, yPosition);
        this.allowedTag = allowedTag;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(allowedTag);
    }
}
