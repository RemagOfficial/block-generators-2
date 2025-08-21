package com.remag.blockgenerators.gui.menu;

import com.remag.blockgenerators.block.ModBlocks;
import com.remag.blockgenerators.block.entities.BlockGeneratorBlockEntity;
import com.remag.blockgenerators.gui.UpgradeSlot;
import com.remag.blockgenerators.item.upgrades.InventoryOutputUpgradeItem;
import com.remag.blockgenerators.item.upgrades.SpeedUpgradeItem;
import com.remag.blockgenerators.item.upgrades.TypeUpgradeItem;
import com.remag.blockgenerators.item.upgrades.VerticalOffsetUpgradeItem;
import com.remag.blockgenerators.util.ModTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class BlockGeneratorMenu extends AbstractContainerMenu {
    public final BlockGeneratorBlockEntity blockEntity;
    private final ContainerData data;
    private final Inventory playerInventory;

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
            this.addSlot(new SlotItemHandler(handler, 0, 79, 22) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    ItemStack typeUpgrade = handler.getStackInSlot(4); // New type upgrade slot
                    if (!typeUpgrade.isEmpty() && typeUpgrade.getItem() instanceof TypeUpgradeItem upgrade) {
                        return stack.getItem().builtInRegistryHolder().is(upgrade.getAllowedBlocks());
                    }
                    // fallback to default tag restriction
                    return stack.is(ModTags.BLOCK_GEN_MISC);
                }
            });
            // this.addSlot(new TagRestrictedSlot(handler, 0, 79, 22, slotRestrictionTag));

            // --- Upgrade Slots ---
            // Slot 1 - speed upgrade
            this.addSlot(new UpgradeSlot(handler, 1, 53, 56, SpeedUpgradeItem.class));

            // Slot 2 - vertical offset upgrade
            this.addSlot(new UpgradeSlot(handler, 2, 71, 56, VerticalOffsetUpgradeItem.class));

            // Slot 3 - inventory output upgrade
            this.addSlot(new UpgradeSlot(handler, 3, 89, 56, InventoryOutputUpgradeItem.class));

            // Slot 4 - type upgrade slot
            this.addSlot(new UpgradeSlot(handler, 4, 107, 56, TypeUpgradeItem.class));
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
                if (!stack.is(ModTags.BLOCK_GEN_MISC) || stack.getItem() instanceof TypeUpgradeItem) {
                    // Try putting into any upgrade slot (1-4)
                    if (!this.moveItemStackTo(stack, 1, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    // Input slot (0)
                    if (!this.moveItemStackTo(stack, 0, 1, false)) {
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