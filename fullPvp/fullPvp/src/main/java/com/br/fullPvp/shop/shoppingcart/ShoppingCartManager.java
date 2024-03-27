package com.br.fullPvp.shop.shoppingcart;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.Getter;

public class ShoppingCartManager {
	
	@Getter
	static ShoppingCartManager instance = new ShoppingCartManager();
	@Getter
	static List<ShoppingCart> storageShoppingCart = new ArrayList<>();
	
	public void add(ShoppingCart shoppingCart) { 
		if(!storageShoppingCart.contains(shoppingCart)) { 
			storageShoppingCart.add(shoppingCart);
		}
	}
	
	public void remove(ShoppingCart shoppingCart) {
		if(storageShoppingCart.contains(shoppingCart)) { 
			storageShoppingCart.remove(shoppingCart);
		}
	}
	
	public ShoppingCart get(UUID uniqueId) { 
		for(ShoppingCart s : storageShoppingCart) {
			if(s.getUniqueId().equals(uniqueId)) { 
				return s;
			}
		}
		return null;
	}
	
	public void loadAllShoppingCart() { 
		try {
			int amount = 0;
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.SHOPPING_CART_SELECT_ALL.getQuery());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) { 
				List<SubItem> cart = new ArrayList<>();
				for(String names : Arrays.asList(rs.getString("cart").split(", "))) { 
					String[] split = names.split(";");
					cart.add(new SubItem(split[0], Integer.valueOf(split[1]), Double.valueOf(split[2])));
				}
				add(new ShoppingCart(UUID.fromString(rs.getString("uniqueid")), (Arrays.asList(rs.getString("cart")).get(0).equals("null") ? new ArrayList<SubItem>() : cart)));
				amount++;
			}
			Main.debug(amount > 0 ? "Carregado " + amount + " carrinho(s) de compras" : "Nenhum carrinho de compra foi carregado!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar os carrinhos de compras!", e.getMessage());
		}
	}
	
	public void saveAllShoppingCart() { 
		for(ShoppingCart s : storageShoppingCart) {
			s.save();
		}
	}

}
