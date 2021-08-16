package mod.omoflop.mbp.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.client.model.ModelLoadingRegistryImpl;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MBPClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new MBPReloadListener());

        ModelLoadingRegistryImpl.INSTANCE.registerModelProvider((mm, out) -> {
            System.out.println("Fetching wanted models: #" + MBPReloadListener.WANTED_MODELS.size());
            for (Identifier id : MBPReloadListener.WANTED_MODELS) {
                out.accept(id);
            }
        });
    }
}
