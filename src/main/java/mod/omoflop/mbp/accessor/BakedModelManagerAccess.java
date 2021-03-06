package mod.omoflop.mbp.accessor;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.util.Identifier;

import java.util.Map;

public interface BakedModelManagerAccess {
    BakedModel reallyGetModel(Identifier model);

    static BakedModelManagerAccess of(BakedModelManager manager) {
        return (BakedModelManagerAccess) manager;
    }
}
