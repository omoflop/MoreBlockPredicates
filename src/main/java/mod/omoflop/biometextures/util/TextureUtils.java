package mod.omoflop.biometextures.util;

import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

public class TextureUtils {
    
    public static SpriteIdentifier getBlockTexture(Identifier id) {
        return new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, id);
    }
}
