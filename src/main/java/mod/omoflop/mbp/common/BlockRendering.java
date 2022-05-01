package mod.omoflop.mbp.common;

import mod.omoflop.mbp.MBPData;
import mod.omoflop.mbp.accessor.BakedModelManagerAccess;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import java.util.Optional;

public class BlockRendering {

    public static BakedModel tryModelOverride(BlockModels models, BlockRenderView world, BlockState state, BlockPos pos, boolean isItem) {
        BlockRenderType blockRenderType = state.getRenderType();
        if (blockRenderType == BlockRenderType.MODEL) {
            Optional<Identifier> override = MBPData.meetsPredicate(world, pos, state, isItem);
            if (override.isPresent()) {
                BakedModel model;
                BakedModelManagerAccess manager = ((BakedModelManagerAccess) models.getModelManager());
                model = manager.reallyGetModel(override.get());
                if (model == models.getModelManager().getMissingModel()) {
                    model = models.getModelManager().getModel(new ModelIdentifier(override.get().toString()));
                }
                return model;
            }
        }
        return null;
    }

}
