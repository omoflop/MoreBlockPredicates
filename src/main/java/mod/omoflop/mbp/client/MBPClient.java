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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Environment(EnvType.CLIENT)
public class MBPClient implements ClientModInitializer {

    public static void log(String level, String format, Object... params) {
        String ts = LocalTime.now().toString();
        String time = ts.substring(0, ts.indexOf("."));
        System.out.printf("[%s] [%s/%s]: %s\n", time, "mbp", level, format.formatted(params));
    }

    @Override
    public void onInitializeClient() {

        ModelLoadingRegistryImpl.INSTANCE.registerModelProvider(new MBPResourceProvider());
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new MBPReloadListener());

    }
}
