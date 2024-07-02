package com.br.fullPvp.duels.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.TypeCoin;
import com.br.fullPvp.utils.ActionBar;
import com.br.fullPvp.utils.Utils;

public class X1 extends Utils implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) { 
			Player player = (Player) sender;
			Account account = AccountManager.getInstance().get(player.getName());
			if(account == null) return true;
			if(args.length == 0) { 
				sintax(sender, label);
				return true;
			} else if(args.length == 4) { 
				if(args[0].equalsIgnoreCase("desafiar")) { 
					Account accountTarget = AccountManager.getInstance().get(args[1]);
					if(accountTarget != null) {
						if(isCoin(args[2].toUpperCase())) {
							if(isDouble(args[3])) { 
								if(account.hasEconomy(TypeCoin.valueOf(args[2].toUpperCase()), Double.valueOf(args[3]))) { 
									if(!account.getRequestReceivedX1().contains(accountTarget.getNickName())) { 
										if(!account.getRequestSendedX1().contains(accountTarget.getNickName())) { 
											account.getRequestSendedX1().add(accountTarget.getNickName());
											accountTarget.getRequestReceivedX1().add(account.getNickName());
											sendMessage(player, false, "§aVocê desafiou §7" + accountTarget.getNickName() + " §apara um X1 valendo §f" + TypeCoin.formatExactFormatter(TypeCoin.valueOf(args[2].toUpperCase()), Double.valueOf(args[3])) + "§a!");
											accountTarget.sendMessage(true, "§eVocê foi desafiado por §7" + account.getNickName() + " §epara um X1 valendo §f" + TypeCoin.formatExactFormatter(TypeCoin.valueOf(args[2].toUpperCase()), Double.valueOf(args[3])) + "§e!");
											for(Account a : AccountManager.getStorageAccounts()) { 
												if(a.isOnline()) { 
													ActionBar.getInstance().sendActionBarMessage(a.getPlayer(), "§bO jogador §7" + account.getNickName() + " §bdesafiou §7" + accountTarget.getNickName() + " §bpara um X1!");
												}
											}
										} else { 
											sendMessage(player, false, "§cVocê já enviou um convite para X1 à este jogador!");
										}
									} else { 
										sendMessage(player, false, "§cVocê já possui um convite para X1 deste jogador!");
									}
								} else { 
									sendMessage(player, false, "§cVocê não tem este valor para enviar!");
								}
							} else { 
								sendMessage(player, false, "§cVocê precisa digitar números!");
								return true;
							}
						} else { 
							sendMessage(player, false, "§cEsta moeda não existe!");
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
	
	private void sintax(CommandSender sender, String label) { 
		sintaxCommand(sender, "§c/" + label + " desafiar <jogador> <real, cash, reputacao> <quantidade>",
							  "§c/" + label + " <aceitar, rejeitar> <jogador>", 
							  "§c/" + label + " lista");
	}
}
