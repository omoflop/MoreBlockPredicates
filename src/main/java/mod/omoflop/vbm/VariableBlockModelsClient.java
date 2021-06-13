package mod.omoflop.vbm;

import java.util.Map;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.model.BakedModel;

public class VariableBlockModelsClient implements ClientModInitializer {

    public static String BIOME_IDENTIFIER = "biome";

    @Override
    public void onInitializeClient() {
        //ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new VBMModelProvider());

        VBMResourceManager.initalize();        
    }    
}
