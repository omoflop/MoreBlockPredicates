package mod.omoflop.mbp.data.logic;

import com.google.gson.JsonElement;
import mod.omoflop.mbp.data.BlockModelPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class Not extends BlockModelPredicate {

    final And condition;

    public Not(And condition) {
        this.condition = condition;
    }

    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state) {
        return !condition.meetsCondition(world, pos, state);
    }

    public static Not parse(JsonElement arg) {
        return new Not(And.parse(arg));
    }
}
