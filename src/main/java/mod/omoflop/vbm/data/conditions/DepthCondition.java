package mod.omoflop.vbm.data.conditions;

import com.google.gson.JsonObject;

import mod.omoflop.vbm.data.ConditionType;
import mod.omoflop.vbm.data.VBMCondition;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@ConditionType(identifier = "vbt:depth", arguments = {"range"})
public class DepthCondition extends VBMCondition {
    
    public final int min;
    public final int max;

    public DepthCondition(JsonObject condition, Identifier model) {
        super(condition, model);
        JsonObject range = condition.get("range").getAsJsonObject();
        min = range.get("min").getAsInt();
        max = range.get("max").getAsInt();
    }

    @Override
    public boolean meetsCondition(World world, BlockPos pos) {
        return false;
    }

    public String toString() {
        return String.format("%s -> [From y:%s to y:%s]", model.toString(), min, max);
    }
}
