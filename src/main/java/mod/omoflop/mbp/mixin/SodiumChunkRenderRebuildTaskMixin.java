package mod.omoflop.mbp.mixin;

import com.google.common.base.Optional;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.tasks.ChunkRenderRebuildTask;
import mod.omoflop.mbp.MBPData;
import mod.omoflop.mbp.accessor.BakedModelManagerAccess;
import mod.omoflop.mbp.common.BlockRendering;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(value = ChunkRenderRebuildTask.class)
public class SodiumChunkRenderRebuildTaskMixin {

    @Unique private BlockPos.Mutable blockPos;

    @Redirect(method = "performBuild", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockModels;getModel(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/model/BakedModel;"))
    public BakedModel getModelRedirect(BlockModels models, BlockState state) {
        BakedModel newModel = BlockRendering.tryModelOverride(models, MinecraftClient.getInstance().world, state,blockPos);
        if (newModel != null)
            return newModel;

        // If failed return original method call
        return models.getModel(state);
    }

    @Redirect(method = "performBuild", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos$Mutable;set(III)Lnet/minecraft/util/math/BlockPos$Mutable;"))
    public BlockPos.Mutable setRedirect(BlockPos.Mutable mutable, int x, int y, int z) {
        blockPos = mutable.set(x,y,z);
        return blockPos;
    }
}