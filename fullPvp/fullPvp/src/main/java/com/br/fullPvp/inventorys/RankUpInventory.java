package com.br.fullPvp.inventorys;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.TypeCoin;
import com.br.fullPvp.ranks.RankManager;
import com.br.fullPvp.ranks.Requeriments;
import com.br.fullPvp.utils.HeadLink;
import com.br.fullPvp.utils.ItemBuilder;
import com.br.fullPvp.utils.Utils;

import lombok.Getter;

public class RankUpInventory extends Utils {
	
	@Getter
	static RankUpInventory instance = new RankUpInventory();
	
	@SuppressWarnings("deprecation")
	public void create(Player player, Account account) { 
		Inventory inventory = Bukkit.createInventory(player, 27, "Upe de rank");
		ItemBuilder ib = null;

		if(RankManager.getInstance().get(account.getRankName()) != null) { 
			ib = new ItemBuilder(Material.SKULL_ITEM).setDurability(3).setName("§a" + account.getRankName()).setSkinUrl(HeadLink.valueOf(account.getRankName().substring(0, 1)).getLink()).setDescription(
					"§7Seu rank atual!");
			ib.build(inventory, 12);
		}
		
		ib = new ItemBuilder(Material.ARROW).setName("§a>");
		ib.build(inventory, 13);
		
		if(RankManager.getInstance().get(account.getRankName()).nextRank() != null) { 
			StringBuilder list = new StringBuilder();
			for(Requeriments r : RankManager.getInstance().get(account.getRankName()).nextRank().getRequirements()) { 
				list.append(TypeCoin.formatExactFormatter(r.getTypeCoin(), r.getValue()) + ", ");
			}
			
			ib = new ItemBuilder(Material.SKULL_ITEM).setDurability(3).setName("§a" + RankManager.getInstance().get(account.getRankName()).nextRank().getName()).setSkinUrl(HeadLink.valueOf(RankManager.getInstance().get(account.getRankName()).nextRank().getName().substring(0, 1)).getLink()).setDescription(
					"§fRequisitos:",
					list.length() <= 0 ? "§cNenhum requisito adicionado!" : "§7" + list.toString().substring(0, list.length() - 2));
			ib.build(inventory, 14);
			
			ib = new ItemBuilder(Material.getMaterial(351)).setDurability(10).setName("§aConfirmar").setDescription("§7Por favor, confirme sua decisão!");
			ib.build(inventory, 16);
		}
		
		player.openInventory(inventory);
	}
}
