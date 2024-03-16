package com.br.fullPvp.accounts.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.TypeCoin;
import com.br.fullPvp.utils.Utils;

public class Money extends Utils implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) { 
			Player player = (Player) sender;
			Account account = AccountManager.getInstance().get(player.getName());
			if(account == null) return true;
			if(args.length == 0) { 
				sendMessage(sender, true, "§aSeu dinheiro: §7" + TypeCoin.formatExactFormatter(TypeCoin.REAL, account.getReal()));
				return true;
			} else if(args.length == 1) { 
				if(!args[0].equalsIgnoreCase("enviar") || !args[0].equalsIgnoreCase("pay")) { 
					Account accountTarget = AccountManager.getInstance().get(args[0]);
					if(accountTarget != null) { 
						sendMessage(sender, true, "§aDinheiro de §f" + accountTarget.getNickName() +"§a: §7" + TypeCoin.formatExactFormatter(TypeCoin.REAL, accountTarget.getReal()));
					} else { 
						sendMessage(player, false, "§cEste jogador não possui contas em nosso banco de dados!");
					}
				} else { 
					sintaxCommand(player, "§c/" + label + " <enviar> <jogador> <quantidade>");
				}
				return true;
			} else if(args.length == 3) { 
				if(args[0].equalsIgnoreCase("enviar") || args[0].equalsIgnoreCase("pay")) { 
					Account accountTarget = AccountManager.getInstance().get(args[1]);
					if(accountTarget != null) { 
						if(isDouble(args[2])) { 
							if(account.hasEconomy(TypeCoin.REAL, Double.valueOf(args[2]))) { 
								account.remove(TypeCoin.REAL, Double.valueOf(args[2]));
								accountTarget.add(TypeCoin.REAL, Double.valueOf(args[2]));
								account.sendMessage(false, "§aVocê enviou §f" + TypeCoin.formatExactFormatter(TypeCoin.REAL, Double.valueOf(args[2])) + " §apara §7" + accountTarget.getNickName() + "§a!");
								accountTarget.sendMessage(false, "§aVocê recebeu §f" + TypeCoin.formatExactFormatter(TypeCoin.REAL, Double.valueOf(args[2])) + " §ade §7" + account.getNickName() + "§a!");
							} else { 
								sendMessage(player, false, "§cVocê não tem este valor para enviar!");
							}
						} else { 
							sendMessage(player, false, "§cVocê precisa digitar números!");
							return true;
						}
					} else { 
						sendMessage(player, false, "§cEste jogador não possui contas em nosso banco de dados!");
					}
				}
				return true;
			}
		} else { 
			sendMessage(sender, false, "Somente jogadores podem digitar este comando!");
		}
		return false;
	}
}
