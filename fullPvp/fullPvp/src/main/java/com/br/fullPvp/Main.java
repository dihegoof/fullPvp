package com.br.fullPvp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.clans.ClanManager;
import com.br.fullPvp.essentials.preferences.ServerPreferencesManager;
import com.br.fullPvp.groups.GroupManager;
import com.br.fullPvp.groups.listener.ServerTimerEvent;
import com.br.fullPvp.kits.KitManager;
import com.br.fullPvp.kits.PlayerKitManager;
import com.br.fullPvp.links.LinkManager;
import com.br.fullPvp.mysql.MySql;
import com.br.fullPvp.ranks.RankManager;
import com.br.fullPvp.shop.item.ItemManager;
import com.br.fullPvp.shop.session.SessionManager;
import com.br.fullPvp.shop.shoppingcart.ShoppingCartManager;
import com.br.fullPvp.tags.TagManager;
import com.br.fullPvp.utils.ClassGetter;
import com.br.fullPvp.warps.WarpManager;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author buteco
 */


/*
 * Caso uma mensagem tenha dados em números utilize a cor '§7' e cor '§f' para nomes 
 * 
 * 
 * 
 * 
 * 
 */

public class Main extends JavaPlugin {
	
	@Getter
	static Plugin plugin = null;
	@Getter
	static boolean debug = true;
	static String[] listeners = { "accounts.listener", "groups.listener", "inventorys.listener", "warps.listener", "essentials.listener", "chat" };
	static String[] commands = { "accounts.commands", "groups.commands", "ranks.commands", "links.commands", "warps.commands", "tags.commands", "essentials.commands", "clans.commands", "shop.commands", "kits.commands" };
	@Getter
	@Setter
	static MySql mySql = null;
	
	@Override
	public void onEnable() {
		plugin = this;
		debug("Plugin habilitado com sucesso!");
		setMySql(new MySql("localhost", "root", "", "test"));
		mySql.open();
		mySql.createTable();
		startLoads();
		for(String names : listeners) { 
			ClassGetter.getInstance().events("com.br.fullPvp." + names);
		}
		for(String names : commands) { 
			ClassGetter.getInstance().commands("com.br.fullPvp." + names);
		}
		new BukkitRunnable() {
			
			public void run() {
				Bukkit.getPluginManager().callEvent(new ServerTimerEvent());
			}
		}.runTaskTimer(getPlugin(), 20L, 20L);
	}

	@Override
	public void onDisable() {
		startSaves();
		debug("Plugin desabilitado com sucesso!");
		setMySql(null);
		plugin = null;
	}

	public static void debug(String... messages) {
		if(!debug) return;
		for(String lines : messages) {
			Bukkit.getConsoleSender().sendMessage(lines);
		}
	}
	
	private void startLoads() { 
		AccountManager.getInstance().loadAllAccounts();
		GroupManager.getInstance().loadAllGroups();
		RankManager.getInstance().loadAllRanks();
		LinkManager.getInstance().loadLinks();
		WarpManager.getInstance().loadAllWarps();
		TagManager.getInstance().loadAllTags();
		ServerPreferencesManager.getInstance().loadPreferences();
		ClanManager.getInstance().loadAllClans();
		SessionManager.getInstance().loadAllSessions();
		ItemManager.getInstance().loadAllItens();
		ShoppingCartManager.getInstance().loadAllShoppingCart();
		KitManager.getInstance().loadAllKits();
		PlayerKitManager.getInstance().loadAllPlayerKits();
	}
	
	private void startSaves() { 
		AccountManager.getInstance().saveAllAccounts();
		GroupManager.getInstance().saveAllGroups();
		RankManager.getInstance().saveAllRanks();
		LinkManager.getInstance().saveLinks();
		WarpManager.getInstance().saveAllWarps();
		TagManager.getInstance().saveAllTags();
		ServerPreferencesManager.getInstance().savePreferences();
		ClanManager.getInstance().saveAllClans();
		SessionManager.getInstance().saveAllSessions();
		ItemManager.getInstance().saveAllItens();
		ShoppingCartManager.getInstance().saveAllShoppingCart();
		KitManager.getInstance().saveAllKits();
		PlayerKitManager.getInstance().saveAllPlayerKits();
	}
}
