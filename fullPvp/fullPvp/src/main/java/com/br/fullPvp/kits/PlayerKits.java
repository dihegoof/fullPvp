package com.br.fullPvp.kits;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.br.fullPvp.Main;
import com.br.fullPvp.kits.Kit.Delay;
import com.br.fullPvp.mysql.SqlQuerys;
import com.br.fullPvp.utils.TimeManager;
import com.br.fullPvp.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PlayerKits extends Utils {
	
	UUID uniqueId;
	List<KitCollected> KitCollected;
	
	public void save() { 
		try {
			PreparedStatement stmt = null;
			List<String> list = new ArrayList<>();
			for(KitCollected k : getKitCollected()) { 
				list.add(k.getName() + ";" + k.getTimeAllowed());
			}
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.PLAYER_KIT_UPDATE.getQuery());
				stmt.setString(1, list.isEmpty() ? "null" : list.toString().replace("[", "").replace("]", ""));
				stmt.setString(2, getUniqueId().toString());
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.PLAYER_KIT_INSERT.getQuery());
				stmt.setString(1, getUniqueId().toString());
				stmt.setString(2, list.isEmpty() ? "null" : list.toString().replace("[", "").replace("]", ""));
			}
			stmt.executeUpdate();
			Main.debug("Kits recolhidos de " + getUniqueId().toString() + " salvo!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar kits recolhidos de " + getUniqueId().toString() + "!", e.getLocalizedMessage());
		}
	}	
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.PLAYER_KIT_SELECT.getQuery());
			stmt.setString(1, getUniqueId().toString());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void delete() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.PLAYER_KIT_DELETE.getQuery());
			stmt.setString(1, getUniqueId().toString());
			stmt.executeUpdate();
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao deletar kits recolhidos de " + getUniqueId().toString() + "!", e.getLocalizedMessage());
		}
	}
	
	public boolean allowGive(Kit kit) { 
		boolean has = false, searched = false;
		for(KitCollected k : getKitCollected()) { 
			if(k.getName().equals(kit.getName())) {
				searched = true;
				if(k.getTimeAllowed() < System.currentTimeMillis()) { 
					has = true;
				}
			}
		}
		if(!searched) { 
			has = true;
		}
		return has;
	}
	
	public void add(Kit kit) { 
		getKitCollected().add(new KitCollected(kit.getName(), kit.getDelay().equals(Delay.CUSTOM) ? TimeManager.getInstance().getTime(kit.getTimeCustom()) : TimeManager.getInstance().getTime(kit.getDelay().getDelay())));
	}

	public String getTimeWait(Kit kit) {
		long remaing = 0L;
		for(KitCollected k : getKitCollected()) { 
			if(k.getName().equals(kit.getName())) { 
				remaing = k.getTimeAllowed();
			}
		}
		return compareTime(remaing);
	}
}
