package mod.omoflop.mbp;

import mod.omoflop.mbp.data.logic.When;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class MBPData {
    public static final HashMap<Block, List<When>> PREDICATES = new HashMap<>();

    public static Optional<Identifier> meetsPredicate(BlockView world, BlockPos pos, BlockState state) {
        if (PREDICATES.containsKey(state.getBlock())) {
            for (When when : PREDICATES.get(state.getBlock())) {
                if (when.meetsCondition(world, pos, state)) {
                    long seed = MathHelper.hashCode(pos);

                    return Optional.of(when.getModel(seed));
                }
            }
        }
        
        return Optional.empty();
    }

}
