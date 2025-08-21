package com.remag.blockgenerators.item.upgrades;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TypeUpgradeItem extends GenericUpgradeItem {

    private final TagKey<Item> allowedBlocks;

    public TypeUpgradeItem(Properties properties, TagKey<Item> allowedBlocks) {
        super(properties);
        this.allowedBlocks = allowedBlocks;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        // Use the upgrade ITEM's namespace + tag path
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId == null) return;

        String itemNamespace = itemId.getNamespace(); // e.g. "block_generators"
        String tagPath = allowedBlocks.location().getPath(); // e.g. "ores", "stone"

        // Translation key scoped to the upgrade item's mod
        String translationKey = "tooltip." + itemNamespace + ".type_upgrade." + tagPath;

        if (I18n.exists(translationKey)) {
            tooltip.add(Component.translatable(translationKey).withStyle(ChatFormatting.GREEN));
        } else {
            // Humanize the tag path for a prettier fallback
            String prettyName = Arrays.stream(tagPath.split("_"))
                    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                    .collect(Collectors.joining(" "));

            tooltip.add(Component.literal("Allows generation of: " + prettyName).withStyle(ChatFormatting.DARK_GREEN));
        }
    }

    public TagKey<Item> getAllowedBlocks() {
        return allowedBlocks;
    }
}
