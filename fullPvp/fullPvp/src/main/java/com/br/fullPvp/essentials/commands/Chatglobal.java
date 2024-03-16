package com.br.fullPvp.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.essentials.preferences.ServerPreferencesManager;
import com.br.fullPvp.utils.Utils;

public class Chatglobal extends Utils implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) { 
			Player player = (Player) sender;
			Account account = AccountManager.getInstance().get(player.getName());
			if(account == null) return true;
			if(!account.hasPermission(Permissions.CHAT_GLOBAL.getPermission())) {
				sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
				return true;
			}
		}
		if(args.length == 0) {
			ServerPreferencesManager.getServerPreference().setChatGlobal(ServerPreferencesManager.getServerPreference().isChatGlobal() ? false : true);
			sendBroadcastMessage(true, "§4O chat global foi " + (ServerPreferencesManager.getServerPreference().isChatGlobal() ? "habilitado" : "desabilitado") + "!");
		}
		return false;
	}
}
