package mod.omoflop.mbp.data.conditions;

import com.google.gson.JsonElement;
import mod.omoflop.mbp.data.BlockModelPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class Custom extends BlockModelPredicate {
    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state, Identifier renderContext) {
        return false;
    }

    public static Custom parse(JsonElement arg) {
        //return new Custom(new Identifier(arg.getAsString()));
    }
}
