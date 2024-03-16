package com.br.fullPvp.clans.commands;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.br.fullPvp.Main;
import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.TypeCoin;
import com.br.fullPvp.clans.Clan;
import com.br.fullPvp.clans.ClanGroup;
import com.br.fullPvp.clans.ClanManager;
import com.br.fullPvp.utils.ActionBar;
import com.br.fullPvp.utils.Utils;

public class Clans extends Utils implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) { 
			Player player = (Player) sender;
			Account account = AccountManager.getInstance().get(player.getName());
			if(account == null) return true;
			Clan clan = null;
			if(args.length == 0) { 
				if(account.hasClan()) { 
					//Abrir inventário
				} else { 
					sintax(account, label);
				}
				return true;
			} else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("ajuda")) { 
					sintax(account, label);
				} else if(args[0].equalsIgnoreCase("sair")) { 
					if(account.hasClan()) {
						if(!account.hasGroupClan(ClanGroup.LEADDER)) {
							clan = ClanManager.getInstance().get(account.getClanName());
							if(clan == null) return true;
							boolean has = false;
							List<String> members = clan.getMembers();
							Iterator<String> iterator =  clan.getMembers().iterator();
							while(iterator.hasNext()) { 
								String[] split = iterator.next().split(";");
								if(split[0].equalsIgnoreCase(player.getName())) { 
									iterator.remove();
									has = true;
								}
							}
							if(has) { 
								clan.setMembers(members);
								clan.sendMessage(true, "O jogador " + player.getName() + " saiu do clã");
								account.setClanName("NRE");
								account.updatePrefix();
							} else { 
								Main.debug("Ocorreu um erro na saída de " + player.getName() + " do clã");
							}
						} else { 
							sendMessage(player, false, "§cVocê não pode sair do clã, você deve deleta-lo!");
						}
					}
				} else if(args[0].equalsIgnoreCase("deletar")) { 
					if(account.hasClan()) {
						if(account.hasGroupClan(ClanGroup.LEADDER)) {
							clan = ClanManager.getInstance().get(account.getClanName());
							if(clan == null) return true;
							for(Player p : Bukkit.getOnlinePlayers()) { 
								ActionBar.getInstance().sendActionBarMessage(p, "§eO clã §7" + clan.getName() + " §efoi deletado!");
							}
							for(Account a : clan.getAccountMembers()) { 
								a.sendMessage(false, "§cSeu clã foi deletado pode líder!");
								a.setClanName("NRE");
								a.updatePrefix();
							}
							clan.delete();
							clan.getStatus().delete();
							ClanManager.getInstance().remove(clan);
						} else { 
							sendMessage(player, false, "§cSomente o líder pode deletar o clã!");
						}
					}
				}
				return true;
			} else if(args.length == 2) { 
				if(args[0].equalsIgnoreCase("info")) { 
					clan = ClanManager.getInstance().get(args[1]);
					if(clan != null) { 
						//Abrir inventário
					} else { 
						sendMessage(player, false, "§cEste clã não existe!");
					}
				} else if(args[0].equalsIgnoreCase("alterartag")) { 
					if(account.hasClan()) {
						if(account.hasGroupClan(ClanGroup.LEADDER)) {
							if(!ClanManager.getInstance().validate(args[1])) {
								if(validateName(args[1])) {
									clan = ClanManager.getInstance().get(account.getClanName());
									clan.setTag(args[1]);
									clan.sendMessage(true, "Tag do clã alterada pelo líder");
									sendMessage(player, false, "§aVocê alterou a tag do clã §7" + args[1] + "§a!");
									for(Account a : clan.getAccountMembers()) { 
										a.updatePrefix();
									}
								} else { 
									sendMessage(player, false, "§cTag inválida!");
								}
							} else { 
								sendMessage(player, false, "§cTag inválida!");
							}
						} else { 
							sendMessage(player, false, "§cSomente o líder pode alterar a tag do clã!");
						}
					} else { 
						sendMessage(player, false, "§cVocê não está em um clã!");
					}
				} else if(args[0].equalsIgnoreCase("convidar")) { 
					if(account.hasClan()) {
						if(account.hasGroupClan(ClanGroup.RECRUIT)) {
							clan = ClanManager.getInstance().get(account.getClanName());
							if(clan == null) return true;
							List<String> request = clan.getRequests();
							Account accountTarget = AccountManager.getInstance().get(args[1]);
							if(accountTarget != null) { 
								if(!request.contains(accountTarget.getNickName())) { 
									request.add(accountTarget.getNickName());
									sendMessage(player, false, "§aVocê convidou §7" + accountTarget.getNickName() + " §apara entrar no clã!");
									accountTarget.sendMessage(false, "§aVocê recebeu um convite para entrar no clã §7" + clan.getName() + "§a!");
									clan.sendMessage(true, "O jogador " + accountTarget.getNickName() + " foi convidado para entrar no clã");
								} else { 
									sendMessage(player, false, "§cEste jogador já recebeu um convite para entrar no clã!");
								}
							} else { 
								sendMessage(player, false, "§cEste jogador não possui contas em nosso banco de dados!");
							}
						} else { 
							sendMessage(player, false, "§cVocê precisa ser ao menos recruta para convidar jogadores!");
						}
					} else { 
						sendMessage(player, false, "§cVocê não está em um clã!");
					}
				} else if(args[0].equalsIgnoreCase("definirconf")) { 
					if(account.hasClan()) {
						if(account.hasGroupClan(ClanGroup.LEADDER)) {
							clan = ClanManager.getInstance().get(account.getClanName());
							if(clan == null) return true;
							if(args[1].equalsIgnoreCase("pvp")) { 
								clan.setPvp(clan.isPvp() ? false : true);
								sendMessage(sender, false, "§aO combate do clã agora " + (clan.isPvp() ? "está" : "não está") + " habilitado!");
							}
						} else { 
							sendMessage(player, false, "§cVocê precisa ser líder para alterar configurações do clã!");
						}
					} else { 
						sendMessage(player, false, "§cVocê não está em um clã!");
					}
				} else if(args[0].equalsIgnoreCase("sacar")) { 
					if(account.hasClan()) {
						if(account.hasGroupClan(ClanGroup.LEADDER)) {
							clan = ClanManager.getInstance().get(account.getClanName());
							if(clan == null) return true;
							if(isDouble(args[1])) { 
								clan.remove(TypeCoin.REAL, Double.valueOf(args[1]));
								sendMessage(player, false, "§aVocê sacou §f" + TypeCoin.formatExactFormatter(TypeCoin.REAL, Double.valueOf(args[2])) + " §ado seu clã!");
							} else { 
								sendMessage(player, false, "§cVocê precisa digitar números!");
							}
						} else { 
							sendMessage(player, false, "§cVocê precisa ser líder para sacar dinheiro do clã!");
						}
					} else { 
						sendMessage(player, false, "§cVocê não está em um clã!");
					}
				} else if(args[0].equalsIgnoreCase("depositar")) { 
					if(account.hasClan()) {
						clan = ClanManager.getInstance().get(account.getClanName());
						if(clan == null) return true;
						if(isDouble(args[1])) { 
							if(account.hasEconomy(TypeCoin.REAL, Double.valueOf(args[1]))) { 
								account.remove(TypeCoin.REAL, Double.valueOf(args[1]));
								clan.add(TypeCoin.REAL, Double.valueOf(args[1]));
								clan.sendMessage(true, player.getName() + " depositou " + TypeCoin.formatExactFormatter(TypeCoin.REAL, Double.valueOf(args[2])) + " para o clã");
							} else { 
								sendMessage(player, false, "§cVocê não tem este valor para depositar!");
							}
						} else { 
							sendMessage(player, false, "§cVocê precisa digitar números!");
						}
					} else { 
						sendMessage(player, false, "§cVocê não está em um clã!");
					}
				}
				return true;
			} else if(args.length == 3) { 
				if(args[0].equalsIgnoreCase("criar")) { 
					if(!account.hasClan()) {
						if(!ClanManager.getInstance().validate(args[1])) {
							if(validateName(args[2])) { 
								if(args[2].length() >= 3 && args[2].length() < 6) { 
									clan = new Clan(args[1], args[2], player.getName());
									ClanManager.getInstance().add(clan);
									List<String> members = clan.getMembers();
									members.add(player.getName() + ";" + ClanGroup.LEADDER.toString());
									clan.setMembers(members);
									account.setClanName(args[1]);
									account.updatePrefix();
									sendMessage(player, false, "§aVocê criou o clã §7" + args[1] + "§a!");
									for(Player p : Bukkit.getOnlinePlayers()) { 
										if(p != player) { 
											ActionBar.getInstance().sendActionBarMessage(p, "§eClã §7" + args[1] + " §ecriado por §7" + player.getName() + "§a!");
										}
									}
								} else { 
									sendMessage(player, false, "§cTag inválida!");
								}
							} else { 
								sendMessage(player, false, "§cTag inválida!");
							}
						} else { 
							sendMessage(player, false, "§cJá existe um clã com um identificador igual!");
						}
					} else { 
						sendMessage(player, false, "§cVocê já está em um clã!");
					}
				} else if(args[0].equalsIgnoreCase("convite")) { 
					clan = ClanManager.getInstance().get(args[2]);
					if(clan != null) { 
						List<String> requests = clan.getRequests();
						if(args[1].equalsIgnoreCase("aceitar")) { 
							if(requests.contains(player.getName())) { 
								requests.remove(player.getName());
								clan.sendMessage(true, player.getName() + " entrou para o clã");
								List<String> members = clan.getMembers();
								members.add(player.getName() + ";" + ClanGroup.MEMBER.toString());
								clan.setMembers(members);
								account.setClanName(clan.getName());
								account.updatePrefix();
								sendMessage(player, false, "§aVocê entrou no clã §7" + clan.getName() + "§a!");
							} else {
								sendMessage(player, false, "§cVocê não recebeu convite para entrar nesse clã!");
							}
						} else if(args[1].equalsIgnoreCase("rejeitar")) { 
							if(requests.contains(player.getName())) { 
								requests.remove(player.getName());
								sendMessage(player, false, "§cVocê rejeitou o convite para entrar no clã §7" + clan.getName() + "§a!");
							} else { 
								sendMessage(player, false, "§cVocê não recebeu convite para entrar nesse clã!");
							}
						}
					} else { 
						sendMessage(player, false, "§cEste clã não existe!");
					}
				} else if(args[0].equalsIgnoreCase("inimigo")) { 
					if(account.hasClan()) {
						if(account.hasGroupClan(ClanGroup.RECRUIT)) {
							clan = ClanManager.getInstance().get(account.getClanName());
							if(clan == null) return true;
							List<String> enemies = clan.getEnemies();
							if(ClanManager.getInstance().get(args[2]) != null) { 
								if(args[1].equalsIgnoreCase("add")) { 
									if(!enemies.contains(ClanManager.getInstance().get(args[2]).getName())) { 
										enemies.add(ClanManager.getInstance().get(args[2]).getName());
										clan.setEnemies(enemies);
										List<String> enemiesTarget = ClanManager.getInstance().get(args[2]).getEnemies();
										enemiesTarget.add(clan.getName());
										ClanManager.getInstance().get(args[2]).setEnemies(enemiesTarget);
										sendMessage(player, false, "§aClã §7" + ClanManager.getInstance().get(args[2]).getName() + " §aagora é inimigo!");
									} else { 
										sendMessage(player, false, "§cEste clã já é inimigo!");
									}
								} else { 
									if(enemies.contains(ClanManager.getInstance().get(args[2]).getName())) { 
										enemies.remove(ClanManager.getInstance().get(args[2]).getName());
										clan.setEnemies(enemies);
										List<String> enemiesTarget = ClanManager.getInstance().get(args[2]).getEnemies();
										enemiesTarget.remove(clan.getName());
										ClanManager.getInstance().get(args[2]).setEnemies(enemiesTarget);
										sendMessage(player, false, "§aClã §7" + ClanManager.getInstance().get(args[2]).getName() + " §anão é mais inimigo!");
									} else { 
										sendMessage(player, false, "§cEste clã não é inimigo!");
									}
								}
							} else { 
								sendMessage(player, false, "§cEste clã não existe!");
							}
						} else {
							sendMessage(player, false, "§cVocê precisa ser ao menos recruta para convidar jogadores!");
						}
					} else {
						sendMessage(player, false, "§cVocê não está em um clã!");
					}
				} else if(args[0].equalsIgnoreCase("aliado")) { 
					if(account.hasClan()) {
						if(account.hasGroupClan(ClanGroup.RECRUIT)) {
							clan = ClanManager.getInstance().get(account.getClanName());
							if(clan == null) return true;
							List<String> allies = clan.getAllies();
							if(ClanManager.getInstance().get(args[2]) != null) { 
								if(args[1].equalsIgnoreCase("add")) { 
									if(!allies.contains(ClanManager.getInstance().get(args[2]).getName())) { 
										allies.add(ClanManager.getInstance().get(args[2]).getName());
										clan.setAllies(allies);
										List<String> alliesTarget = ClanManager.getInstance().get(args[2]).getAllies();
										alliesTarget.add(clan.getName());
										ClanManager.getInstance().get(args[2]).setEnemies(alliesTarget);
										sendMessage(player, false, "§aClã §7" + ClanManager.getInstance().get(args[2]).getName() + " §aagora é alíado!");
									} else { 
										sendMessage(player, false, "§cEste clã já é alíado!");
									}
								} else { 
									if(allies.contains(ClanManager.getInstance().get(args[2]).getName())) { 
										allies.remove(ClanManager.getInstance().get(args[2]).getName());
										clan.setAllies(allies);
										List<String> alliesTarget = ClanManager.getInstance().get(args[2]).getAllies();
										alliesTarget.remove(clan.getName());
										ClanManager.getInstance().get(args[2]).setAllies(alliesTarget);
										sendMessage(player, false, "§aClã §7" + ClanManager.getInstance().get(args[2]).getName() + " §anão é mais alíado!");
									} else { 
										sendMessage(player, false, "§cEste clã não é alíado!");
									}
								}
							} else { 
								sendMessage(player, false, "§cEste clã não existe!");
							}
						} else {
							sendMessage(player, false, "§cVocê precisa ser ao menos recruta para convidar jogadores!");
						}
					} else {
						sendMessage(player, false, "§cVocê não está em um clã!");
					}
				}
				return true;
			}
		} else { 
			sendMessage(sender, false, "Somente jogadores podem digitar este comando!");
		}
		return false;
	}
	
	private void sintax(Account account, String label) { 
		if(!account.hasClan()) { 
			sintaxCommand(account.getPlayer(), 
				      "§c/" + label + " criar <nome> <tag>",
					  "§c/" + label + " info <nome>",
					  "§c/" + label + " convite <aceitar, rejeitar> <nome>");
		} else { 
			if(account.hasGroupClan(ClanGroup.LEADDER)) { 
				sintaxCommand(account.getPlayer(), 
						  "§c/" + label + " <promover, rebaixar, expulsar> <jogador>",
						  "§c/" + label + " definirconf <pvp>",
						  "§c/" + label + " alterartag <nova tag>",
						  "§c/" + label + " definirlema <mensagem>",
						  "§c/" + label + " inimigo <add, remover> <nome>",
						  "§c/" + label + " aliado <add, remover> <nome>",
						  "§c/" + label + " convidar <jogador>",
						  "§c/" + label + " sacar <quantidade>",
						  "§c/" + label + " depositar <quantidade>",
						  "§c/" + label + " info <nome>",
						  "§c/" + label + " deletar");
			} else if(account.hasGroupClan(ClanGroup.RECRUIT)) { 
				sintaxCommand(account.getPlayer(), 
						  "§c/" + label + " inimigo <add, remover> <nome>",
						  "§c/" + label + " aliado <add, remover> <nome>",
						  "§c/" + label + " convidar <jogador>",
						  "§c/" + label + " depositar <quantidade>",
						  "§c/" + label + " info <nome>",
						  "§c/" + label + " sair");
			} else { 
				sintaxCommand(account.getPlayer(), 
						  "§c/" + label + " depositar <quantidade>",
						  "§c/" + label + " info <nome>",
						  "§c/" + label + " sair");
			}
		}
	}
}
