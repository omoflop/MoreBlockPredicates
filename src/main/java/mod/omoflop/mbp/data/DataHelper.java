package mod.omoflop.mbp.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.math.BlockPos;

public final class DataHelper {

    public static NumberRange.IntRange parseIntRange(JsonElement arg) {
        JsonObject object = arg.getAsJsonObject();

        boolean hasMin = object.has("min"), hasMax = object.has("max");
        if (hasMin && hasMax) {
            return NumberRange.IntRange.between(object.get("min").getAsInt(), object.get("max").getAsInt());
        } else if (hasMin) {
            return NumberRange.IntRange.atLeast(object.get("min").getAsInt());
        } else if (hasMax) {
            return NumberRange.IntRange.atMost(object.get("max").getAsInt());
        } else {
            // none?!?!??!?!?!?!?!?!
            throw new JsonParseException("No min or max defined for range!");
        }
    }

    public static BlockPos parseBlockPos(JsonObject offset) {
        int x = offset.get("x").getAsInt();
        int y = offset.get("y").getAsInt();
        int z = offset.get("z").getAsInt();
        return new BlockPos(x, y, z);
    }
}
