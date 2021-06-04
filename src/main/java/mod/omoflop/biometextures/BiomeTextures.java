package mod.omoflop.biometextures;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BiomeTextures implements ModInitializer {
	@Override
	public void onInitialize() {
		System.out.println("Hello Fabric world!");


		Registry.register(Registry.ITEM, new Identifier("biome_textures", "model_test"), new Item(new Item.Settings()));
	}
}
