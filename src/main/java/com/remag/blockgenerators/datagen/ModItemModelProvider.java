package com.remag.blockgenerators.datagen;

import com.remag.blockgenerators.BlockGenerators;
import com.remag.blockgenerators.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BlockGenerators.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        simpleItem(ModItems.ORE_TYPE_UPGRADE);
        simpleItem(ModItems.COBBLESTONE_TYPE_UPGRADE);
        simpleItem(ModItems.STONE_TYPE_UPGRADE);

        simpleItem(ModItems.INVENTORY_OUTPUT_UPGRADE);
        simpleItem(ModItems.SPEED_UPGRADE);
        simpleItem(ModItems.VERTICAL_OFFSET_UPGRADE);
        simpleItem(ModItems.REDSTONE_INFUSED_IRON);
        simpleItem(ModItems.UPGRADE_BASE);
    }

    private <T extends Item> ItemModelBuilder simpleItem(RegistryObject<T> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.tryParse("item/generated"))
                .texture("layer0",
                        ResourceLocation.fromNamespaceAndPath(BlockGenerators.MODID, "item/" + item.getId().getPath()));
    }
}
