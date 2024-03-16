package com.br.fullPvp.groups;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.Getter;

public class GroupManager {
	
	@Getter
	static GroupManager instance = new GroupManager();
	@Getter
	static List<Group> storageGroups = new ArrayList<>();
	
	public void add(Group group) { 
		if(!storageGroups.contains(group)) { 
			storageGroups.add(group);
		}
	}
	
	public void remove(Group group) {
		if(storageGroups.contains(group)) { 
			storageGroups.remove(group);
		}
	}
	
	public Group get(String name) { 
		for(Group g : storageGroups) {
			if(g.getName().equals(name)) { 
				return g;
			}
		}
		return null;
	}

	public void loadAllGroups() { 
		try {
			int amount = 0;
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.GROUP_SELECT_ALL.getQuery());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) { 
				List<String> permissions = new ArrayList<>();
				for(String names : Arrays.asList(rs.getString("permissions").split(", "))) { 
					permissions.add(names);
				}
				add(new Group(rs.getString("name"), rs.getString("prefix"), rs.getInt("priority"), rs.getBoolean("staff"), rs.getBoolean("defaulted"), (permissions.get(0).equals("null") ? new ArrayList<String>() : permissions)));
				amount++;
			}
			Main.debug(amount > 0 ? "Carregado " + amount + " grupo(s)" : "Nenhum grupo foi carregado!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar os grupos!", e.getLocalizedMessage());
		}
	}
	
	public void saveAllGroups() { 
		for(Group g : storageGroups) {
			g.save();
		}
	}
	
	public Group groupDefaulted() { 
		for(Group g : storageGroups) {
			if(g.isDefaulted()) {
				return g;
			}
		}
		return null;
	}
}
