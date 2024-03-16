package com.br.fullPvp.inventorys.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.br.fullPvp.Main;
import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.accounts.TypeCoin;
import com.br.fullPvp.inventorys.AccountInventory;
import com.br.fullPvp.inventorys.TagInventory;
import com.br.fullPvp.inventorys.TagInventory.TypeInventoryTag;
import com.br.fullPvp.inventorys.WarpInventory;
import com.br.fullPvp.inventorys.AccountInventory.TypeInventoryAccount;
import com.br.fullPvp.ranks.RankManager;
import com.br.fullPvp.tags.Tag;
import com.br.fullPvp.tags.TagManager;
import com.br.fullPvp.utils.ActionBar;
import com.br.fullPvp.utils.Utils;
import com.br.fullPvp.warps.Warp;
import com.br.fullPvp.warps.WarpManager;

public class InventoryListener extends Utils implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) { 
		if(event.getClickedInventory() == null) return;
		if(event.getInventory().getTitle() == null) { 
			if(event.getCurrentItem() != null) {
				Main.debug("Clickou em inv desbloqueado");
			}
		}
		Player player = (Player) event.getWhoClicked();
		Account account = null;
		if(event.getInventory().getTitle().equals("Sua conta") ||  event.getInventory().getTitle().startsWith("Conta de ")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = event.getInventory().getTitle().equals("Sua conta") ? AccountManager.getInstance().get(player.getName()) : AccountManager.getInstance().get( event.getInventory().getTitle().replace("Conta de ", ""));
			if(account == null) return;
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aStatus")) {
				AccountInventory.getInstance().create(player, account, TypeInventoryAccount.STATUS, 1);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPreferências")) {
				AccountInventory.getInstance().create(player, account, TypeInventoryAccount.PREFERENCES, 1);
			}
			return;
		} else if(event.getInventory().getTitle().equals("Seu status") ||  event.getInventory().getTitle().startsWith("Status de ")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = event.getInventory().getTitle().equals("Seu status") ? AccountManager.getInstance().get(player.getName()) : AccountManager.getInstance().get( event.getInventory().getTitle().replace("Status de ", ""));
			if(account == null) return;
			if(event.getCurrentItem().getType() == Material.ARROW) {
				AccountInventory.getInstance().create(player, account, TypeInventoryAccount.ACCOUNT, 1);
			}
			return;
		} else if(event.getInventory().getTitle().startsWith("Preferências")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = event.getInventory().getTitle().equals("Preferências") ? AccountManager.getInstance().get(player.getName()) : AccountManager.getInstance().get(event.getInventory().getTitle().replace("Preferências de ", ""));
			if(account == null) return;
			if(event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
				if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cDesativar")) { 
					if(event.getSlot() == 19) { 
						account.getPreferences().setReceiveTell(false);
					} else if(event.getSlot() == 20) { 
						account.getPreferences().setReceiveDonate(false);
					} else if(event.getSlot() == 21) { 
						account.getPreferences().setScoreBoard(false);
						//Scoreboarding.getInstance().turnMode(player, false);
					} else if(event.getSlot() == 22) { 
						account.getPreferences().setRequestsClan(false);
					} else if(event.getSlot() == 23) { 
						account.getPreferences().setRequestsTpa(false);
					} else if(event.getSlot() == 24) { 
						account.getPreferences().setStaffChat(false);
					} else if(event.getSlot() == 25) { 
						Bukkit.dispatchCommand(player, "admin");
					} else if(event.getSlot() == 37) { 
						account.getPreferences().setReceiveReports(false);
					} else if(event.getSlot() == 38) { 
						account.getPreferences().setReceiveWarns(false);
					}
				} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aAtivar")) { 
					if(event.getSlot() == 19) { 
						account.getPreferences().setReceiveTell(true);
					} else if(event.getSlot() == 20) { 
						account.getPreferences().setReceiveDonate(true);
					} else if(event.getSlot() == 21) { 
						account.getPreferences().setScoreBoard(true);
						 //Scoreboarding.getInstance().create(player, Scoreboarding.getTypeScoreboard());
					} else if(event.getSlot() == 22) { 
						account.getPreferences().setRequestsClan(true);
					} else if(event.getSlot() == 23) { 
						account.getPreferences().setRequestsTpa(true);
					} else if(event.getSlot() == 24) { 
						account.getPreferences().setStaffChat(true);
					} else if(event.getSlot() == 25) { 
						Bukkit.dispatchCommand(player, "admin");
					} else if(event.getSlot() == 37) { 
						account.getPreferences().setReceiveReports(true);
					} else if(event.getSlot() == 38) { 
						account.getPreferences().setReceiveWarns(true);
					}
				}
				AccountInventory.getInstance().create(player, account, TypeInventoryAccount.PREFERENCES, 1);
			} else if(event.getCurrentItem().getType() == Material.ARROW) {
				AccountInventory.getInstance().create(player, account, TypeInventoryAccount.ACCOUNT, 1);
			}
			return;
		} else if(event.getInventory().getTitle().equalsIgnoreCase("Upe de rank")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			if(account == null) return;
			if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aConfirmar")) { 
				if(account.allowUp()) { 
					for(String r : RankManager.getInstance().get(account.getRankName()).getRequirements()) { 
						String[] split = r.split(";");
						account.remove(TypeCoin.valueOf(split[0]), Double.valueOf(split[1]));
					}
					account.setRankName(RankManager.getInstance().get(account.getRankName()).nextRank().getName());
					for(Player p : Bukkit.getOnlinePlayers()) { 
						if(p != player) { 
							ActionBar.getInstance().sendActionBarMessage(p, "§7" + player.getName() + " §esubiu para o rank §7" + account.getRankName());
						} else {
							ActionBar.getInstance().sendActionBarMessage(player, "§eVocê subiu para o rank §7" + account.getRankName());
						}
					}
				} else { 
					sendMessage(player, false, "§cVocê não possui os requisitos necessários para subir de rank!");
				}
				player.closeInventory();
			}
			return;
		} else if(event.getInventory().getTitle().equalsIgnoreCase("Warps")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			if(account == null) return;
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ") || event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) { 
				int pageNow = 0;
				if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§a")) { 
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				} else {
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				}
				WarpInventory.getInstance().create(player, account, pageNow);
			} else { 
				Warp warp = WarpManager.getInstance().get(event.getCurrentItem().getItemMeta().getDisplayName().replace("§a", ""));
				if(warp != null) {
					AccountManager.getLastWarp().put(account.getUniqueId(), account.inSomeWarp() ? account.getWarp().getName() : "NRE");
					account.setWarp(warp);
					account.startTeleport();
					sendMessage(player, false, "§aTeleportando em §f" + (account.hasPermission(Permissions.VANTAGE_WARP.getPermission()) ? 3 : 5) + "(s) §apara §7" + warp.getName() + "§a!");
				} else { 
					sendMessage(player, false, "§cEsta warp encontra-se com problemas, contacte um administrador!");
				}
				player.closeInventory();
			}
			return;
		} else if(event.getInventory().getTitle().equalsIgnoreCase("Tag")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			if(account == null) return;
			if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aSuas tags")) { 
				TagInventory.getInstance().create(player, account, TypeInventoryTag.YOUR_TAGS, 1);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aTags")) { 
				TagInventory.getInstance().create(player, account, TypeInventoryTag.LIST_TAGS, 1);
			}
			return;
		} else if(event.getInventory().getTitle().equalsIgnoreCase("Suas tags")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			if(account == null) return;
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ") || event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) { 
				int pageNow = 0;
				if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§a")) { 
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				} else {
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				}
				TagInventory.getInstance().create(player, account, TypeInventoryTag.YOUR_TAGS, pageNow);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVoltar")) { 
				TagInventory.getInstance().create(player, account, TypeInventoryTag.MENU, 1);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aTag do grupo atual")) { 
				account.setTagUsing("NRE");
				account.updatePrefix();
				player.closeInventory();
				sendMessage(player, false, "§aTag do grupo atual atualizada!");
			} else { 
				Tag tag = TagManager.getInstance().get(event.getCurrentItem().getItemMeta().getDisplayName().replace("§a", ""));
				if(tag != null) { 
					account.setTagUsing(tag.getName());
					account.updatePrefix();
					sendMessage(player, false, "§aTag §7" + tag.getName() + " §aatualizada com sucesso!");
				} else { 
					sendMessage(player, false, "§cEsta tag encontra-se com problemas, contacte um administrador!");
				}
				player.closeInventory();
			}
			return;
		} else if(event.getInventory().getTitle().equalsIgnoreCase("Tags")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			if(account == null) return;
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ") || event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) { 
				int pageNow = 0;
				if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§a")) { 
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				} else {
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				}
				TagInventory.getInstance().create(player, account, TypeInventoryTag.LIST_TAGS, pageNow);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVoltar")) { 
				TagInventory.getInstance().create(player, account, TypeInventoryTag.MENU, 1);
			} else { 
				Tag tag = TagManager.getInstance().get(event.getCurrentItem().getItemMeta().getDisplayName().replace("§a", ""));
				if(tag != null) { 
					List<String> tags = account.getTags();
					if(!tags.contains(tag.getName())) { 
						if(tag.isFree()) { 
							tags.add(tag.getName());
							if(account.hasPermission(tag.getPermission())) { 
								tags.add(tag.getName());
							} else { 
								sendMessage(player, false, "§cVocê não tem permissão para reivindicar esta tag!");
								player.closeInventory();
								return;
							}
						}
						account.setTags(tags);
						sendMessage(player, false, "§aTag §7" + tag.getName() + " §areivindicada!");
					} else { 
						sendMessage(player, false, "§cVocê já possui esta tag!");
					}
				} else { 
					sendMessage(player, false, "§cEsta tag encontra-se com problemas, contacte um administrador!");
				}
				player.closeInventory();
			}
			return;
		}
	}
}
