package mod.omoflop.mbp.mixin;

import mod.omoflop.mbp.MBPData;
import mod.omoflop.mbp.accessor.BlockRenderManagerAccess;
import mod.omoflop.mbp.client.MBPClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.DisplayEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.DisplayEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisplayEntityRenderer.BlockDisplayEntityRenderer.class)
public class BlockDisplayEntityRendererMixin {
    @Shadow @Final private BlockRenderManager blockRenderManager;

    @Inject(method = "render(Lnet/minecraft/entity/decoration/DisplayEntity$BlockDisplayEntity;Lnet/minecraft/entity/decoration/DisplayEntity$BlockDisplayEntity$Data;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V", at = @At("HEAD"))
    public void getContext(DisplayEntity.BlockDisplayEntity blockDisplayEntity, DisplayEntity.BlockDisplayEntity.Data data, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, float f, CallbackInfo ci) {
        BlockRenderManagerAccess.of(blockRenderManager).moreBlockPredicates$setContextEntity(blockDisplayEntity);
    }
}
