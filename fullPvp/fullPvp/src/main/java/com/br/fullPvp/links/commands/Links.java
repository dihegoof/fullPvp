package com.br.fullPvp.links.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.links.LinkManager;
import com.br.fullPvp.links.Link.TypeLinks;
import com.br.fullPvp.utils.Utils;

public class Links extends Utils implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) { 
			Player player = (Player) sender;
			Account account = AccountManager.getInstance().get(player.getName());
			if(account == null) return true;
			if(!account.hasPermission(Permissions.MANAGE_LINKS.getPermission())) { 
				sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
				return true;
			}
		}
		if(args.length == 0) {
			sintax(sender, label);
			return true;
		} else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("resetarmidia")) { 
				for(TypeLinks t : TypeLinks.values()) { 
					LinkManager.getLink().exactLink(t.getName(), t.getValue());
				}
				sendMessage(sender, false, "§aLinks de mídia resetados!");
			} else if(args[0].equalsIgnoreCase("lista")) { 
				StringBuilder list = new StringBuilder();
				for(TypeLinks t : TypeLinks.values()) { 
					list.append(t.getName().toLowerCase() + ", ");
				}
				sendMessage(sender, false, "§aLista de mídias: §7" + (list.length() == 0 ? "§cNenhuma mídia encontrada!" : list.toString().substring(0, list.length() - 2)));
			}
		} else if(args.length >= 3) {
			if(args[0].equalsIgnoreCase("definir")) { 
				if(isTypeLink(captalize(args[1]))) {
					if(TypeLinks.get(captalize(args[1])).getName().equals("Motd")) {
						String motd = createArgs(2, args, label, false);
						LinkManager.getLink().exactLink(TypeLinks.get(captalize(args[1])).getName(), motd.replace("&", "§"));
						sendMessage(sender, false, "§aLink de " + TypeLinks.get(captalize(args[1])).getName() + " alterado para §7" + motd.replace("&", "§") + "§a!");
					} else { 
						LinkManager.getLink().exactLink(TypeLinks.get(captalize(args[1])).getName(), args[2].replace("&", "§"));
						sendMessage(sender, false, "§aLink de " + TypeLinks.get(captalize(args[1])).getName() + " alterado para §7" + args[2].replace("&", "§") + "§a!");
					}
				} else { 
					sendMessage(sender, false, "§cNenhuma mídia alteravel foi encontrada com esse nome!");
				}
			}
			return true;
		}
		return false;
	}
	
	private void sintax(CommandSender sender, String label) { 
		sintaxCommand(sender, "§c/" + label + " lista",
							  "§c/" + label + " definir <midia> <valor>",
							  "§c/" + label + " resetarmidia");
	}
}
