package mod.omoflop.mbp.mixin;

import mod.omoflop.mbp.MBPData;
import mod.omoflop.mbp.accessor.BakedModelManagerAccess;
import mod.omoflop.mbp.common.ContextIdentifiers;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.BlockMarkerParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(BlockMarkerParticle.class)
public abstract class BlockMarkerParticleMixin extends SpriteBillboardParticle {

    protected BlockMarkerParticleMixin(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);

    }

    @Inject(at = @At(value = "TAIL"), method = "<init>")
    public void init(ClientWorld world, double x, double y, double z, BlockState state, CallbackInfo ci) {
        Optional<Identifier> identifier = MBPData.meetsPredicate(world, new BlockPos((int)x, (int)y, (int)z), state, ContextIdentifiers.MARKER_PARTICLE);

        MinecraftClient client = MinecraftClient.getInstance();
        if (identifier.isPresent()) {
            BakedModelManagerAccess access = BakedModelManagerAccess.of(client.getBakedModelManager());
            setSprite(access.reallyGetModel(identifier.get()).getParticleSprite());
        } else {
            this.setSprite(client.getBlockRenderManager().getModels().getModelParticleSprite(state));
        }
    }
}
