package mod.omoflop.mbp.data.conditions;

import com.google.gson.JsonElement;
import mod.omoflop.mbp.data.BlockModelPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class IsContext extends BlockModelPredicate {

    final Identifier expectedContext;

    public IsContext(Identifier expectedContext) {
        this.expectedContext = expectedContext;
    }

    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state, Identifier renderContext) {
        return expectedContext.equals(renderContext);
    }

    public static IsContext parse(JsonElement arg) {
        return new IsContext(new Identifier(arg.getAsString()));
    }

}
