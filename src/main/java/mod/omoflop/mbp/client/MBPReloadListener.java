package mod.omoflop.mbp.client;

import com.google.gson.*;
import mod.omoflop.mbp.MBPData;
import mod.omoflop.mbp.data.logic.When;
import mod.omoflop.mbp.util.Utils;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.impl.client.model.ModelLoadingRegistryImpl;
import net.minecraft.block.Block;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MBPReloadListener implements SimpleSynchronousResourceReloadListener {
    public static final Identifier IDENTIFIER = new Identifier("mbp:predicateloader");
    public static final int VERSION = 0;
    public static final List<Identifier> WANTED_MODELS = new ArrayList<>();

    @Override
    public Identifier getFabricId() {
        return IDENTIFIER;
    }

    @Override
    public void reload(ResourceManager manager) {
        MBPData.PREDICATES.clear();
        WANTED_MODELS.clear();

        JsonParser parser = new JsonParser();
        for (Identifier id : manager.findResources("mbp", path -> path.endsWith(".json"))) {

            try {
                Identifier blockTarget = new Identifier(id.toString().substring(0,id.toString().length()-5).replace("mbp/", ""));
                String jsonString = inputStreamToString(manager.getResource(id).getInputStream());
                JsonObject asset = parser.parse(jsonString).getAsJsonObject();

                Optional<Block> block = Utils.getBlock(blockTarget);

                if (block.isPresent()) {
                    JsonArray overrides = asset.getAsJsonArray("overrides");
                    List<When> whenList = new ArrayList<>();
                    for(JsonElement overrideEntry : overrides) {
                        When when = When.parse(overrideEntry);
                        whenList.add(when);
                        WANTED_MODELS.add(when.resultModel);
                    }
                    MBPData.PREDICATES.put(block.get(), Collections.unmodifiableList(whenList));
                } else {
                    System.out.println("Block entry not found: " + blockTarget);
                }

            } catch (JsonParseException | IOException ignored) {
                // L
                System.out.println(ignored);
            }
        }


    }

    // i found this on stack overflow two months ago :)
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

}
