package com.br.fullPvp.inventorys;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.utils.ItemBuilder;
import com.br.fullPvp.utils.Utils;
import com.br.fullPvp.warps.Warp;
import com.br.fullPvp.warps.WarpManager;

import lombok.Getter;

public class WarpInventory extends Utils {
	
	@Getter
	private static List<Integer> empty = new ArrayList<>();
	static {
		for (int i = 0; i <= 9; i++)
			empty.add(i);
		for (int i = 45; i <= 53; i++)
			empty.add(i);
		empty.add(18);
		empty.add(27);
		empty.add(36);
		empty.add(17);
		empty.add(26);
		empty.add(35);
		empty.add(44);
	}
	
	@Getter
	static WarpInventory instance = new WarpInventory();
	
	public void create(Player player, Account account,  int page) { 
		Inventory inventory = Bukkit.createInventory(player, 54, "Warps");
		List<Warp> list = WarpManager.getStorageWarps();
		Warp warp = null;
		ItemBuilder ib = null;
		int start = (page > 1 ? (28 * page) - 28 : 0);
		if(list.isEmpty()) { 
			ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhuma warp definida");
			ib.build(inventory, 22);
		}
		for(int x = 0; x < inventory.getSize(); x++) { 
			try {  
				if(list.get(start) == null) continue; 
			} catch (Exception e) { 
				break; 
			}
			if(inventory.getItem(x) != null) continue;
			if(empty.contains(x)) continue;
			warp = list.get(start);
			if(warp.isClosed() && account.hasPermission(warp.getPermission())) {
				ib = new ItemBuilder(warp.getIcon().getType()).setDurability(warp.getIcon().getDurability()).setName("§a" + warp.getName()).setDescription("§e" + warp.getPlayerAccounts().size() + " jogadores.", "§7Clique aqui para entrar!", "§cEsta warp está fechada para jogadores sem permissão!");
				ib.build(inventory, x);
			} else if(!warp.isExclusive()) { 
				ib = new ItemBuilder(warp.getIcon().getType()).setDurability(warp.getIcon().getDurability()).setName("§a" + warp.getName()).setDescription("§e" + warp.getPlayerAccounts().size() + " jogadores.", "§7Clique aqui para entrar!");
				ib.build(inventory, x);
			}
			start++;
		}
		if(page > 1) { 
			ib = new ItemBuilder(Material.ARROW).setName("§cPágina " + (page - 1)).setDescription("§7Clique aqui mudar de página!");
			ib.build(inventory, 45);
		}
		if(inventory.getItem(43) != null) { 
			ib = new ItemBuilder(Material.ARROW).setName("§aPágina " + (page + 1)).setDescription("§7Clique aqui mudar de página!");
			ib.build(inventory, 53);
		}
		player.openInventory(inventory);
	}
}
