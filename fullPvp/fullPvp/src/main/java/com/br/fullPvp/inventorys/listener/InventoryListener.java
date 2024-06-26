package com.br.fullPvp.inventorys.listener;

import java.util.ArrayList;
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
import com.br.fullPvp.accounts.AccountManager.Action;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.accounts.TypeCoin;
import com.br.fullPvp.clans.Clan;
import com.br.fullPvp.clans.ClanGroup;
import com.br.fullPvp.clans.ClanManager;
import com.br.fullPvp.inventorys.AccountInventory;
import com.br.fullPvp.inventorys.ClanInventory;
import com.br.fullPvp.inventorys.KitInventory;
import com.br.fullPvp.inventorys.KitInventory.TypeInventoryKit;
import com.br.fullPvp.inventorys.ShopInventory;
import com.br.fullPvp.inventorys.ShopInventory.TypeInventoryShop;
import com.br.fullPvp.inventorys.ClanInventory.TypeInventoryClan;
import com.br.fullPvp.inventorys.TagInventory;
import com.br.fullPvp.inventorys.TagInventory.TypeInventoryTag;
import com.br.fullPvp.inventorys.WarpInventory;
import com.br.fullPvp.kits.Kit;
import com.br.fullPvp.kits.KitCollected;
import com.br.fullPvp.kits.KitManager;
import com.br.fullPvp.kits.PlayerKitManager;
import com.br.fullPvp.kits.PlayerKits;
import com.br.fullPvp.kits.Kit.Delay;
import com.br.fullPvp.inventorys.AccountInventory.TypeInventoryAccount;
import com.br.fullPvp.ranks.RankManager;
import com.br.fullPvp.ranks.Requeriments;
import com.br.fullPvp.shop.item.Item;
import com.br.fullPvp.shop.item.ItemManager;
import com.br.fullPvp.shop.session.Session;
import com.br.fullPvp.shop.session.SessionManager;
import com.br.fullPvp.shop.shoppingcart.ShoppingCart;
import com.br.fullPvp.shop.shoppingcart.ShoppingCartManager;
import com.br.fullPvp.shop.shoppingcart.SubItem;
import com.br.fullPvp.tags.Tag;
import com.br.fullPvp.tags.TagManager;
import com.br.fullPvp.utils.ActionBar;
import com.br.fullPvp.utils.ItemBuilder;
import com.br.fullPvp.utils.ItemName;
import com.br.fullPvp.utils.Utils;
import com.br.fullPvp.warps.Warp;
import com.br.fullPvp.warps.WarpManager;

public class InventoryListener extends Utils implements Listener {
	
