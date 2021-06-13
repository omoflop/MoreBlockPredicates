package mod.omoflop.vbm.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.omoflop.vbm.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


@Mixin(ChunkRendererRegion.class)
public abstract class ChunkRendererRegionMixin {

    @Shadow
    public abstract int getIndex(BlockPos pos);

    @Shadow
    @Final
    protected BlockState[] blockStates;

    @Inject(
        method = "getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    public void getBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> ci) {
        BlockState val = hook(pos);
        if (val != null) {
            ci.setReturnValue(val);
        }
        //return this.blockStates[this.getIndex(pos)];
    }

    public BlockState hook(BlockPos pos) {
        BlockState state = blockStates[getIndex(pos)];     
       
        
        MinecraftClient client = MinecraftClient.getInstance();
        World world = client.world;
        if (state.getBlock() == Blocks.GRANITE) {
            if (Utils.getBiomeIdentifier(world.getBiome(pos)).equals(new Identifier("minecraft:beach"))) {
                return (Blocks.ACACIA_FENCE.getDefaultState());
            } else {
                return (Blocks.GRANITE_SLAB.getDefaultState());
            }
        }       

        return null;
    }
}