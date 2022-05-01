package mod.omoflop.mbp.client;

import com.google.gson.*;
import mod.omoflop.mbp.MBPData;
import mod.omoflop.mbp.data.logic.When;
import mod.omoflop.mbp.util.Utils;
import net.fabricmc.fabric.api.client.model.ExtraModelProvider;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.block.Block;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;


public class MBPResourceProvider implements ExtraModelProvider {

    @Override
    public void provideExtraModels(ResourceManager manager, Consumer<Identifier> out) {
        final ArrayList<Identifier> wantedModels = new ArrayList<>();

        JsonParser parser = new JsonParser();
        for (Identifier id : manager.findResources("mbp", path -> path.endsWith(".json"))) {

            try {
                Identifier blockTarget = new Identifier(id.toString().substring(0,id.toString().length()-5).replace("mbp/", ""));
                String jsonString = MBPReloadListener.inputStreamToString(manager.getResource(id).getInputStream());
                JsonObject asset = parser.parse(jsonString).getAsJsonObject();

                Optional<Block> block = Utils.getBlock(blockTarget);

                if (block.isPresent()) {
                    JsonArray overrides = asset.getAsJsonArray("overrides");
                    List<When> whenList = new ArrayList<>();
                    for(JsonElement overrideEntry : overrides) {
                        try {
                            When when = When.parse(overrideEntry);
                            whenList.add(when);

                            wantedModels.addAll(when.getModels());
                        } catch (Exception e) {
                            MBPClient.log("ERROR", "Error found in file: " + id);
                        }
                    }
                }

            } catch (Exception e) {
                MBPClient.log("ERROR", e.toString());
            }
        }

        wantedModels.forEach(out::accept);
    }
}
