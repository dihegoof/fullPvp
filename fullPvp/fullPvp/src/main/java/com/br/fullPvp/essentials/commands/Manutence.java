package com.br.fullPvp.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.essentials.preferences.ServerPreferencesManager;
import com.br.fullPvp.groups.GroupManager;
import com.br.fullPvp.utils.Utils;

public class Manutence extends Utils implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) { 
			Player player = (Player) sender;
			Account account = AccountManager.getInstance().get(player.getName());
			if(account == null) return true;
			if(!account.hasPermission(Permissions.MANUTENCE.getPermission())) {
				sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
				return true;
			}
		}
		if(args.length == 0) {
			ServerPreferencesManager.getServerPreference().setManutence(ServerPreferencesManager.getServerPreference().isManutence() ? false : true);
			sendBroadcastMessage(true, "§4A manutenção do servidor foi " + (ServerPreferencesManager.getServerPreference().isManutence() ? "habilitada" : "desabilitada") + "!");
			for(Account a : AccountManager.getStorageAccounts()) { 
				if(a.isOnline() && !GroupManager.getInstance().get(a.getGroupName()).isStaff()) { 
					a.getPlayer().kickPlayer("§cVocê foi §lKICKADO§c!\n\n§cO servidor entrou em manutenção!\n§cVisite nossas mídias sociais para saber o motivo e previsão de retorno!");
				}
			}
		}
		return false;
	}
}
