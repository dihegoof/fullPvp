package com.br.fullPvp.clans;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.Getter;

public class ClanManager {
	
	@Getter
	static ClanManager instance = new ClanManager();
	@Getter
	static List<Clan> storageClans = new ArrayList<>();
	
	public void add(Clan clan) { 
		if(!storageClans.contains(clan)) { 
			storageClans.add(clan);
		}
	}
	
	public void remove(Clan clan) {
		if(storageClans.contains(clan)) { 
			storageClans.remove(clan);
		}
	}
	
	public Clan get(String name) { 
		for(Clan c : storageClans) {
			if(c.getName().equals(name) || c.getTag().equalsIgnoreCase(name)) { 
				return c;
			}
		}
		return null;
	}
	
	public boolean validate(String name) { 
		for(Clan c : storageClans) {
			if(c.getName().equals(name) || c.getTag().equalsIgnoreCase(name)) { 
				return true;
			}
		}
		return false;
	}

	public void loadAllClans() { 
		try {
			int amount = 0;
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.CLAN_SELECT_ALL.getQuery());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) { 
				List<String> members = new ArrayList<>();
				for(String names : Arrays.asList(rs.getString("members").split(", "))) { 
					members.add(names);
				}
				List<String> climbed = new ArrayList<>();
				for(String names : Arrays.asList(rs.getString("climbed").split(", "))) { 
					climbed.add(names);
				}
				List<String> invites = new ArrayList<>();
				for(String names : Arrays.asList(rs.getString("invites").split(", "))) { 
					invites.add(names);
				}
				List<String> allies = new ArrayList<>();
				for(String names : Arrays.asList(rs.getString("allies").split(", "))) { 
					allies.add(names);
				}
				List<String> enemies = new ArrayList<>();
				for(String names : Arrays.asList(rs.getString("enemies").split(", "))) { 
					enemies.add(names);
				}
				Clan clan = new Clan(rs.getString("name"), rs.getString("tag"), rs.getString("leadder"), rs.getString("motto"), (members.get(0).equals("null") ? new ArrayList<String>() : members), (climbed.get(0).equals("null") ? new ArrayList<String>() : climbed), (invites.get(0).equals("null") ? new ArrayList<String>() : invites), (allies.get(0).equals("null") ? new ArrayList<String>() : allies), (enemies.get(0).equals("null") ? new ArrayList<String>() : enemies), rs.getDouble("real"), rs.getLong("createdin"), rs.getBoolean("pvp"), null);
				clan.loadStatus();
				add(clan);
				amount++;
			}
			Main.debug(amount > 0 ? "Carregado " + amount + " clan(s)" : "Nenhum clan foi carregado!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar os clans!", e.getLocalizedMessage());
		}
	}
	
	public void saveAllClans() { 
		for(Clan c : storageClans) {
			c.save();
		}
	}
}
