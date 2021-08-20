package mod.omoflop.mbp.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class AdjacentBlock extends BlockModelPredicate{

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

    static AdjacentBlock parse(JsonElement arg) {
        JsonObject obj = arg.getAsJsonObject();
        IsBlockState stateCondition = IsBlockState.parse(obj.get("state"));
        JsonObject offset = obj.getAsJsonObject("offset");
        int x = offset.get("x").getAsInt();
        int y = offset.get("y").getAsInt();
        int z = offset.get("z").getAsInt();
        return new AdjacentBlock(stateCondition, new BlockPos(x,y,z));
    }
}
