package mod.omoflop.mbp.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mod.omoflop.mbp.data.logic.When;
import mod.omoflop.mbp.util.Utils;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.fabricmc.loader.impl.util.log.LogLevel;
import net.minecraft.block.Block;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class MBPModelLoadingPlugin implements PreparableModelLoadingPlugin<HashSet<Identifier>> {


    @Override
    public void onInitializeModelLoader(HashSet<Identifier> data, ModelLoadingPlugin.Context pluginContext) {
        pluginContext.addModels(data);
    }

    public static class ModelIdLoader implements PreparableModelLoadingPlugin.DataLoader<HashSet<Identifier>> {
        @Override
        public CompletableFuture<HashSet<Identifier>> load(ResourceManager manager, Executor executor) {

            return CompletableFuture.supplyAsync(() -> {
                HashSet<Identifier> wantedModels = new HashSet<>();

                Map<Identifier, Resource> map = manager.findResources("mbp", id -> id.getPath().endsWith(".json"));
                for (Identifier id : map.keySet()) {

                    try {
                        Identifier blockTarget = new Identifier(id.toString().substring(0,id.toString().length()-5).replace("mbp/", ""));
                        JsonObject asset = JsonParser.parseReader(map.get(id).getReader()).getAsJsonObject();

                        Optional<Block> block = Utils.getBlock(blockTarget);

                        if (block.isPresent()) {
                            JsonArray overrides = asset.getAsJsonArray("overrides");
                            for(JsonElement overrideEntry : overrides) {
                                try {
                                    When when = When.parse(overrideEntry);

                                    wantedModels.addAll(when.getModels());
                                } catch (Exception e) {
                                    MBPClient.LOGGER.error("Error found in file: " + id);
                                    e.printStackTrace();
                                }
                            }
                        }

                    } catch (Exception e) {
                        MBPClient.LOGGER.error("Error found in file: " + id);
                        e.printStackTrace();
                    }
                }
                return wantedModels;
            }, executor);


        }
    }
}
