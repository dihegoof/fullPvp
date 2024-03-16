package com.br.fullPvp.inventorys;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.tags.Tag;
import com.br.fullPvp.tags.TagManager;
import com.br.fullPvp.utils.ItemBuilder;
import com.br.fullPvp.utils.Utils;

import lombok.Getter;

public class TagInventory extends Utils {
	
	public enum TypeInventoryTag { 
		
		MENU, YOUR_TAGS, LIST_TAGS;
		
	}
	
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
	static TagInventory instance = new TagInventory();

	public void create(Player player, Account account, TypeInventoryTag typeInventory, int page) { 
		Inventory inventory = null;
		ItemBuilder ib = null;
		if(typeInventory.equals(TypeInventoryTag.MENU)) { 
			inventory = Bukkit.createInventory(player, 27, "Tag");
			ib = new ItemBuilder(Material.CHEST).setName("§aSuas tags").setDescription("§7Confira as tags que você pode utilizar!");
			ib.build(inventory, 12);
			
			ib = new ItemBuilder(Material.BOOK_AND_QUILL).setName("§aTags").setDescription("§7Dê uma olhada nas tags existentes!");
			ib.build(inventory, 14);
		} else if(typeInventory.equals(TypeInventoryTag.YOUR_TAGS)) { 
			inventory = Bukkit.createInventory(player, 54, "Suas tags");
			List<Tag> list = account.getTagsReal();
			Tag tag = null;
			int start = (page > 1 ? (28 * page) - 28 : 0);
			if(list.isEmpty()) { 
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhuma tag encontrada");
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
				tag = list.get(start);
				ib = new ItemBuilder(Material.PAPER).setName("§a" + tag.getName()).setDescription("§fPrefixo " + tag.getPrefix(), "§7Clique aqui para selecionar esta tag!");
				ib.build(inventory, x);
				start++;
			}
			ib = new ItemBuilder(Material.MILK_BUCKET).setName("§aTag do grupo atual").setDescription("§7Clique aqui atualizar com sua tag do grupo!");
			ib.build(inventory, 49);
			if(page > 1) { 
				ib = new ItemBuilder(Material.ARROW).setName("§cPágina " + (page - 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 45);
			} else { 
				ib = new ItemBuilder(Material.ARROW).setName("§cVoltar").setDescription("§7Clique aqui para voltar ao menu!");
				ib.build(inventory, 45);
			}
			if(inventory.getItem(43) != null) { 
				ib = new ItemBuilder(Material.ARROW).setName("§aPágina " + (page + 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 53);
			}
		} else if(typeInventory.equals(TypeInventoryTag.LIST_TAGS)) { 
			inventory = Bukkit.createInventory(player, 54, "Tags");
			List<Tag> list = TagManager.getStorageTags();
			Tag tag = null;
			int start = (page > 1 ? (28 * page) - 28 : 0);
			if(list.isEmpty()) { 
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhuma tag encontrada");
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
				tag = list.get(start);
				ib = new ItemBuilder(Material.PAPER).setName("§a" + tag.getName()).setDescription("§fPrefixo " + tag.getPrefix(), "§fGrátis? §7" + (tag.isFree() ? "§aSim" : "§cNão"), (tag.isFree() ? "§7Clique aqui para reivindicar!" : (account.hasPermission(tag.getPermission()) ? "§7Clique aqui para reivindicar!" : "§cVocê não possui permissão para reivindicar esta tag!")));
				ib.build(inventory, x);
				start++;
			}
			if(page > 1) { 
				ib = new ItemBuilder(Material.ARROW).setName("§cPágina " + (page - 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 45);
			} else { 
				ib = new ItemBuilder(Material.ARROW).setName("§cVoltar").setDescription("§7Clique aqui para voltar ao menu!");
				ib.build(inventory, 45);
			}
			if(inventory.getItem(43) != null) { 
				ib = new ItemBuilder(Material.ARROW).setName("§aPágina " + (page + 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 53);
			}
		}
		player.openInventory(inventory);
	}
}
