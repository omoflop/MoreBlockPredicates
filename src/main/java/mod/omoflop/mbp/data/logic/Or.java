package mod.omoflop.mbp.data.logic;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mod.omoflop.mbp.data.BlockModelPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.List;

public class Or extends BlockModelPredicate {

    final List<And> conditions;

    public Or(List<And> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state, boolean isItem) {
        for (And action : conditions) {
            if (action.meetsCondition(world, pos, state, isItem)) return true;
        }
        return false;
    }

    public static Or parse(JsonElement arg) {
        ArrayList<And> conditionsList = new ArrayList<>();
        JsonArray entryArray = arg.getAsJsonArray();
        for (JsonElement entry : entryArray) {
            conditionsList.add(And.parse(entry));
        }
        return new Or(ImmutableList.copyOf(conditionsList));
    }
}
