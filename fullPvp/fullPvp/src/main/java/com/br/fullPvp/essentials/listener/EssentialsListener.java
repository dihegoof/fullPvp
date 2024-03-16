package com.br.fullPvp.essentials.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;

import com.br.fullPvp.Main;
import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.essentials.preferences.ServerPreferencesManager;
import com.br.fullPvp.groups.Group;
import com.br.fullPvp.groups.GroupManager;
import com.br.fullPvp.links.Link;
import com.br.fullPvp.links.LinkManager;
import com.br.fullPvp.ranks.Rank;
import com.br.fullPvp.ranks.RankManager;
import com.br.fullPvp.tags.Tag;
import com.br.fullPvp.tags.TagManager;
import com.br.fullPvp.utils.Chat;
import com.br.fullPvp.utils.Utils;

public class EssentialsListener extends Utils implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) { 
		Player player = event.getPlayer();
		for(Player p : Bukkit.getOnlinePlayers()) { 
			if(AccountManager.getInstance().get(p.getName()).getPreferences().isAdminMode()) { 
				sendMessage(p.getPlayer(), false, "§dVocê está do modo admin!");
				player.hidePlayer(p);
				p.getPlayer().setAllowFlight(true);
				p.getPlayer().setFlying(true);
			}
		}
	}
	
	@EventHandler
	public void onServerListPing(ServerListPingEvent event) {
		Link link = LinkManager.getLink();
		if(link != null) { 
			String[] split = link.getMotd().split("/");
			Main.debug("Motd > L1: " + split[0] + ", L2: " + split[1]);
			event.setMotd(Chat.makeCenteredMotd(split[0]) + "\n" + (ServerPreferencesManager.getServerPreference().isManutence() ? Chat.makeCenteredMotd("§cEstamos em §lMANUTENÇÃO§c!") : Chat.makeCenteredMotd(split[1])));
			event.setMaxPlayers(Bukkit.getOnlinePlayers().size() + 1);
		}
	}
	
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) { 
		Player player = event.getPlayer();
		Account account = AccountManager.getInstance().get(player.getName());
		if(account == null) return;
		if(event.isCancelled()) return;
		event.setCancelled(true);
		int distance = 30;
		if(!ServerPreferencesManager.getServerPreference().isChatLocal()) { 
			if(!account.hasPermission(Permissions.SAY_CHAT_OFF.getPermission())) { 
				sendMessage(player, false, "§cO chat local está desativado!");
				return;
			}
		}
		List<Player> list = new ArrayList<>();
		for(Entity e : player.getNearbyEntities(distance, distance, distance)) { 
			if(e instanceof Player) { 
				Player targets = (Player) e;
				if(targets.getName().equals(player.getName())) continue;
				if(!list.contains(targets)) { 
					list.add(targets);
				}
			}
		}
		String message = event.getMessage().replace("&", (account.hasPermission(Permissions.CHAT_COLOR.getPermission()) ? "§" : ""));
		String displayName = "";
		Group group = GroupManager.getInstance().get(account.getGroupName());
		Rank rank = RankManager.getInstance().get(account.getRankName());
		Tag tag = TagManager.getInstance().get(account.getTagUsing());
		if(rank != null) { 
			displayName = "§7[" + rank.getPrefix().replace("&", "§") + "§7] ";
		}
		if(tag == null) { 
			displayName += group.getPrefix().replace("&", "§");
		} else { 
			displayName += tag.getPrefix().replace("&", "§");
		}
		displayName += " " + player.getName();
		sendMessage(player, false, "§7[§eL§7] " + displayName + "§f:§7 " + message);
		if(list.size() == 0) { 
			sendMessage(player, false, "§cNenhum jogador por perto para conversar!");
			return;
		}
		for(Player players : list) { 
			sendMessage(players, false, "§7[§eL§7] " + displayName + "§f:§7 " + message);
		}
		list.clear();
	}
}
