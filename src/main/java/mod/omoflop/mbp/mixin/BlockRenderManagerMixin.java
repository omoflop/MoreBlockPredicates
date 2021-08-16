package mod.omoflop.mbp.mixin;

import com.google.common.base.Optional;
import mod.omoflop.mbp.MBPData;
import mod.omoflop.mbp.MBPMod;
import mod.omoflop.mbp.accessor.BakedModelManagerAccess;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Identifier;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Random;

@Mixin(value = BlockRenderManager.class, priority = 2000)
public abstract class BlockRenderManagerMixin {

    @Shadow @Final private BlockModelRenderer blockModelRenderer;
    @Shadow @Final private BlockModels models;

    @Inject(method = "renderBlock", at = @At("HEAD"), cancellable = true)
    public void renderBlock(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrix, VertexConsumer vertexConsumer, boolean cull, Random random, CallbackInfoReturnable<Boolean> cir) {
        BlockRenderType blockRenderType = state.getRenderType();
        if (blockRenderType == BlockRenderType.MODEL) {
            Optional<Identifier> override = MBPData.meetsPredicate(world, pos, state);
            if (override.isPresent()) {
                BakedModelManagerAccess manager = ((BakedModelManagerAccess) this.models.getModelManager());
                BakedModel model = manager.reallyGetModel(override.get());
                if (model == this.models.getModelManager().getMissingModel()) {
                    model = this.models.getModelManager().getModel(new ModelIdentifier(override.get().toString()));
                }
                cir.setReturnValue(this.blockModelRenderer.render(world, model, state, pos, matrix, vertexConsumer, cull, random, state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV));
            }
        }
    }

}
