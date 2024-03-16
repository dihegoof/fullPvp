package com.br.fullPvp.warps.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.utils.Utils;
import com.br.fullPvp.warps.WarpManager;

public class WarpListener extends Utils implements Listener {
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(AccountManager.getLastWarp().containsKey(event.getPlayer().getUniqueId())) { 
			Account account = AccountManager.getInstance().get(event.getPlayer().getName());
			if(account == null) return;
			account.setWarp(WarpManager.getInstance().get(AccountManager.getLastWarp().get(event.getPlayer().getUniqueId())));
			account.updateFly();
			sendMessage(event.getPlayer(), false, "§cSeu teleporte foi cancelado por movimento!");
			AccountManager.getLastWarp().remove(event.getPlayer().getUniqueId());
		}
	}
}
