package mod.omoflop.mbp;

import mod.omoflop.mbp.client.MBPClient;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.fabricmc.loader.impl.util.log.LogLevel;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class MBPMixinPlugin implements IMixinConfigPlugin {
    public static boolean HAS_SODIUM = false;
    public static boolean HAS_WORLDMESHER = false;

    @Override
    public void onLoad(String mixinPackage) {
        HAS_SODIUM = FabricLoader.getInstance().isModLoaded("sodium");
        HAS_WORLDMESHER = FabricLoader.getInstance().isModLoaded("worldmesher");
        StringBuilder builder = new StringBuilder();
        builder.append("Starting MBP");
        Log.logFormat(LogLevel.INFO, LogCategory.LOG, "Starting MBP" + (HAS_SODIUM ? " with sodium compatibility!" : ""));
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals("compat.sodium.ChunkBuilderMeshingTaskMixin"))
            return HAS_SODIUM;
        //if (mixinClassName.equals("BlockRenderManagerMixin"))
        //    return !HAS_SODIUM;
        //if (mixinClassName.equals("WorldMeshMixin"))
            //return HAS_WORLDMESHER;
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of("compat.sodium.ChunkBuilderMeshingTaskMixin");
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
