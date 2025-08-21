package com.remag.blockgenerators.block.entities;

import com.remag.blockgenerators.Config;
import com.remag.blockgenerators.gui.menu.BlockGeneratorMenu;
import com.remag.blockgenerators.item.upgrades.InventoryOutputUpgradeItem;
import com.remag.blockgenerators.item.upgrades.SpeedUpgradeItem;
import com.remag.blockgenerators.item.upgrades.TypeUpgradeItem;
import com.remag.blockgenerators.item.upgrades.VerticalOffsetUpgradeItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class BlockGeneratorBlockEntity extends BlockEntity implements MenuProvider {

    private static final int SLOT_COUNT = 5;
    private ItemStack cachedRenderStack = ItemStack.EMPTY;

    private final ItemStackHandler itemHandler = new ItemStackHandler(SLOT_COUNT) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();

            // Update render stack if the input slot changed
            if (slot == 0) {
                cachedRenderStack = getStackInSlot(0).copy();
            }

            // Notify client of change
            if (!level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == 0) return stack.getItem() instanceof BlockItem;
            if (slot == 1) return stack.getItem() instanceof SpeedUpgradeItem;
            if (slot == 2) return stack.getItem() instanceof VerticalOffsetUpgradeItem;
            if (slot == 3) return stack.getItem() instanceof InventoryOutputUpgradeItem;
            if (slot == 4) return stack.getItem() instanceof TypeUpgradeItem;
            return false;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            super.deserializeNBT(nbt);
            if (getSlots() != SLOT_COUNT) setSize(SLOT_COUNT);
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private int placeCooldown = 0;

    protected final ContainerData data = new ContainerData() {
        @Override public int get(int index) { return 0; }
        @Override public void set(int index, int value) {}
        @Override public int getCount() { return 0; }
    };

    public BlockGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLOCK_GENERATOR.get(), pos, state);
    }

    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);

        // Ensure the render stack is initialized on load
        cachedRenderStack = itemHandler.getStackInSlot(0).copy();
        if (!level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public ItemStack getRenderStack() {
        return cachedRenderStack;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.block_generators.block_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new BlockGeneratorMenu(containerId, playerInventory, this, data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("PlaceCooldown", placeCooldown);
        tag.put("RenderStack", cachedRenderStack.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        placeCooldown = tag.getInt("PlaceCooldown");

        if (tag.contains("RenderStack")) {
            cachedRenderStack = ItemStack.of(tag.getCompound("RenderStack"));
        } else {
            cachedRenderStack = itemHandler.getStackInSlot(0).copy();
        }
    }

    // --- Networking for client sync ---
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("RenderStack", cachedRenderStack.serializeNBT());
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        if (tag.contains("RenderStack")) {
            cachedRenderStack = ItemStack.of(tag.getCompound("RenderStack"));
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt);
        return ClientboundBlockEntityDataPacket.create(this, be -> nbt);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        load(pkt.getTag());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BlockGeneratorBlockEntity be) {
        if (level.isClientSide) return;

        if (be.placeCooldown > 0) {
            be.placeCooldown--;
            return;
        }

        be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            ItemStack stack = handler.getStackInSlot(0); // block slot
            ItemStack speedUpgrade = handler.getStackInSlot(1);
            ItemStack verticalOffset = handler.getStackInSlot(2);
            ItemStack inventoryOutput = handler.getStackInSlot(3);
            ItemStack typeUpgrade = handler.getStackInSlot(4);

            if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem blockItem)) {
                return;
            }

            // --- Check type upgrade ---
            if (!typeUpgrade.isEmpty() && typeUpgrade.getItem() instanceof TypeUpgradeItem upgrade) {
                if (!stack.getItem().builtInRegistryHolder().is(upgrade.getAllowedBlocks())) {
                    return; // block not allowed by upgrade
                }
            }

            // --- Determine vertical offset ---
            int yOffset = 1; // default above
            if (!verticalOffset.isEmpty() && verticalOffset.getItem() instanceof VerticalOffsetUpgradeItem) {
                yOffset += verticalOffset.getCount(); // add gap based on count
            }
            BlockPos above = pos.above(yOffset);

            AtomicBoolean actionPerformed = new AtomicBoolean(false); // track if we did something

            // --- Inventory output upgrade ---
            if (!inventoryOutput.isEmpty() && inventoryOutput.getItem() instanceof InventoryOutputUpgradeItem) {
                BlockEntity targetBE = level.getBlockEntity(above);

                if (targetBE != null) {
                    targetBE.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).ifPresent(targetHandler -> {
                        ItemStack singleBlock = stack.copy();
                        singleBlock.setCount(1);

                        ItemStack remainder = ItemHandlerHelper.insertItem(targetHandler, singleBlock, false);

                        if (remainder.isEmpty()) {
                            // Successfully inserted
                            be.setChanged();
                            actionPerformed.set(true);
                        }
                    });
                }
            }
            // --- Normal block placement ---
            else if (level.isEmptyBlock(above)) {
                BlockState placeState = blockItem.getBlock().defaultBlockState();
                level.setBlock(above, placeState, 3);
                actionPerformed.set(true);
            }

            // --- Apply cooldown only if we actually placed/inserted ---
            if (actionPerformed.get()) {
                int upgradeCount = speedUpgrade.getCount();
                int baseCooldown = Config.blockGeneratorGenerateTime;
                double multiplier = Math.pow(0.5, upgradeCount); // halves per upgrade
                be.placeCooldown = Math.max(1, (int)(baseCooldown * multiplier));
                be.setChanged();
            }
        });
    }
}