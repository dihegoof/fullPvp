package com.br.fullPvp.inventorys;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.utils.ItemBuilder;
import com.br.fullPvp.utils.TimeManager;
import com.br.fullPvp.utils.Utils;

import lombok.Getter;

public class AccountInventory extends Utils {
	
	public enum TypeInventoryAccount { 
		
		ACCOUNT, STATUS, HISTORIC_PUNISH, PREFERENCES;
		
	}
	
	@Getter
	static AccountInventory instance = new AccountInventory();
	
	public void create(Player player, Account account, TypeInventoryAccount typeInventory, int page) { 
		Inventory inventory = null;
		ItemBuilder ib = null;
		if(typeInventory.equals(TypeInventoryAccount.ACCOUNT)) { 
			inventory = Bukkit.createInventory(player, 27, account.getNickName().equals(player.getName()) ? "Sua conta" : "Conta de " + account.getNickName());
			ib = new ItemBuilder(Material.SKULL_ITEM).setDurability(3).setSkull(account.getNickName()).setName("§aInformações:").setDescription(
					"§fConta criada §7" + formatData(account.getFirstLogin()),
					"§fRank §7" + account.getRankName(),
					"§fGrupo §7" + account.getGroupName(), 
					"" + (account.getTimeGroup() != -1 ? "   §8Acaba em " + TimeManager.getInstance().getTime(account.getTimeGroup()) : "   §8Grupo vitalício."),
					"§fClã §7" + account.getClanName(),
					"",
					(account.isOnline() ? "§aNo momento encontra-se online!" : "§cVisto por último há " + compareTime(account.getLastSee())));
			ib.build(inventory, 12);
			
			ib = new ItemBuilder(Material.PAPER).setName("§aStatus").setDescription("§7Saiba mais sobre seus status de combate.");
			ib.build(inventory, 13);
			
			ib = new ItemBuilder(Material.TNT).setName("§aHistórico de punições").setDescription("§7Abra o histórico de punições desta conta.");
			ib.build(inventory, 14);
			
			if(account.getNickName().equals(player.getName())) { 
				ib = new ItemBuilder(Material.LEVER).setName("§aPreferências").setDescription("§7Gerencie as prefêrencias de sua conta.");
				ib.build(inventory, 16);
			}
		} else if(typeInventory.equals(TypeInventoryAccount.STATUS)) { 
			inventory = Bukkit.createInventory(player, 27, account.getNickName().equals(player.getName()) ? "Seu status" : "Status de " + account.getNickName());
			ib = new ItemBuilder(Material.DIAMOND_SWORD).setName("§aStatus").setDescription(
					"§fMatou §7" + formatMoney(account.getStatus().getKills()).replace("R$", ""), 
					"§fMorreu §7" + formatMoney(account.getStatus().getDeaths()).replace("R$", ""), 
					"§fKDR §7" + formatMoney(account.getStatus().getKdr()).replace("R$", ""));
			ib.build(inventory, 13);
			
			ib = new ItemBuilder(Material.ARROW).setName("§cVoltar").setDescription("§7Clique aqui para voltar ao menu!");
			ib.build(inventory, 18);
		} else if(typeInventory.equals(TypeInventoryAccount.HISTORIC_PUNISH)) { 
			
		} else { 
			inventory = Bukkit.createInventory(player, 54, (player.getName().equals(account.getNickName()) ? "Preferências" : "Preferências de " + account.getNickName()));
			
			ib = new ItemBuilder(Material.BOOK_AND_QUILL).setName("§aMensagens privadas").setDescription("§7Mensagens diretas, sussurros.");
			ib.build(inventory, 10);
			
			ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setName((account.getPreferences().isReceiveTell() ? "§cDesativar" : "§aAtivar")).setDurability((account.getPreferences().isReceiveTell() ? 14 : 5));
			ib.build(inventory, 19);
			
			ib = new ItemBuilder(Material.GOLD_INGOT).setName("§aDoações").setDescription("§7Doação de coins, cash.");
			ib.build(inventory, 11);
			
			ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setName((account.getPreferences().isReceiveDonate() ? "§cDesativar" : "§aAtivar")).setDurability((account.getPreferences().isReceiveDonate() ? 14 : 5));
			ib.build(inventory, 20);
			
			ib = new ItemBuilder(Material.PAPER).setName("§aScoreboard").setDescription("§7Sidebar, scoreboard.", "§8Clique aqui para alterar para a scoreboard do modo status.");
			ib.build(inventory, 12);
			
			ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setName((account.getPreferences().isScoreBoard() ? "§cDesativar" : "§aAtivar")).setDurability((account.getPreferences().isScoreBoard() ? 14 : 5));
			ib.build(inventory, 21);
			
			ib = new ItemBuilder(Material.FEATHER).setName("§aConvite de clan").setDescription("§7Recebimento de convite para clan.");
			ib.build(inventory, 13);
			
			ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setName((account.getPreferences().isRequestsClan() ? "§cDesativar" : "§aAtivar")).setDurability((account.getPreferences().isRequestsClan() ? 14 : 5));
			ib.build(inventory, 22);
			
			ib = new ItemBuilder(Material.ENDER_PEARL).setName("§aConvite de Tpa").setDescription("§7Recebimento de convite para teleporte.");
			ib.build(inventory, 14);
			
			ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setName((account.getPreferences().isRequestsTpa() ? "§cDesativar" : "§aAtivar")).setDurability((account.getPreferences().isRequestsTpa() ? 14 : 5));
			ib.build(inventory, 23);
			
			if(account.hasPermission("chat.staff")) { 
				ib = new ItemBuilder(Material.BOOK).setName("§aChat da equipe").setDescription("§7Mensagens enviadas no chat privado da equipe.");
				ib.build(inventory, 15);
				
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setName((account.getPreferences().isStaffChat() ? "§cDesativar" : "§aAtivar")).setDurability((account.getPreferences().isStaffChat() ? 14 : 5));
				ib.build(inventory, 24);
			}
			if(account.hasPermission(Permissions.ADMIN_MODE.getPermission())) { 
				ib = new ItemBuilder(Material.REDSTONE).setName("§aModo administrador").setDescription("§7Entrar no servidor no modo admin.");
				ib.build(inventory, 16);
				
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setName((account.getPreferences().isAdminMode() ? "§cDesativar" : "§aAtivar")).setDurability((account.getPreferences().isAdminMode() ? 14 : 5));
				ib.build(inventory, 25);
			}
			if(account.hasPermission(Permissions.REPORTS_SEE.getPermission())) { 
				ib = new ItemBuilder(Material.ANVIL).setName("§aDenúncias").setDescription("§7Aviso de denúncias.");
				ib.build(inventory, 28);
				
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setName((account.getPreferences().isReceiveReports() ? "§cDesativar" : "§aAtivar")).setDurability((account.getPreferences().isReceiveReports() ? 14 : 5));
				ib.build(inventory, 37);
			}
			if(account.hasPermission(Permissions.WARNS_SEE.getPermission())) { 
				ib = new ItemBuilder(Material.SHEARS).setName("§aAvisos").setDescription("§7Avisos de bugs, denúncias restantes, tentativas de logins.");
				ib.build(inventory, 29);
				
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setName((account.getPreferences().isReceiveWarns() ? "§cDesativar" : "§aAtivar")).setDurability((account.getPreferences().isReceiveWarns() ? 14 : 5));
				ib.build(inventory, 38);
			}
			
			ib = new ItemBuilder(Material.ARROW).setName("§cVoltar").setDescription("§7Clique aqui para voltar ao menu!");
			ib.build(inventory, 45);
		}
		player.openInventory(inventory);
	}
}
