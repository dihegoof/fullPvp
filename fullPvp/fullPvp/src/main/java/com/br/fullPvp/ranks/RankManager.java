package com.br.fullPvp.ranks;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.br.fullPvp.Main;
import com.br.fullPvp.accounts.TypeCoin;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.Getter;

public class RankManager {
	
	@Getter
	static RankManager instance = new RankManager();
	@Getter
	static List<Rank> storageRanks = new ArrayList<>();
	
	public void add(Rank rank) { 
		if(!storageRanks.contains(rank)) { 
			storageRanks.add(rank);
		}
	}
	
	public void remove(Rank rank) {
		if(storageRanks.contains(rank)) { 
			storageRanks.remove(rank);
		}
	}
	
	public Rank get(String name) { 
		for(Rank r : storageRanks) {
			if(r.getName().equals(name)) { 
				return r;
			}
		}
		return null;
	}
	
	public Rank get(int id) { 
		for(Rank r : storageRanks) {
			if(r.getId() == id) { 
				return r;
			}
		}
		return null;
	}

	public void loadAllRanks() { 
		try {
			int amount = 0;
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.RANK_SELECT_ALL.getQuery());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) { 
				List<Requeriments> requirements = new ArrayList<>();
				if(!rs.getString("requirements").equalsIgnoreCase("null")) { 
					for(String names : Arrays.asList(rs.getString("requirements").split(", "))) { 
						String[] split = names.split(";");
						requirements.add(new Requeriments(TypeCoin.valueOf(split[0]), Double.valueOf(split[1])));
					}
				}
				add(new Rank(rs.getInt("id"), rs.getString("name"), rs.getString("prefix"), rs.getInt("priority"), requirements, rs.getBoolean("defaulted")));
				amount++;
			}
			Main.debug(amount > 0 ? "Carregado " + amount + " rank(s)" : "Nenhum rank foi carregado!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar os ranks!", e.getLocalizedMessage());
		}
	}
	
	public void saveAllRanks() { 
		for(Rank r : storageRanks) {
			r.save();
		}
	}
	
	public Rank rankDefaulted() { 
		for(Rank r : storageRanks) {
			if(r.isDefaulted()) {
				return r;
			}
		}
		return null;
	}
	
	public int newId() { 
		return storageRanks.size() + 1;
	}
}
