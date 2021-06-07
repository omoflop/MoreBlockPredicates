package mod.omoflop.biometextures;

import org.jetbrains.annotations.Nullable;

import mod.omoflop.biometextures.util.TextureUtils;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

public class TestModelProvider implements ModelResourceProvider {
    public final DynamicTexturedModel DYNAMIC_TEST_MODEL_1 = new DynamicTexturedModel(
        TextureUtils.getBlockTexture(new Identifier("minecraft:block/dirt")), 
        TextureUtils.getBlockTexture(new Identifier("minecraft:block/stone")), 
        new Identifier("minecraft:ocean"));
    public static final Identifier TEST_1_BLOCK = new Identifier("biome_textures:block/test1");
    public static final Identifier TEST_1_ITEM  = new Identifier("biome_textures:item/test1");

    public final DynamicTexturedModel DYNAMIC_TEST_MODEL_2 = new DynamicTexturedModel(
        TextureUtils.getBlockTexture(new Identifier("minecraft:block/emerald_ore")), 
        TextureUtils.getBlockTexture(new Identifier("minecraft:block/furnace_front")), 
        new Identifier("minecraft:beach"));
    public static final Identifier TEST_2_BLOCK = new Identifier("biome_textures:block/test2");
    public static final Identifier TEST_2_ITEM  = new Identifier("biome_textures:item/test2");


    @Override
    public @Nullable UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext context) throws ModelProviderException {
        
        
        if(identifier.equals(TEST_1_BLOCK) || identifier.equals(TEST_1_ITEM)) {
            return DYNAMIC_TEST_MODEL_1;
        } else if (identifier.equals(TEST_2_BLOCK) || identifier.equals(TEST_2_ITEM)) {
            return DYNAMIC_TEST_MODEL_2;
        } else {
            return null;
        }
    }
}
