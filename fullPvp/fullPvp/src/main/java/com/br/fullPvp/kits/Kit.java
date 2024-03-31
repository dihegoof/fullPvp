package com.br.fullPvp.kits;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;
import com.br.fullPvp.utils.SerializeItemStack;

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
		
		DIARY(8, "Diário", "24h"), WEEK(9, "Semanal", "7d"), MONTH(13, "Mensal", "30d"), CUSTOM(5, "Custumizado", "1h");
		
		int id;
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
				ArrayList<String> itens = new ArrayList<>();
				for(ItemStack i : getItens()) { 
					itens.add(SerializeItemStack.getInstance().convert(i, 0));
				}
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.KIT_UPDATE.getQuery());
				stmt.setString(1, getPermission());
				stmt.setString(2, getTimeCustom());
				stmt.setString(3, new StringBuilder().append(getIcon().getTypeId() + ";" + getIcon().getDurability()).toString());
				stmt.setString(4, getDelay().toString());
				stmt.setBoolean(5, isFree());
				stmt.setString(6, itens.isEmpty() ? "null" : itens.toString().replace("[", "").replace("]", ""));
				stmt.setString(7, getName());
			} else { 
				ArrayList<String> itens = new ArrayList<>();
				for(ItemStack i : getItens()) { 
					itens.add(SerializeItemStack.getInstance().convert(i, 0));
				}
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.KIT_INSERT.getQuery());
				stmt.setString(1, getName());
				stmt.setString(2, getPermission());
				stmt.setString(3, getTimeCustom());
				stmt.setString(4, new StringBuilder().append(getIcon().getTypeId() + ";" + getIcon().getDurability()).toString());
				stmt.setString(5, getDelay().toString());
				stmt.setBoolean(6, isFree());
				stmt.setString(7, itens.isEmpty() ? "null" : itens.toString().replace("[", "").replace("]", ""));
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

	public void give(Player player) {
		for(ItemStack i : getItens()) { 
			player.getInventory().addItem(i);
		}
	}
}
