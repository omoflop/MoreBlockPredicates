package mod.omoflop.mbp.mixin;

import me.jellysquid.mods.sodium.client.render.chunk.tasks.ChunkRenderRebuildTask;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import mod.omoflop.mbp.common.BlockRendering;
import mod.omoflop.mbp.common.ContextIdentifiers;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(value = ChunkRenderRebuildTask.class)
public class SodiumChunkRenderRebuildTaskMixin {

    @Unique private int x = 0;
    @Unique private int y = 0;
    @Unique private int z = 0;

    @Redirect(method = "performBuild", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockModels;getModel(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/model/BakedModel;"))
    public BakedModel getModelRedirect(BlockModels models, BlockState state) {
        BakedModel newModel = BlockRendering.tryModelOverride(models, MinecraftClient.getInstance().world, state, new BlockPos(x, y, z), ContextIdentifiers.CHUNK_MESH);
        if (newModel != null)
            return newModel;

        // If failed return original method call
        return models.getModel(state);
    }

    @Redirect(method = "performBuild", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/world/WorldSlice;getBlockState(III)Lnet/minecraft/block/BlockState;"))
    public BlockState getBlockStateRedirect(WorldSlice worldSlice, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return worldSlice.getBlockState(x,y,z);
    }
}