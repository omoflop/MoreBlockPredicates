package mod.omoflop.mbp.data;

import com.google.gson.JsonElement;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class IsBlockState extends BlockModelPredicate{

    final BlockStateArgument blockStatePredicate;

    public IsBlockState(BlockStateArgument blockStatePredicate) {
        this.blockStatePredicate = blockStatePredicate;
    }

    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state) {
        return blockStatePredicate.test(new CachedBlockPosition(MinecraftClient.getInstance().world, pos, false));
    }

    static IsBlockState parse(JsonElement arg) {
        try {
            BlockStateArgument blockStateArgument = BlockStateArgumentType.blockState().parse(new StringReader(arg.getAsString()));
            return new IsBlockState(blockStateArgument);
        } catch (CommandSyntaxException e) {
           throw new RuntimeException(e);
        }
    }

}
