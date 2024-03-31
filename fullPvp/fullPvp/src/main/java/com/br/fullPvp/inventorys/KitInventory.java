package com.br.fullPvp.inventorys;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.kits.KitManager;
import com.br.fullPvp.kits.Kit;
import com.br.fullPvp.kits.Kit.Delay;
import com.br.fullPvp.utils.ItemBuilder;
import com.br.fullPvp.utils.TimeManager;
import com.br.fullPvp.utils.Utils;

import lombok.Getter;

public class KitInventory extends Utils {

	public enum TypeInventoryKit {

		KITS, KIT_SAME_TIME, VIEW_KIT;

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
	static KitInventory instance = new KitInventory();
	
	@SuppressWarnings("deprecation")
	public void create(Player player, TypeInventoryKit typeInventory, Delay delay, Account account, Kit kit, int page) { 
		Inventory inventory = null;
		ItemBuilder ib = null;
		if(typeInventory.equals(TypeInventoryKit.KITS)) { 
			inventory = Bukkit.createInventory(player, 27, "Kit");
			int slot = 10;
			for(Delay d : Delay.values()) { 
				ib = new ItemBuilder(Material.getMaterial(351)).setDurability(d.getId()).setName("§aKit " + d.getName()).setDescription("§7Confira os kits disponíveis para esta classificação.");
				ib.build(inventory, slot);
				slot += 2;
			}
		} else if(typeInventory.equals(TypeInventoryKit.KIT_SAME_TIME)) { 
			inventory = Bukkit.createInventory(player, 54, "Kits " + delay.getName());
			List<Kit> list = new ArrayList<>();
			for(Kit k : KitManager.getStorageKits()) { 
				if(k.getDelay().equals(delay)) {
					 list.add(k);
				}
			}
			Kit ki = null;
			int start = (page > 1 ? (28 * page) - 28 : 0);
			if(list.isEmpty()) { 
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhuma kit adicionado");
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
				ki = list.get(start);
				ib = new ItemBuilder(ki.getIcon().getType()).setDurability(ki.getIcon().getDurability()).setName("§aKit " + ki.getName()).setDescription("§fTempo de espera §7" + (ki.getDelay().equals(Delay.CUSTOM) ? compareTime(TimeManager.getInstance().getTime(ki.getTimeCustom())) : compareTime(TimeManager.getInstance().getTime(ki.getDelay().getDelay()))), "§7Clique aqui para recolher!");
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
		} else if(typeInventory.equals(TypeInventoryKit.VIEW_KIT)) { 
			inventory = Bukkit.createInventory(player, 54, "Visualizando kit " + kit.getName());
			List<ItemStack> list = kit.getItens();
			ItemStack itemStack = null;
			int start = (page > 1 ? (28 * page) - 28 : 0);
			if(list.isEmpty()) { 
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhuma item adicionado");
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
				itemStack = list.get(start);
				ib = new ItemBuilder().chanceItemStack(itemStack);
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
