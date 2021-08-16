package mod.omoflop.mbp.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class LightRange extends BlockModelPredicate{

    public final NumberRange.IntRange range;

    public LightRange(NumberRange.IntRange range) {
        this.range = range;
    }

    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state) {
        int lvl = MinecraftClient.getInstance().world.getLightLevel(pos);
        return range.test(lvl);
    }

    static LightRange parse(JsonElement arg) {
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
            throw new JsonParseException("No min or max defined for light_range!");
        }

        return new LightRange(range);
    }
}
