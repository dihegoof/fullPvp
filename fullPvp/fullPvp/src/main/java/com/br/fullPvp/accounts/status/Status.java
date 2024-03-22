package com.br.fullPvp.accounts.status;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Status {
	
	public enum TypeUnit { 
		
		KILLS, DEATHS;
		
	}
	
	UUID uniqueId;
	double kills, deaths, kdr;
	
	public void save() { 
		try {
			PreparedStatement stmt = null;
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.STATUS_UPDATE.getQuery());
				stmt.setDouble(1, getKills());
				stmt.setDouble(2, getDeaths());
				stmt.setString(3, getUniqueId().toString());
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.STATUS_INSERT.getQuery());
				stmt.setString(1, getUniqueId().toString());
				stmt.setDouble(2, getKills());
				stmt.setDouble(3, getDeaths());
			}
			stmt.executeUpdate();
			Main.debug("Status de " + getUniqueId() + " salva!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar status de " + getUniqueId() + "!", e.getLocalizedMessage());
		}
	}
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.STATUS_SELECT.getQuery());
			stmt.setString(1, getUniqueId().toString());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void add(double amount, TypeUnit typeUnit) { 
		if(typeUnit.equals(TypeUnit.KILLS)) { 
			this.kills += amount;
		} else { 
			this.deaths += amount;
		}
	}
	
	public void remove(double amount, TypeUnit typeUnit) { 
		if(typeUnit.equals(TypeUnit.KILLS)) { 
			this.kills = (kills < amount ? 0 : kills - amount);
		} else { 
			this.deaths = (deaths < amount ? 0 : deaths - amount);
		}
	}
	
	public double getKdr() {
		if(kills == 0 && deaths == 0) { 
			return 0.0;
		}
		return kills / deaths;
	}
}
