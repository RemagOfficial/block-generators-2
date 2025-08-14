package com.remag.blockgenerators.gui.menu;

import com.remag.blockgenerators.block.ModBlocks;
import com.remag.blockgenerators.block.entities.BlockGeneratorBlockEntity;
import com.remag.blockgenerators.gui.TagRestrictedSlot;
import com.remag.blockgenerators.gui.UpgradeSlot;
import com.remag.blockgenerators.item.upgrades.InventoryOutputUpgradeItem;
import com.remag.blockgenerators.item.upgrades.SpeedUpgradeItem;
import com.remag.blockgenerators.item.upgrades.VerticalOffsetUpgradeItem;
import com.remag.blockgenerators.util.ModTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class BlockGeneratorMenu extends AbstractContainerMenu {
    public final BlockGeneratorBlockEntity blockEntity;
    private final ContainerData data;
    private final Inventory playerInventory;
    private final TagKey<Item> slotRestrictionTag = ModTags.BLOCK_GEN_INPUTS;

    public BlockGeneratorMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv,
                (BlockGeneratorBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()),
                new SimpleContainerData(0));
    }

    public BlockGeneratorMenu(int containerId, Inventory inv, BlockGeneratorBlockEntity entity, ContainerData data) {
        super(ModMenuTypes.BLOCK_GENERATOR_MENU.get(), containerId);
        this.blockEntity = entity;
        this.playerInventory = inv;
        this.data = data;

        // Add our single input slot (index 0)
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new TagRestrictedSlot(handler, 0, 79, 22, slotRestrictionTag));

            // --- Upgrade Slots ---
            // Slot 1 - speed upgrade
            this.addSlot(new UpgradeSlot(handler, 1, 61, 56, SpeedUpgradeItem.class));

            // Slot 2 - vertical offset upgrade
            this.addSlot(new UpgradeSlot(handler, 2, 79, 56, VerticalOffsetUpgradeItem.class));

            // Slot 3 - inventory output upgrade
            this.addSlot(new UpgradeSlot(handler, 3, 97, 56, InventoryOutputUpgradeItem.class));
        });

        // Add player inventory + hotbar
        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        addDataSlots(data);
    }

    // --- Inventory layout helpers ---
    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    // --- Shift-click transfer ---
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();

            int containerSlots = this.slots.size() - playerInventory.items.size();

            if (index < containerSlots) {
                // From tile to player
                if (!this.moveItemStackTo(stack, containerSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // From player to tile
                if (stack.is(slotRestrictionTag)) {
                    if (!this.moveItemStackTo(stack, 0, 1, false)) { // main slot only
                        return ItemStack.EMPTY;
                    }
                } else {
                    // Allow putting into upgrade slots (1-3) without restriction
                    if (!this.moveItemStackTo(stack, 1, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), blockEntity.getBlockPos()),
                player, ModBlocks.BLOCK_GENERATOR.get());
    }
}