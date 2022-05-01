package mod.omoflop.mbp.data.conditions;

import com.google.gson.JsonElement;
import mod.omoflop.mbp.data.BlockModelPredicate;
import mod.omoflop.mbp.data.DataHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LightRange extends BlockModelPredicate {

    public final NumberRange.IntRange range;

    public LightRange(NumberRange.IntRange range) {
        this.range = range;
    }

    @Override
    public boolean meetsCondition(BlockView _unused, BlockPos pos, BlockState state, boolean isItem) {
        World world = MinecraftClient.getInstance().world;

        boolean c = range.test(world.getLightLevel(pos));
            c = c || range.test(world.getLightLevel(pos.up()));
            c = c || range.test(world.getLightLevel(pos.down()));
            c = c || range.test(world.getLightLevel(pos.north()));
            c = c || range.test(world.getLightLevel(pos.south()));
            c = c || range.test(world.getLightLevel(pos.east()));
            c = c || range.test(world.getLightLevel(pos.west()));

        return c;
    }

    public static LightRange parse(JsonElement arg) {
        return new LightRange(DataHelper.parseIntRange(arg));
    }
}
