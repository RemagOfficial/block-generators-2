package com.remag.blockgenerators.item.upgrades;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class VerticalOffsetUpgradeItem extends GenericUpgradeItem {
    public VerticalOffsetUpgradeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("item.block_generators.vertical_offset_upgrade.tooltip.line1"));
        tooltip.add(Component.translatable("item.block_generators.vertical_offset_upgrade.tooltip.line2").withStyle(ChatFormatting.GREEN));
    }
}
