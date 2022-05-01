package mod.omoflop.mbp.data.conditions;

import com.google.gson.JsonElement;
import mod.omoflop.mbp.data.BlockModelPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class IsItem extends BlockModelPredicate {

    final boolean shouldBeItem;

    public IsItem(boolean shouldBeItem) {
        this.shouldBeItem = shouldBeItem;
    }

    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state, boolean isItem) {
        return isItem == shouldBeItem;
    }

    public static IsItem parse(JsonElement arg) {
        return new IsItem(arg.getAsBoolean());
    }

}
