package com.br.fullPvp.groups.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.utils.Utils;

public class GroupListener extends Utils implements Listener {
	
	@EventHandler
	public void onServerTimerEvent(ServerTimerEvent event) {
		for(Account a : AccountManager.getStorageAccounts()) { 
			if(!a.isGroupPermanent() && a.getTimeGroup() < System.currentTimeMillis()) { 
				a.sendMessage(true, "§cSeu grupo temporário expirou!");
				a.setGroupName(a.getLastGroupName());
				a.setLastGroupName("NRE");
				a.setTimeGroup(-1L);
			}
		}
	}
}
