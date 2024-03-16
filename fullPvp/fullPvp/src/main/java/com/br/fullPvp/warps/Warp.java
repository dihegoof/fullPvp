package com.br.fullPvp.warps;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.br.fullPvp.Main;
import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.mysql.SqlQuerys;
import com.br.fullPvp.utils.SerializeLocation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Warp {
	
	String name, permission, location;
	boolean closed, exclusive, fly, pvp;
	ItemStack icon;
	List<String> blockedCommands;

	@SuppressWarnings("deprecation")
	public void save() { 
		try {
			PreparedStatement stmt = null;
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.WARP_UPDATE.getQuery());
				stmt.setString(1, getPermission());
				stmt.setString(2, getLocation());
				stmt.setBoolean(3, isClosed());
				stmt.setBoolean(4, isExclusive());
				stmt.setBoolean(5, isFly());
				stmt.setBoolean(6, isPvp());
				stmt.setString(7, new StringBuilder().append(getIcon().getTypeId() + ";" + getIcon().getDurability()).toString());
				stmt.setString(8, getBlockedCommands().isEmpty() ? "null" : getBlockedCommands().toString().replace("[", "").replace("]", ""));
				stmt.setString(9, getName());
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.WARP_INSERT.getQuery());
				stmt.setString(1, getName());
				stmt.setString(2, getPermission());
				stmt.setString(3, getLocation());
				stmt.setBoolean(4, isClosed());
				stmt.setBoolean(5, isExclusive());
				stmt.setBoolean(6, isFly());
				stmt.setBoolean(7, isPvp());
				stmt.setString(8, new StringBuilder().append(getIcon().getTypeId() + ";" + getIcon().getDurability()).toString());
				stmt.setString(9, getBlockedCommands().isEmpty() ? "null" : getBlockedCommands().toString().replace("[", "").replace("]", ""));
			}
			stmt.executeUpdate();
			Main.debug("Warp " + getName() + " salva!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar warp " + getName() + "!", e.getLocalizedMessage());
		}
	}	
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.WARP_SELECT.getQuery());
			stmt.setString(1, getName());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void delete() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.WARP_DELETE.getQuery());
			stmt.setString(1, getName());
			stmt.executeUpdate();
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao deletar rank " + getName() + "!", e.getLocalizedMessage());
		}
	}
	
	public List<Account> getPlayerAccounts() { 
		List<Account> accounts = new ArrayList<>();
		for(Account a : AccountManager.getStorageAccounts()) { 
			if(a.isOnline() && a.inWarp(this)) { 
				accounts.add(a);
			}
		}
		return accounts;
	}
	
	public Location getLocationReal() { 
		return SerializeLocation.getInstance().deserializeLocation(getLocation(), true);
	}

	public void teleport(Player player) {
		player.teleport(getLocationReal());
	}
}
