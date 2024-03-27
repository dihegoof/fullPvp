package com.br.fullPvp.tags;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Tag {
	
	String name, prefix, permission;
	boolean free;
	
	public void save() { 
		try {
			PreparedStatement stmt = null;
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.TAG_UPDATE.getQuery());
				stmt.setString(1, getPrefix());
				stmt.setString(2, getPermission());
				stmt.setBoolean(3, isFree());
				stmt.setString(4, getName());
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.TAG_INSERT.getQuery());
				stmt.setString(1, getName());
				stmt.setString(2, getPrefix());
				stmt.setString(3, getPermission());
				stmt.setBoolean(4, isFree());
			}
			stmt.executeUpdate();
			Main.debug("Tag " + getName() + " salvo!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar tag " + getName() + "!", e.getLocalizedMessage());
		}
	}	
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.TAG_SELECT.getQuery());
			stmt.setString(1, getName());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void delete() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.TAG_DELETE.getQuery());
			stmt.setString(1, getName());
			stmt.executeUpdate();
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao deletar tag " + getName() + "!", e.getLocalizedMessage());
		}
	}
}
