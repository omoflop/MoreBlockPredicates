package mod.omoflop.biometextures.data.conditions;

import net.minecraft.util.Identifier;

public class BiomeTextureCondition {
    public final Identifier biome;
    public final Identifier texture;
    public BiomeTextureCondition(Identifier biome, Identifier texture) {
        this.biome = biome;
        this.texture = texture;
    }
}