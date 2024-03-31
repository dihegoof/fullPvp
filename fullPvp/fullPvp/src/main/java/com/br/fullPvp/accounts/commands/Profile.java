package com.br.fullPvp.accounts.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.accounts.TypeCoin;
import com.br.fullPvp.groups.Group;
import com.br.fullPvp.groups.GroupManager;
import com.br.fullPvp.groups.PermissionsCase;
import com.br.fullPvp.inventorys.AccountInventory;
import com.br.fullPvp.inventorys.AccountInventory.TypeInventoryAccount;
import com.br.fullPvp.utils.TimeManager;
import com.br.fullPvp.utils.Utils;

public class Profile extends Utils implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			if(sender instanceof Player) { 
				Player player = (Player) sender;
				Account account = AccountManager.getInstance().get(player.getName());
				if(account == null) return true;
				AccountInventory.getInstance().create(player, account, TypeInventoryAccount.ACCOUNT, 1);
			} else { 
				sendMessage(sender, false, "Somente jogadores podem digitar este comando!");
			}
			return true;
		} else if(args.length == 1) { 
			if(sender instanceof Player) { 
				Player player = (Player) sender;
				Account account = AccountManager.getInstance().get(player.getName());
				if(account == null) return true;
				if(!account.hasPermission(Permissions.MANAGE_PROFILE.getPermission())) { 
					sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("ajuda") || args[0].equalsIgnoreCase("help")) { 
				sintax(sender, label);
			} else { 
				if(sender instanceof Player) { 
					Player player = (Player) sender;
					Account account = AccountManager.getInstance().get(args[0]);
					if(account != null) { 
						AccountInventory.getInstance().create(player, account, TypeInventoryAccount.ACCOUNT, 1);
					} else { 
						sendMessage(sender, false, "§cEste jogador não possui contas em nosso banco de dados!");
					}
 				} else { 
					sendMessage(sender, false, "Somente jogadores podem digitar este comando!");
				}
			}
			return true;
		} else if(args.length == 3) { 
			if(sender instanceof Player) { 
				Player player = (Player) sender;
				Account account = AccountManager.getInstance().get(player.getName());
				if(account == null) return true;
				if(!account.hasPermission(Permissions.MANAGE_PROFILE.getPermission())) { 
					sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("definir")) { 
				Group group = GroupManager.getInstance().get(args[1]);
				if(group != null) { 
					Account accountTarget = AccountManager.getInstance().get(args[2]);
					if(accountTarget != null) { 
						if(!accountTarget.isGroupPermanent()) { 
							accountTarget.setTimeGroup(-1L);
						}
						accountTarget.setGroupName(group.getName());
						accountTarget.sendMessage(true, "§aVocê recebeu o grupo §7" + group.getName() + " §apermanentemente!");
						accountTarget.updatePrefix();
						sendMessage(sender, false, "§aGrupo §7" + group.getName() + " §adefinido à §7" + accountTarget.getNickName() + " §apermanentemente!");
					} else { 
						sendMessage(sender, false, "§cEste jogador não possui contas em nosso banco de dados!");
					}
				} else { 
					sendMessage(sender, false, "§cEste grupo não existe!");
				}
			} else if(args[0].equalsIgnoreCase("perm")) {
				Account accountTarget = AccountManager.getInstance().get(args[1]);
				if(args[2].equalsIgnoreCase("lista")) { 
					if(accountTarget != null) { 
						StringBuilder list = new StringBuilder();
						for(PermissionsCase p : accountTarget.getPermissions()) { 
							list.append(
									p.isPermanent() ? 
											p.getPermission() + ", " : 
											Long.valueOf(p.getTime()) < System.currentTimeMillis() ? 
													p.getPermission() + "§8(§cexpirado§8)§7, " : 
											p.getPermission() + 
											"§8(" + compareTime(Long.valueOf(p.getTime())) + ")§7, ");
						}
						sendMessage(sender, true, "§aPermissões de §7" + accountTarget.getNickName() + "§a: §7"  + (list.length() <= 0 ? "§cNenhuma permissão encontrada!" : list.toString().substring(0, list.length() - 2)));
					} else { 
						sendMessage(sender, false, "§cEste jogador não possui contas em nosso banco de dados!");
					}
				}
			}
			return true;
		} else if(args.length == 4) { 
			if(sender instanceof Player) { 
				Player player = (Player) sender;
				Account account = AccountManager.getInstance().get(player.getName());
				if(account == null) return true;
				if(!account.hasPermission(Permissions.MANAGE_PROFILE.getPermission())) { 
					sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("definir")) { 
				Group group = GroupManager.getInstance().get(args[1]);
				if(group != null) { 
					Account accountTarget = AccountManager.getInstance().get(args[2]);
					if(accountTarget != null) { 
						if(isTime(args[3])) { 
							if(accountTarget.isGroupPermanent()) { 
								accountTarget.setLastGroupName(accountTarget.getGroupName());
							}
							accountTarget.setGroupName(group.getName());
							accountTarget.setTimeGroup(TimeManager.getInstance().getTime(args[3]));
							accountTarget.sendMessage(true, "§aVocê recebeu o grupo §7" + group.getName() + " §adurante §f" + compareTime(TimeManager.getInstance().getTime(args[3])) + "§a!");
							accountTarget.updatePrefix();
							sendMessage(sender, false, "§aGrupo §7" + group.getName() + " §adefinido à §7" + accountTarget.getNickName() + " §adurante §f" + compareTime(TimeManager.getInstance().getTime(args[3])) + "§a!");
						} else { 
							sendMessage(sender, false, "§cTempo inválido!");
						}
					} else { 
						sendMessage(sender, false, "§cEste jogador não possui contas em nosso banco de dados!");
					}
				} else { 
					sendMessage(sender, false, "§cEste grupo não existe!");
				}
				return true;
			} else if(args[0].equalsIgnoreCase("perm")) {
				Account accountTarget = AccountManager.getInstance().get(args[2]);
				if(accountTarget != null) { 
					if(args[1].equalsIgnoreCase("add")) { 
						if(!accountTarget.hasPermission(args[3])) { 
							accountTarget.add(args[3], -1);
							sendMessage(sender, false, "§aPermissão §7" + args[3] + " §aadicionada ao jogador §7" + accountTarget.getNickName() + " §apermanentemente!");
						} else { 
							sendMessage(sender, false, "§cEste jogador já possui esta permissão!");
						}
					} else if(args[1].equalsIgnoreCase("remover")) { 
						if(accountTarget.hasPermission(args[3])) { 
							accountTarget.remove(args[3]);
							sendMessage(sender, false, "§aPermissão §7" + args[3] + " §aremovida do jogador §7" + accountTarget.getNickName() + "§a!");
						} else { 
							sendMessage(sender, false, "§cEste jogador não possui esta permissão!");
						}
					}
				} else { 
					sendMessage(sender, false, "§cEste jogador não possui contas em nosso banco de dados!");
				}
				return true;
			}
			TypeCoin typeCoin = null;
			try {
				typeCoin = TypeCoin.valueOf(args[0].toUpperCase());
			} catch (Exception e) {
				sendMessage(sender, false, "§cEste tipo de economia não foi encontrada!");
				return true;
			}
			Account accountTarget = AccountManager.getInstance().get(args[2]);
			if(accountTarget != null) { 
				if(isDouble(args[3])) { 
					if(args[1].equalsIgnoreCase("add")) { 
						accountTarget.add(typeCoin, Double.valueOf(args[3]));
						accountTarget.sendMessage(false, "§aFoi creditado em sua conta §f" + (typeCoin.equals(TypeCoin.REAL) ? formatMoney(Double.valueOf(args[3])) : formatCash(Double.valueOf(args[3]))) + "§a!");
					} else if(args[1].equalsIgnoreCase("remover")) { 
						accountTarget.remove(typeCoin, Double.valueOf(args[3]));
						accountTarget.sendMessage(false, "§aFoi removido da sua conta §f" + (typeCoin.equals(TypeCoin.REAL) ? formatMoney(Double.valueOf(args[3])) : formatCash(Double.valueOf(args[3]))) + "§a!");
					} else { 
						sendMessage(sender, false, "§cÉ possível apenas adicionar e remover valores!");
						return true;
					}
				} else { 
					sendMessage(sender, false, "§cVocê precisa digitar números!");
					return true;
				}
			} else { 
				sendMessage(sender, false, "§cEste jogador não possui contas em nosso banco de dados!");
				return true;
			}
			return true;
		} else if(args.length == 5) { 
			if(args[0].equalsIgnoreCase("perm")) {
				Account accountTarget = AccountManager.getInstance().get(args[2]);
				if(accountTarget != null) { 
					if(args[1].equalsIgnoreCase("add")) { 
						if(!accountTarget.hasPermission(args[3])) { 
							if(isTime(args[4])) { 
								accountTarget.add(args[3], TimeManager.getInstance().getTime(args[4]));
							} else {
								sendMessage(sender, false, "§cTempo inválido!");
								return true;
							}
							sendMessage(sender, false, "§aPermissão §7" + args[3] + " §aadicionada ao jogador §7" + accountTarget.getNickName() + " §adurante §f" + compareTime(TimeManager.getInstance().getTime(args[4])) + "§a!");
						} else { 
							sendMessage(sender, false, "§cEste jogador já possui esta permissão!");
						}
					}
				} else { 
					sendMessage(sender, false, "§cEste jogador não possui contas em nosso banco de dados!");
				}
			}
			return true;
		}
		return false;
	}
	
	private void sintax(CommandSender sender, String label) { 
		sintaxCommand(sender, "§c/" + label + " <jogador>", 
							  "§c/" + label + " <real, cash, reputacao> <add, remover> <jogador> <quantidade>",
							  "§c/" + label + " perm <add, remover> <jogador> <permissão>",
							  "§c/" + label + " perm <add> <jogador> <permissão> <tempo>",
							  "§c/" + label + " perm <jogador> lista",
							  "§c/" + label + " definir <grupo> <jogador> <tempo>");
	}
}
