package com.br.fullPvp.tags;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.Getter;

public class TagManager {
	
	@Getter
	static TagManager instance = new TagManager();
	@Getter
	static List<Tag> storageTags = new ArrayList<>();
	
	public void add(Tag tag) { 
		if(!storageTags.contains(tag)) { 
			storageTags.add(tag);
		}
	}
	
	public void remove(Tag tag) {
		if(storageTags.contains(tag)) { 
			storageTags.remove(tag);
		}
	}
	
	public Tag get(String name) { 
		for(Tag t : storageTags) {
			if(t.getName().equals(name)) { 
				return t;
			}
		}
		return null;
	}

	public void loadAllTags() { 
		try {
			int amount = 0;
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.TAG_SELECT_ALL.getQuery());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) { 
				add(new Tag(rs.getString("name"), rs.getString("prefix"), rs.getString("permission"), rs.getBoolean("free")));
				amount++;
			}
			Main.debug(amount > 0 ? "Carregada " + amount + " tag(s)" : "Nenhuma tag foi carregada!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar os tags!", e.getLocalizedMessage());
		}
	}
	
	public void saveAllRanks() { 
		for(Tag t : storageTags) {
			t.save();
		}
	}
}
