package mod.omoflop.vbm.data.conditions;

import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import mod.omoflop.vbm.data.ConditionType;
import mod.omoflop.vbm.data.VBMCondition;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@ConditionType(identifier = "vbt:biome", arguments = {"biome"})
public class BiomeCondition extends VBMCondition {

    public final Identifier[] biomes;

    public BiomeCondition(JsonObject condition, Identifier model) {
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
    }

    @Override
    public boolean meetsCondition(World world, BlockPos pos) {
        return false;
    }

    public String toString() {
        return String.format("%s -> %s", model.toString(), Arrays.toString(biomes));
    }
    
}
