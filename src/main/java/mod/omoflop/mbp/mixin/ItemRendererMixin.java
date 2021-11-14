package mod.omoflop.mbp.mixin;

import com.google.common.base.Optional;
import mod.omoflop.mbp.MBPData;
import mod.omoflop.mbp.accessor.BakedModelManagerAccess;
import mod.omoflop.mbp.client.MBPClient;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow protected abstract void renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices);

    @Shadow @Final private ItemModels models;
    @Unique private World world;
    @Unique private Entity entity;

    @Inject(method = "getModel", at = @At(value = "HEAD"))
    private void getHeldItemModelVariableStealerLol(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
        this.world = world;
        this.entity = entity;
    }


    @Redirect(method = "getModel", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/render/item/ItemModels;getModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/BakedModel;"))
    private BakedModel getHeldItemModelMixin(ItemModels itemModels, ItemStack stack) {
        if (world == null || entity == null) return itemModels.getModel(stack);

        BlockPos targetPos = entity.getBlockPos();
        if (entity instanceof PlayerEntity player) {
            HitResult hit = player.raycast(8, MinecraftClient.getInstance().getTickDelta(), false);
            if (hit instanceof BlockHitResult blockHitResult) {
                targetPos = blockHitResult.getBlockPos().add(blockHitResult.getSide().getVector());
            }
        }

        Optional<Identifier> identifier = MBPData.meetsPredicate(world, targetPos, Block.getBlockFromItem(stack.getItem()).getDefaultState());

        if (identifier.isPresent()) {
            BakedModelManagerAccess access = BakedModelManagerAccess.of(itemModels.getModelManager());
            return access.reallyGetModel(identifier.get());
        }

        world = null;
        entity = null;
        return itemModels.getModel(stack);
    }

    @Redirect(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V"))
    private void renderBakedModelMixin(ItemRenderer itemRenderer, BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices) {
        if (world == null) world = MinecraftClient.getInstance().world;
        if (entity == null) entity = MBPClient.currentEntity;
        if (entity == null) entity = MinecraftClient.getInstance().player;

        BlockPos targetPos = entity.getBlockPos();
        if (entity instanceof PlayerEntity player) {
            HitResult hit = player.raycast(8, MinecraftClient.getInstance().getTickDelta(), false);
            if (hit instanceof BlockHitResult blockHitResult) {
                targetPos = blockHitResult.getBlockPos().add(blockHitResult.getSide().getVector());
            }
        }

        Optional<Identifier> identifier = MBPData.meetsPredicate(world, targetPos, Block.getBlockFromItem(stack.getItem()).getDefaultState());
        if (identifier.isPresent()) {
            BakedModelManagerAccess access = BakedModelManagerAccess.of(models.getModelManager());
            model = access.reallyGetModel(identifier.get());
        }
        this.renderBakedItemModel(model, stack, light, overlay, matrices, vertices);
        MBPClient.currentEntity = null;
    }
}
