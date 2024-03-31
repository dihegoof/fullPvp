package com.br.fullPvp.kits;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.Getter;

public class PlayerKitManager {
	
	@Getter
	static PlayerKitManager instance = new PlayerKitManager();
	@Getter
	static List<PlayerKits> storagePlayerKits = new ArrayList<>();
	
	public void add(PlayerKits playerKit) { 
		if(!storagePlayerKits.contains(playerKit)) { 
			storagePlayerKits.add(playerKit);
		}
	}
	
	public void remove(PlayerKits playerKit) {
		if(storagePlayerKits.contains(playerKit)) { 
			storagePlayerKits.remove(playerKit);
		}
	}
	
	public PlayerKits get(UUID uniqueId) { 
		for(PlayerKits p : storagePlayerKits) {
			if(p.getUniqueId().equals(uniqueId)) { 
				return p;
			}
		}
		return null;
	}
	
	public void loadAllPlayerKits() { 
		try {
			int amount = 0;
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.PLAYER_KIT_SELECT_ALL.getQuery());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) { 
				List<KitCollected> kitCollected = new ArrayList<>();
				if(!rs.getString("kitcollected").equalsIgnoreCase("null")) { 
					for(String names : Arrays.asList(rs.getString("kitcollected").split(", "))) { 
						String[] split = names.split(";");
						kitCollected.add(new KitCollected(split[0], Long.valueOf(split[1])));
					}
				}
				add(new PlayerKits(UUID.fromString(rs.getString("uniqueid")), kitCollected));
				amount++;
			}
			Main.debug(amount > 0 ? "Carregado " + amount + " kit(s) recolhidos" : "Nenhuma kit recolhido foi carregado!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar os kits recolhidos!", e.getLocalizedMessage());
		}
	}
	
	public void saveAllPlayerKits() { 
		for(PlayerKits p : storagePlayerKits) {
			p.save();
		}
	}
}
