package mod.omoflop.biometextures.data.conditions;

import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import mod.omoflop.biometextures.data.ConditionType;
import mod.omoflop.biometextures.data.VBTCondition;
import net.minecraft.util.Identifier;

@ConditionType(identifier = "vbt:biome", arguments = {"biome"})
public class ConditionBiome extends VBTCondition {

    public final Identifier model;
    public final Identifier[] biomes;

    public ConditionBiome(JsonObject condition, Identifier model) {
        super(condition, model);
        JsonElement biome = condition.get("biome");
        if (biome.isJsonArray()) {
            JsonArray biomeArray = biome.getAsJsonArray();
            biomes = new Identifier[biomeArray.size()];
            for (int i = 0; i < biomes.length; i++) {
                biomes[i] = new Identifier(biomeArray.get(i).getAsString());
            }
        } else {
            biomes = new Identifier[] {new Identifier(biome.getAsString())};
        }
        this.model = model;
    }

    @Override
    public Identifier getModel() {
        return model;
    }

    public String toString() {
        return String.format("%s -> %s", model.toString(), Arrays.toString(biomes));
    }
    
}
