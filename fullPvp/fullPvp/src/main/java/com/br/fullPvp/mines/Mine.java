package com.br.fullPvp.mines;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;
import com.br.fullPvp.utils.SerializeLocation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Mine {

	String name, timeToReset;
	long time;
	boolean enable, enableHolo;
	Location pos1, pos2, locHolo;
	List<Composition> composition;
	
	public void save() { 
		try {
			PreparedStatement stmt = null;
			ArrayList<String> composition = new ArrayList<>();
			for(Composition c : getComposition()) { 
				composition.add(c.getId() + ";" + c.getDurability() + ";" + c.getPercent());
			}
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.KIT_UPDATE.getQuery());
				stmt.setString(1, getName());
				stmt.setString(2, getTimeToReset());
				stmt.setBoolean(3, isEnable());
				stmt.setString(4, SerializeLocation.getInstance().serializeLocation(getPos1(), false));
				stmt.setString(5, SerializeLocation.getInstance().serializeLocation(getPos2(), false));
				stmt.setString(6, SerializeLocation.getInstance().serializeLocation(getLocHolo(), true));
				stmt.setString(7, composition.isEmpty() ? "null" : composition.toString().replace("[", "").replace("]", ""));
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.KIT_INSERT.getQuery());
				stmt.setString(1, getTimeToReset());
				stmt.setBoolean(2, isEnable());
				stmt.setString(3, SerializeLocation.getInstance().serializeLocation(getPos1(), false));
				stmt.setString(4, SerializeLocation.getInstance().serializeLocation(getPos2(), false));
				stmt.setString(5, SerializeLocation.getInstance().serializeLocation(getLocHolo(), true));
				stmt.setString(6, composition.isEmpty() ? "null" : composition.toString().replace("[", "").replace("]", ""));
				stmt.setString(7, getName());
			}
			stmt.executeUpdate();
			Main.debug("Mina " + getName() + " salva!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar mina " + getName() + "!", e.getLocalizedMessage());
		}
	}	
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.MINE_SELECT.getQuery());
			stmt.setString(1, getName());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void delete() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.MINE_DELETE.getQuery());
			stmt.setString(1, getName());
			stmt.executeUpdate();
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao deletar mina " + getName() + "!", e.getLocalizedMessage());
		}
	}
	
}