	@SuppressWarnings("deprecation")
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
					for(Requeriments r : RankManager.getInstance().get(account.getRankName()).getRequirements()) { 
						account.remove(r.getTypeCoin(), r.getValue());
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
		} else if(event.getInventory().getTitle().equals("Seu clã") ||  event.getInventory().getTitle().startsWith("Clã ")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			Clan clan = ClanManager.getInstance().get(event.getInventory().getTitle().equals("Seu clã") ? account.getClanName() : event.getInventory().getTitle().replace("Clã ", ""));
			if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aMembros")) { 
				ClanInventory.getInstance().create(player, TypeInventoryClan.MEMBERS, account, clan, 1);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aStatus")) { 
				ClanInventory.getInstance().create(player, TypeInventoryClan.STATUS, account, clan, 1);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aConvites")) { 
				ClanInventory.getInstance().create(player, TypeInventoryClan.REQUESTS, account, clan, 1);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aAlíados")) { 
				ClanInventory.getInstance().create(player, TypeInventoryClan.ALLIES, account, clan, 1);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aÍnimigos")) { 
				ClanInventory.getInstance().create(player, TypeInventoryClan.ENEMIES, account, clan, 1);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aEscalação")) { 
				ClanInventory.getInstance().create(player, TypeInventoryClan.SCALATION, account, clan, 1);
			}
			return;
		} else if(event.getInventory().getTitle().equals("Seus alíados")) { 
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			Clan clan = ClanManager.getInstance().get(account.getClanName());
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ") || event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) { 
				int pageNow = 0;
				if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§a")) { 
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				} else {
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				}
				ClanInventory.getInstance().create(player, TypeInventoryClan.ALLIES, account, clan, pageNow);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVoltar")) { 
				ClanInventory.getInstance().create(player, TypeInventoryClan.INFO, account, clan, 1);
			}
			return;
		} else if(event.getInventory().getTitle().equals("Seus inimigos")) { 
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			Clan clan = ClanManager.getInstance().get(account.getClanName());
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ") || event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) { 
				int pageNow = 0;
				if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§a")) { 
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				} else {
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				}
				ClanInventory.getInstance().create(player, TypeInventoryClan.ENEMIES, account, clan, pageNow);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVoltar")) { 
				ClanInventory.getInstance().create(player, TypeInventoryClan.INFO, account, clan, 1);
			}
			return;
		} else if(event.getInventory().getTitle().equals("Membros do seu clã") ||  event.getInventory().getTitle().startsWith("Membros do clã ")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			Clan clan = ClanManager.getInstance().get(event.getInventory().getTitle().equals("Membros do seu clã") ? account.getClanName() : event.getInventory().getTitle().replace("Membros do clã ", ""));
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ") || event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) { 
				int pageNow = 0;
				if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§a")) { 
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				} else {
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				}
				ClanInventory.getInstance().create(player, TypeInventoryClan.MEMBERS, account, clan, pageNow);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVoltar")) { 
				ClanInventory.getInstance().create(player, TypeInventoryClan.INFO, account, clan, 1);
			}
		} else if(event.getInventory().getTitle().equals("Status do seu clã") ||  event.getInventory().getTitle().startsWith("Status do clã ")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			Clan clan = ClanManager.getInstance().get(event.getInventory().getTitle().equals("Status do seu clã") ? account.getClanName() : event.getInventory().getTitle().replace("Status do clã ", ""));
			if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVoltar")) { 
				ClanInventory.getInstance().create(player, TypeInventoryClan.INFO, account, clan, 1);
			}
			return;
		} else if(event.getInventory().getTitle().equals("Convites do seu clã")) { 
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			Clan clan = ClanManager.getInstance().get(account.getClanName());
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ") || event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) { 
				int pageNow = 0;
				if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§a")) { 
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				} else {
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				}
				ClanInventory.getInstance().create(player, TypeInventoryClan.REQUESTS, account, clan, pageNow);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVoltar")) { 
				ClanInventory.getInstance().create(player, TypeInventoryClan.INFO, account, clan, 1);
			} else {
				if(event.isRightClick()) { 
					if(account.hasGroupClan(ClanGroup.RECRUIT)) { 
						List<String> list = clan.getInvites();
						if(list.contains(event.getCurrentItem().getItemMeta().getDisplayName().replace("§a", ""))) { 
							list.remove(event.getCurrentItem().getItemMeta().getDisplayName().replace("§a", ""));
							clan.setInvites(list);
						} else { 
							sendMessage(player, false, "§cEste convite expirou!");
						}
					} else { 
						sendMessage(player, false, "§cVocê precisa ser ao menos recruta para cancelar um convite!");
					}
					player.closeInventory();
					ClanInventory.getInstance().create(player, TypeInventoryClan.REQUESTS, account, clan, 1);
				}
			}
			return;
		} else if(event.getInventory().getTitle().equals("Escalação")) { 
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			Clan clan = ClanManager.getInstance().get(account.getClanName());
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ") || event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) { 
				int pageNow = 0;
				if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§a")) { 
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				} else {
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				}
				ClanInventory.getInstance().create(player, TypeInventoryClan.SCALATION, account, clan, pageNow);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVoltar")) { 
				ClanInventory.getInstance().create(player, TypeInventoryClan.INFO, account, clan, 1);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cResetar escalação")) { 
				if(account.hasGroupClan(ClanGroup.LEADDER)) { 
					List<String> climbed = clan.getClimbed();
					if(climbed.size() > 0) { 
						clan.setClimbed(new ArrayList<String>());
					} else { 
						sendMessage(player, false, "§cA escalação não está formada!");
					}
					ClanInventory.getInstance().create(player, TypeInventoryClan.SCALATION, account, clan, 1);
				} else { 
					player.closeInventory();
					sendMessage(player, false, "§cVocê precisa ser líder do clã para formar a escalação!");
				}
			} else if(event.getCurrentItem().getType() == Material.SKULL_ITEM && event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§e")) { 
				if(account.hasGroupClan(ClanGroup.LEADDER)) { 
					Account accountTarget = AccountManager.getInstance().get(event.getCurrentItem().getItemMeta().getDisplayName().replace("§e", ""));
					if(accountTarget == null) return;
					List<String> climbed = clan.getClimbed();
					if(climbed.size() <= 5) { 
						if(!climbed.contains(accountTarget.getNickName())) { 
							climbed.add(accountTarget.getNickName());
							clan.setClimbed(climbed);
						} else { 
							sendMessage(player, false, "§cEste jogador já está escalado!");
						}
					} else { 
						sendMessage(player, false, "§cA escalação já está formada!");
					}
					ClanInventory.getInstance().create(player, TypeInventoryClan.SCALATION, account, clan, 1);
				} else { 
					player.closeInventory();
					sendMessage(player, false, "§cVocê precisa ser líder do clã para formar a escalação!");
				}
			}
			return;
		} else if(event.getInventory().getTitle().equals("Sessões")) { 
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ") || event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) { 
				int pageNow = 0;
				if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§a")) { 
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				} else {
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				}
				ShopInventory.getInstance().create(player, account, TypeInventoryShop.SESSIONS, null, null, pageNow, 1);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aCarrinho de compras")) { 
				ShoppingCart shoppingCart = ShoppingCartManager.getInstance().get(account.getUniqueId());
				if(shoppingCart == null) { 
					ShoppingCartManager.getInstance().add(new ShoppingCart(player.getUniqueId(), new ArrayList<SubItem>()));
				}
				ShopInventory.getInstance().create(player, account, TypeInventoryShop.SHOPPING_CART, null, null, 1, 1);
			} else { 
				Session session = SessionManager.getInstance().get(event.getCurrentItem().getItemMeta().getDisplayName().replace("§a", ""));
				if(session == null) return;
				if(account.getPreferences().isAdminMode()) { 
					if(event.isLeftClick()) { 
						if(session.isPromo()) { 
							sendMessage(player, false, "§cDesconto removido!");
							session.setPromo(false);
							session.setDescount(0.0D);
						} else { 
							sendMessage(player, false, "§aDigite um valor para desconto a essa sessão:");
							AccountManager.getAdminShop().put(player.getUniqueId(), session.getName(), Action.SETTING_DESCOUNT);
						}
						player.closeInventory();
					} else { 
						ShopInventory.getInstance().create(player, account, TypeInventoryShop.EXACT_SESSION, session, null, 1, 1);
					}
					return;
				}
				ShopInventory.getInstance().create(player, account, TypeInventoryShop.EXACT_SESSION, session, null, 1, 1);
			}
			return;
		} else if(event.getInventory().getTitle().startsWith("Sessão ")) { 
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			Session session = SessionManager.getInstance().get(event.getInventory().getTitle().replace("Sessão ", ""));
			if(session == null) return;
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ") || event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) { 
				int pageNow = 0;
				if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§a")) { 
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				} else {
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				}
				ShopInventory.getInstance().create(player, account, TypeInventoryShop.EXACT_SESSION, session, null, pageNow, 1);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVoltar")) { 
				ShopInventory.getInstance().create(player, account, TypeInventoryShop.SESSIONS, null, null, 1, 1);
			} else { 
				Item item = ItemManager.getInstance().get(event.getCurrentItem().getType(), event.getCurrentItem().getDurability());
				if(item == null) return;
				if(account.getPreferences().isAdminMode()) { 
					if(event.isRightClick()) { 
						sendMessage(player, false, "§aDigite um valor para o item:");
						AccountManager.getAdminShop().put(player.getUniqueId(), item.getUniqueId(), Action.SETTING_PRICE);
					} else { 
						if(item.isPromo()) { 
							sendMessage(player, false, "§cDesconto removido!");
							item.setPromo(false);
							item.setDescount(0.0D);
						} else { 
							sendMessage(player, false, "§aDigite um valor para desconto ao item:");
							AccountManager.getAdminShop().put(player.getUniqueId(), item.getUniqueId(), Action.SETTING_DESCOUNT);
						}
					}
					player.closeInventory();
					return;
				}
				ShopInventory.getInstance().create(player, account, TypeInventoryShop.CONFIRM, null, item, 1, 1);
			}
			return;
		} else if(event.getInventory().getTitle().startsWith("Comprando ID#")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			Item item = ItemManager.getInstance().get(event.getInventory().getTitle().replace("Comprando ID#", ""));
			if(item == null) return;
			int amount = event.getInventory().getItem(13).getAmount();
			if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§c-")) { 
				if(event.getInventory().getItem(13).getAmount() > 1) { 
					ShopInventory.getInstance().create(player, account, TypeInventoryShop.CONFIRM, null, item, 1, amount - 1);
				} else { 
					ActionBar.getInstance().sendActionBarMessage(player, "§cQuantidade mínima já alcançada!");
				}
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§a+")) { 
				if(event.getInventory().getItem(13).getAmount() < 65) { 
					ShopInventory.getInstance().create(player, account, TypeInventoryShop.CONFIRM, null, item, 1, amount + 1);
				} else { 
					ActionBar.getInstance().sendActionBarMessage(player, "§cQuantidade máxima já alcançada!");
				}
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aAdicionar ao carrinho")) { 
				ShoppingCart shoppingCart = ShoppingCartManager.getInstance().get(account.getUniqueId());
				if(shoppingCart == null) { 
					ShoppingCartManager.getInstance().add(new ShoppingCart(player.getUniqueId(), new ArrayList<SubItem>()));
					shoppingCart = ShoppingCartManager.getInstance().get(account.getUniqueId());
				}
				double descount = ((item.getDescount() + item.getSession().getDescount()) * item.getPrice()) / 100;
				shoppingCart.getCart().add(new SubItem(item.getUniqueId(), amount, item.isPromo() || item.getSession().isPromo() ? (item.getPrice() - descount) * amount : item.getPrice() * amount));
				ActionBar.getInstance().sendActionBarMessage(player, "§aItem adicionado ao carrinho de compras!");
				ShopInventory.getInstance().create(player, account, TypeInventoryShop.EXACT_SESSION, item.getSession(), null, 1, 1);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cCancelar")) { 
				player.closeInventory();
				ActionBar.getInstance().sendActionBarMessage(player, "§cCompra cancelada!");
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aConfirmar")) {
				double descount = ((item.getDescount() + item.getSession().getDescount()) * item.getPrice()) / 100;
				if(account.hasEconomy(TypeCoin.REAL, item.isPromo() || item.getSession().isPromo() ? (item.getPrice() - descount) * amount : item.getPrice() * amount)) { 
					if(account.slotsFree() >= 1) { 
						account.remove(TypeCoin.REAL, item.isPromo() || item.getSession().isPromo() ? (item.getPrice() - descount) * amount : item.getPrice() * amount);
						player.getInventory().addItem(new ItemBuilder().chanceItemStack(item.getItemStack()).setAmount(amount).getStack());
						sendMessage(player, false, "§aVocê comprou §fx" + amount + " §ade §7" + ItemName.valueOf(item.getItemStack().getType(), item.getItemStack().getDurability()).getName() + " §apor §f" + formatMoney(item.isPromo() ? (item.getPrice() - descount) * amount : item.getPrice() * amount) + "§a!");
					} else { 
						sendMessage(player, false, "§cVocê não tem espaço no inventário para comprar este item!");
					}
				} else { 
					sendMessage(player, false, "§cVocê não tem dinheiro suficiente para comprar este item!");
				}
				player.closeInventory();
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aCompra em grande escala")) { 
				ShopInventory.getInstance().create(player, account, TypeInventoryShop.CONFIRM_LARGE_SCALE, null, item, 1, 1);
			}
			return;
		} else if(event.getInventory().getTitle().startsWith("Comprando mais de ID#")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			Item item = ItemManager.getInstance().get(event.getInventory().getTitle().replace("Comprando mais de ID#", ""));
			if(item == null) return;
			if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVoltar")) { 
				ShopInventory.getInstance().create(player, account, TypeInventoryShop.CONFIRM, null, item, 1, 1);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cCancelar")) { 
				player.closeInventory();
				ActionBar.getInstance().sendActionBarMessage(player, "§cCompra cancelada!");
			} else { 
				int amount = Integer.valueOf(event.getCurrentItem().getItemMeta().getLore().get(1).replace("§fQuantidade §7", ""));
				double descount = ((item.getDescount() + item.getSession().getDescount()) * item.getPrice()) / 100;
				if(account.hasEconomy(TypeCoin.REAL, item.isPromo() || item.getSession().isPromo() ? ((item.getPrice() - descount) * 64) * amount : (item.getPrice() * 64) * amount)) { 
					if(account.slotsFree() >= amount) {
						account.remove(TypeCoin.REAL, item.isPromo() || item.getSession().isPromo() ? ((item.getPrice() - descount) * 64) * amount : (item.getPrice() * 64) * amount);
						for(int i = 0; i <= amount; i++) { 
							player.getInventory().addItem(new ItemBuilder().chanceItemStack(item.getItemStack()).setAmount(64).getStack());
						}
						sendMessage(player, false, "§aVocê comprou §fx" + formatMoney(amount * 64).replace("R$", "") + " §ade §7" + ItemName.valueOf(item.getItemStack().getType(), item.getItemStack().getDurability()).getName() + " §apor §f" + formatMoney(item.isPromo() || item.getSession().isPromo() ? ((item.getPrice() - descount) * 64) * amount : (item.getPrice() * 64) * amount) + "§a!");
					} else { 
						sendMessage(player, false, "§cVocê não tem espaço no inventário para comprar este item!");
					}
				} else { 
					sendMessage(player, false, "§cVocê não tem dinheiro suficiente para comprar este item!");
				}
				player.closeInventory();
			}
			return;
		} else if(event.getInventory().getTitle().startsWith("Carrinho de compras")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			ShoppingCart shoppingCart = ShoppingCartManager.getInstance().get(player.getUniqueId());
			if(shoppingCart == null) return;
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ") || event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) { 
				int pageNow = 0;
				if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§a")) { 
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				} else {
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				}
				ShopInventory.getInstance().create(player, account, TypeInventoryShop.SHOPPING_CART, null, null, pageNow, 1);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aFinalizar compra")) {
				if(shoppingCart.isEmpty()) { 
					if(account.hasEconomy(TypeCoin.REAL, shoppingCart.totalPrice())) { 
						if(account.slotsFree() >= shoppingCart.getCart().size()) {
							account.remove(TypeCoin.REAL, shoppingCart.totalPrice());
							for(SubItem s : shoppingCart.getCart()) { 
								player.getInventory().addItem(new ItemBuilder().chanceItemStack(s.get().getItemStack()).setAmount(s.getAmount()).getStack());
							}
							sendMessage(player, false, "§aVocê comprou todos os itens adicionados ao seu carrinho de compras por §f" + formatMoney(shoppingCart.totalPrice()) + "§a!");
							shoppingCart.setCart(new ArrayList<SubItem>());
						} else { 
							sendMessage(player, false, "§cVocê não tem espaço no inventário para comprar este item!");
						}
					} else { 
						sendMessage(player, false, "§cVocê não tem dinheiro suficiente para comprar este item!");
					}
					player.closeInventory();
				} else { 
					sendMessage(player, false, "§cVocê não tem nada no carrinho de compras!");
				}
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cEsvaziar carrinho")) { 
				if(!shoppingCart.isEmpty()) { 
					sendMessage(player, false, "§cVocê esvaziou seu carrinho de compras!");
					shoppingCart.setCart(new ArrayList<SubItem>());
					ShopInventory.getInstance().create(player, account, TypeInventoryShop.SHOPPING_CART, null, null, 1, 1);
				} else { 
					sendMessage(player, false, "§cVocê não tem nada no carrinho de compras!");
				}
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVoltar")) { 
				ShopInventory.getInstance().create(player, account, TypeInventoryShop.SESSIONS, null, null, 1, 1);
			} else { 
				Item item = ItemManager.getInstance().get(event.getCurrentItem().getType(), event.getCurrentItem().getDurability());
				if(item == null) return;
				shoppingCart.remove(item);
				ActionBar.getInstance().sendActionBarMessage(player, "§cItem removido do carrinho de compras!");
				ShopInventory.getInstance().create(player, account, TypeInventoryShop.SHOPPING_CART, null, null, 1, 1);
			}
			return;
		} else if(event.getInventory().getTitle().equalsIgnoreCase("Kit")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			account = AccountManager.getInstance().get(player.getName());
			Delay delay = event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aKit Diário") ? Delay.DIARY : event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aKit Semanal") ? Delay.WEEK : event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aKit Mensal") ? Delay.MONTH : Delay.CUSTOM;
			KitInventory.getInstance().create(player, TypeInventoryKit.KIT_SAME_TIME, delay, account, null, 1);
			return;
		} else if(event.getInventory().getTitle().startsWith("Kits ")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			Delay delay = event.getInventory().getTitle().contains("Diário") ? Delay.DIARY : event.getInventory().getTitle().contains("Semanal") ? Delay.WEEK : event.getInventory().getTitle().contains("Mensal") ? Delay.MONTH : Delay.CUSTOM;
			if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§aPágina ") || event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cPágina ")) { 
				int pageNow = 0;
				if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§a")) { 
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aPágina ", ""));
				} else {
					pageNow = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace("§cPágina ", ""));
				}
				KitInventory.getInstance().create(player, TypeInventoryKit.KIT_SAME_TIME, delay, account, null, pageNow);
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVoltar")) { 
				KitInventory.getInstance().create(player, TypeInventoryKit.KITS, null, account, null, 1);
			} else { 
				Kit kit = KitManager.getInstance().get(event.getCurrentItem().getItemMeta().getDisplayName().replace("§aKit ", ""));
				if(event.isLeftClick()) { 
					PlayerKits playerKit = PlayerKitManager.getInstance().get(player.getUniqueId());
					if(playerKit == null) { 
						playerKit = new PlayerKits(player.getUniqueId(), new ArrayList<KitCollected>());
						PlayerKitManager.getInstance().add(playerKit);
						playerKit = PlayerKitManager.getInstance().get(player.getUniqueId());
					}
					if(playerKit.allowGive(kit)) { 
						kit.give(player);
						playerKit.add(kit);
						sendMessage(player, false, "§aKit §7" + kit.getName() + " §arecolhido!");
					} else { 
						sendMessage(player, false, "§cVocê deve aguardar §f" + playerKit.getTimeWait(kit) + " §cpara recolher este kit!");
					}
					player.closeInventory();
				} else { 
					if(kit == null) return;
					KitInventory.getInstance().create(player, TypeInventoryKit.VIEW_KIT, null, account, kit, 1);
				}
			}
			return;
		} else if(event.getInventory().getTitle().startsWith("Visualizando kit ")) {
			if(event.getCurrentItem() == null) return;
			if(!event.getCurrentItem().hasItemMeta()) return;
			if(event.getInventory() == null) return;
			event.setCancelled(true);
			Kit kit = KitManager.getInstance().get(event.getInventory().getTitle().replace("Visualizando kit ", ""));
			if(kit == null) return;
			if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVoltar")) { 
				KitInventory.getInstance().create(player, TypeInventoryKit.KIT_SAME_TIME, kit.getDelay(), account, null, 1);
			}
			return;
		}
	}
}
