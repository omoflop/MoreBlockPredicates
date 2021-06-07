package mod.omoflop.biometextures.data;

import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;

public abstract class VBTCondition {
    public VBTCondition(JsonObject condition, Identifier model) {

    }
    public abstract Identifier getModel();
}
