package com.echo28.bukkit.craftdispenser.crafts;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.echo28.bukkit.craftdispenser.CraftDispenser;
import com.echo28.bukkit.craftdispenser.Items;


abstract public class Craft
{
	protected CraftDispenser plugin;
	protected Block block = null;
	protected Dispenser dispenser = null;
	protected Inventory inventory;

	public Craft(CraftDispenser plugin, Block block)
	{
		this.plugin = plugin;
		this.block = (Block) block;
		this.dispenser = (Dispenser) this.block.getState();
		this.inventory = this.dispenser.getInventory();
	}

	abstract public boolean make();
	
	public int countAir()
	{
		int airCount = 0;
		for (int i = 0; i < 9; i++)
		{
			if (inventory.getItem(i).getType() == Material.AIR)
				airCount += 1;
		}
		return airCount;
	}

	protected boolean checkVerticalItems(int[][] items)
	{
		if (countAir() != (9 - items.length))
			return false;
		
		
		if (items.length == 2) {
			for (int i = 0; i < 6; i++){
				if(Items.itemMatchesStack(items[0], inventory.getItem(i)) &&
						Items.itemMatchesStack(items[1], inventory.getItem(i+3))) {
					newSubtractItem(i, items[0][3]);
					newSubtractItem(i+3, items[1][3]);
					return true;
				}
			}
			return false;
		} else if (items.length == 3) {
			for (int i = 0; i < 3; i++){
				if(Items.itemMatchesStack(items[0], inventory.getItem(i)) &&
						Items.itemMatchesStack(items[1], inventory.getItem(i+3)) &&
						Items.itemMatchesStack(items[2], inventory.getItem(i+6))) {
					newSubtractItem(i, items[0][3]);
					newSubtractItem(i+3, items[1][3]);
					newSubtractItem(i+6, items[2][3]);
					return true;
				}
			}
			return false;
		}
		
		return false;
	}
	
	protected boolean checkCustomItems(int[][] items)
	{
		int[] slotsToSubtract = new int[9];
		for (int i = 0; i < 9; i++)
		{
			int[] item = items[i];
			
			if (item[0] == 0)
				continue;
			
			if (Items.itemMatchesStack(item, inventory.getItem(i)))
				slotsToSubtract[i] = item[3];
			else
				return false;
		}
		
		newSubtractItems(slotsToSubtract);
		return true;
	}
	
	public void newSubtractItems(int[] slots)
	{
		for (int slot = 0; slot < 9; slot++)
		{
			newSubtractItem(slot, slots[slot]);
		}
	}

	public void newSubtractItem(int slot, int howMuch)
	{
		if (howMuch == 0) return;
		ItemStack items = inventory.getItem(slot);
		int amount = items.getAmount() - howMuch;
		if (amount < 0) {
			System.out.printf("%d %d %s", amount, howMuch, items.toString());
			new Exception().printStackTrace();
			items = null;
		}
		else if (amount == 0)
			items = null;
		else
			items.setAmount(amount);
		
		inventory.setItem(slot, items);
	}

	/*public void emptyBucket(int slot)
	{
		ItemStack items;
		items = inventory.getItem(slot);
		if ((items.getType() == Material.LAVA_BUCKET) || (items.getType() == Material.WATER_BUCKET) || (items.getType() == Material.MILK_BUCKET))
		{
			items = new ItemStack(Material.BUCKET, 1);
			inventory.setItem(slot, items);
		}
	}*/
	
}
