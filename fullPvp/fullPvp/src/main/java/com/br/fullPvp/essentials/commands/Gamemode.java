package com.br.fullPvp.essentials.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.utils.Utils;

public class Gamemode extends Utils implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) { 
			Player player = (Player) sender;
			Account account = AccountManager.getInstance().get(player.getName());
			if(account == null) return true;
			if(!account.hasPermission(Permissions.GAME_MODE.getPermission())) {
				sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
				return true;
			}
		}
		if(args.length == 0) { 
			sintaxCommand(sender, "§c/" + label + " <modo de jogo>",
				      			  "§c/" + label + " <modo de jogo> <jogador>");
			return true;
		} else if(args.length == 1) {
			if(sender instanceof Player) { 
				if(isInteger(args[0])) { 
					Player player = (Player) sender;
					Account account = AccountManager.getInstance().get(player.getName());
					if(account == null) return true;
					player.setGameMode(Integer.valueOf(args[0]) == 1 ? GameMode.CREATIVE : GameMode.SURVIVAL);
					sendMessage(player, false, "§aModo de jogo alterado para " + (Integer.valueOf(args[0]) == 1 ? "criativo" : "sobrevivência") + "!");
				} else { 
					sendMessage(sender, false, "§cVocê precisa digitar números!");
				}
			} else { 
				sendMessage(sender, false, "Somente jogadores podem digitar este comando!");
			}
			return true;
		} else if(args.length == 2) {
			if(isInteger(args[0])) { 
				Account account = AccountManager.getInstance().get(args[1]);
				if(account == null) return true;
				if(account.isOnline()) {
					account.getPlayer().setGameMode(Integer.valueOf(args[0]) == 1 ? GameMode.CREATIVE : GameMode.SURVIVAL);
					sendMessage(account.getPlayer(), false, "§aModo de jogo alterado para " + (Integer.valueOf(args[0]) == 1 ? "criativo" : "sobrevivência") + "!");
					sendMessage(sender, false, "§aModo de jogo de §7" + account.getNickName() + " §aalterado para " + (Integer.valueOf(args[0]) == 1 ? "criativo" : "sobrevivência") + "!");
				} else { 
					sendMessage(sender, false, "§cEste jogador não está online!");
				}
			} else { 
				sendMessage(sender, false, "§cVocê precisa digitar números!");
			}
			return true;
		}
		return false;
	}
}
