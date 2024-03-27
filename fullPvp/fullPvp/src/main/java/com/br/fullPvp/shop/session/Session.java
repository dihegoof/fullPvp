package com.br.fullPvp.shop.session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;
import com.br.fullPvp.shop.item.Item;
import com.br.fullPvp.shop.item.ItemManager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Session {
	
	String name;
	double descount;
	ItemStack icon;
	boolean promo;
	
	@SuppressWarnings("deprecation")
	public void save() { 
		try {
			PreparedStatement stmt = null;
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.SESSION_UPDATE.getQuery());
				stmt.setDouble(1, getDescount());
				stmt.setString(2, new StringBuilder().append(getIcon().getTypeId() + ";" + getIcon().getDurability()).toString());
				stmt.setBoolean(3, isPromo());
				stmt.setString(4, getName());
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.SESSION_INSERT.getQuery());
				stmt.setString(1, getName());
				stmt.setDouble(2, getDescount());
				stmt.setString(3, new StringBuilder().append(getIcon().getTypeId() + ";" + getIcon().getDurability()).toString());
				stmt.setBoolean(4, isPromo());
			}
			stmt.executeUpdate();
			Main.debug("Sessão " + getName() + " salva!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar sessão " + getName() + "!", e.getLocalizedMessage());
		}
	}	
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.SESSION_SELECT.getQuery());
			stmt.setString(1, getName());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void delete() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.SESSION_DELETE.getQuery());
			stmt.setString(1, getName());
			stmt.executeUpdate();
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao deletar sessão " + getName() + "!", e.getLocalizedMessage());
		}
	}

	public List<Item> getItens() {
		List<Item> list = new ArrayList<>();
		for(Item i : ItemManager.getStorageItens()) {
			if(i.getSession().getName().equals(getName())) { 
				list.add(i);
			}
		}
		return list;
	}
	
	public boolean hasItensPromo() { 
		boolean has = false;
		for(Item i : getItens()) { 
			if(i.isPromo()) { 
				has = true;
			}
		}
		return has;
	}
}
