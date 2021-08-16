package mod.omoflop.mbp.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.block.BlockState;
import net.minecraft.predicate.NumberRange;
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
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state) {
        return range.test(pos.getComponentAlongAxis(this.axis));
    }

    static CoordinateRange parse(JsonElement arg) {
        JsonObject object = arg.getAsJsonObject();

        NumberRange.IntRange range;
        boolean hasMin = object.has("min"), hasMax = object.has("max");
        if (hasMin && hasMax) {
            range = NumberRange.IntRange.between(object.get("min").getAsInt(),object.get("max").getAsInt());
        } else if (hasMin) {
            range = NumberRange.IntRange.atLeast(object.get("min").getAsInt());
        } else if (hasMin) {
            range = NumberRange.IntRange.atMost(object.get("max").getAsInt());
        } else {
            // none?!?!??!?!?!?!?!?!
            throw new JsonParseException("No min or max defined for coordinate_range!");
        }

        Direction.Axis axis = Direction.Axis.fromName(object.get("axis").getAsString());
        return new CoordinateRange(range, axis);
    }
}
