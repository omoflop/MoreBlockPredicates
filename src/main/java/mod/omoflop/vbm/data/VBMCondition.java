package mod.omoflop.vbm.data;

import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class VBMCondition {

    public final Identifier model;

    public VBMCondition(JsonObject condition, Identifier model) {
        this.model = model;
    }
    public abstract boolean meetsCondition(World world, BlockPos pos);

    public static VBMCondition parseJson(JsonObject curEntry) {
        Identifier modelId = new Identifier(curEntry.get("model").getAsString());
        JsonObject condition = curEntry.get("condition").getAsJsonObject();
        String type = condition.get("type").getAsString();
        return ConditionFactory.from(type, condition, modelId);
    }
}
