package mod.omoflop.biometextures;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import mod.omoflop.biometextures.data.conditions.BiomeTextureCondition;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.impl.client.indigo.IndigoConfig;
import net.fabricmc.fabric.impl.client.indigo.IndigoMixinConfigPlugin;
import net.fabricmc.fabric.impl.client.indigo.renderer.IndigoRenderer;
import net.fabricmc.fabric.impl.client.indigo.renderer.accessor.AccessBlockModelRenderer;
import net.fabricmc.fabric.impl.client.indigo.renderer.aocalc.VanillaAoHelper;
import net.fabricmc.loader.lib.gson.JsonReader;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class BiomeTexturesClient implements ClientModInitializer {

    public static String BIOME_IDENTIFIER = "biome";

    @Override
    public void onInitializeClient() {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new TestModelProvider());

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return new Identifier("vbt:resource");
                    }

                    @Override
                    public void apply(ResourceManager manager) {
                        biomeVariants.clear();

                        JsonParser parser = new JsonParser();
                        for (Identifier id : manager.findResources("textures", path -> path.endsWith(".json"))) {
                            try {
                                System.out.println(id);
                                String metadata = inputStreamToString(manager.getResource(id).getInputStream());
                                JsonObject json = parser.parse(metadata).getAsJsonObject();
                                if (json.has("variable_block_textures")) {
                                    Identifier refId = new Identifier(id.toString().substring(0,id.toString().length()-5));
                                    processJsonConditions(json.get("variable_block_textures").getAsJsonArray(), refId);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JsonParseException je) {
                                System.err.println("Failed to read json!");
                            }
                        }
                    
                        debugPrintBiomeVariantInfo();
                    }
                });
    
    }

    private static String inputStreamToString(InputStream stream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = stream.read(buffer)) != -1;) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }

    private static void processJsonConditions(JsonArray conditions, Identifier textureID) {
        BiomeTextureCondition[] biomeTextureConditions = new BiomeTextureCondition[conditions.size()];
        for (int i = 0; i < conditions.size(); i++) {
            JsonObject curCondition = conditions.get(i).getAsJsonObject();
            if (curCondition.has("biome") && curCondition.has("texture")) {
                Identifier biomeId   = new Identifier(curCondition.get("biome").getAsString());
                Identifier textureId = new Identifier(curCondition.get("texture").getAsString());
                biomeTextureConditions[i] = new BiomeTextureCondition(biomeId, textureId);
            } else {
                //! warn the user
            }
        }
        System.out.println("Registed VBT for " + textureID.toString());
        biomeVariants.put(textureID, biomeTextureConditions);
    }

    private static final HashMap<Identifier, BiomeTextureCondition[]> biomeVariants = new HashMap<>();
    private static void debugPrintBiomeVariantInfo() {
        for (Identifier key : biomeVariants.keySet()) {
            System.out.printf("%s\n", key);
            for (BiomeTextureCondition c : biomeVariants.get(key)) {
                System.out.printf("\t\"%s\" -> \"%s\"\n", c.biome, c.texture);
            }
        }
    }
    
}
