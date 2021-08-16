package mod.omoflop.mbp.data.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mod.omoflop.mbp.data.BlockModelPredicate;
import mod.omoflop.mbp.data.WorldViewCondition;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.List;

public class When implements WorldViewCondition {

    final And conditions;
    public final Identifier resultModel;

    public When(And conditions, Identifier resultModel) {
        this.conditions = conditions;
        this.resultModel = new Identifier(resultModel.getNamespace(), "block/"+resultModel.getPath());
    }

    public static When parse(JsonElement arg) {
        JsonObject object = arg.getAsJsonObject();
        List<BlockModelPredicate> conditions = BlockModelPredicate.parseFromJson(object.getAsJsonObject("when"));
        return new When(new And(conditions), new Identifier(object.get("apply").getAsString()));
    }

    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state) {
        return conditions.meetsCondition(world,pos,state);
    }
}
