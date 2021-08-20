package mod.omoflop.mbp.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mod.omoflop.mbp.client.MBPClient;
import mod.omoflop.mbp.data.logic.Not;
import mod.omoflop.mbp.data.logic.Or;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class BlockModelPredicate implements WorldViewCondition {
    private static final HashMap<String, Function<JsonElement, BlockModelPredicate>> HANDLERS = new HashMap<>() {{
        // Logical operators
        put("OR", Or::parse);
        put("NOT", Not::parse);

        // Actual conditions
        put("coordinate_range", CoordinateRange::parse);
        put("biome", InBiome::parse);
        put("state", IsBlockState::parse);
        put("light_range", LightRange::parse);
    }};

    public static ArrayList<BlockModelPredicate> parseFromJson(JsonObject object) {
        ArrayList<BlockModelPredicate> predicates = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entries : object.entrySet()) {
            if (HANDLERS.containsKey(entries.getKey())) {
                try {
                    predicates.add(HANDLERS.get(entries.getKey()).apply(entries.getValue()));
                } catch (JsonParseException e) {
                    MBPClient.LOGGER.warning(String.format("Failed to load predicate \"%s\"! Reason: %s", entries.getKey(), e.getMessage()));
                }
            } else {
                MBPClient.LOGGER.warning(String.format("Unhandled predicate \"%s\"!", entries.getKey()));
            }
        }

        return predicates;
    }



}
