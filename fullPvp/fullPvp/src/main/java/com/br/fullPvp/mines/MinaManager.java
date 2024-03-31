package com.br.fullPvp.mines;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;
import com.br.fullPvp.utils.SerializeLocation;

import lombok.Getter;

public class MinaManager {

	@Getter
	static MinaManager instance = new MinaManager();
	@Getter
	static List<Mine> storageMines = new ArrayList<>();
	
	public void add(Mine mine) { 
		if(!storageMines.contains(mine)) { 
			storageMines.add(mine);
		}
	}
	
	public void remove(Mine mine) {
		if(storageMines.contains(mine)) { 
			storageMines.remove(mine);
		}
	}
	
	public Mine get(String name) { 
		for(Mine m : storageMines) {
			if(m.getName().equals(name)) { 
				return m;
			}
		}
		return null;
	}
	
	public void loadAllKits() { 
		try {
			int amount = 0;
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.MINE_SELECT_ALL.getQuery());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) { 
				List<Composition> composition = new ArrayList<>();
				if(!rs.getString("composition").equalsIgnoreCase("null")) { 
					for(String names : Arrays.asList(rs.getString("composition").split(", "))) { 
						String[] split = names.split(";");
						composition.add(new Composition(Integer.valueOf(split[0]), Integer.valueOf(split[1]), Double.valueOf(split[2])));
					}
				}
				add(new Mine(rs.getString("name"), rs.getString("timetoreset"), 0L, rs.getBoolean("enable"), rs.getBoolean("enableholo"), SerializeLocation.getInstance().deserializeLocation(rs.getString("pos1"), false), SerializeLocation.getInstance().deserializeLocation(rs.getString("pos2"), false), rs.getBoolean("enableholo") ? SerializeLocation.getInstance().deserializeLocation(rs.getString("locholo"), true) : null, composition));
				amount++;
			}
			Main.debug(amount > 0 ? "Carregado " + amount + " mina(s)" : "Nenhuma mina foi carregado!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar os mina !", e.getLocalizedMessage());
		}
	}
	
	public void saveAllKits() { 
		for(Mine m : storageMines) {
			m.save();
		}
	}
}
