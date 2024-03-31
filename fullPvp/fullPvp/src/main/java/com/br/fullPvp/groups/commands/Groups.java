package com.br.fullPvp.groups.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.groups.Group;
import com.br.fullPvp.groups.GroupManager;
import com.br.fullPvp.groups.PermissionsCase;
import com.br.fullPvp.utils.TimeManager;
import com.br.fullPvp.utils.Utils;

public class Groups extends Utils implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) { 
			Player player = (Player) sender;
			Account account = AccountManager.getInstance().get(player.getName());
			if(account == null) return true;
			if(!account.hasPermission(Permissions.MANAGE_GROUPS.getPermission())) { 
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
				for(Group g : GroupManager.getStorageGroups()) { 
					list.append(g.getName() + ", ");
				}
				sendMessage(sender, false, "§aLista de grupos: §7" + (list.length() == 0 ? "§cNenhum grupo encontrado!" : list.toString().substring(0, list.length() - 2)));
			}
			return true;
		} else if(args.length == 2) { 
			Group group = null;
			if(args[0].equalsIgnoreCase("criar")) { 
				group = GroupManager.getInstance().get(captalize(args[1]));
				if(group == null) { 
					GroupManager.getInstance().add(new Group(captalize(args[1]), "NRE", 9, false, false, new ArrayList<PermissionsCase>()));
					sendMessage(sender, false, "§aGrupo §7" + captalize(args[1]) + " §acriado!");
				} else { 
					sendMessage(sender, false, "§cEste grupo já existe!");
				}
			} else if(args[0].equalsIgnoreCase("deletar")) { 
				group = GroupManager.getInstance().get(captalize(args[1]));
				if(group != null) { 
					sendMessage(sender, false, "§aGrupo §7" + group.getName() + " §adeletado!");
					for(Account a : AccountManager.getStorageAccounts()) { 
						if(a.getGroupName().equalsIgnoreCase(group.getName())) { 
							if(a.isGroupPermanent()) { 
								a.setGroupName(a.hasLastGroup() ? a.getLastGroupName() : GroupManager.getInstance().groupDefaulted().getName());
								a.sendMessage(false, "§cSeu grupo atual foi deletado, um novo grupo foi definido à você!");
							}
						}
					}
					group.delete();
					GroupManager.getInstance().remove(group);
				} else { 
					sendMessage(sender, false, "§cEste grupo não existe!");
				}
			} else if(args[0].equalsIgnoreCase("info")) { 
				group = GroupManager.getInstance().get(captalize(args[1]));
				if(group != null) { 
					StringBuilder list = new StringBuilder();
					for(PermissionsCase p : group.getPermissions()) { 
						list.append(
								p.isPermanent() ? 
										p.getPermission() + ", " : 
										Long.valueOf(p.getTime()) < System.currentTimeMillis() ? 
												p.getPermission() + "§8(§cexpirado§8)§7, " : 
										p.getPermission() + 
										"§8(" + compareTime(Long.valueOf(p.getTime())) + ")§7, ");
					}
					sendMessage(sender, true, "§aGrupo §7" + group.getName() + "§a: §8(" + group.getPrefix().replace("&", "§") + "§8)", 
											  "§aPrioridade: §7" + group.getPriority(),
											  "§aStaff? §7" + (group.isStaff() ? "Sim" : "Não"),
											  "§aPadrão? §7" + (group.isDefaulted() ? "Sim" : "Não"),
											  "§aPermissões: §7" + (list.length() <= 0 ? "§cNenhuma permissão encontrada!" : list.toString().substring(0, list.length() - 2)));
				} else { 
					sendMessage(sender, false, "§cEste grupo não existe!");
				}
			}
			return true;
		} else if(args.length == 4) { 
			if(args[0].equalsIgnoreCase("perm")) { 
				Group group = GroupManager.getInstance().get(captalize(args[2]));
				if(group != null) { 
					if(args[1].equalsIgnoreCase("add")) { 
						if(!group.hasPermission(args[3])) { 
							group.add(args[3], -1);
							sendMessage(sender, false, "§aPermissão §7" + args[3] + " §aadicionada ao grupo §7" + group.getName() + " §apermanentemente!");
						} else { 
							sendMessage(sender, false, "§cEste grupo já possui esta permissão!");
						}
					} else if(args[1].equalsIgnoreCase("remover")) { 
						if(group.hasPermission(args[3])) { 
							group.remove(args[3]);
							sendMessage(sender, false, "§aPermissão §7" + args[3] + " §aremovida do grupo §7" + group.getName() + "§a!");
						} else { 
							sendMessage(sender, false, "§cEste grupo não possui esta permissão!");
						}
					}
				} else { 
					sendMessage(sender, false, "§cEste grupo não existe!");
				}
			}
			return true;
		} else if(args.length == 5) { 
			if(args[0].equalsIgnoreCase("perm")) { 
				Group group = GroupManager.getInstance().get(captalize(args[2]));
				if(group != null) { 
					if(args[1].equalsIgnoreCase("add")) { 
						if(!group.hasPermission(args[3])) { 
							if(isTime(args[4])) { 
								group.add(args[3], TimeManager.getInstance().getTime(args[4]));
							} else { 
								sendMessage(sender, false, "§cTempo inválido!");
								return true;
							}
							sendMessage(sender, false, "§aPermissão §7" + args[3] + " §aadicionada ao grupo §7" + group.getName() + " §adurante §f" + compareTime(TimeManager.getInstance().getTime(args[4])) + "§a!");
						} else { 
							sendMessage(sender, false, "§cEste grupo já possui esta permissão!");
						}
					}
				} else { 
					sendMessage(sender, false, "§cEste grupo não existe!");
				}
			}
			return true;
		} else if(args.length >= 3) {
			if(args[0].equalsIgnoreCase("definirconf")) { 
				Group group = GroupManager.getInstance().get(captalize(args[1]));
				if(group != null) { 
					if(args[2].equalsIgnoreCase("equipe")) { 
						group.setStaff(group.isStaff() ? false : true);
						sendMessage(sender, false, "§aGrupo §7" + group.getName() + " §aagora " + (group.isStaff() ? "é" : "não é") + " da equipe!");
					} else if(args[2].equalsIgnoreCase("padrao")) { 
						group.setDefaulted(group.isDefaulted() ? false : true);
						for(Group g : GroupManager.getStorageGroups()) { 
							if(g.isDefaulted() && g != group) { 
								g.setDefaulted(false);
							}
						}
						sendMessage(sender, false, "§aGrupo §7" + group.getName() + " §aagora " + (group.isDefaulted() ? "é" : "não é") + " padrão!");
					}
				} else { 
					sendMessage(sender, false, "§cEste grupo não existe!");
				}
				return true;
			} else if(args[0].equalsIgnoreCase("definirprefixo")) { 
				Group group = GroupManager.getInstance().get(captalize(args[1]));
				if(group != null) { 
					String prefix = createArgs(2, args, label, false);
					group.setPrefix(prefix.replace("&", "§"));
					for(Account a : AccountManager.getStorageAccounts()) { 
						if(a.getGroupName().equals(group.getName())) { 
							if(a.isOnline() && a.usingTagGroup()) { 
								a.updatePrefix();
							}
						}
					}
					sendMessage(sender, false, "§aPrefixo do grupo §7" + group.getName() + " §aalterado para " + prefix.replace("&", "§") + "§a!");
				} else { 
					sendMessage(sender, false, "§cEste grupo não existe!");
				}
			}
			return true;
		}
		return false;
	}
	
	
	private void sintax(CommandSender sender, String label) { 
		sintaxCommand(sender, "§c/" + label + " lista",
							  "§c/" + label + " <criar, deletar, info> <nome>", 
							  "§c/" + label + " perm <add, remover> <grupo> <permissão>",
							  "§c/" + label + " perm <add> <grupo> <permissão> <tempo>",
							  "§c/" + label + " definirprefixo <grupo> <prefixo>",
							  "§c/" + label + " definirconf <grupo> <equipe, padrao>");
	}
}
