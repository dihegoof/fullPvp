package com.br.fullPvp.ranks;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.br.fullPvp.Main;
import com.br.fullPvp.accounts.TypeCoin;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Rank {
	
	int id;
	String name, prefix;
	int priority;
	List<Requeriments> requirements;
	boolean defaulted;
	
	public void save() { 
		try {
			PreparedStatement stmt = null;
			List<String> list = new ArrayList<>();
			for(Requeriments r : getRequirements()) { 
				list.add(r.getTypeCoin().toString() + ";" + r.getValue());
			}
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.RANK_UPDATE.getQuery());
				stmt.setString(1, getPrefix());
				stmt.setInt(2, getPriority());
				stmt.setBoolean(3, isDefaulted());
				stmt.setString(4, list.isEmpty() ? "null" : list.toString().replace("[", "").replace("]", ""));
				stmt.setString(5, getName());
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.RANK_INSERT.getQuery());
				stmt.setInt(1, getId());
				stmt.setString(2, getName());
				stmt.setString(3, getPrefix());
				stmt.setInt(4, getPriority());
				stmt.setBoolean(5, isDefaulted());
				stmt.setString(6, list.isEmpty() ? "null" : list.toString().replace("[", "").replace("]", ""));
			}
			stmt.executeUpdate();
			Main.debug("Rank " + getName() + " salvo!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar grupo " + getName() + "!", e.getLocalizedMessage());
		}
	}	
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.RANK_SELECT.getQuery());
			stmt.setString(1, getName());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void delete() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.RANK_DELETE.getQuery());
			stmt.setString(1, getName());
			stmt.executeUpdate();
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao deletar rank " + getName() + "!", e.getLocalizedMessage());
		}
	}

	public boolean hasRequeriment(TypeCoin typeCoin) {
		for(Requeriments r : getRequirements()) { 
			return r.getTypeCoin().equals(typeCoin);
		}
		return false;
	}

	public Rank nextRank() {
		return RankManager.getInstance().get(getId() + 1);
	}

	public void remove(TypeCoin typeCoin) {
		for(Requeriments r : getRequirements()) { 
			if(r.getTypeCoin().equals(typeCoin)) { 
				getRequirements().remove(r);
			}
		}
	}
	
	public void add(TypeCoin typeCoin, double value) { 
		getRequirements().add(new Requeriments(typeCoin, value));
	}
}
