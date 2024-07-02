package com.br.fullPvp.duels;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class DuelManager {
	
	@Getter
	static DuelManager instance = new DuelManager();
	@Getter
	static List<Duel> storageDuel = new ArrayList<>();
	
	public void add(Duel duel) { 
		if(!storageDuel.contains(duel)) { 
			storageDuel.add(duel);
		}
	}
	
	public void remove(Duel duel) {
		if(storageDuel.contains(duel)) { 
			storageDuel.remove(duel);
		}
	}
	
	public Duel get(String name) { 
		for(Duel d : storageDuel) {
			if(d.getPlayer1().getNickName().equalsIgnoreCase(name) || d.getPlayer2().getNickName().equalsIgnoreCase(name)) { 
				return d;
			}
		}
		return null;
	}
	
	public Duel get(int id) { 
		for(Duel d : storageDuel) {
			if(d.getId() == id) { 
				return d;
			}
		}
		return null;
	}

}
