package mod.omoflop.biometextures;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;

public class BiomeTexturesClient implements ClientModInitializer {

    public static String BIOME_IDENTIFIER = "biome";

    @Override
    public void onInitializeClient() {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new TestModelProvider());

        ResourcePackLoader.initalize();
    }

    
}
