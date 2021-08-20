package mod.omoflop.mbp;

import mod.omoflop.mbp.client.MBPClient;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MBPMixinPlugin implements IMixinConfigPlugin {
    public static boolean HAS_SODIUM = false;

    @Override
    public void onLoad(String mixinPackage) {
        HAS_SODIUM = FabricLoader.getInstance().isModLoaded("sodium");
        MBPClient.log("INFO", "Starting MBP" + (HAS_SODIUM ? " with sodium compatibility!" : ""));
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals("SodiumChunkRenderRebuildTaskMixin"))
            return HAS_SODIUM;
        if (mixinClassName.equals("BlockRenderManagerMixin"))
            return !HAS_SODIUM;
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of("BakedModelManagerMixin","BlockRenderManagerMixin","SodiumChunkRenderRebuildTaskMixin");
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
