package com.remag.blockgenerators.util;

import com.remag.blockgenerators.BlockGenerators;
import com.remag.blockgenerators.block.ModBlocks;
import com.remag.blockgenerators.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BlockGenerators.MODID);

    public static final RegistryObject<CreativeModeTab> BLOCK_GENERATORS_TAB = CREATIVE_MODE_TABS.register("block_generators_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.BLOCK_GENERATOR_BLOCK_ITEM.get()))
                    .title(Component.translatable("creativeTab.block_generators_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.BLOCK_GENERATOR_BLOCK_ITEM.get());
                        pOutput.accept(ModItems.GENERATOR_BASE_BLOCK_ITEM.get());
                        pOutput.accept(ModItems.UPGRADE_BASE.get());
                        pOutput.accept(ModItems.SPEED_UPGRADE.get());
                        pOutput.accept(ModItems.VERTICAL_OFFSET_UPGRADE.get());
                        pOutput.accept(ModItems.INVENTORY_OUTPUT_UPGRADE.get());
                        pOutput.accept(ModItems.REDSTONE_INFUSED_IRON.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
