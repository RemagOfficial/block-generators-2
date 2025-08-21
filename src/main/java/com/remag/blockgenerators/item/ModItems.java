package com.remag.blockgenerators.item;

import com.remag.blockgenerators.BlockGenerators;
import com.remag.blockgenerators.block.ModBlocks;
import com.remag.blockgenerators.item.upgrades.InventoryOutputUpgradeItem;
import com.remag.blockgenerators.item.upgrades.SpeedUpgradeItem;
import com.remag.blockgenerators.item.upgrades.TypeUpgradeItem;
import com.remag.blockgenerators.item.upgrades.VerticalOffsetUpgradeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BlockGenerators.MODID);

    // Upgrades

    public static final RegistryObject<Item> SPEED_UPGRADE = ITEMS.register("speed_upgrade",
            () -> new SpeedUpgradeItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> VERTICAL_OFFSET_UPGRADE = ITEMS.register("vertical_offset_upgrade",
            () -> new VerticalOffsetUpgradeItem(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> INVENTORY_OUTPUT_UPGRADE = ITEMS.register("inventory_output_upgrade",
            () -> new InventoryOutputUpgradeItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> REDSTONE_INFUSED_IRON = ITEMS.register("redstone_infused_iron",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> UPGRADE_BASE = ITEMS.register("upgrade_base",
            () -> new Item(new Item.Properties()));

    // Type Upgrades

    public static final RegistryObject<TypeUpgradeItem> ORE_TYPE_UPGRADE = ITEMS.register("ore_type_upgrade",
            () -> new TypeUpgradeItem(new Item.Properties().stacksTo(1), Tags.Items.ORES));

    public static final RegistryObject<TypeUpgradeItem> STONE_TYPE_UPGRADE = ITEMS.register("stone_type_upgrade",
            () -> new TypeUpgradeItem(new Item.Properties().stacksTo(1), Tags.Items.STONE));

    public static final RegistryObject<TypeUpgradeItem> COBBLESTONE_TYPE_UPGRADE = ITEMS.register("cobblestone_type_upgrade",
            () -> new TypeUpgradeItem(new Item.Properties().stacksTo(1), Tags.Items.COBBLESTONE));

    public static final List<TypeUpgradeItem> ALL_TYPE_UPGRADES = new ArrayList<>();

    // Block Items

    public static final RegistryObject<Item> BLOCK_GENERATOR_BLOCK_ITEM = ITEMS.register("block_generator",
            () -> new BlockItem(ModBlocks.BLOCK_GENERATOR.get(), new Item.Properties()));

    public static final RegistryObject<Item> GENERATOR_BASE_BLOCK_ITEM = ITEMS.register("generator_base",
            () -> new BlockItem(ModBlocks.GENERATOR_BASE.get(), new Item.Properties()));

    public static void registerAllUpgrades() {
        ALL_TYPE_UPGRADES.add(ORE_TYPE_UPGRADE.get());
        ALL_TYPE_UPGRADES.add(STONE_TYPE_UPGRADE.get());
        ALL_TYPE_UPGRADES.add(COBBLESTONE_TYPE_UPGRADE.get());
    }
}
