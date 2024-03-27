package com.br.fullPvp.shop.shoppingcart;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;
import com.br.fullPvp.shop.item.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ShoppingCart {
	
	UUID uniqueId;
	List<SubItem> cart;
	
	public void save() { 
		try {
			PreparedStatement stmt = null;
			if(exists()) { 
				List<String> list = new ArrayList<>();
				for(SubItem s : getCart()) { 
					list.add(s.getUniqueIdItem() + ";" + s.getAmount() + ";" + s.getPrice());
				}
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.SHOPPING_CART_UPDATE.getQuery());
				stmt.setString(1, list.isEmpty() ? "null" : list.toString().replace("[", "").replace("]", ""));
				stmt.setString(2, getUniqueId().toString());
			} else { 
				List<String> list = new ArrayList<>();
				for(SubItem s : getCart()) { 
					list.add(s.getUniqueIdItem() + ";" + s.getAmount() + ";" + s.getPrice());
				}
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.SHOPPING_CART_INSERT.getQuery());
				stmt.setString(1, getUniqueId().toString());
				stmt.setString(2, list.isEmpty() ? "null" : list.toString().replace("[", "").replace("]", ""));
			}
			stmt.executeUpdate();
			Main.debug("Carrinho de " + getUniqueId().toString() + " salvo!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar carrinho de compras de " + getUniqueId().toString() + "!", e.getLocalizedMessage());
		}
	}	
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.SHOPPING_CART_SELECT.getQuery());
			stmt.setString(1, getUniqueId().toString());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void delete() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.SHOPPING_CART_DELETE.getQuery());
			stmt.setString(1, getUniqueId().toString());
			stmt.executeUpdate();
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao deletar carrinho de compras de " + getUniqueId().toString() + "!", e.getLocalizedMessage());
		}
	}
	
	public void add(Item item, int amount) { 
		double descount = ((item.getDescount() + item.getSession().getDescount()) * item.getPrice()) / 100;
		getCart().add(new SubItem(item.getUniqueId(), amount, item.isPromo() || item.getSession().isPromo() ? (item.getPrice() - descount) * amount : item.getPrice() * amount));
	}
	
	public void remove(Item item) { 
		for(SubItem s : getCart()) {
			if(s.getUniqueIdItem().equals(item.getUniqueId())) { 
				getCart().remove(s);
			}
		}
	}
	
	public double totalPrice() { 
		double price = 0;
		for(SubItem s : getCart()) { 
			price += s.getPrice();
		}
		return price;
	}

	public boolean isEmpty() {
		return getCart().size() == 0;
	}
}
