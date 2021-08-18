package mod.omoflop.mbp.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.client.model.ModelLoadingRegistryImpl;
import net.fabricmc.fabric.impl.registry.sync.FabricRegistry;
import net.fabricmc.fabric.impl.resource.loader.FabricModResourcePack;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.launch.common.FabricLauncher;
import net.fabricmc.loom.configuration.FabricApiExtension;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MBPClient implements ClientModInitializer {

    public static boolean HAS_SODIUM = false;

    @Override
    public void onInitializeClient() {
        HAS_SODIUM = FabricLoader.getInstance().isModLoaded("sodium");
        System.out.println("Starting MBP" + (HAS_SODIUM ? " with sodium compatability!" : ""));

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new MBPReloadListener());

        ModelLoadingRegistryImpl.INSTANCE.registerModelProvider((mm, out) -> {
            System.out.println("Fetching wanted models: #" + MBPReloadListener.WANTED_MODELS.size());
            for (Identifier id : MBPReloadListener.WANTED_MODELS) {
                out.accept(id);
            }
        });
    }
}
