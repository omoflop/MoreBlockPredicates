package mod.omoflop.mbp.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.client.model.ModelLoadingRegistryImpl;
import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingPluginManager;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceType;

@Environment(EnvType.CLIENT)
public class MBPClient implements ClientModInitializer {

    public static Entity currentEntity;

    @Override
    public void onInitializeClient() {
        ModelLoadingPluginManager.registerPlugin(new MBPModelLoadingPlugin.ModelIdLoader(), new MBPModelLoadingPlugin());
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new MBPReloadListener());

    }
}
