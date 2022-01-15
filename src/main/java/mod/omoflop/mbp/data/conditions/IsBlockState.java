package mod.omoflop.mbp.data.conditions;

import com.google.gson.JsonElement;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mod.omoflop.mbp.data.BlockModelPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class IsBlockState extends BlockModelPredicate {

    final @Nullable BlockStateArgument blockStatePredicate;
    final boolean checkSolid;
    final boolean checkFullCube;

    public IsBlockState(@Nullable BlockStateArgument blockStatePredicate, boolean checkSolid, boolean checkFullCube) {
        this.blockStatePredicate = blockStatePredicate;
        this.checkSolid = checkSolid;
        this.checkFullCube = checkFullCube;
    }

    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state) {
        boolean a = true;
        if (blockStatePredicate != null) a = blockStatePredicate.test(new CachedBlockPosition(MinecraftClient.getInstance().world, pos, false));
        if (checkSolid) a = a && state.isSolidBlock(world, pos);
        if (checkFullCube) a = a && state.isFullCube(world, pos);
        return a;
    }

    public static IsBlockState parse(JsonElement arg) {
        try {
            String str = arg.getAsString();
            boolean solid = false;
            boolean fullCube = false;
            if (str.endsWith("!")) {
                solid = true;
                str = str.substring(0,str.length()-1);
            }
            if (str.endsWith("?")) {
                fullCube = true;
                str = str.substring(0,str.length()-1);
            }

            BlockStateArgument blockStateArgument = null;
            if (!str.equals("*")) {
                blockStateArgument = BlockStateArgumentType.blockState().parse(new StringReader(str));
            }

            return new IsBlockState(blockStateArgument, solid, fullCube);
        } catch (CommandSyntaxException e) {
           throw new RuntimeException(e);
        }
    }

}
