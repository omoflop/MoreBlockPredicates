package mod.omoflop.mbp.accessor;

import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.Entity;

public interface BlockRenderManagerAccess {
    void moreBlockPredicates$setContextEntity(Entity entity);

    static BlockRenderManagerAccess of(BlockRenderManager manager) {
        return (BlockRenderManagerAccess) manager;
    }
}
