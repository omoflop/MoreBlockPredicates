package mod.omoflop.biometextures;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import mod.omoflop.biometextures.data.VBTCondition;
import mod.omoflop.biometextures.data.conditions.ConditionBiome;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class ResourcePackLoader {
    /**
     * Stores the conditions for model swapping
     */
    public static final HashMap<Identifier, VBTCondition[]> blockModelConditions = new HashMap<>();

    public static void initalize() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
        .registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier("vbt:resource");
            }

            @Override
            public void apply(ResourceManager manager) {
                onApply(manager);
            }
        });

    }

    /**
     * Ran whenever resource packs are applied
     * @param manager
     */
    private static void onApply(ResourceManager manager) {
        blockModelConditions.clear();

        JsonParser parser = new JsonParser();
        for (Identifier id : manager.findResources("models/block", path -> path.endsWith(".json"))) {
            try {
                Identifier refId = new Identifier(id.toString().substring(0,id.toString().length()-5));
                String metadata = inputStreamToString(manager.getResource(id).getInputStream());
                JsonObject json = parser.parse(metadata).getAsJsonObject();
                if (json.has("overrides")) {
                    processOverrides(refId, json.get("overrides").getAsJsonArray());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JsonParseException je) {
                System.err.println("Failed to read json!");
            }
        }

        debugPrint();
    }

    /**
     * Proccesses the JSON overrides in our special format
     * @param overrides
     */
    private static void processOverrides(Identifier baseModel, JsonArray overrides) {
        VBTCondition[] conditions = new VBTCondition[overrides.size()];
        for (int i = 0; i < conditions.length; i++) {
            JsonObject curEntry = overrides.get(i).getAsJsonObject();
            if (validOverrideEntry(curEntry)) {
                Identifier modelId = new Identifier(curEntry.get("model").getAsString());
                JsonObject condition = curEntry.get("condition").getAsJsonObject();
                String type = condition.get("type").getAsString();
                if (type.equals("vbt:biome")) {
                    conditions[i] = new ConditionBiome(condition, modelId);
                }
            }
        }
        blockModelConditions.put(baseModel, conditions);
    }

    /**
     * Checks if a override entry is correctly formatted
     * @param entry
     * @return
     */
    private static boolean validOverrideEntry(JsonObject entry) {
        boolean hasCondition = entry.has("condition");
        boolean hasModel = entry.has("model");
        if (hasCondition && hasModel) {
            JsonObject condition = entry.get("condition").getAsJsonObject();
            boolean hasType = condition.has("type");
            boolean hasBiome = condition.has("biome");
            if (hasType && hasBiome) {
                return true;
            }
        }  
        return false;
    }

    
    /**
     * Converts an input stream into a string, for reading JSONs and other files
     * @param stream
     * @return
     * @throws IOException
     */
    private static String inputStreamToString(InputStream stream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = stream.read(buffer)) != -1;) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
    
    private static void debugPrint() {
        for (Identifier modelId : blockModelConditions.keySet()) {
            System.out.println(modelId.toString());
            for (VBTCondition condition : blockModelConditions.get(modelId)) {
                System.out.println("\t"+condition.toString());
            }
        }
    }
}
