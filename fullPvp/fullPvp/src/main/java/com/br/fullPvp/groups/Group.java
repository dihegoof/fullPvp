package com.br.fullPvp.groups;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Group {
	
	String name, prefix;
	int priority;
	boolean staff, defaulted;
	List<String> permissions;
	
	public void save() { 
		try {
			PreparedStatement stmt = null;
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.GROUP_UPDATE.getQuery());
				stmt.setString(1, getPrefix());
				stmt.setInt(2, getPriority());
				stmt.setBoolean(3, isStaff());
				stmt.setBoolean(4, isDefaulted());
				stmt.setString(5, getPermissions().isEmpty() ? "null" : getPermissions().toString().replace("[", "").replace("]", ""));
				stmt.setString(6, getName());
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.GROUP_INSERT.getQuery());
				stmt.setString(1, getName());
				stmt.setString(2, getPrefix());
				stmt.setInt(3, getPriority());
				stmt.setBoolean(4, isStaff());
				stmt.setBoolean(5, isDefaulted());
				stmt.setString(6, getPermissions().isEmpty() ? "null" : getPermissions().toString().replace("[", "").replace("]", ""));
			}
			stmt.executeUpdate();
			Main.debug("Grupo " + getName() + " salvo!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar grupo " + getName() + "!", e.getLocalizedMessage());
		}
	}
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.GROUP_SELECT.getQuery());
			stmt.setString(1, getName());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void delete() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.GROUP_DELETE.getQuery());
			stmt.setString(1, getName());
			stmt.executeUpdate();
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao deletar grupo " + getName() + "!", e.getLocalizedMessage());
		}
	}
	
	public boolean hasPermission(String permission) {
		boolean has = false;
		if(!getPermissions().isEmpty()) { 
			for(String p : getPermissions()) { 
				String[] split = p.split(";");
				if(split[0].equalsIgnoreCase("*")) { 
					has = true;
				} else if(split[0].equalsIgnoreCase(permission)) { 
					has = split[1].equalsIgnoreCase("-1") ? true : Long.valueOf(split[1]).longValue() > System.currentTimeMillis() ? true : false;
				}
			}
		}
		return has;
	}
}
