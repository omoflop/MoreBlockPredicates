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

    final @Nullable BlockPredicateArgumentType.BlockPredicate blockStatePredicate;
    public IsBlockState(@Nullable BlockPredicateArgumentType.BlockPredicate blockStatePredicate) {
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

            BlockPredicateArgumentType.BlockPredicate blockStateArgument = null;
            if (str != null) {
                blockStateArgument = new BlockPredicateArgumentType.BlockPredicate() {
                    final BlockArgumentParser.BlockResult res = BlockArgumentParser.block(Registries.BLOCK.getReadOnlyWrapper(), str, false);

                    @Override
                    public boolean hasNbt() {
                        return false;
                    }

                    @Override
                    public boolean test(CachedBlockPosition cachedBlockPosition) {
                        return cachedBlockPosition.getBlockState().equals(res.blockState());
                    }
                };
            }

            return new IsBlockState(blockStateArgument);
        } catch (CommandSyntaxException e) {
           throw new RuntimeException(e);
        }
    }



}
