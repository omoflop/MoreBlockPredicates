package mod.omoflop.mbp.mixin;

import mod.omoflop.mbp.common.BlockRendering;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockRenderManager.class, priority = 2000)
public abstract class BlockRenderManagerMixin {

    @Shadow @Final private BlockModelRenderer blockModelRenderer;
    @Shadow @Final private BlockModels models;

    @Inject(at = @At("HEAD"), method = "renderBlock", cancellable = true)
    public void renderBlock(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random, CallbackInfo ci) {
        BlockRenderType blockRenderType = state.getRenderType();
        if (blockRenderType == BlockRenderType.MODEL) {
            BakedModel newModel = BlockRendering.tryModelOverride(this.models, world, state, pos, false);
            if (newModel != null) {
                this.blockModelRenderer.render(world, newModel, state, pos, matrices, vertexConsumer, cull, random, state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);
                ci.cancel();
            }
        }
    }

}
