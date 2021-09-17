package mod.omoflop.mbp.data.conditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mod.omoflop.mbp.data.BlockModelPredicate;
import mod.omoflop.mbp.data.DataHelper;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class AdjacentBlock extends BlockModelPredicate {

    private IsBlockState stateCondition;
    private BlockPos offset;

    public AdjacentBlock(IsBlockState stateCondition, BlockPos offset) {
        this.stateCondition = stateCondition;
        this.offset = offset;
    }

    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state) {
        return stateCondition.meetsCondition(world, pos.add(offset), state);
    }

    public static AdjacentBlock parse(JsonElement arg) {
        JsonObject obj = arg.getAsJsonObject();
        IsBlockState stateCondition = IsBlockState.parse(obj.get("state"));

        return new AdjacentBlock(stateCondition, DataHelper.parseBlockPos(obj.getAsJsonObject("offset")));
    }
}
