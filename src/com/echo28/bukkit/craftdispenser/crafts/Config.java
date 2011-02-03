package com.echo28.bukkit.craftdispenser.crafts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.util.config.Configuration;

import com.echo28.bukkit.craftdispenser.CraftDispenser;
import com.echo28.bukkit.craftdispenser.ItemSpec;


public class Config extends Craft
{
	private List<Configuration> configs = new ArrayList<Configuration>();

	public Config(CraftDispenser plugin, Block block)
	{
		super(plugin, block);

		loadCustom();
	}

	private void loadCustom()
	{
		for (File file : plugin.getDataFolder().listFiles())
		{
			if (file.getName().equalsIgnoreCase("config.yml"))
				continue;
			
			Configuration config = new Configuration(file);
			config.load();
			configs.add(config);
		}
	}

	@Override
	public boolean make()
	{
		for (Configuration config : configs)
		{
			if (checkConfig(config))
			{
				ItemSpec[] items = ItemSpec.parseItemList(config.getStringList("craft", null));
				
				CraftDispenser.dispenseItems(block, ItemSpec.createItemStacks(items));
				
				
				ItemSpec[] outputItems = ItemSpec.parseItemList(config.getStringList("output-items", null));
				if (outputItems != null && outputItems.length == 9) {
					for (int i = 0; i < 9; i++) {
						ItemSpec item = outputItems[i];
						if (item.id != 0 && item.id != -1) {
							inventory.setItem(i, item.createItemStack());
						}
					}
				}
				
				return true;
			}
		}
		return false;
	}

	private boolean checkConfig(Configuration config)
	{
		if (config.getProperty("input-items-vertical") != null) {
			return checkVerticalItems(ItemSpec.parseItemList(config.getStringList("input-items-vertical", null)));
		}
		if (config.getProperty("input-items") != null) {
			return checkCustomItems(ItemSpec.parseItemList(config.getStringList("input-items", null)));
		}
		return false;
	}
}
