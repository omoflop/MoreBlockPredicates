package mod.omoflop.mbp.data.conditions;

import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mod.omoflop.mbp.data.BlockModelPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.command.argument.BlockPredicateArgumentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class IsBlockState extends BlockModelPredicate {

    final @Nullable BlockPredicateImpl blockStatePredicate;
    public IsBlockState(@Nullable BlockPredicateImpl blockStatePredicate) {
        this.blockStatePredicate = blockStatePredicate;
    }

    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state, Identifier renderContext) {
        if (blockStatePredicate == null) return true;
        return blockStatePredicate.test(new CachedBlockPosition(MinecraftClient.getInstance().world, pos, false));
    }

    public static IsBlockState parse(JsonElement arg) {
        try {
            String str = arg.getAsString();

            if (str != null) {
                return new IsBlockState(new BlockPredicateImpl(str));
            }

            return null; // explode
        } catch (CommandSyntaxException e) {
           throw new RuntimeException(e);
        }
    }

    private static class BlockPredicateImpl implements BlockPredicateArgumentType.BlockPredicate {
        final BlockArgumentParser.BlockResult res;
        public final String stateString;
        public final boolean fuzzy;

        public BlockPredicateImpl(String stateString) throws CommandSyntaxException {
            this.stateString = stateString;
            fuzzy = !stateString.contains("[");
            res = BlockArgumentParser.block(Registries.BLOCK.getReadOnlyWrapper(), stateString, false);
        }

        @Override
        public boolean hasNbt() {
            return false;
        }

        @Override
        public boolean test(CachedBlockPosition cachedBlockPosition) {
            if (fuzzy) return cachedBlockPosition.getBlockState().getBlock() == res.blockState().getBlock();
            return cachedBlockPosition.getBlockState().equals(res.blockState());
        }
    }


}
