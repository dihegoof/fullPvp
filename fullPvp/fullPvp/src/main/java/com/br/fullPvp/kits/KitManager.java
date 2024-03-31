package com.br.fullPvp.kits;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.br.fullPvp.Main;
import com.br.fullPvp.kits.Kit.Delay;
import com.br.fullPvp.mysql.SqlQuerys;
import com.br.fullPvp.utils.ItemBuilder;
import com.br.fullPvp.utils.SerializeItemStack;

import lombok.Getter;

public class KitManager {
	
	@Getter
	static KitManager instance = new KitManager();
	@Getter
	static List<Kit> storageKits = new ArrayList<>();
	
	public void add(Kit kit) { 
		if(!storageKits.contains(kit)) { 
			storageKits.add(kit);
		}
	}
	
	public void remove(Kit kit) {
		if(storageKits.contains(kit)) { 
			storageKits.remove(kit);
		}
	}
	
	public Kit get(String name) { 
		for(Kit k : storageKits) {
			if(k.getName().equals(name)) { 
				return k;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public void loadAllKits() { 
		try {
			int amount = 0;
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.KIT_SELECT_ALL.getQuery());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) { 
				List<ItemStack> itens = new ArrayList<>();
				if(!rs.getString("itens").equalsIgnoreCase("null")) { 
					for(String names : Arrays.asList(rs.getString("itens").split(", "))) { 
						itens.add(SerializeItemStack.getInstance().desconvert(names));
					}
				}
				String[] split = rs.getString("icon").split(";");
				add(new Kit(rs.getString("name"), rs.getString("permission"), rs.getString("timecustom"), new ItemBuilder(Material.getMaterial(Integer.valueOf(split[0]))).setDurability(Integer.valueOf(split[1])).getStack(), Delay.valueOf(rs.getString("delay")), itens, rs.getBoolean("free")));
				amount++;
			}
			Main.debug(amount > 0 ? "Carregado " + amount + " kit(s)" : "Nenhuma kit foi carregado!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar os kits!", e.getLocalizedMessage());
		}
	}
	
	public void saveAllKits() { 
		for(Kit k : storageKits) {
			k.save();
		}
	}
}
