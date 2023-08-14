package mod.omoflop.mbp.mixin;

import mod.omoflop.mbp.MBPData;
import mod.omoflop.mbp.accessor.BakedModelManagerAccess;
import mod.omoflop.mbp.common.ContextIdentifiers;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.PistonBlockEntityRenderer;
import net.minecraft.client.render.entity.FallingBlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(FallingBlockEntityRenderer.class)
public class FallingBlockEntityRendererMixin {

    @Unique private FallingBlockEntity fallingBlockEntity;

    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/entity/FallingBlockEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    public void render(FallingBlockEntity fallingBlockEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        this.fallingBlockEntity = fallingBlockEntity;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockRenderManager;getModel(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/model/BakedModel;"),method = "render(Lnet/minecraft/entity/FallingBlockEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    public BakedModel render(BlockRenderManager instance, BlockState state) {
        Optional<Identifier> identifier = MBPData.meetsPredicate(fallingBlockEntity.getWorld(), fallingBlockEntity.getBlockPos(), state, ContextIdentifiers.FALLING_BLOCK);

        MinecraftClient client = MinecraftClient.getInstance();
        if (identifier.isPresent()) {
            BakedModelManagerAccess access = BakedModelManagerAccess.of(client.getBakedModelManager());
            return access.reallyGetModel(identifier.get());
        } else {
            return client.getBlockRenderManager().getModels().getModel(state);
        }
    }
}
