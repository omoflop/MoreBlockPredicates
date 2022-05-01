package mod.omoflop.mbp.data.conditions;

import com.google.gson.JsonElement;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mod.omoflop.mbp.data.BlockModelPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.BlockPredicateArgumentType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class IsBlockState extends BlockModelPredicate {

    final @Nullable BlockPredicateArgumentType.BlockPredicate blockStatePredicate;
    Predicate<CachedBlockPosition> predicate;
    public IsBlockState(@Nullable BlockPredicateArgumentType.BlockPredicate blockStatePredicate) {
        this.blockStatePredicate = blockStatePredicate;
    }

    @Override
    public boolean meetsCondition(BlockView world, BlockPos pos, BlockState state, boolean isItem) {
        if (blockStatePredicate == null) return true;
        if (predicate == null) {
            try {
                predicate = blockStatePredicate.create(Registry.BLOCK);
            } catch (Exception ignored) {}
        }
        return predicate != null && predicate.test(new CachedBlockPosition(MinecraftClient.getInstance().world, pos, false));
    }

    public static IsBlockState parse(JsonElement arg) {
        try {
            String str = arg.getAsString();

            BlockPredicateArgumentType.BlockPredicate blockStateArgument = null;
            if (str != null) {
                blockStateArgument = BlockPredicateArgumentType.blockPredicate().parse(new StringReader(str));
            }

            return new IsBlockState(blockStateArgument);
        } catch (CommandSyntaxException e) {
           throw new RuntimeException(e);
        }
    }

}
