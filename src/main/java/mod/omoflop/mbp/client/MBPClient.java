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
import org.spongepowered.asm.launch.MixinLaunchPlugin;

import java.util.logging.Logger;

@Environment(EnvType.CLIENT)
public class MBPClient implements ClientModInitializer {

    public static final Logger LOGGER = Logger.getLogger("mbp");

    @Override
    public void onInitializeClient() {


        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new MBPReloadListener());

        ModelLoadingRegistryImpl.INSTANCE.registerModelProvider((mm, out) -> {
            for (Identifier id : MBPReloadListener.WANTED_MODELS) {
                out.accept(id);
            }
        });


    }
}
