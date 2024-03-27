package com.br.fullPvp.shop.session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;
import com.br.fullPvp.utils.ItemBuilder;

import lombok.Getter;

public class SessionManager {
	
	@Getter
	static SessionManager instance = new SessionManager();
	@Getter
	static List<Session> storageSession = new ArrayList<>();
	
	public void add(Session session) { 
		if(!storageSession.contains(session)) { 
			storageSession.add(session);
		}
	}
	
	public void remove(Session session) {
		if(storageSession.contains(session)) { 
			storageSession.remove(session);
		}
	}
	
	public Session get(String name) { 
		for(Session s : storageSession) {
			if(s.getName().equals(name)) { 
				return s;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public void loadAllSessions() { 
		try {
			int amount = 0;
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.SESSION_SELECT_ALL.getQuery());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) { 
				String[] split = rs.getString("icon").split(";");
				Main.debug("Split > " + split[0] + ", " + split[1]);
				add(new Session(rs.getString("name"), rs.getDouble("descount"), new ItemBuilder(Material.getMaterial(Integer.valueOf(split[0]))).setDurability(Integer.valueOf(split[1])).getStack(), rs.getBoolean("promo")));
				amount++;
			}
			Main.debug(amount > 0 ? "Carregada " + amount + " sessão(ões)" : "Nenhuma sessão foi carregada!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar as sessões!", e.getLocalizedMessage());
		}
	}
	
	public void saveAllSessions() { 
		for(Session s : storageSession) {
			s.save();
		}
	}
}
