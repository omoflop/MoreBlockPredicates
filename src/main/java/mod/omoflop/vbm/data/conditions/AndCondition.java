package mod.omoflop.vbm.data.conditions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import mod.omoflop.vbm.data.VBMCondition;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AndCondition extends VBMCondition {

    public final VBMCondition[] conditions;

    public AndCondition(JsonObject condition, Identifier model) {
        super(condition, model);
        JsonArray conditionArray = condition.get("conditions").getAsJsonArray();
        conditions = new VBMCondition[conditionArray.size()];
        for (int i = 0; i < conditionArray.size(); i++) {
            conditions[i] = VBMCondition.parseJson(conditionArray.get(i).getAsJsonObject());
        }
    }

    @Override
    public boolean meetsCondition(World world, BlockPos pos) {
        for (int i = 0; i < conditions.length; i++) {
            if (!conditions[i].meetsCondition(world, pos)) {
                return false;
            }
        }
        return true;
    }
    
}
