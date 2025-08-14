package com.remag.blockgenerators.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.remag.blockgenerators.block.entities.BlockGeneratorBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class BlockGeneratorRenderer implements BlockEntityRenderer<BlockGeneratorBlockEntity> {

    private final ItemRenderer itemRenderer;

    public BlockGeneratorRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(BlockGeneratorBlockEntity be, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        // ItemStack stack = be.getInventory().getStackInSlot(0);

        ItemStack stack = be.getRenderStack();

        if (!stack.isEmpty()) {
            poseStack.pushPose();

            // Center the item in front of the block
            Direction facing = be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            float rotation = -facing.toYRot();
            poseStack.translate(0.5, 0.5, 0.5); // center of block
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation)); // rotate to face front
            poseStack.translate(0.0, 0.0, -0.4375); // move slightly forward out of block face

            // Scale down a bit
            float itemScale = 0.75f;
            poseStack.scale(itemScale, itemScale, itemScale);

            // Render as if in an item frame
            int fullBright = 0xF000F0;
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, fullBright, combinedOverlay, poseStack, buffer, be.getLevel(), 0);

            poseStack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(BlockGeneratorBlockEntity be) {
        return true;
    }
}
