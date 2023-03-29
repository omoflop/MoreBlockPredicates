package mod.omoflop.mbp.mixin;

import mod.omoflop.mbp.MBPData;
import mod.omoflop.mbp.accessor.BakedModelManagerAccess;
import mod.omoflop.mbp.client.MBPClient;
import mod.omoflop.mbp.common.BlockRendering;
import mod.omoflop.mbp.common.ContextIdentifiers;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = BlockRenderManager.class, priority = 2000)
public abstract class BlockRenderManagerMixin {

    @Shadow @Final private BlockModelRenderer blockModelRenderer;
    @Shadow @Final private BlockModels models;
    @Shadow @Final private BlockColors blockColors;

    @Inject(at = @At("HEAD"), method = "renderBlock", cancellable = true)
    public void renderBlock(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random, CallbackInfo ci) {
        BlockRenderType blockRenderType = state.getRenderType();
        if (blockRenderType == BlockRenderType.MODEL) {
            BakedModel newModel = BlockRendering.tryModelOverride(this.models, world, state, pos, ContextIdentifiers.MISC);
            if (newModel != null) {
                this.blockModelRenderer.render(world, newModel, state, pos, matrices, vertexConsumer, cull, random, state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);
                ci.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "renderBlockAsEntity", cancellable = true)
    public void renderBlockAsEntity(BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (state.getRenderType() == BlockRenderType.MODEL) {
            Optional<Identifier> id = MBPData.meetsPredicate(MinecraftClient.getInstance().world, BlockPos.ORIGIN, state, ContextIdentifiers.ENTITY);
            if (id.isEmpty()) return;

            BakedModel bakedModel = ((BakedModelManagerAccess) this.models.getModelManager()).reallyGetModel(id.get());
            int i = this.blockColors.getColor(state, null, null, 0);
            float f = (float) (i >> 16 & 0xFF) / 255.0F;
            float g = (float) (i >> 8 & 0xFF) / 255.0F;
            float h = (float) (i & 0xFF) / 255.0F;
            this.blockModelRenderer
                    .render(
                            matrices.peek(),
                            vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state, false)),
                            state,
                            bakedModel,
                            f,
                            g,
                            h,
                            light,
                            overlay
                    );
            ci.cancel();
        }
    }

}
