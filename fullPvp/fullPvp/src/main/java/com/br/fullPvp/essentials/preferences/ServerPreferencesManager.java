package com.br.fullPvp.essentials.preferences;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.Getter;
import lombok.Setter;

public class ServerPreferencesManager {
	
	@Getter
	static ServerPreferencesManager instance = new ServerPreferencesManager();
	@Getter
	@Setter
	static ServerPreferences serverPreference = null;
	
	public void loadPreferences() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.PREFERENCES_SERVER_SELECT.getQuery());
			stmt.setInt(1, 1);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) { 
				setServerPreference(new ServerPreferences(rs.getBoolean("chatlocal"), rs.getBoolean("chatglobal"), rs.getBoolean("manutence")));
			} else { 
				setServerPreference(new ServerPreferences(true, true, false));
			}
			Main.debug("Preferências do servidor salvas!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar as preferências do servidor!", e.getLocalizedMessage());
		}
	}
	
	public void savePreferences() { 
		getServerPreference().save();
	}

}
