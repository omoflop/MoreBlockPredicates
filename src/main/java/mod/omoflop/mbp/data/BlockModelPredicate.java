package mod.omoflop.mbp.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mod.omoflop.mbp.client.MBPClient;
import mod.omoflop.mbp.data.conditions.*;
import mod.omoflop.mbp.data.logic.Not;
import mod.omoflop.mbp.data.logic.Or;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.fabricmc.loader.impl.util.log.LogLevel;
import net.minecraft.client.render.entity.EndermanEntityRenderer;
import net.minecraft.client.render.entity.feature.EndermanBlockFeatureRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;

public abstract class BlockModelPredicate implements WorldViewCondition {
    private static final HashMap<String, Function<JsonElement, BlockModelPredicate>> HANDLERS = new HashMap<>() {{
        // Logical operators
        put("or", Or::parse);
        put("not", Not::parse);

        // Actual conditions
        put("adjacent_block", AdjacentBlock::parse);
        put("coordinate_range", CoordinateRange::parse);
        put("biome", InBiome::parse);
        put("state", IsBlockState::parse);
        put("light_range", LightRange::parse);
        put("is_context", IsContext::parse);
    }};

    public static ArrayList<BlockModelPredicate> parseFromJson(JsonElement element) {
        ArrayList<JsonObject> objects = new ArrayList<>();
        if (element.isJsonArray()) {
            JsonArray arr = element.getAsJsonArray();
            for (JsonElement obj : arr) {
                objects.add(obj.getAsJsonObject());
            }
        } else {
            objects.add(element.getAsJsonObject());
        }

        ArrayList<BlockModelPredicate> predicates = new ArrayList<>();
        for(JsonObject curObject : objects) {
            for (Map.Entry<String, JsonElement> entries : curObject.entrySet()) {
                if (HANDLERS.containsKey(entries.getKey())) {
                    try {
                        predicates.add(HANDLERS.get(entries.getKey()).apply(entries.getValue()));
                    } catch (JsonParseException e) {
                        Log.logFormat(LogLevel.WARN, LogCategory.LOG, String.format("Failed to load predicate \"%s\"! Reason: %s", entries.getKey(), e.getMessage()));
                    }
                } else {
                    Log.logFormat(LogLevel.WARN, LogCategory.LOG, String.format("Unhandled predicate \"%s\"!", entries.getKey()));
                }
            }
        }

        return predicates;
    }



}
