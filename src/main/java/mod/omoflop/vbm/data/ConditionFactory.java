package mod.omoflop.vbm.data;

import com.google.gson.JsonObject;

import mod.omoflop.vbm.data.conditions.*;
import net.minecraft.util.Identifier;

public class ConditionFactory {
    public static VBMCondition from(String name, JsonObject condition, Identifier model) {
        switch (name) {
            case "vbt:biome":
                return new BiomeCondition(condition, model);
            case "vbt:depth":
                return new DepthCondition(condition, model);
            case "vbt:and":
                return new AndCondition(condition, model);
            default:
                return null;
        }
    }
}
