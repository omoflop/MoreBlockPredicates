package mod.omoflop.mbp.util;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.Optional;
import java.util.function.Supplier;

public class Utils {

    private static DynamicRegistryManager getRegistryManager() {
        MinecraftClient instance =MinecraftClient.getInstance();
        if (instance != null) {
            if (instance.world != null) {
                return instance.world.getRegistryManager();
            }
        }
        return null;
    }
    public static final Supplier<Optional<Registry<Biome>>> BIOME_REGISTRY = () -> {
        DynamicRegistryManager drm = getRegistryManager();
        if (drm != null) {
            Registry<Biome> b = drm.get(RegistryKeys.BIOME);
            if (b != null) {
                return Optional.of(b);
            }
        }
        return Optional.empty();
    };

    public static Optional<Biome> getBiome(Identifier biomeId) {
        Optional<Registry<Biome>> registry = BIOME_REGISTRY.get();
        if (registry.isPresent()) {
            return registry.get().getOrEmpty(biomeId);
        }
        return Optional.empty();
    }
    public static Optional<Identifier> getBiome(Biome biome) {
        Optional<Registry<Biome>> registry = BIOME_REGISTRY.get();
        return registry.map(biomes -> biomes.getId(biome));
    }
    public static Optional<Block> getBlock(Identifier blockId) {
        return Registries.BLOCK.getOrEmpty(blockId);
    }

}
