package mod.omoflop.biometextures;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

public class TestModelProvider implements ModelResourceProvider {
    public static final FourSidedFurnaceModel FOUR_SIDED_FURNACE_MODEL = new FourSidedFurnaceModel();
    public static final Identifier FOUR_SIDED_FURNACE_MODEL_BLOCK = new Identifier("biome_textures:block/four_sided_furnace");
    public static final Identifier FOUR_SIDED_FURNACE_MODEL_ITEM  = new Identifier("biome_textures:item/four_sided_furnace");

    @Override
    public @Nullable UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext context) throws ModelProviderException {
        if(identifier.equals(FOUR_SIDED_FURNACE_MODEL_BLOCK) || identifier.equals(FOUR_SIDED_FURNACE_MODEL_ITEM)) {
            return FOUR_SIDED_FURNACE_MODEL;
        } else {
            return null;
        }
    }
}
