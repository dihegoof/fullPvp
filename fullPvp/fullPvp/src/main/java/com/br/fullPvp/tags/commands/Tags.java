package com.br.fullPvp.tags.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.inventorys.TagInventory;
import com.br.fullPvp.inventorys.TagInventory.TypeInventoryTag;
import com.br.fullPvp.tags.Tag;
import com.br.fullPvp.tags.TagManager;
import com.br.fullPvp.utils.Utils;

public class Tags extends Utils implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) { 
			if(sender instanceof Player) { 
				Player player = (Player) sender;
				Account account = AccountManager.getInstance().get(player.getName());
				if(account == null) return true;
				TagInventory.getInstance().create(player, account, TypeInventoryTag.MENU, 1);
			} else { 
				sendMessage(sender, false, "Somente jogadores podem digitar este comando!");
			}
			return true;
		} else if(args.length == 1) {
			if(sender instanceof Player) { 
				Player player = (Player) sender;
				Account account = AccountManager.getInstance().get(player.getName());
				if(account == null) return true;
				if(!account.hasPermission(Permissions.MANAGE_TAG.getPermission())) { 
					sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("ajuda")) { 
				sintax(sender, label);
			}
			return true;
		} else if(args.length == 2) {
			if(sender instanceof Player) { 
				Player player = (Player) sender;
				Account account = AccountManager.getInstance().get(player.getName());
				if(account == null) return true;
				if(!account.hasPermission(Permissions.MANAGE_TAG.getPermission())) { 
					sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
					return true;
				}
			}
			Tag tag = TagManager.getInstance().get(args[1]);
			if(args[0].equalsIgnoreCase("criar")) { 
				if(tag == null) { 
					TagManager.getInstance().add(new Tag(captalize(args[1]), "NRE", "NRE", false));
					sendMessage(sender, false, "§aTag §7" + captalize(args[1]) + " §acriada!");
				} else { 
					sendMessage(sender, false, "§cEsta tag já existe!");
				}
			} else if(args[0].equalsIgnoreCase("deletar")) { 
				if(tag != null) {
					for(Account a : AccountManager.getStorageAccounts()) { 
						List<String> tags = a.getTags();
						if(tags.contains(tag.getName())) { 
							tags.remove(tag.getName());
							a.setTags(tags);
						}
						if(a.getTagUsing().equalsIgnoreCase(tag.getName())) { 
							a.setTagUsing("NRE");
							a.updatePrefix();
						}
					}
					tag.delete();
					TagManager.getInstance().remove(tag);
					sendMessage(sender, false, "§aTag §7" + captalize(args[1]) + " §adeletada!");
				} else { 
					sendMessage(sender, false, "§cEsta tag não existe!");
				}
			} else if(args[0].equalsIgnoreCase("info")) { 
				if(tag != null) { 
					sendMessage(sender, true, 
							  "§aTag §7" + tag.getName() + "§a: §8(" + tag.getPrefix().replace("&", "§") + "§8)", 
							  "§aGrátis? §7" + (tag.isFree() ? "Sim" : "Sim §8§oPermissão: " + tag.getPermission()));
				} else { 
					sendMessage(sender, false, "§cEsta tag não existe!");
				}
			}
			return true;
		} else if(args.length == 3) {
			if(sender instanceof Player) { 
				Player player = (Player) sender;
				Account account = AccountManager.getInstance().get(player.getName());
				if(account == null) return true;
				if(!account.hasPermission(Permissions.MANAGE_TAG.getPermission())) { 
					sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
					return true;
				}
			}
			Tag tag = TagManager.getInstance().get(args[1]);
			if(args[0].equalsIgnoreCase("definirprefixo")) { 
				if(tag != null) { 
					String prefix = createArgs(2, args, label, false);
					tag.setPrefix(prefix.replace("&", "§"));
					for(Account a : AccountManager.getStorageAccounts()) { 
						if(a.getTagUsing().equalsIgnoreCase(tag.getName())) { 
							a.updatePrefix();
						}
					}
					sendMessage(sender, false, "§aPrefixo da tag §7" + tag.getName() + " §aalterado para " + prefix.replace("&", "§") + "§a!");
				} else {
					sendMessage(sender, false, "§cEsta tag não existe!");
				}
			} else if(args[0].equalsIgnoreCase("definirconf")) { 
				if(args[2].equalsIgnoreCase("gratis")) { 
					if(tag != null) {
						if(!tag.isFree()) { 
							tag.setFree(true);
							tag.setPermission("NRE");
							sendMessage(sender, false, "§aTag §7" + tag.getName() + " §aagora " + (tag.isFree() ? "é" : "não é") + " grátis!");
						} else { 
							sendMessage(sender, false, "§cEsta tag é grátis, para alterar, acrescente a permissão ao final da sintaxe!");
						}
					} else { 
						sendMessage(sender, false, "§cEsta tag não existe!");
					}
				}
			}
			return true;
		} else if(args.length == 4) {
			if(sender instanceof Player) { 
				Player player = (Player) sender;
				Account account = AccountManager.getInstance().get(player.getName());
				if(account == null) return true;
				if(!account.hasPermission(Permissions.MANAGE_TAG.getPermission())) { 
					sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("definirconf")) { 
				Tag tag = TagManager.getInstance().get(args[1]);
				if(args[2].equalsIgnoreCase("gratis")) { 
					if(tag != null) { 
						if(tag.isFree()) { 
							tag.setFree(false);
							tag.setPermission(args[3]);
							sendMessage(sender, false, "§aTag §7" + tag.getName() + " §aagora " + (tag.isFree() ? "é" : "não é") + " grátis!");
						} else { 
							sendMessage(sender, false, "§cEsta tag não é grátis, para alterar, remova a permissão do final da sintaxe!");
						}
					} else { 
						sendMessage(sender, false, "§cEsta tag não existe!");
					}
				}
			}
			return true;
		}
		return false;
	}
	
	private void sintax(CommandSender sender, String label) { 
		sintaxCommand(sender, "§c/" + label + " <criar, deletar, info> <nome>", 
							  "§c/" + label + " definirprefixo <tag> <prefixo>",
							  "§c/" + label + " definirconf <tag> <gratis>",
							  "§c/" + label + " definirconf <tag> <gratis> <permissão>");
	}
}
