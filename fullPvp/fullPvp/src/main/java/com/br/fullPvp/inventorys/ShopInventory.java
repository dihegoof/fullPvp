package com.br.fullPvp.inventorys;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.shop.item.Item;
import com.br.fullPvp.shop.session.Session;
import com.br.fullPvp.shop.session.SessionManager;
import com.br.fullPvp.shop.shoppingcart.ShoppingCart;
import com.br.fullPvp.shop.shoppingcart.ShoppingCartManager;
import com.br.fullPvp.shop.shoppingcart.SubItem;
import com.br.fullPvp.utils.ItemBuilder;
import com.br.fullPvp.utils.ItemName;
import com.br.fullPvp.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ShopInventory extends Utils {

	public enum TypeInventoryShop { 
		
		SESSIONS, EXACT_SESSION, CONFIRM, CONFIRM_LARGE_SCALE, SHOPPING_CART;
		
	}
	
	@AllArgsConstructor
	@Getter
	public enum SizeBuyLargeScale { 
		
		THREE(3), SIX(6), TWELVE(12), EIGHTEEN(18), TWENTY_FOUR(24), THIRTY(30), THIRTY_SIX(36);
		
		int number;
	}
	
	@Getter
	private static List<Integer> empty = new ArrayList<>();
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
	static ShopInventory instance = new ShopInventory();
	
	@SuppressWarnings("deprecation")
	public void create(Player player, Account account, TypeInventoryShop typeInventory, Session session, Item item, int page, int amount) { 
		Inventory inventory = null;
		ItemBuilder ib = null;
		if(typeInventory.equals(TypeInventoryShop.SESSIONS)) { 
			inventory = Bukkit.createInventory(player, 54, "Sessões");
			List<Session> list = SessionManager.getStorageSession();
			Session sessio = null;
			int start = (page > 1 ? (28 * page) - 28 : 0);
			if(list.isEmpty()) { 
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhuma sessão encontrada");
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
				sessio = list.get(start);
				ib = new ItemBuilder(sessio.getIcon().getType()).
						setDurability(sessio.getIcon().getDurability()).
						setName("§a" + sessio.getName()).
						setDescription(sessio.isPromo() ? "§c§lSESSÃO EM PROMOÇÃO" : sessio.hasItensPromo() ? "§c§lITENS EM PROMOÇÃO" : "§7Abra e confira os itens desta sessão.");
				ib.build(inventory, x);
				start++;
			}
			ib = new ItemBuilder(Material.STORAGE_MINECART).setName("§aCarrinho de compras").setDescription("§7Abra e confira oque tem em seu carrinho de compras!");
			ib.build(inventory, 49);
			
			if(page > 1) { 
				ib = new ItemBuilder(Material.ARROW).setName("§cPágina " + (page - 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 45);
			}
			if(inventory.getItem(43) != null) { 
				ib = new ItemBuilder(Material.ARROW).setName("§aPágina " + (page + 1)).setDescription("§7Clique aqui mudar de página!");
				ib.build(inventory, 53);
			}
		} else if(typeInventory.equals(TypeInventoryShop.EXACT_SESSION)) { 
			inventory = Bukkit.createInventory(player, 54, "Sessão " + session.getName());
			List<Item> list = session.getItens();
			Item ite = null;
			int start = (page > 1 ? (28 * page) - 28 : 0);
			if(list.isEmpty()) { 
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhum item encontrado");
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
				ite = list.get(start);
				double descount = ((ite.getDescount() + ite.getSession().getDescount()) * ite.getPrice()) / 100;
				ib = new ItemBuilder(ite.getItemStack().getType()).setDurability(ite.getItemStack().getDurability()).setName("§a" + ItemName.valueOf(ite.getItemStack().getType(), ite.getItemStack().getDurability()).getName()).setDescription("§fPreço §7" + (ite.isPromo() || ite.getSession().isPromo() ? "§m" + formatMoney(ite.getPrice()) + "§e " + formatMoney(ite.getPrice() - descount) + " §c§l" + formatPercent(Double.valueOf(ite.getDescount())) + "OFF " + (ite.getSession().isPromo() ? "§b§l+" + formatPercent(Double.valueOf(ite.getSession().getDescount())) + "OFF" : "") : formatMoney(ite.getPrice()))).setGlowed(ite.isPromo() || session.isPromo() ? true : false);
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
		} else if(typeInventory.equals(TypeInventoryShop.CONFIRM)) { 
			inventory = Bukkit.createInventory(player, 36, "Comprando ID#" + item.getUniqueId());
			
			ib = new ItemBuilder(Material.CHEST).setName("§aCompra em grande escala").setDescription("§7Olhe as opções de compra em massa!");
			ib.build(inventory, 10);
			
			ib = new ItemBuilder(Material.getMaterial(351)).setDurability(8).setName("§c-").setDescription("§7Diminua a quantidade de itens.");
			ib.build(inventory, 12);
			
			//valor_desconto * valor_total / 100
			//resultado - valor_total = valor_final
			
			double descount = ((item.getDescount() + item.getSession().getDescount()) * item.getPrice()) / 100;
			ib = new ItemBuilder(item.getItemStack().getType()).setDurability(item.getItemStack().getDurability()).setName("§a" + item.getItemStack().getItemMeta().getDisplayName()).setDescription("§fPreço §7" + (item.isPromo() || item.getSession().isPromo() ? "§m" + formatMoney(item.getPrice() * amount) + "§e " + formatMoney((item.getPrice() - descount) * amount) + " §c§l" + formatPercent(item.getDescount()) + "OFF " + (item.getSession().isPromo() ? "§b§l+" + formatPercent(Double.valueOf(item.getSession().getDescount())) + "OFF" : "") : formatMoney(item.getPrice() * amount))).setAmount(amount);
			ib.build(inventory, 13);
			
			ib = new ItemBuilder(Material.getMaterial(351)).setDurability(10).setName("§a+").setDescription("§7Aumente a quantidade de itens.");
			ib.build(inventory, 14);
			
			ib = new ItemBuilder(Material.STORAGE_MINECART).setName("§aAdicionar ao carrinho").setDescription("§7Adicione este item ao seu carrinho de compras.");
			ib.build(inventory, 16);
			
			ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(15).setName("§cCancelar").setDescription("§7Ao clicar aqui você cancela sua compra!");
			ib.build(inventory, 30);
			
			ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(5).setName("§aConfirmar").setDescription("§7Ao clicar aqui, o item é SEU!");
			ib.build(inventory, 32);
		} else if(typeInventory.equals(TypeInventoryShop.CONFIRM_LARGE_SCALE)) { 
			inventory = Bukkit.createInventory(player, 36, "Comprando mais de ID#" + item.getUniqueId());
		
			double descount = ((item.getDescount() + item.getSession().getDescount()) * item.getPrice()) / 100;
			int slot = 10;
			for(SizeBuyLargeScale s : SizeBuyLargeScale.values()) {
				ib = new ItemBuilder(item.getItemStack().getType()).setDurability(item.getItemStack().getDurability()).setName("§a" + item.getItemStack().getItemMeta().getDisplayName()).setDescription("§fPreço §7" + (item.isPromo() || item.getSession().isPromo() ? "§m" + formatMoney((item.getPrice() * 64) * s.getNumber()) + "§e " + formatMoney(((item.getPrice() - descount) * 64) * s.getNumber()) + " §c§l" + formatPercent(item.getDescount()) + "OFF " + (item.getSession().isPromo() ? "§b§l+" + formatPercent(Double.valueOf(item.getSession().getDescount())) + "OFF": "") : "§fPreço §7" + ((item.getPrice() * 64) * s.getNumber())), "§fQuantidade §7" + s.getNumber()).setAmount(64);
				ib.build(inventory, slot);
				slot++;
			}
			
			ib = new ItemBuilder(Material.ARROW).setName("§cVoltar").setDescription("§7Clique aqui para voltar ao menu!");
			ib.build(inventory, 27);
			
			ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(15).setName("§cCancelar").setDescription("§7Ao clicar aqui você cancela sua compra!");
			ib.build(inventory, 31);
		} else if(typeInventory.equals(TypeInventoryShop.SHOPPING_CART)) { 
			inventory = Bukkit.createInventory(player, 54, "Carrinho de compras");
			ShoppingCart shoppingCart = ShoppingCartManager.getInstance().get(player.getUniqueId());
			if(shoppingCart == null) return;
			SubItem subItem = null;
			List<SubItem> list = shoppingCart.getCart();
			int start = (page > 1 ? (28 * page) - 28 : 0);
			if(list.isEmpty()) { 
				ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName("§cNenhum item adicionado");
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
				subItem = list.get(start);
				ib = new ItemBuilder(subItem.get().getItemStack().getType()).setDurability(subItem.get().getItemStack().getDurability()).setName("§a" + ItemName.valueOf(subItem.get().getItemStack().getType(), subItem.get().getItemStack().getDurability()).getName()).setAmount(subItem.getAmount());
				ib.build(inventory, x);
				start++;
			}
			ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(15).setName("§cEsvaziar carrinho").setDescription("§7Limpe seu carrinho de compras com apenas um clique!");
			ib.build(inventory, 48);
			
			ib = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(5).setName("§aFinalizar compra").setDescription("§fValor §7" + formatMoney(shoppingCart.totalPrice()));
			ib.build(inventory, 50);
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
