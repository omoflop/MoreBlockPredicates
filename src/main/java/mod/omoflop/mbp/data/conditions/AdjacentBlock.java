package mod.omoflop.mbp.data.conditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mod.omoflop.mbp.data.BlockModelPredicate;
import mod.omoflop.mbp.data.DataHelper;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class AdjacentBlock extends BlockModelPredicate {

    private final @Nullable IsBlockState stateCondition;
    private final BlockPos offset;
    private final boolean checkFullCube;
    private final boolean checkTransparent;
    public AdjacentBlock(@Nullable IsBlockState stateCondition, BlockPos offset, boolean checkFullCube, boolean checkTransparent) {
        this.stateCondition = stateCondition;
        this.offset = offset;
        this.checkFullCube = checkFullCube;
        this.checkTransparent = checkTransparent;
    }

    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state, boolean isItem) {
        BlockState block = world.getBlockState(pos);
        boolean b = true;
        if (checkFullCube) b = block.isFullCube(world, pos);
        if (checkTransparent) b &= block.isTranslucent(world, pos);
        if (stateCondition != null) b &= stateCondition.meetsCondition(world, pos.add(offset), state, isItem);
        return b;
    }

    public static AdjacentBlock parse(JsonElement arg) {
        JsonObject obj = arg.getAsJsonObject();
        IsBlockState stateCondition = null;
        if (obj.has("state")) {
            stateCondition = IsBlockState.parse(obj.get("state"));
        }

        boolean checkFullCube = false;
        boolean checkTransparent = false;
        if (obj.has("is_full_cube")) checkFullCube = obj.get("is_full_cube").getAsBoolean();
        if (obj.has("is_transparent")) checkTransparent = obj.get("is_transparent").getAsBoolean();

        return new AdjacentBlock(stateCondition, DataHelper.parseBlockPos(obj.getAsJsonObject("offset")), checkFullCube, checkTransparent);
    }
}
