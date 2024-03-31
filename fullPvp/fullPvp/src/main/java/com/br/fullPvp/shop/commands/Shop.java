package com.br.fullPvp.shop.commands;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.inventorys.ShopInventory;
import com.br.fullPvp.inventorys.ShopInventory.TypeInventoryShop;
import com.br.fullPvp.shop.item.Item;
import com.br.fullPvp.shop.item.ItemManager;
import com.br.fullPvp.shop.session.Session;
import com.br.fullPvp.shop.session.SessionManager;
import com.br.fullPvp.utils.ItemBuilder;
import com.br.fullPvp.utils.Utils;

public class Shop extends Utils implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) { 
			Player player = (Player) sender;
			Account account = AccountManager.getInstance().get(player.getName());
			if(account == null) return true;
			if(args.length == 0) { 
				if(account.getPreferences().isAdminMode() && account.hasPermission(Permissions.MANAGE_SHOP.getPermission())) { 
					sendMessage(player, false, "§7Você pode alterar os preços dos itens e defini-los como promocionais!");
				}
				ShopInventory.getInstance().create(player, account, TypeInventoryShop.SESSIONS, null, null, 1, 1);
				return true;
			} else if(args.length == 1) { 
				if(args[0].equalsIgnoreCase("ajuda") || args[0].equalsIgnoreCase("help")) { 
					if(!account.hasPermission(Permissions.MANAGE_SHOP.getPermission())) { 
						sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
						return true;
					}
					sintax(sender, label);
				}
				return true;
			} else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("criar")) { 
					Session session = SessionManager.getInstance().get(captalize(args[1]));
					if(session == null) { 
						SessionManager.getInstance().add(new Session(captalize(args[1]), 0.0D, new ItemBuilder(Material.STONE).getStack(), false));
						sendMessage(sender, false, "§aSessão §7" + captalize(args[1]) + " §acriada!");
					} else { 
						sendMessage(player, false, "§cEsta sessão já existe!");
					}
				} else if(args[0].equalsIgnoreCase("deletar")) { 
					Session session = SessionManager.getInstance().get(captalize(args[1]));
					if(session != null) { 
						sendMessage(sender, false, "§aSessão §7" + session.getName() + " §adeletada!");
						session.delete();
						SessionManager.getInstance().remove(session);
					} else { 
						sendMessage(sender, false, "§cEsta sessão não existe!");
					}
				}
				return true;
			} else if(args.length == 3) { 
				if(args[0].equalsIgnoreCase("item")) {
					if(args[1].equalsIgnoreCase("add")) {
						Session session = SessionManager.getInstance().get(captalize(args[2]));
						if(session != null) { 
							if(!account.isInventoryEmpty()) {
								int amount = 0;
								for(ItemStack i : player.getInventory().getContents()) { 
									if(i != null && i.getType() != Material.AIR) { 
										ItemManager.getInstance().add(new Item(i, session, 0.0D, 0.0D, false));
										amount++;
									}
								}
								sendMessage(player, false, (amount > 0 ? "§aForam adicionados §f" + amount + " §aà sessão §7" + session.getName() + "§a!" : "§cNenhum item foi adicionado à sessão §7" + session.getName() + "§c!"));
								player.getInventory().clear();
							} else { 
								for(Item i : session.getItens()) { 
									i.delete();
									ItemManager.getInstance().remove(i);
								}
								sendMessage(player, false, "§cItens da sessão §7" + session.getName() + " §cdeletados!");
							}
						} else { 
							sendMessage(sender, false, "§cEsta sessão não existe!");
						}
					}
				}
				return true;
			} else if(args.length == 4) { 
				if(args[0].equalsIgnoreCase("valor")) {
					Session session = SessionManager.getInstance().get(captalize(args[1]));
					if(session != null) { 
						if(isInteger(args[2])) { 
							if(isInteger(args[3])) { 
								int amount = 0;
								for(Item i : session.getItens()) { 
									int price = new Random().nextInt((Integer.valueOf(args[3]) - Integer.valueOf(args[2])) + 1) + Integer.valueOf(args[2]);
									i.setPrice(price);
									amount++;
								}
								sendMessage(sender, false, "§aPreço randômico foi definido para §f" + amount + " §aitens da sessão §7" + session.getName() + "§a!");
							} else { 
								sendMessage(sender, false, "§cNúmero inválido!");
							}
						} else { 
							sendMessage(sender, false, "§cNúmero inválido!");
						}
					} else { 
						sendMessage(sender, false, "§cEsta sessão não existe!");
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
		sintaxCommand(sender, "§c/" + label + " <criar, deletar> <sessão>", 
							  "§c/" + label + " item <add> <sessão>",
							  "§c/" + label + " valor <sessão> <valor mínimo> <valor máximo>"
							  );
	}
}
