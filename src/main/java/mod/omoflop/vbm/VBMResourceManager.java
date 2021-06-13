package mod.omoflop.vbm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import mod.omoflop.vbm.data.VBMCondition;
import mod.omoflop.vbm.util.Utils;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class VBMResourceManager {
    /**
     * Stores the conditions for model swapping
     */
    public static final HashMap<Identifier, VBMCondition[]> blockModelConditions = new HashMap<>();

    public static BlockState executeCondition(Identifier blockIdentifier, BlockPos pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        VBMCondition[] conditions = blockModelConditions.get(blockIdentifier);
        for (VBMCondition vbtCondition : conditions) {
            if (vbtCondition.meetsCondition(client.world, pos)) {
                Block b = Utils.getBlock(vbtCondition.model);
                return b.getDefaultState();
            }
        }
        return Utils.getBlock(blockIdentifier).getDefaultState();
    }

    public static void initalize() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
        .registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier("vbm:resource");
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
        VBMCondition[] conditions = new VBMCondition[overrides.size()];
        for (int i = 0; i < conditions.length; i++) {
            conditions[i] = VBMCondition.parseJson(overrides.get(i).getAsJsonObject());
        }

        blockModelConditions.put(baseModel, conditions);
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
            for (VBMCondition condition : blockModelConditions.get(modelId)) {
                System.out.println("\t"+condition.toString());
            }
        }
    }
}
