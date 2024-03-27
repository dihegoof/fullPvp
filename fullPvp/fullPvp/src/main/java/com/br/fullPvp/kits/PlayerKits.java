package com.br.fullPvp.kits;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PlayerKits {
	
	UUID uniqueId;
	List<String> kitCollected;
	
	public void save() { 
		try {
			PreparedStatement stmt = null;
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.PLAYER_KIT_UPDATE.getQuery());
				stmt.setString(1, getKitCollected().isEmpty() ? "null" : getKitCollected().toString().replace("[", "").replace("]", ""));
				stmt.setString(2, getUniqueId().toString());
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.PLAYER_KIT_INSERT.getQuery());
				stmt.setString(1, getUniqueId().toString());
				stmt.setString(2, getKitCollected().isEmpty() ? "null" : getKitCollected().toString().replace("[", "").replace("]", ""));
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

}
