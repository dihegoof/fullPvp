package com.br.fullPvp.essentials.preferences;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ServerPreferences {
	
	boolean chatLocal, chatGlobal, manutence;

	public void save() { 
		try {
			PreparedStatement stmt = null;
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.PREFERENCES_SERVER_UPDATE.getQuery());
				stmt.setBoolean(1, isChatLocal());
				stmt.setBoolean(2, isChatGlobal());
				stmt.setBoolean(3, isManutence());
				stmt.setInt(4, 1);
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.PREFERENCES_SERVER_INSERT.getQuery());
				stmt.setInt(1, 1);
				stmt.setBoolean(2, isChatLocal());
				stmt.setBoolean(3, isChatGlobal());
				stmt.setBoolean(4, isManutence());
			}
			stmt.executeUpdate();
			Main.debug("Preferências do servidor salvas!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar preferências do servidor!", e.getLocalizedMessage());
		}
	}	
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.PREFERENCES_SERVER_SELECT.getQuery());
			stmt.setInt(1, 1);
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
}
