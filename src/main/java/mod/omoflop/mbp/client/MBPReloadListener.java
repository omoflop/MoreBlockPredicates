package mod.omoflop.mbp.client;

import com.google.gson.*;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import mod.omoflop.mbp.MBPData;
import mod.omoflop.mbp.MBPMixinPlugin;
import mod.omoflop.mbp.data.logic.When;
import mod.omoflop.mbp.util.Utils;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.fabricmc.loader.impl.util.log.LogLevel;
import net.minecraft.block.Block;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;

public class MBPReloadListener implements SimpleSynchronousResourceReloadListener {
    public static final Identifier IDENTIFIER = new Identifier("mbp:predicateloader");

    @Override
    public Identifier getFabricId() {
        return IDENTIFIER;
    }

    @Override
    public void reload(ResourceManager manager) {
        MBPData.PREDICATES.clear();

        JsonParser parser = new JsonParser();
        Map<Identifier, Resource> map = manager.findResources("mbp", id -> id.getPath().endsWith(".json"));
        for (Identifier id : map.keySet()) {

            try {
                Identifier blockTarget = new Identifier(id.toString().substring(0,id.toString().length()-5).replace("mbp/", ""));
                String jsonString = inputStreamToString(map.get(id).getInputStream());
                JsonObject asset = parser.parse(jsonString).getAsJsonObject();

                Optional<Block> block = Utils.getBlock(blockTarget);

                if (block.isPresent()) {
                    JsonArray overrides = asset.getAsJsonArray("overrides");
                    List<When> whenList = new ArrayList<>();
                    for(JsonElement overrideEntry : overrides) {
                        When when = When.parse(overrideEntry);
                        whenList.add(when);
                    }
                    MBPData.PREDICATES.put(block.get(), Collections.unmodifiableList(whenList));
                } else {
                    Log.logFormat(LogLevel.WARN, LogCategory.LOG,"Block entry not found in file %s: %s ", id, blockTarget);
                }

            } catch (Exception e) {
                Log.logFormat(LogLevel.ERROR, LogCategory.LOG, "Error in file: %s", id);
                e.printStackTrace();
            }
        }
    }

    // i found this on stack overflow two months ago :)
    /**
     * Converts an input stream into a string, for reading JSONs and other files
     */
    static String inputStreamToString(InputStream stream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = stream.read(buffer)) != -1;) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8);
    }

}
