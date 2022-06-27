package mod.omoflop.mbp.mixin;

import io.wispforest.worldmesher.DynamicRenderInfo;
import io.wispforest.worldmesher.WorldMesh;
import io.wispforest.worldmesher.renderers.WorldMesherBlockModelRenderer;
import io.wispforest.worldmesher.renderers.WorldMesherFluidRenderer;
import mod.omoflop.mbp.common.BlockRendering;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.WorldMesherRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mixin(WorldMesh.class)
public abstract class WorldMeshMixin {
    @Shadow @Final private World world;

    @Shadow protected abstract VertexConsumer getOrCreateBuffer(RenderLayer layer);

    @Shadow @Final private boolean cull;

    @Shadow public abstract void render(MatrixStack matrices);
    @Unique private BakedModel overrideModel;

    @Inject(method = "build", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockRenderManager;getModel(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/model/BakedModel;", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void preGetModel(CallbackInfo ci, MinecraftClient client, BlockRenderManager blockRenderManager, WorldMesherBlockModelRenderer blockRenderer, WorldMesherFluidRenderer fluidRenderer, MatrixStack matrices, WorldMesherRenderContext renderContext, Random random, List possess, int blocksToBuild, DynamicRenderInfo.Mutable tempRenderInfo, CompletableFuture entitiesFuture, int i, BlockPos pos, BlockPos renderPos, BlockState state, RenderLayer renderLayer) {
        overrideModel = BlockRendering.tryModelOverride(blockRenderManager.getModels(), world, state, pos, false);
    }

    @Redirect(method = "build", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockRenderManager;getModel(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/model/BakedModel;"))
    private BakedModel getModel(BlockRenderManager models, BlockState state) {
        if (overrideModel != null)
            return overrideModel;
        return models.getModel(state);
    }

    @ModifyVariable(
            method = "build",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockRenderManager;getModel(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/model/BakedModel;", shift = At.Shift.AFTER),
            index = 6)
    private WorldMesherRenderContext a(WorldMesherRenderContext value) {
        return overrideModel != null ? null : value;
    }
}
