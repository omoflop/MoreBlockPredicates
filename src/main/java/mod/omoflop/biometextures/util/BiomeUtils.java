package mod.omoflop.biometextures.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public final class BiomeUtils {
    public static Identifier getIdentifier(Biome biome) {
        MinecraftClient instance = MinecraftClient.getInstance();
        return instance.world.getRegistryManager().get(Registry.BIOME_KEY).getId(biome);
    }

}
