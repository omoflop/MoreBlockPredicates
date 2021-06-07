package mod.omoflop.biometextures;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import mod.omoflop.biometextures.util.BiomeUtils;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

public class DynamicTexturedModel implements UnbakedModel, BakedModel, FabricBakedModel {

    private final SpriteIdentifier[] SPRITE_IDS;
    private Sprite[] SPRITES;
    private Identifier BIOME;

    public DynamicTexturedModel(SpriteIdentifier spriteDefault, SpriteIdentifier spriteBiome, Identifier biome) {
        SPRITE_IDS = new SpriteIdentifier[] { 
            spriteDefault,
            spriteBiome
        };
        BIOME = biome;
        SPRITES = new Sprite[SPRITE_IDS.length];
    }

    //! Stuff for rendering the model as an item, may not use in the final mod
    private final Identifier DEFAULT_BLOCK_MODEL = new Identifier("minecraft:block/block");
    private ModelTransformation transformation;

    //! Depends on the default block model to show as an item properly
    @Override
    public Collection<Identifier> getModelDependencies() {
        return Arrays.asList(DEFAULT_BLOCK_MODEL);
    }
    
    @Override
    public ModelTransformation getTransformation() {
        return transformation;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    //! makes items render correctly apparently
    @Override
    public boolean isSideLit() {
        return true;
    }

    // False to trigger fabric's rendering
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        MinecraftClient inst = MinecraftClient.getInstance();
        ClientWorld world = inst.world;
        context.meshConsumer().accept(getMesh(world, pos));
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        MinecraftClient instance = MinecraftClient.getInstance();
        ClientWorld world = instance.world;
        context.meshConsumer().accept(getMesh(world, instance.player.getBlockPos()));
    }

    private Mesh getMesh(World world, BlockPos pos) {
        Identifier biomeId = BiomeUtils.getIdentifier(world.getBiome(pos));
        int id = 0;
        if (biomeId.equals(BIOME)) id = 1;
        return makeTexturedCube(SPRITES[id]);
    }

    
    

    

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
        return null; // We dont use this because we're using FabricBakedModel instead
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true; // Gives the block a shadow based on surrounding blocks
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    // Used for block breaking particles
    @Override
    public Sprite getSprite() {
        return SPRITES[1];
    }


    // Every texture this model requires (including it's dependencies textures!)
    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return Arrays.asList(SPRITE_IDS);
    }

    public Mesh makeTexturedCube(Sprite texture) {

        // Build the mesh using renderer api
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        

        for(Direction dir : Direction.values()) {
            // Add a new face to the mesh
            emitter.square(dir, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f); // Arbitrary values that probably make a square

            // Set the sprite of that new face
            // Has to be called after .square
            // We dont use UV coordinates, so we use BAKE_LOCK_UV to use the whole texture
            emitter.spriteBake(0, texture, MutableQuadView.BAKE_LOCK_UV);

            // Enable texture usage (?????)
            emitter.spriteColor(0, -1, -1, -1, -1);

            // Add the quad to the mesh
            emitter.emit();
        }
        return builder.build();
    }

    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        // Load the default block model
        JsonUnbakedModel defaultBlockModel = (JsonUnbakedModel) loader.getOrLoadModel(DEFAULT_BLOCK_MODEL);
        // Get its model transformation
        transformation = defaultBlockModel.getTransformations();
        
        
        // Loads every sprite that may be used
        for(int i = 0; i < SPRITE_IDS.length; i++) {
            SPRITES[i] = textureGetter.apply(SPRITE_IDS[i]);
        }
        return this;
    }

}
