package mod.omoflop.mbp.data.conditions;

import com.google.gson.JsonElement;
import mod.omoflop.mbp.data.BlockModelPredicate;
import mod.omoflop.mbp.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Optional;

public class InBiome extends BlockModelPredicate {

    final Identifier biomeID;

    public InBiome(Identifier biomeID) {
        this.biomeID = biomeID;
    }

    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state, boolean isItem) {
        World w = MinecraftClient.getInstance().world;;
        assert w != null;
        return w.getBiome(pos).matchesId(biomeID);
    }

    public static InBiome parse(JsonElement arg) {
        return new InBiome(new Identifier(arg.getAsString()));
    }
}
