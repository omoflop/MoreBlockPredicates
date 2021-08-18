package mod.omoflop.mbp.mixin;

import me.jellysquid.mods.sodium.client.render.pipeline.ChunkBlockRenderPipeline;
import me.jellysquid.mods.sodium.client.render.pipeline.ChunkRenderPipeline;
import mod.omoflop.mbp.common.BlockRendering;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(value = ChunkRenderPipeline.class)
public class ChunkRenderPipelineMixin {

    @Shadow @Final private BlockModels models;
    @Shadow @Final private ChunkBlockRenderPipeline blockRenderer;
    @Shadow @Final private Random random;

    @Inject(at = @At("HEAD"), cancellable = true, method = "renderBlock", remap = false)
    public void renderModel(BlockState state, BlockPos pos, BlockRenderView world, Vec3f offset, BufferBuilder builder, boolean cull, CallbackInfoReturnable<Boolean> cir) {
        BakedModel override = BlockRendering.tryModelOverride(this.models, world, state, pos);
        if (override != null) {
            cir.setReturnValue(this.blockRenderer.renderModel(world, override, state, pos, offset, builder, cull, this.random, state.getRenderingSeed(pos)));
        }
    }

}
