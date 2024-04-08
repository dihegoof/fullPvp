package com.br.fullPvp.accounts.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.br.fullPvp.Main;
import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.groups.listener.ServerTimerEvent;
import com.br.fullPvp.links.LinkManager;
import com.br.fullPvp.utils.TagUpdate;
import com.br.fullPvp.utils.Title;

public class AccountListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) { 
		TagUpdate.getInstance().sendTeams(event.getPlayer());
		event.setJoinMessage(null);
		for(int i = 0; i < 100; i++) { 
			event.getPlayer().sendMessage(" ");
		}
		Account account = AccountManager.getInstance().get(event.getPlayer().getName());
		if(account == null) { 
			account = new Account(event.getPlayer().getUniqueId(), event.getPlayer().getName(), event.getPlayer());
			AccountManager.getInstance().add(account);
			Main.debug("Conta de " + event.getPlayer().getName() + " criada!");
		}
		if(!account.getAddress().equals(event.getPlayer().getAddress().getHostString())) { 
			account.setLastAddress(account.getAddress());
			account.setAddress(event.getPlayer().getAddress().getHostString());
		}
		account.setPlayer(event.getPlayer());
		account.setOnline(true);
		account.updatePrefix();
		account.teleportSpawn();
		account.updateFly();
		account.sendMessage(true, "§aSeja bem-vindo ao servidor!", "§aBoa sorte e bom-jogo!");
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		TagUpdate.getInstance().reset(event.getPlayer().getName());
		event.setQuitMessage(null);
		Account account = AccountManager.getInstance().get(event.getPlayer().getName());
		if(account == null) return; 
		account.setPlayer(null);
		account.setLastSee(System.currentTimeMillis());
		account.setOnline(false);
		account.setWarp(null);
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		TagUpdate.getInstance().reset(event.getPlayer().getName());
		Account account = AccountManager.getInstance().get(event.getPlayer().getName());
		if(account == null) return; 
		account.setPlayer(null);
		account.setLastSee(System.currentTimeMillis());
		account.setOnline(false);
		account.setWarp(null);
	}
	
	@EventHandler
	public void onServerTimerEvent(ServerTimerEvent event) {
		for(Account a : AccountManager.getStorageAccounts()) { 
			if(a.isOnline()) { 
				Title.getInstance().sendTabTitle(a.getPlayer(), 
						"\n" +
						LinkManager.getLink().getPrefix() + 
						"\n\n      §eSeja bem-vindo ao servidor!\n", 
						"\n§aSite §7" + LinkManager.getLink().getWebsite() + 
						"\n§aDiscord §7" + LinkManager.getLink().getDiscord() +
						"\n§aLoja §7" + LinkManager.getLink().getShop());
			}
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) { 
		if(event.getEntity() instanceof Player) { 
			Account account = AccountManager.getInstance().get(event.getEntity().getName());
			if(account == null) return; 
			event.setCancelled(account.isInvencible() ? true : false);
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) { 
		if(event.getEntity() instanceof Player) { 
			Account account = AccountManager.getInstance().get(event.getEntity().getName());
			if(account == null) return; 
			event.setCancelled(account.isInvencible() ? true : false);
		}
	}
}
