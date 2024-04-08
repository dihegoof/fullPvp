package com.br.fullPvp.mines.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.br.fullPvp.groups.listener.ServerTimerEvent;
import com.br.fullPvp.mines.Mine;
import com.br.fullPvp.mines.MineManager;
import com.br.fullPvp.mines.Position;
import com.br.fullPvp.mines.PositionManager;
import com.br.fullPvp.utils.Utils;

public class MineListener extends Utils implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) { 
		Player player = event.getPlayer();
		Position position = PositionManager.getInstance().getPosition(player.getUniqueId());
		if(position == null) return;
		if(player.getItemInHand().getType() == Material.WOOD_AXE) { 
			if(event.getClickedBlock() != null) { 
				if(event.getAction().toString().contains("LEFT")) { 
					position.setPos1(event.getClickedBlock().getLocation());
				} else if(event.getAction().toString().contains("RIGHT")) { 
					position.setPos2(event.getClickedBlock().getLocation());
				}
				sendMessage(player, false, "§aVocê demarcou uma localização para criação de uma mina!");
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onServerTimerEvent(ServerTimerEvent event) {
		for(Mine m : MineManager.getStorageMines()) { 
			if(m.isEnable()) { 
				if(System.currentTimeMillis() > m.getTime()) {
					m.reset();
				}
			}
		}
	}
}
