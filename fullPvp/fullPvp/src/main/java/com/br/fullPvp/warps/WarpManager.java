package com.br.fullPvp.warps;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;
import com.br.fullPvp.utils.ItemBuilder;

import lombok.Getter;

public class WarpManager {
	
	@Getter
	static WarpManager instance = new WarpManager();
	@Getter
	static List<Warp> storageWarps = new ArrayList<>();
	
	public void add(Warp warp) { 
		if(!storageWarps.contains(warp)) { 
			storageWarps.add(warp);
		}
	}
	
	public void remove(Warp warp) {
		if(storageWarps.contains(warp)) { 
			storageWarps.remove(warp);
		}
	}
	
	public Warp get(String name) { 
		for(Warp w : storageWarps) {
			if(w.getName().equals(name)) { 
				return w;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public void loadAllWarps() { 
		try {
			int amount = 0;
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.WARP_SELECT_ALL.getQuery());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) { 
				List<String> blockedCommands = new ArrayList<>();
				for(String names : Arrays.asList(rs.getString("blockedcommands").split(", "))) { 
					blockedCommands.add(names);
				}
				String[] split = rs.getString("icon").split(";");
				add(new Warp(rs.getString("name"), rs.getString("permission"), rs.getString("location"), rs.getBoolean("closed"), rs.getBoolean("exclusive"), rs.getBoolean("fly"), rs.getBoolean("pvp"),  new ItemBuilder(Material.getMaterial(Integer.valueOf(split[0]))).setDurability(Integer.valueOf(split[1])).getStack(), (blockedCommands.get(0).equals("null") ? new ArrayList<String>() : blockedCommands)));
				amount++;
			}
			Main.debug(amount > 0 ? "Carregada " + amount + " warp(s)" : "Nenhuma warp foi carregada!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar as warps!", e.getMessage());
		}
	}
	
	public void saveAllWarps() { 
		for(Warp w : storageWarps) {
			w.save();
		}
	}
	
	public boolean existsSpawn() { 
		return get("Spawn") != null;
	}
	
	public boolean existsSpawnVip() { 
		return get("Spawnvip") != null;
	}
}
