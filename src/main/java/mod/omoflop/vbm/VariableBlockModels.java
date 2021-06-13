package mod.omoflop.vbm;

import java.util.logging.Logger;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class VariableBlockModels implements ModInitializer {
	public static final Logger LOGGER = Logger.getLogger("VBM");
	
	@Override
	public void onInitialize() {
		System.out.println("Hello Fabric world!");

		Block block  = new Block(Block.Settings.of(Material.AGGREGATE));
		Block block2 = new Block(Block.Settings.of(Material.AGGREGATE));
		Registry.register(Registry.BLOCK, new Identifier("vbm:test1"), block);
		Registry.register(Registry.ITEM,  new Identifier("vbm:test1"), new BlockItem(block, new Item.Settings()));

		Registry.register(Registry.BLOCK, new Identifier("vbm:test2"), block2);
		Registry.register(Registry.ITEM,  new Identifier("vbm:test2"), new BlockItem(block2, new Item.Settings()));

	}
}
