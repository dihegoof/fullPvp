package com.br.fullPvp.accounts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.Getter;

public class AccountManager {
	
	@Getter
	static AccountManager instance = new AccountManager();
	@Getter
	static List<Account> storageAccounts = new ArrayList<>();
	@Getter
	static HashMap<UUID, String> lastWarp = new HashMap<>();
	
	public void add(Account account) { 
		if(!storageAccounts.contains(account)) { 
			storageAccounts.add(account);
		}
	}
	
	public void remove(Account account) {
		if(storageAccounts.contains(account)) { 
			storageAccounts.remove(account);
		}
	}
	
	public Account get(String data) { 
		for(Account a : storageAccounts) {
			if(a.getNickName().equals(data)) { 
				//if(a.getNickName().equals(data) || a.getUniqueId().equals(UUID.fromString(data))) { 
				return a;
			}
		}
		/*
		if(Bukkit.getPlayer(data) != null) { 
			Bukkit.getPlayer(data).kickPlayer("§cVocê foi §lKICKADO§c!\n\n§cSua conta encontra-se com problemas técnicos!\n§cEntre em contato com um administrador caso persista!");
		}
		*/
		return null;
	}
	
	public void loadAllAccounts() { 
		try {
			int amount = 0;
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.ACCOUNT_SELECT_ALL.getQuery());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) { 
				List<String> permissions = new ArrayList<>();
				for(String names : Arrays.asList(rs.getString("permissions").split(", "))) { 
					permissions.add(names);
				}
				List<String> tags = new ArrayList<>();
				for(String names : Arrays.asList(rs.getString("tags").split(", "))) { 
					tags.add(names);
				}
				Account account = new Account(UUID.fromString(rs.getString("uniqueid")), rs.getString("nickname"), rs.getString("rankname"), rs.getString("groupname"), rs.getString("lastgroupname"), rs.getString("clanname"), rs.getString("address"), rs.getString("lastaddress"), rs.getString("tagusing"), rs.getLong("timegroup"), rs.getLong("firstlogin"), rs.getLong("lastsee"), rs.getDouble("real"), rs.getDouble("cash"), rs.getDouble("reputation"), false, (tags.get(0).equals("null") ? new ArrayList<String>() : tags), (permissions.get(0).equals("null") ? new ArrayList<String>() : permissions), null, null, null, null, null);
				account.loadStatus();
				account.loadPreferences();
				add(account);
				amount++;
			}
			Main.debug(amount > 0 ? "Carregado " + amount + " conta(s)" : "Nenhum conta foi carregada!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar as contas!", e.getLocalizedMessage());
		}
	}
	
	public void saveAllAccounts() { 
		for(Account a : storageAccounts) {
			a.save();
		}
	}
}
