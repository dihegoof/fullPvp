package com.br.fullPvp.mines;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.br.fullPvp.utils.ItemBuilder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Position {
	
	Location pos1, pos2;
	
	public Position() { }

	public void give(Player player) {
		player.getInventory().addItem(new ItemBuilder(Material.WOOD_AXE).setName("§aMarcador").getStack());
	}
	
	@SuppressWarnings("deprecation")
	public void remove(Player player) {
		for(ItemStack i : player.getInventory().getContents()) { 
			if(new ItemBuilder().checkItem(i, "§aMarcador")) { 
				player.getInventory().remove(i);
			}
		}
	}
}
