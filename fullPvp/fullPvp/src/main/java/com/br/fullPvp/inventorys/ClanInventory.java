package com.br.fullPvp.inventorys;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.clans.Clan;
import com.br.fullPvp.clans.ClanManager;
import com.br.fullPvp.clans.ClanMember;
import com.br.fullPvp.utils.HeadLink;
import com.br.fullPvp.utils.ItemBuilder;
import com.br.fullPvp.utils.Utils;

import lombok.Getter;

public class ClanInventory extends Utils {
	
	public enum TypeInventoryClan { 
		
		INFO, STATUS, ALLIES, ENEMIES, MEMBERS, REQUESTS, SCALATION;
		
	}

	@Getter
	private static List<Integer> empty = new ArrayList<>();
	private static List<Integer> emptyAdd = new ArrayList<>();
	static {
		for (int i = 0; i <= 9; i++)
			empty.add(i);
		for (int i = 45; i <= 53; i++)
			empty.add(i);
		empty.add(18);
		empty.add(27);
		empty.add(36);
		empty.add(17);
		empty.add(26);
		empty.add(35);
		empty.add(44);
	}
	
	@Getter
	static ClanInventory instance = new ClanInventory();
	
	@SuppressWarnings("deprecation")
	public void create(Player player, TypeInventoryClan typeInventory, Account account, Clan clan, int page) { 
		Inventory inventory = null;
		ItemBuilder ib = null;
		if(typeInventory.equals(TypeInventoryClan.INFO)) { 
			inventory = Bukkit.createInventory(player, 27, account.getClanName().equals(clan.getName()) ? "Seu clã" : "Clã " + clan.getName());
			if(account.getClanName().equals(clan.getName())) { 
				ib = new ItemBuilder(Material.CHEST).setName("§aMembros").setDescription("§7Clique aqui para abrir a lista de membros do clan.");
				ib.build(inventory, 10);
				
				ib = new ItemBuilder(Material.BOOK_AND_QUILL).setName("§aInformações").setDescription(
						"§b" + clan.getMotto(),
						"",
						"§fTag §7" + clan.getTag().replace("&", "§"), 
						"§fCriado em §7" + formatData(clan.getCreatedIn()),
						"§fCriado §7" + clan.getLeadder(),
						"§fSaldo §7" + formatMoney(clan.getReal()));
				ib.build(inventory, 11);
				
				ib = new ItemBuilder(Material.PAPER).setName("§aStatus").setDescription("§7Saiba mais sobre o status deste clã!");
				ib.build(inventory, 12);
				
				ib = new ItemBuilder(Material.BOOK).setName("§aConvites").setDescription("§7Clique aqui para abrir a lista de convites enviados.");
				ib.build(inventory, 13);
				
				ib = new ItemBuilder(Material.MINECART).setName("§aEscalação").setDescription("§7Confira a escalação de jogadores para eventos.");
				ib.build(inventory, 14);
				
				ib = new ItemBuilder(Material.getMaterial(351)).setDurability(10).setName("§aAlíados").setDescription("§7Clique aqui para abrir a lista de clans alíados.");
				ib.build(inventory, 15);
				
				ib = new ItemBuilder(Material.getMaterial(351)).setDurability(8).setName("§aÍnimigos").setDescription("§7Clique aqui para abrir a lista de clans ínimigos.");
				ib.build(inventory, 16);
			} else { 
				ib = new ItemBuilder(Material.CHEST).setName("§aMembros").setDescription("§7Clique aqui para abrir a lista de membros do clan.");
				ib.build(inventory, 12);
				
				ib = new ItemBuilder(Material.BOOK_AND_QUILL).setName("§aInformações").setDescription(
						"§b" + clan.getMotto(),
						"",
						"§fTag §7" + clan.getTag().replace("&", "§"), 
						"§fCriado em §7" + formatData(clan.getCreatedIn()),
						"§fCriado §7" + clan.getLeadder(),
						"§fSaldo §7" + formatMoney(clan.getReal()));
				ib.build(inventory, 13);
				
				ib = new ItemBuilder(Material.PAPER).setName("§aStatus").setDescription("§7Saiba mais sobre o status deste clã!");
				ib.build(inventory, 14);
			}
		} else if(typeInventory.equals(TypeInventoryClan.ALLIES)) { 
			inventory = Bukkit.createInventory(player, 54, "Seus alíados");
			List<String> list = clan.getAllies();
			int start = (page > 1 ? (28 * page) - 28 : 0);
			String allie = null;
			if(list.isEmpty()) { 
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhum clã alíado");
				ib.build(inventory, 22);
			}
			for(int x = 0; x < inventory.getSize(); x++) { 
				try {  
					if(list.get(start) == null) continue; 
				} catch (Exception e) { 
					break; 
				}
				if(inventory.getItem(x) != null) continue;
				if(empty.contains(x)) continue;
				allie = list.get(start);
				ib = new ItemBuilder(Material.SKULL_ITEM).setDurability(3).setName("§a" + allie).setDescription(
						"§fMembros §7" + ClanManager.getInstance().get(allie).getAccountMembers().size(), 
						"§fJogadores online §7" + ClanManager.getInstance().get(allie).getMembersOnline()).setSkinUrl(HeadLink.valueOf(allie.substring(0, 1)).getLink());
				ib.build(inventory, x);
				start++;
			}
			if(page > 1) { 
				ib = new ItemBuilder(Material.ARROW).setName("§cPágina " + (page - 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 45);
			} else { 
				ib = new ItemBuilder(Material.ARROW).setName("§cVoltar").setDescription("§7Clique aqui para voltar ao menu!");
				ib.build(inventory, 45);
			}
			if(inventory.getItem(43) != null) { 
				ib = new ItemBuilder(Material.ARROW).setName("§aPágina " + (page + 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 53);
			}
		} else if(typeInventory.equals(TypeInventoryClan.ENEMIES)) { 
			inventory = Bukkit.createInventory(player, 54, "Seus inimigos");
			List<String> list = clan.getEnemies();
			int start = (page > 1 ? (28 * page) - 28 : 0);
			String enemie = null;
			if(list.isEmpty()) { 
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhum clã inimigo");
				ib.build(inventory, 22);
			}
			for(int x = 0; x < inventory.getSize(); x++) { 
				try {  
					if(list.get(start) == null) continue; 
				} catch (Exception e) { 
					break; 
				}
				if(inventory.getItem(x) != null) continue;
				if(empty.contains(x)) continue;
				enemie = list.get(start);
				ib = new ItemBuilder(Material.SKULL_ITEM).setDurability(3).setName("§a" + enemie).setDescription(
						"§fMembros §7" + ClanManager.getInstance().get(enemie).getAccountMembers().size(), 
						"§fJogadores online §7" + ClanManager.getInstance().get(enemie).getMembersOnline()).setSkinUrl(HeadLink.valueOf(enemie.substring(0, 1)).getLink());
				ib.build(inventory, x);
				start++;
			}
			if(page > 1) { 
				ib = new ItemBuilder(Material.ARROW).setName("§cPágina " + (page - 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 45);
			} else { 
				ib = new ItemBuilder(Material.ARROW).setName("§cVoltar").setDescription("§7Clique aqui para voltar ao menu!");
				ib.build(inventory, 45);
			}
			if(inventory.getItem(43) != null) { 
				ib = new ItemBuilder(Material.ARROW).setName("§aPágina " + (page + 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 53);
			}
		} else if(typeInventory.equals(TypeInventoryClan.MEMBERS)) { 
			inventory = Bukkit.createInventory(player, 54, account.getClanName().equals(clan.getName()) ? "Membros do seu clã" : "Membros do clã " + clan.getName());
			List<ClanMember> list = clan.getMembers();
			int start = (page > 1 ? (28 * page) - 28 : 0);
			ClanMember member = null;
			if(list.isEmpty()) { 
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhum membro");
				ib.build(inventory, 22);
			}
			for(int x = 0; x < inventory.getSize(); x++) { 
				try {  
					if(list.get(start) == null) continue; 
				} catch (Exception e) { 
					break; 
				}
				if(inventory.getItem(x) != null) continue;
				if(empty.contains(x)) continue;
				member = list.get(start);
				ib = new ItemBuilder(Material.SKULL_ITEM).setDurability(3).setName("§a" + member.getName()).setDescription("§fCargo §7" + member.getClanGroup().getName(), "§fData de entrada §7" + formatData(member.getJoinIn())).setSkull(member.getName());
				ib.build(inventory, x); 
				start++;
			}
			if(page > 1) { 
				ib = new ItemBuilder(Material.ARROW).setName("§cPágina " + (page - 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 45);
			} else { 
				ib = new ItemBuilder(Material.ARROW).setName("§cVoltar").setDescription("§7Clique aqui para voltar ao menu!");
				ib.build(inventory, 45);
			}
			if(inventory.getItem(43) != null) { 
				ib = new ItemBuilder(Material.ARROW).setName("§aPágina " + (page + 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 53);
			}
		} else if(typeInventory.equals(TypeInventoryClan.STATUS)) { 
			inventory = Bukkit.createInventory(player, 27, account.getClanName().equals(clan.getName()) ? "Status do seu clã" : "Status do clã " + clan.getName());
			ib = new ItemBuilder(Material.DIAMOND_SWORD).setName("§aStatus").setDescription("", 
					"§fMatou §7" + formatMoney(clan.getStatus().getKills()).replace("R$", ""), 
					"§fMorreu §7" + formatMoney(clan.getStatus().getDeaths()).replace("R$", ""), 
					"§fKillStreak §7" + formatMoney(clan.getStatus().getKillStreak()).replace("R$", ""), 
					"§fKDR §7" + clan.getStatus().getKdr());
			ib.build(inventory, 13);
			
			ib = new ItemBuilder(Material.ARROW).setName("§cVoltar").setDescription("§7Clique aqui para voltar ao menu!");
			ib.build(inventory, 18);
		} else if(typeInventory.equals(TypeInventoryClan.REQUESTS)) { 
			inventory = Bukkit.createInventory(player, 54, "Convites do seu clã");
			List<String> list = clan.getInvites();
			int start = (page > 1 ? (28 * page) - 28 : 0);
			String target = null;
			if(list.isEmpty()) { 
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhum convite enviado");
				ib.build(inventory, 22);
			}
			for(int x = 0; x < inventory.getSize(); x++) { 
				try {  
					if(list.get(start) == null) continue; 
				} catch (Exception e) { 
					break; 
				}
				if(inventory.getItem(x) != null) continue;
				if(empty.contains(x)) continue;
				target = list.get(start);
				ib = new ItemBuilder(Material.SKULL_ITEM).setDurability(3).setName("§a" + target).setDescription("§7Clique com botão direito para cancelar!").setSkull(target);
				ib.build(inventory, x); 
				start++;
			}
			if(page > 1) { 
				ib = new ItemBuilder(Material.ARROW).setName("§cPágina " + (page - 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 45);
			} else { 
				ib = new ItemBuilder(Material.ARROW).setName("§cVoltar").setDescription("§7Clique aqui para voltar ao menu!");
				ib.build(inventory, 45);
			}
			if(inventory.getItem(43) != null) { 
				ib = new ItemBuilder(Material.ARROW).setName("§aPágina " + (page + 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 53);
			}
		} else if(typeInventory.equals(TypeInventoryClan.SCALATION)) { 
			inventory = Bukkit.createInventory(player, 54, "Escalação");
			List<String> list = clan.getClimbed();
			int start = (page > 1 ? (14 * page) - 14 : 0);
			String climbed = null;
			for(int i = 11; i <= 15; i++) {
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(5).setName("§aVaga disponível");
				ib.build(inventory, i);
			}
			for(int i = 0; i <= 27; i++)
				emptyAdd.add(i);
			for(int x = 11; x < inventory.getSize(); x++) { 
				try {  
					if(list.get(start) == null) continue; 
				} catch (Exception e) { 
					break; 
				}
				if(empty.contains(x)) continue;
				climbed = list.get(start);
				ib = new ItemBuilder(Material.SKULL_ITEM).setDurability(3).setName("§a" + climbed).setSkull(climbed);
				ib.build(inventory, x); 
				start++;
			}
			ClanMember member = null;
			List<ClanMember> listMembers = clan.getMembers();
			for(int x = 0; x < inventory.getSize(); x++) { 
				try {  
					if(listMembers.get(start) == null) continue; 
				} catch (Exception e) { 
					break; 
				}
				if(inventory.getItem(x) != null) continue;
				if(empty.contains(x)) continue;
				if(emptyAdd.contains(x)) continue;
				if(list.contains(listMembers.get(start).getName())) continue;
				member = listMembers.get(start);
				ib = new ItemBuilder(Material.SKULL_ITEM).setDurability(3).setName("§e" + member.getName()).setDescription("§fCargo §7" + member.getClanGroup().getName()).setSkull(member.getName());
				ib.build(inventory, x); 
				start++;
			}
			ib = new ItemBuilder(Material.MILK_BUCKET).setName("§cResetar escalação").setDescription("§7Clique aqui remover todos jogadores escalados.");
			ib.build(inventory, 49);
			if(page > 1) { 
				ib = new ItemBuilder(Material.ARROW).setName("§cPágina " + (page - 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 45);
			} else { 
				ib = new ItemBuilder(Material.ARROW).setName("§cVoltar").setDescription("§7Clique aqui para voltar ao menu!");
				ib.build(inventory, 45);
			}
			if(inventory.getItem(43) != null) { 
				ib = new ItemBuilder(Material.ARROW).setName("§aPágina " + (page + 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 53);
			}
		}
		player.openInventory(inventory);
	}
}
