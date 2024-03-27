package com.br.fullPvp.kits.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.kits.Kit;
import com.br.fullPvp.kits.Kit.Delay;
import com.br.fullPvp.kits.KitManager;
import com.br.fullPvp.utils.ItemBuilder;
import com.br.fullPvp.utils.ItemName;
import com.br.fullPvp.utils.TimeManager;
import com.br.fullPvp.utils.Utils;

public class Kits extends Utils implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) { 
			Player player = (Player) sender;
			Account account = AccountManager.getInstance().get(player.getName());
			if(account == null) return true;
			if(args.length == 0) {
				//Abrir inventário
				return true;
			} else if(args.length == 1) { 
				if(!account.hasPermission(Permissions.MANAGE_KIT.getPermission())) { 
					sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
					return true;
				}
				if(args[0].equalsIgnoreCase("ajuda") || args[0].equalsIgnoreCase("help")) { 
					sintax(sender, label);
				}
				return true;
			} else if(args.length == 2) { 
				if(!account.hasPermission(Permissions.MANAGE_KIT.getPermission())) { 
					sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
					return true;
				}
				Kit kit = KitManager.getInstance().get(args[1]);
				if(args[0].equalsIgnoreCase("criar")) { 
					if(kit == null) { 
						if(!account.isInventoryEmpty()) {
							List<ItemStack> itens = new ArrayList<>();
							for(ItemStack i : player.getInventory().getContents()) { 
								if(i != null && i.getType() != Material.AIR) { 
									itens.add(i);
								}
							}
							KitManager.getInstance().add(new Kit(captalize(args[1]), "kit." + args[1].toLowerCase(), "5h", new ItemBuilder(Material.STONE).getStack(), Delay.CUSTOM, itens, false));
							sendMessage(sender, false, "§aKit §7" + captalize(args[1]) + " §acriado!");
						} else { 
							sendMessage(player, false, "§cVocê está com o inventário vazio!");
						}
					} else { 
						sendMessage(sender, false, "§cEste kit já existe!");
					}
				} else if(args[0].equalsIgnoreCase("deletar")) { 
					if(kit != null) { 
						sendMessage(sender, false, "§aKit §7" + kit.getName() + " §adeletado!");
						kit.delete();
						KitManager.getInstance().remove(kit);
					} else {
						sendMessage(sender, false, "§cEste kit não existe!");
					}
				} else if(args[0].equalsIgnoreCase("info")) { 
					if(kit != null) { 
						sendMessage(sender, true, 
								  "§aNome §7" + kit.getName() + "§a:", 
								  "§aGratis? §7" + (kit.isFree() ? "Sim" : "Não §8§oPermissão:" + kit.getPermission()), 
								  "§aDelay? §7" + kit.getDelay().getName() + " §8(" + (kit.getDelay().equals(Delay.CUSTOM) ? compareTime(TimeManager.getInstance().getTime(kit.getTimeCustom())) : compareTime(TimeManager.getInstance().getTime(kit.getDelay().getDelay()))) + ")");
					} else { 
						sendMessage(sender, false, "§cEste kit não existe!");
					}
				} else if(args[0].equalsIgnoreCase("definiricone")) { 
					if(kit != null) { 
						if(player.getItemInHand() != null && !player.getItemInHand().getType().equals(Material.AIR)) { 
							kit.setIcon(player.getItemInHand());
							sendMessage(player, false, "§aO ícone do kit §7" + kit.getName() + " §afoi alterado para §7" + ItemName.valueOf(player.getItemInHand().getType(), player.getItemInHand().getDurability()).getName() + "§a!");
						} else { 
							sendMessage(player, false, "§cVocê precisa estar com um item válido na mão!");
						}
					} else { 
						sendMessage(sender, false, "§cEste kit não existe!");
					}
				}
				return true;
			} else if(args.length == 3) { 
				if(!account.hasPermission(Permissions.MANAGE_KIT.getPermission())) { 
					sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
					return true;
				}
				Kit kit = KitManager.getInstance().get(args[1]);
				if(args[0].equalsIgnoreCase("definirconf")) { 
					if(kit != null) {
						if(args[2].equalsIgnoreCase("gratis")) { 
							if(!kit.isFree()) { 
								kit.setFree(true);
								kit.setPermission("NRE");
								sendMessage(sender, false, "§aKit §7" + kit.getName() + " §aagora " + (kit.isFree() ? "é" : "não é") + " grátis!");
							} else { 
								sendMessage(sender, false, "§cEste kit é grátis, para alterar, acrescente a permissão ao final da sintaxe!");
							}
						} 
					} else { 
						sendMessage(sender, false, "§cEste kit não existe!");
					}
				}
				return true;
			} else if(args.length == 4) {
				if(!account.hasPermission(Permissions.MANAGE_KIT.getPermission())) { 
					sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
					return true;
				}
				Kit kit = KitManager.getInstance().get(args[1]);
				if(args[0].equalsIgnoreCase("definirconf")) { 
					if(kit != null) {
						if(args[2].equalsIgnoreCase("gratis")) { 
							if(kit.isFree()) { 
								kit.setFree(false);
								kit.setPermission(args[3].toLowerCase());
								sendMessage(sender, false, "§aKit §7" + kit.getName() + " §aagora " + (kit.isFree() ? "é" : "não é") + " grátis!");
							} else { 
								sendMessage(sender, false, "§cEste kit não grátis, para alterar, remova a permissão do final da sintaxe!");
							}
						} else if(args[2].equalsIgnoreCase("tempo")) { 
							if(args[3].equalsIgnoreCase("diario")) { 
								kit.setDelay(Delay.DIARY);
							} else if(args[3].equalsIgnoreCase("semanal")) { 
								kit.setDelay(Delay.WEEK);
							} else if(args[3].equalsIgnoreCase("mensal")) { 
								kit.setDelay(Delay.MONTH);
							}
							sendMessage(player, false, "§aKit §7" + kit.getName() + " §acom delay alterado para " + kit.getDelay().getName().toLowerCase() + "§a!");
						}
					} else { 
						sendMessage(sender, false, "§cEste kit não existe!");
					}
				}
				return true;
			} else if(args.length == 5) { 
				if(!account.hasPermission(Permissions.MANAGE_KIT.getPermission())) { 
					sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
					return true;
				}
				Kit kit = KitManager.getInstance().get(args[1]);
				if(args[0].equalsIgnoreCase("definirconf")) { 
					if(kit != null) {
						if(args[2].equalsIgnoreCase("tempo")) { 
							if(args[3].equalsIgnoreCase("custom")) { 
								kit.setDelay(Delay.CUSTOM);
								kit.setTimeCustom(args[4]);
								sendMessage(player, false, "§aKit §7" + kit.getName() + " §acom delay alterado para " + kit.getDelay().getName().toLowerCase() + " §acom §f" + TimeManager.getInstance().getTime(args[4]) + "§a!");
							}
						}
					} else { 
						sendMessage(sender, false, "§cEste kit não existe!");
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
							  "§c/" + label + " definirconf <nome> <gratis>",
							  "§c/" + label + " definirconf <nome> <gratis> <permissão>",
							  "§c/" + label + " definirconf <nome> tempo <diario, semanal, mensal>",
							  "§c/" + label + " definirconf <nome> tempo <custom> <tempo>");
	}
}
