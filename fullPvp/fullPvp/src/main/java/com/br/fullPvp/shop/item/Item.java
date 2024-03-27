package com.br.fullPvp.shop.item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import org.bukkit.inventory.ItemStack;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;
import com.br.fullPvp.shop.session.Session;
import com.br.fullPvp.utils.SerializeItemStack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Item {
	
	String uniqueId;
	ItemStack itemStack;
	Session session;
	double price, descount;
	boolean promo;
	
	public Item(ItemStack itemStack, Session session, double price, double descount, boolean promo) {
		this.uniqueId = createUnique();
		this.itemStack = itemStack;
		this.session = session;
		this.price = price;
		this.descount = descount;
		this.promo = promo;
	}
	
	public void save() { 
		try {
			PreparedStatement stmt = null;
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.ITEM_UPDATE.getQuery());
				stmt.setString(1, getSession().getName());
				stmt.setDouble(2, getPrice());
				stmt.setDouble(3, getDescount());
				stmt.setBoolean(4, isPromo());
				stmt.setString(5, getUniqueId());
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.ITEM_INSERT.getQuery());
				stmt.setString(1, getUniqueId());
				stmt.setString(2, SerializeItemStack.getInstance().convert(getItemStack(), 0));
				stmt.setString(3, getSession().getName());
				stmt.setDouble(4, getPrice());
				stmt.setDouble(5, getDescount());
				stmt.setBoolean(6, isPromo());
			}
			stmt.executeUpdate();
			Main.debug("Item " + getUniqueId() + " salvo!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar item " + getUniqueId() + "!", e.getLocalizedMessage());
		}
	}	
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.ITEM_SELECT.getQuery());
			stmt.setString(1, getUniqueId());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void delete() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.ITEM_DELETE.getQuery());
			stmt.setString(1, getUniqueId());
			stmt.executeUpdate();
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao deletar item " + getUniqueId() + "!", e.getLocalizedMessage());
		}
	}
	
	public String createUnique() { 
		String uniqueId = "", values = "AbCdEfGhIjKlMnOpQrStUvWxYz0123456789";
		for(int i = 0; i < 6; i++) {
			uniqueId += values.charAt(new Random().nextInt(values.length()));
			if(ItemManager.getInstance().get(uniqueId) != null) {
				Main.debug("O ID " + uniqueId + " já existe!");
				continue;
			}
		}
		return uniqueId;
	}
}
