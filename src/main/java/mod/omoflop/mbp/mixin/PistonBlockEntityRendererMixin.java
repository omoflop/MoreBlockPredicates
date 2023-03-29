package mod.omoflop.mbp.mixin;

import mod.omoflop.mbp.MBPData;
import mod.omoflop.mbp.accessor.BakedModelManagerAccess;
import mod.omoflop.mbp.common.ContextIdentifiers;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.PistonBlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PistonBlockEntityRenderer.class)
public class PistonBlockEntityRendererMixin {

    @Unique private BlockPos tempBlockPos;
    @Unique private World tempWorld;

    @Inject(at = @At("HEAD"), method = "renderModel")
    public void renderModel(BlockPos pos, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, boolean cull, int overlay, CallbackInfo ci) {
        this.tempBlockPos = pos;
        this.tempWorld = world;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockRenderManager;getModel(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/model/BakedModel;"), method = "renderModel")
    public BakedModel renderModel(BlockRenderManager instance, BlockState state) {
        Optional<Identifier> identifier = MBPData.meetsPredicate(tempWorld, tempBlockPos, state, ContextIdentifiers.PISTON_PUSHING);

        MinecraftClient client = MinecraftClient.getInstance();
        if (identifier.isPresent()) {
            BakedModelManagerAccess access = BakedModelManagerAccess.of(client.getBakedModelManager());
            return access.reallyGetModel(identifier.get());
        } else {
            return client.getBlockRenderManager().getModels().getModel(state);
        }
    }

}
