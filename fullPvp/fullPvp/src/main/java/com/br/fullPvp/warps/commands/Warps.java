package com.br.fullPvp.warps.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.inventorys.WarpInventory;
import com.br.fullPvp.utils.ItemBuilder;
import com.br.fullPvp.utils.ItemName;
import com.br.fullPvp.utils.SerializeLocation;
import com.br.fullPvp.utils.Utils;
import com.br.fullPvp.warps.Warp;
import com.br.fullPvp.warps.WarpManager;

public class Warps extends Utils implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) { 
			Player player = (Player) sender;
			Account account = AccountManager.getInstance().get(player.getName());
			if(account == null) return true;
			if(args.length == 0) { 
				WarpInventory.getInstance().create(player, account, 1);
				return true;
			} else if(args.length == 1) { 
				if(args[0].equalsIgnoreCase("ajuda")) { 
					if(!account.hasPermission(Permissions.MANAGE_WARP.getPermission())) { 
						sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
						return true;
					}
					sintax(sender, label);
				} else { 
					Warp warp = WarpManager.getInstance().get(captalize(args[0]));
					if(warp != null) {
						AccountManager.getLastWarp().put(account.getUniqueId(), account.inSomeWarp() ? account.getWarp().getName() : "NRE");
						account.setWarp(warp);
						account.startTeleport();
						sendMessage(player, false, "§aTeleportando em §f" + (account.hasPermission(Permissions.VANTAGE_WARP.getPermission()) ? 3 : 5) + "(s) §apara §7" + warp.getName() + "§a!");
					} else { 
						sendMessage(player, false, "§cEsta warp encontra-se com problemas, contacte um administrador!");
					}
				}
				return true;
			} else if(args.length == 2) { 
				if(!account.hasPermission(Permissions.MANAGE_WARP.getPermission())) { 
					sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
					return true;
				}
				Warp warp = WarpManager.getInstance().get(args[1]);
				if(args[0].equalsIgnoreCase("criar")) { 
					if(warp == null) { 
						WarpManager.getInstance().add(new Warp(captalize(args[1]), "NRE", SerializeLocation.getInstance().serializeLocation(player.getLocation(), true), false, false, false, false, new ItemBuilder(Material.STONE).getStack(), new ArrayList<String>()));
						sendMessage(sender, false, "§aWarp §7" + captalize(args[1]) + " §acriada!");
					} else { 
						sendMessage(player, false, "§cEsta warp já existe!");
					}
				} else if(args[0].equalsIgnoreCase("deletar")) { 
					if(warp != null) { 
						for(Account a : warp.getPlayerAccounts()) { 
							a.setWarp(null);
						}
						sendMessage(sender, false, "§aWarp §7" + warp.getName() + " §adeletada!");
						warp.delete();
						WarpManager.getInstance().remove(warp);
					} else { 
						sendMessage(sender, false, "§cEsta warp não existe!");
					}
				} else if(args[0].equalsIgnoreCase("info")) { 
					if(warp != null) {
						StringBuilder list = new StringBuilder();
						for(String c : warp.getBlockedCommands()) { 
							list.append(c + ", ");
						}
						sendMessage(sender, true, 
								  "§aNome §7" + warp.getName() + "§a:", 
								  "§aJogadores §7#" + (warp.getPlayerAccounts().isEmpty() ? "0" : warp.getPlayerAccounts().size()),
								  "§aFechada? §7" + (warp.isClosed() ? "Sim" : "Não"), 
								  "§aExclusiva? §7" + (warp.isExclusive() ? "Sim §8§oPermissão: " + warp.getPermission() : "Não"),
								  "§aFly? §7" + (warp.isFly() ? "Sim" : "Não"),
								  "§aPvP? §7" + (warp.isPvp() ? "Sim" : "Não"),
								  "§aComandos bloqueados: §7" + (list.length() <= 0 ? "§cNenhum comando bloqueado!" : list.toString().substring(0, list.length() - 2)),
								  "§aLocation: ",
								  "  §7X: " + warp.getLocationReal().getX(),
								  "  §7Y: " + warp.getLocationReal().getY(),
								  "  §7Z: " + warp.getLocationReal().getZ(),
								  "  §7Mundo: " + warp.getLocationReal().getWorld().getName());
					} else { 
						sendMessage(sender, false, "§cEsta warp não existe!");
					}
				} else if(args[0].equalsIgnoreCase("definiricone")) { 
					if(warp != null) { 
						if(player.getItemInHand() != null && !player.getItemInHand().getType().equals(Material.AIR)) { 
							warp.setIcon(player.getItemInHand());
							sendMessage(player, false, "§aO ícone da warp §7" + warp.getName() + " §afoi alterado para §7" + ItemName.valueOf(player.getItemInHand().getType(), player.getItemInHand().getDurability()).getName() + "§a!");
						} else { 
							sendMessage(player, false, "§cVocê precisa estar com um item válido na mão!");
						}
					} else {
						sendMessage(sender, false, "§cEsta warp não existe!");
					}
				}
				return true;
			} else if(args.length == 3) {
				if(!account.hasPermission(Permissions.MANAGE_WARP.getPermission())) { 
					sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
					return true;
				}
				Warp warp = WarpManager.getInstance().get(args[1]);
				if(args[0].equalsIgnoreCase("definirconf")) { 
					if(args[2].equalsIgnoreCase("fechar")) { 
						warp.setClosed(warp.isClosed() ? false : true);
						sendMessage(sender, false, "§aWarp §7" + warp.getName() + " §aagora " + (warp.isClosed() ? "está" : "não está") + " fechada!");
					} else if(args[2].equalsIgnoreCase("exclusiva")) { 
						warp.setExclusive(warp.isExclusive() ? false : true);
						sendMessage(sender, false, "§aWarp §7" + warp.getName() + " §aagora " + (warp.isExclusive() ? "é" : "não é") + " exclusiva!");
					} else if(args[2].equalsIgnoreCase("voar")) { 
						warp.setFly(warp.isFly() ? false : true);
						sendMessage(sender, false, "§aNa warp §7" + warp.getName() + " §aagora " + (warp.isFly() ? "pode" : "não pode") + " voar!");
					} else if(args[2].equalsIgnoreCase("pvp")) { 
						warp.setPvp(warp.isPvp() ? false : true);
						sendMessage(sender, false, "§aNa warp §7" + warp.getName() + " §aagora " + (warp.isPvp() ? "pode" : "não pode") + " iniciar um combate!");
					}
				}
				return true;
			} else if(args.length == 4) {
				if(!account.hasPermission(Permissions.MANAGE_WARP.getPermission())) { 
					sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
					return true;
				}
				Warp warp = WarpManager.getInstance().get(args[1]);
				if(args[0].equalsIgnoreCase("definircmd")) { 
					if(warp != null) { 
						List<String> list = warp.getBlockedCommands();
						if(args[2].equalsIgnoreCase("add")) { 
							if(!list.contains(args[3])) { 
								list.add(args[3]);
								warp.setBlockedCommands(list);
								sendMessage(player, false, "§aO comando §7" + args[3] + " §afoi bloqueado na warp §7" + warp.getName() + "§a!");
							} else { 
								sendMessage(player, false, "§cEste comando já é bloqueado nesta warp!");
							}
						} else if(args[0].equalsIgnoreCase("remover")) { 
							if(list.contains(args[3])) { 
								list.remove(args[3]);
								warp.setBlockedCommands(list);
								sendMessage(player, false, "§aO comando §7" + args[3] + " §afoi removido da lista de comandos bloqueados na warp §7" + warp.getName() + "§a!");
							} else { 
								sendMessage(player, false, "§cEste comando não é bloqueado nesta warp!");
							}
						}
					} else {
						sendMessage(sender, false, "§cEsta warp não existe!");
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
		sintaxCommand(sender, "§c/" + label + " <criar, deletar, info> <nome>", 
							  "§c/" + label + " definiricone <nome>",
							  "§c/" + label + " definirconf <nome> <fechar, exclusiva, voar, pvp>",
							  "§c/" + label + " definircmd <nome> <add, remover> <comando>");
	}
}
