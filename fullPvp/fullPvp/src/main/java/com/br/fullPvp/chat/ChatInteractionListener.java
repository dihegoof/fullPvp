package com.br.fullPvp.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.AccountManager.Action;
import com.br.fullPvp.shop.item.Item;
import com.br.fullPvp.shop.item.ItemManager;
import com.br.fullPvp.shop.session.Session;
import com.br.fullPvp.shop.session.SessionManager;
import com.br.fullPvp.utils.ItemName;
import com.br.fullPvp.utils.Utils;

public class ChatInteractionListener extends Utils implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) { 
		Account account = AccountManager.getInstance().get(event.getPlayer().getName());
		if(account == null) return;
		if(AccountManager.getAdminShop().containsKey(account.getUniqueId())) {
			if(!event.getMessage().contains("cancel")) { 
				Item item = ItemManager.getInstance().get(AccountManager.getAdminShop().getValue(account.getUniqueId()));
				if(item != null) { 
					if(isInteger(event.getMessage())) { 
						if(AccountManager.getAdminShop().getSubValue(account.getUniqueId()).equals(Action.SETTING_PRICE)) {
							item.setPrice(Double.valueOf(event.getMessage()));
							sendMessage(event.getPlayer(), false, "§aValor §f" + formatMoney(Double.valueOf(event.getMessage())) + " §adefinido para §7" + ItemName.valueOf(item.getItemStack().getType(), item.getItemStack().getDurability()).getName() + "§a!");
						} else { 
							item.setPromo(true);
							item.setDescount(Double.valueOf(event.getMessage()));
							sendMessage(event.getPlayer(), false, "§aDesconto de §f" + formatPercent(Double.valueOf(event.getMessage())) + " §adefinido para §7" + ItemName.valueOf(item.getItemStack().getType(), item.getItemStack().getDurability()).getName() + "§a!");
						}
					} else { 
						sendMessage(event.getPlayer(), false, "§cValor inválido!");
					}
				} else { 
					Session session = SessionManager.getInstance().get(AccountManager.getAdminShop().getValue(account.getUniqueId()));
					if(session != null) { 
						if(isInteger(event.getMessage())) { 
							if(AccountManager.getAdminShop().getSubValue(account.getUniqueId()).equals(Action.SETTING_DESCOUNT)) {
								session.setPromo(true);
								session.setDescount(Double.valueOf(event.getMessage()));
								sendMessage(event.getPlayer(), false, "§aDesconto de §f" + formatPercent(Double.valueOf(event.getMessage())) + " §adefinido para sessão §7" + session.getName() + "§a!");
							}
						} else { 
							sendMessage(event.getPlayer(), false, "§cValor inválido!");
						}
					} else { 
						sendMessage(event.getPlayer(), false, "§cOcorreu um erro ao definir valores e descontos, ação cancelada!");
						AccountManager.getAdminShop().clear();
						event.setCancelled(true);
					}
				}
			} else { 
				sendMessage(event.getPlayer(), false, "§cInserção de valores cancelada!");
			}
			AccountManager.getAdminShop().clear();
			event.setCancelled(true);
			return;
		}
	}
}
