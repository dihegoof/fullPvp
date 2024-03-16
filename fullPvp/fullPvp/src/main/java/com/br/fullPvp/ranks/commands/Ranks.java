package com.br.fullPvp.ranks.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.accounts.TypeCoin;
import com.br.fullPvp.ranks.Rank;
import com.br.fullPvp.ranks.RankManager;
import com.br.fullPvp.utils.Utils;

public class Ranks extends Utils implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) { 
			Player player = (Player) sender;
			Account account = AccountManager.getInstance().get(player.getName());
			if(account == null) return true;
			if(!account.hasPermission(Permissions.MANAGE_RANKS.getPermission())) { 
				sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
				return true;
			}
		}
		if(args.length == 0) {
			sintax(sender, label);
			return true;
		} else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("lista")) { 
				StringBuilder list = new StringBuilder();
				for(Rank r : RankManager.getStorageRanks()) { 
					list.append(r.getName() + ", ");
				}
				sendMessage(sender, false, "§aLista de ranks: §7" + (list.length() == 0 ? "§cNenhum rank encontrado!" : list.toString().substring(0, list.length() - 2)));
			}
			return true;
		} else if(args.length == 2) { 
			Rank rank = null;
			if(args[0].equalsIgnoreCase("criar")) { 
				rank = RankManager.getInstance().get(captalize(args[1]));
				if(rank == null) { 
					RankManager.getInstance().add(new Rank(RankManager.getInstance().newId(), captalize(args[1]), "NRE", 9, new ArrayList<String>(), false));
					sendMessage(sender, false, "§aRank §7" + captalize(args[1]) + " §acriado!");
				} else { 
					sendMessage(sender, false, "§cEste rank já existe!");
				}
			} else if(args[0].equalsIgnoreCase("deletar")) { 
				rank = RankManager.getInstance().get(captalize(args[1]));
				if(rank != null) { 
					sendMessage(sender, false, "§aRank §7" + rank.getName() + " §adeletado!");
					rank.delete();
					RankManager.getInstance().remove(rank);
				} else { 
					sendMessage(sender, false, "§cEste rank não existe!");
				}
			} else if(args[0].equalsIgnoreCase("info")) { 
				rank = RankManager.getInstance().get(captalize(args[1]));
				if(rank != null) { 
					StringBuilder list = new StringBuilder();
					for(String r : rank.getRequirements()) { 
						String[] split = r.split(";");
						list.append(TypeCoin.formatExactFormatter(TypeCoin.valueOf(split[0]), Double.valueOf(split[1])) + ", ");
					}
					sendMessage(sender, true, 
							  "§aRank §7" + rank.getName() + "§a: §8(" + rank.getPrefix().replace("&", "§") + "§8)", 
							  "§aID §7#" + rank.getId(),
							  "§aPrioridade: §7" + rank.getPriority(),
							  "§aPadrão? §7" + (rank.isDefaulted() ? "Sim" : "Não"),
							  "§aRequisitos: §7" + (list.length() <= 0 ? "§cNenhum requisito adicionado!" : list.toString().substring(0, list.length() - 2)));
				} else {
					sendMessage(sender, false, "§cEste rank não existe!");
				}
			}
			return true;
		} else if(args.length == 4) { 
			Rank rank = null;
			if(args[0].equalsIgnoreCase("req")) { 
				if(args[1].equalsIgnoreCase("remover")) { 
					rank = RankManager.getInstance().get(captalize(args[2]));
					if(rank != null) { 
						if(isCoin(args[3].toUpperCase())) { 
							if(rank.hasRequeriment(args[3].toUpperCase())) {
								boolean has = false;
								List<String> requeriments = rank.getRequirements();
								Iterator<String> iterator =  rank.getRequirements().iterator();
								while(iterator.hasNext()) { 
									String[] split = iterator.next().split(";");
									if(split[0].equalsIgnoreCase(args[3].toUpperCase())) { 
										iterator.remove();
										has = true;
									}
								}
								if(has) { 
									rank.setRequirements(requeriments);
									sendMessage(sender, false, "§aRequisito removido do grupo §7" + rank.getName() + "§a!");
								} else { 
									sendMessage(sender, false, "§cRequisito não encontrado!");
								}
							} else {
								sendMessage(sender, false, "§cEste rank não possui este requisito!");
							}
						} else { 
							sendMessage(sender, false, "§cEsta economia não existe!");
						}
					} else { 
						sendMessage(sender, false, "§cEste rank não existe!");
					}
				}
			}
			return true;
		} else if(args.length == 5) { 
			Rank rank = null;
			if(args[0].equalsIgnoreCase("req")) { 
				if(args[1].equalsIgnoreCase("add")) { 
					rank = RankManager.getInstance().get(captalize(args[2]));
					if(rank != null) { 
						if(isCoin(args[3].toUpperCase())) { 
							if(isDouble(args[4])) { 
								if(!rank.hasRequeriment(args[3].toUpperCase())) {
									List<String> requeriments = rank.getRequirements();
									requeriments.add(TypeCoin.valueOf(args[3].toUpperCase()).toString() + ";" + args[4]);
									rank.setRequirements(requeriments);
									sendMessage(sender, false, "§aRequisito §7" + TypeCoin.formatExactFormatter(TypeCoin.valueOf(args[3].toUpperCase()), Double.valueOf(args[4])) + " §aadicionado para rank §7" + rank.getName() + "§a!");
								} else { 
									sendMessage(sender, false, "§cEste rank já possui este requisito!");
								}
							} else { 
								sendMessage(sender, false, "§cVocê precisa digitar números!");
							}
						} else {
							sendMessage(sender, false, "§cEsta economia não existe!");
						}
					} else { 
						sendMessage(sender, false, "§cEste rank não existe!");
					}
				}
			}
			return true;
		} else if(args.length >= 3) { 
			Rank rank = RankManager.getInstance().get(captalize(args[1]));
			if(args[0].equalsIgnoreCase("definirconf")) { 
				if(rank != null) { 
					if(args[2].equalsIgnoreCase("padrao")) {
						rank.setDefaulted(rank.isDefaulted() ? false : true);
						for(Rank r : RankManager.getStorageRanks()) { 
							if(r.isDefaulted() && r != rank) { 
								r.setDefaulted(false);
							}
						}
						sendMessage(sender, false, "§aRank §7" + rank.getName() + " §aagora " + (rank.isDefaulted() ? "é" : "não é") + " padrão!");
					}
				} else { 
					sendMessage(sender, false, "§cEste rank não existe!");
				}
			} else if(args[0].equalsIgnoreCase("definirprefixo")) { 
				if(rank != null) { 
					String prefix = createArgs(2, args, label, false);
					rank.setPrefix(prefix.replace("&", "§"));
					sendMessage(sender, false, "§aPrefixo do rank §7" + rank.getName() + " §aalterado para " + prefix.replace("&", "§") + "§a!");
				} else { 
					sendMessage(sender, false, "§cEste grupo não existe!");
				}
			}
		}
		return false;
	}
	
	private void sintax(CommandSender sender, String label) { 
		sintaxCommand(sender, "§c/" + label + " lista",
							  "§c/" + label + " <criar, deletar, info> <nome>", 
							  "§c/" + label + " req <add> <rank> <economia> <quantidade>",
							  "§c/" + label + " req <remover> <rank> <economia>",
							  "§c/" + label + " definirprefixo <rank> <prefixo>",
							  "§c/" + label + " definirconf <rank> <padrao>");
	}
}
