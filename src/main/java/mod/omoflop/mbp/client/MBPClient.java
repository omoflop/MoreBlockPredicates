package mod.omoflop.mbp.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.client.model.ModelLoadingRegistryImpl;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceType;

import java.time.LocalTime;

@Environment(EnvType.CLIENT)
public class MBPClient implements ClientModInitializer {

    public static Entity currentEntity;


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
