package mod.omoflop.mbp.mixin;

import com.google.common.collect.ImmutableSortedMap;
import mod.omoflop.mbp.accessor.StateManagerAccess;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(StateManager.class)
public class StateManagerMixin implements StateManagerAccess {

    @Shadow @Final private ImmutableSortedMap<String, Property<?>> properties;

    @Override @Unique
    public boolean hasProperty(String name) {
        return this.properties.containsKey(name);
    }
}
