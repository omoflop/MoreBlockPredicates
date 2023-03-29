package mod.omoflop.mbp.client;

import com.google.gson.*;
import mod.omoflop.mbp.MBPData;
import mod.omoflop.mbp.data.logic.When;
import mod.omoflop.mbp.util.Utils;
import net.fabricmc.fabric.api.client.model.ExtraModelProvider;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.fabricmc.loader.impl.util.log.LogLevel;
import net.minecraft.block.Block;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;


public class MBPResourceProvider implements ExtraModelProvider {

    @Override
    public void provideExtraModels(ResourceManager manager, Consumer<Identifier> out) {
        final ArrayList<Identifier> wantedModels = new ArrayList<>();

        JsonParser parser = new JsonParser();
        Map<Identifier, Resource> map = manager.findResources("mbp", id -> id.getPath().endsWith(".json"));
        for (Identifier id : map.keySet()) {

            try {
                Identifier blockTarget = new Identifier(id.toString().substring(0,id.toString().length()-5).replace("mbp/", ""));
                String jsonString = MBPReloadListener.inputStreamToString(map.get(id).getInputStream());
                JsonObject asset = parser.parse(jsonString).getAsJsonObject();

                Optional<Block> block = Utils.getBlock(blockTarget);

                if (block.isPresent()) {
                    JsonArray overrides = asset.getAsJsonArray("overrides");
                    for(JsonElement overrideEntry : overrides) {
                        try {
                            When when = When.parse(overrideEntry);

                            wantedModels.addAll(when.getModels());
                        } catch (Exception e) {
                            Log.logFormat(LogLevel.ERROR, LogCategory.LOG, "Error found in file: " + id);
                        }
                    }
                }

            } catch (Exception e) {
                Log.logFormat(LogLevel.ERROR, LogCategory.LOG, e.toString());
            }
        }

        wantedModels.forEach(out);
    }
}
