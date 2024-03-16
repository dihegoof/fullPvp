package com.br.fullPvp.essentials.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.utils.Utils;

public class Admin extends Utils implements CommandExecutor {
	
	static HashMap<UUID, ItemStack[]> contents = new HashMap<>();
	static HashMap<UUID, ItemStack[]> armor = new HashMap<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) { 
			Player player = (Player) sender;
			Account account = AccountManager.getInstance().get(player.getName());
			if(account == null) return true;
			if(!account.hasPermission(Permissions.ADMIN_MODE.getPermission())) {
				sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
				return true;
			}
			if(args.length == 0) { 
				if(!account.getPreferences().isAdminMode()) { 
					contents.put(player.getUniqueId(), player.getInventory().getContents());
					armor.put(player.getUniqueId(), player.getInventory().getArmorContents());
					for(Player p : Bukkit.getOnlinePlayers()) { 
						if(!AccountManager.getInstance().get(p.getName()).getPreferences().isAdminMode()) { 
							p.hidePlayer(player);
						}
						player.showPlayer(p);
					}
					player.setGameMode(GameMode.CREATIVE);
					player.setAllowFlight(true);
					player.setFlying(true);
					player.getInventory().clear();
					player.getInventory().setArmorContents(null);
					account.getPreferences().setAdminMode(true);
				} else { 
					if(contents.containsKey(player.getUniqueId())) {
						player.getInventory().clear();
						player.getInventory().setContents(contents.get(player.getUniqueId()));
					}
					if(armor.containsKey(player.getUniqueId())) {
						player.getInventory().setArmorContents(null);
						player.getInventory().setArmorContents(armor.get(player.getUniqueId()));
					}
					for(Player p : Bukkit.getOnlinePlayers()) { 
						if(AccountManager.getInstance().get(p.getName()).getPreferences().isAdminMode()) { 
							player.hidePlayer(p);
						}
						p.showPlayer(player);
					}
					player.setGameMode(GameMode.SURVIVAL);
					account.getPreferences().setAdminMode(false);
					account.updateFly();
				}
				sendMessage(player, false, "§dVocê " + (account.getPreferences().isAdminMode() ? "entrou" : "saiu") + " do modo admin!");
				return true;
			}
		} else { 
			sendMessage(sender, false, "Somente jogadores podem digitar este comando!");
		}
		return false;
	}
}
