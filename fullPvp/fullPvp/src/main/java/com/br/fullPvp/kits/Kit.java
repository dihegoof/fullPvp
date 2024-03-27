package com.br.fullPvp.kits;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Kit {
	
	@AllArgsConstructor
	@Getter
	public enum Delay { 
		
		DIARY("Diário", "24h"), WEEK("Semanal", "7d"), MONTH("Mensal", "30d"), CUSTOM("Custumizado", "1h");
		
		String name, delay;
	}
	
	String name, permission, timeCustom;
	ItemStack icon;
	Delay delay;
	List<ItemStack> itens;
	boolean free;
	
	@SuppressWarnings("deprecation")
	public void save() { 
		try {
			PreparedStatement stmt = null;
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.KIT_UPDATE.getQuery());
				stmt.setString(1, getPermission());
				stmt.setString(2, getTimeCustom());
				stmt.setString(3, new StringBuilder().append(getIcon().getTypeId() + ";" + getIcon().getDurability()).toString());
				stmt.setString(4, getDelay().toString());
				stmt.setBoolean(5, isFree());
				stmt.setString(6, getItens().isEmpty() ? "null" : getItens().toString().replace("[", "").replace("]", ""));
				stmt.setString(7, getName());
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.KIT_INSERT.getQuery());
				stmt.setString(1, getName());
				stmt.setString(2, getPermission());
				stmt.setString(3, getTimeCustom());
				stmt.setString(4, new StringBuilder().append(getIcon().getTypeId() + ";" + getIcon().getDurability()).toString());
				stmt.setString(5, getDelay().toString());
				stmt.setBoolean(6, isFree());
				stmt.setString(7, getItens().isEmpty() ? "null" : getItens().toString().replace("[", "").replace("]", ""));
			}
			stmt.executeUpdate();
			Main.debug("Kit " + getName() + " salvo!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar kit " + getName() + "!", e.getLocalizedMessage());
		}
	}	
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.KIT_SELECT.getQuery());
			stmt.setString(1, getName());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void delete() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.KIT_DELETE.getQuery());
			stmt.setString(1, getName());
			stmt.executeUpdate();
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao deletar kit " + getName() + "!", e.getLocalizedMessage());
		}
	}
}
