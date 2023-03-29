package mod.omoflop.mbp.data.conditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mod.omoflop.mbp.data.BlockModelPredicate;
import mod.omoflop.mbp.data.DataHelper;
import net.minecraft.block.BlockState;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class CoordinateRange extends BlockModelPredicate {

    public final NumberRange.IntRange range;
    public final Direction.Axis axis;

    public CoordinateRange(NumberRange.IntRange range, Direction.Axis axis) {
        this.range = range;
        this.axis = axis;
    }

    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state, Identifier renderContext) {
        return range.test(pos.getComponentAlongAxis(this.axis));
    }

    public static CoordinateRange parse(JsonElement arg) {
        JsonObject object = arg.getAsJsonObject();

        Direction.Axis axis = Direction.Axis.fromName(object.get("axis").getAsString());
        return new CoordinateRange(DataHelper.parseIntRange(arg), axis);
    }
}
