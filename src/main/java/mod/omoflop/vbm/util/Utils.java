package mod.omoflop.vbm.util;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public final class Utils {
    

    private static final Supplier<MutableRegistry<Block>> blockRegistry = () -> {
        MinecraftClient instance = MinecraftClient.getInstance();
        return instance.world.getRegistryManager().get(Registry.BLOCK_KEY);
    };    
    private static final Supplier<MutableRegistry<Biome>> biomeRegistry = () -> {
        MinecraftClient instance = MinecraftClient.getInstance();
        return instance.world.getRegistryManager().get(Registry.BIOME_KEY);
    };

    public static SpriteIdentifier getBlockTexture(Identifier id) {
        return new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, id);
    }

    public static Identifier getBiomeIdentifier(Biome biome) {
        return biomeRegistry.get().getId(biome);
    }

    public static Block getBlock(Identifier id) {
        return blockRegistry.get().get(id);
    }

    public static Identifier getBlockIdentifier(Block block) {
        return blockRegistry.get().getId(block);
    }

}
