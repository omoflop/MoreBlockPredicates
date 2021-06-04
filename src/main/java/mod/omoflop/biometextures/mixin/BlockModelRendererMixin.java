package mod.omoflop.biometextures.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

@Mixin(BlockModelRenderer.class)
public class BlockModelRendererMixin {

    // this does like nothing i think idk 
    @Inject(at = @At(value = "HEAD"), method="render")    
    public void render(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrix, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay, CallbackInfoReturnable<Boolean> callback) {
        //boolean bl = MinecraftClient.isAmbientOcclusionEnabled() && state.getLuminance() == 0 && model.useAmbientOcclusion();
        //Vec3d vec3d = state.getModelOffset(world, pos);
        //matrix.translate(vec3d.x, vec3d.y, vec3d.z);
        System.out.println("WHat");
        callback.setReturnValue(false);
        callback.cancel();
     }
}
