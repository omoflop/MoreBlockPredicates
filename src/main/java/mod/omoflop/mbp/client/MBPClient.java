package mod.omoflop.mbp.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.client.model.ModelLoadingRegistryImpl;
import net.fabricmc.loader.impl.game.minecraft.Log4jLogHandler;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogHandler;
import net.fabricmc.tinyremapper.extension.mixin.common.Logger;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceType;

import java.time.LocalTime;
import java.util.logging.Level;

@Environment(EnvType.CLIENT)
public class MBPClient implements ClientModInitializer {

    public static Entity currentEntity;

    @Override
    public void onInitializeClient() {
        ModelLoadingRegistryImpl.INSTANCE.registerModelProvider(new MBPResourceProvider());
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new MBPReloadListener());

    }
}
