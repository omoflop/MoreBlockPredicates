package mod.omoflop.vbm.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.omoflop.vbm.VBMResourceManager;
import mod.omoflop.vbm.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.util.math.BlockPos;
@Mixin(BlockModels.class)
public class BlockModelsMixin {
    
    @Final
    @Shadow
    private Map<BlockState, BakedModel> models;
    
    @Final
    @Shadow
    private BakedModelManager modelManager;
    
    @Inject(
        method = "getModel(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/model/BakedModel",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private void getModelMixin(BlockState blockState, CallbackInfoReturnable<BakedModel> ci) {
        //MinecraftClient client = MinecraftClient.getInstance();
        //BlockPos pos = client.player.getBlockPos();
        //BlockState b = VBMResourceManager.executeCondition(Utils.getBlockIdentifier(blockState.getBlock()), pos);
        //ci.setReturnValue(models.get(b));
    }
}