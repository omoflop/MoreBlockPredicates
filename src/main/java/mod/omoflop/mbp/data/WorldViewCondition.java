package mod.omoflop.mbp.data;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public interface WorldViewCondition {
    boolean meetsCondition(BlockView world, BlockPos pos, BlockState state);
}
