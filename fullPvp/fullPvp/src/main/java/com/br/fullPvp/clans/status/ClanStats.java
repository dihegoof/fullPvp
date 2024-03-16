package com.br.fullPvp.clans.status;

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
public class ClanStats {
	
	String clanName;
	double kills, deaths, killStreak;
	
	public void save() { 
		try {
			PreparedStatement stmt = null;
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.CLAN_STATUS_UPDATE.getQuery());
				stmt.setDouble(1, getKills());
				stmt.setDouble(2, getDeaths());
				stmt.setDouble(3, getKillStreak());
				stmt.setString(4, getClanName());
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.CLAN_STATUS_INSERT.getQuery());
				stmt.setString(1, getClanName());
				stmt.setDouble(2, getKills());
				stmt.setDouble(3, getDeaths());
				stmt.setDouble(4, getKillStreak());
			}
			stmt.executeUpdate();
			Main.debug("Status do clan " + getClanName() + " salvo!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar status do clan " + getClanName() + "!", e.getLocalizedMessage());
		}
	}	
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.CLAN_STATUS_SELECT.getQuery());
			stmt.setString(1, getClanName());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void delete() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.CLAN_STATUS_DELETE.getQuery());
			stmt.setString(1, getClanName());
			stmt.executeUpdate();
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao deletar status do clan " + getClanName() + "!", e.getLocalizedMessage());
		}
	}

	public void add(String name) { 
		if(name.equals("k")) { 
			this.kills += 1;
		} else if(name.equals("d")) { 
			this.deaths += 1;
		} else if(name.equals("ks")) { 
			this.killStreak += 1;
		}
	}
}
