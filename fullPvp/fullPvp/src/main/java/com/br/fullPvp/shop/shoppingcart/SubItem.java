package com.br.fullPvp.shop.shoppingcart;

import com.br.fullPvp.shop.item.Item;
import com.br.fullPvp.shop.item.ItemManager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SubItem {
	
	String uniqueIdItem;
	int amount;
	double price;
	
	public Item get() { 
		return ItemManager.getInstance().get(getUniqueIdItem());
	}
}
