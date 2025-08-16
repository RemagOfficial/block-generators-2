package com.remag.blockgenerators.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.remag.blockgenerators.block.entities.BlockGeneratorBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

        ItemStack stack = be.getRenderStack();

        if (!stack.isEmpty()) {
            Level level = be.getLevel();
            BlockPos pos = be.getBlockPos();

            for (Direction side : new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}) {
                // Skip if there's a full block on this side
                if (level != null && level.getBlockState(pos.relative(side)).isSolidRender(level, pos.relative(side))) {
                    continue;
                }

                poseStack.pushPose();

                // Center of block
                poseStack.translate(0.5, 0.5, 0.5);

                // Rotate for current side
                float rotation = -side.toYRot();
                poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

                // Move outwards from the face
                poseStack.translate(0.0, 0.0, 0.4375);

                // Scale down
                float itemScale = 0.75f;
                poseStack.scale(itemScale, itemScale, itemScale);

                // Render as fullbright
                int fullBright = 0xF000F0;
                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, fullBright, combinedOverlay,
                        poseStack, buffer, level, 0);

                poseStack.popPose();
            }
        }
    }

    @Override
    public boolean shouldRenderOffScreen(BlockGeneratorBlockEntity be) {
        return true;
    }
}
