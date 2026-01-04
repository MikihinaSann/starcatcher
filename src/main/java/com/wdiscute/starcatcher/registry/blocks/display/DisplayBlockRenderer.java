package com.wdiscute.starcatcher.registry.blocks.display;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class DisplayBlockRenderer implements BlockEntityRenderer<DisplayBlockEntity>
{
    private final DisplayBookModel bookModel;

    public static final Material BOOK_LOCATION = new Material(
            TextureAtlas.LOCATION_BLOCKS, ResourceLocation.withDefaultNamespace("entity/enchanting_table_book")
    );

    public DisplayBlockRenderer(BlockEntityRendererProvider.Context context)
    {
        this.bookModel = new DisplayBookModel(context.bakeLayer(DisplayBookModel.LAYER_LOCATION));
    }

    public void render(DisplayBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay)
    {
        BlockState blockstate = blockEntity.getBlockState();
        if (blockstate.getValue(DisplayBlock.HAS_BOOK))
        {
            poseStack.pushPose();

            float ticks = (float) blockEntity.time + partialTick;



            if(blockEntity.open == 0)
            {
                poseStack.translate(0.5F, 0.95F, 0.5F);
            }
            else
            {
                poseStack.translate(0.5F, 1.1F, 0.5F);
                //float up and down
                poseStack.translate(0.0F, 0.1F + Mth.sin(ticks / 10 * 0.6F) * 0.03F, 0.0F);

                double x = Math.cos(blockEntity.rot);
                double y = Math.sin(blockEntity.rot);

                poseStack.translate(x / 3, 0f, y / 3);

            }






            System.out.println(blockEntity.open);

            float rotDiff = blockEntity.rot - blockEntity.oRot;

            while (rotDiff >= (float) Math.PI) rotDiff -= (float) (Math.PI * 2);
            while (rotDiff < (float) -Math.PI) rotDiff += (float) (Math.PI * 2);

            float f2 = blockEntity.oRot + rotDiff * partialTick;
            poseStack.mulPose(Axis.YP.rotation(-f2));

            if(blockEntity.open == 0)
            {
                poseStack.mulPose(Axis.ZP.rotationDegrees(00.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            }
            else
            {
                poseStack.mulPose(Axis.ZP.rotationDegrees(30.0F));
            }




            float f3 = Mth.lerp(partialTick, blockEntity.oFlip, blockEntity.flip);
            float f4 = Mth.frac(f3 + 0.25F) * 1.6F - 0.3F;
            float f5 = Mth.frac(f3 + 0.75F) * 1.6F - 0.3F;
            float f6 = Mth.lerp(partialTick, blockEntity.oOpen, blockEntity.open);

            this.bookModel.setupAnim(ticks, Mth.clamp(f4, 0.0F, 1.0F), Mth.clamp(f5, 0.0F, 1.0F), f6);

            VertexConsumer vertexconsumer = buffer.getBuffer(bookModel.renderType(Starcatcher.rl("textures/entity/book.png")));
            this.bookModel.render(poseStack, vertexconsumer, packedLight, packedOverlay, -1);
            poseStack.popPose();
        }
    }

    @Override
    public AABB getRenderBoundingBox(DisplayBlockEntity blockEntity)
    {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0F, pos.getY() + 1.5F, pos.getZ() + 1.0F);
    }
}
