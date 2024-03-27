package com.br.fullPvp.shop.item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;
import com.br.fullPvp.shop.session.SessionManager;
import com.br.fullPvp.utils.SerializeItemStack;

import lombok.Getter;

public class ItemManager {
	
	@Getter
	static ItemManager instance = new ItemManager();
	@Getter
	static List<Item> storageItens = new ArrayList<>();
	
	public void add(Item item) { 
		if(!storageItens.contains(item)) { 
			storageItens.add(item);
		}
	}
	
	public void remove(Item item) {
		if(storageItens.contains(item)) { 
			storageItens.remove(item);
		}
	}
	
	public Item get(String uniqueId) { 
		for(Item i : storageItens) {
			if(i.getUniqueId().equals(uniqueId)) { 
				return i;
			}
		}
		return null;
	}
	
	public Item getByDisplayName(String displayName) { 
		for(Item i : storageItens) {
			if(i.getItemStack().getItemMeta().getDisplayName().equals(displayName)) { 
				return i;
			}
		}
		return null;
	}
	
	public Item get(Material material, int durability) { 
		for(Item i : storageItens) {
			if(i.getItemStack().getType().equals(material) && i.getItemStack().getDurability() == durability) { 
				return i;
			}
		}
		return null;
	}

	public void loadAllItens() { 
		try {
			int amount = 0;
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.ITEM_SELECT_ALL.getQuery());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) { 
				add(new Item(rs.getString("uniqueid"), SerializeItemStack.getInstance().desconvert(rs.getString("item")), SessionManager.getInstance().get(rs.getString("session")), rs.getDouble("price"), rs.getDouble("descount"), rs.getBoolean("promo")));
				amount++;
			}
			Main.debug(amount > 0 ? "Carregado " + amount + " item(ns)" : "Nenhuma item foi carregado!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar os itens!", e.getLocalizedMessage());
		}
	}
	
	public void saveAllItens() { 
		for(Item i : storageItens) {
			i.save();
		}
	}
}
