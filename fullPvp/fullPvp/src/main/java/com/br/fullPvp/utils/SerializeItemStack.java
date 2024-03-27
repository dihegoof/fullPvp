package com.br.fullPvp.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

@SuppressWarnings("deprecation")
public class SerializeItemStack extends Utils {
	
	@Getter
	static SerializeItemStack instance = new SerializeItemStack();

	public String convert(ItemStack itemStack, int slot) { 
		StringBuilder sb = new StringBuilder();
		sb.append(slot + ";" + itemStack.getAmount() + ";" + itemStack.getTypeId() + ";" + itemStack.getDurability());
		if(itemStack.getItemMeta().hasDisplayName()) { 
			sb.append(";" + itemStack.getItemMeta().getDisplayName().replace("§", "&"));
		} else { 
			sb.append(";&f" + ItemName.valueOf(itemStack.getType(), itemStack.getDurability()).getName());
		}
		if(itemStack.getEnchantments().size() > 0) { 
			String list = "";
			for(Map.Entry<Enchantment, Integer> enc : itemStack.getEnchantments().entrySet()) {
				list += enc.getKey().getName() + "-" + enc.getValue() + ",";
			}
			sb.append(";" + list.toString().substring(0, list.length() - 1));
		} else { 
			sb.append(";null");
		}
		if(itemStack.getItemMeta().hasLore() && itemStack.getItemMeta().getLore().size() > 3) { 
			String lines = String.join("]", itemStack.getItemMeta().getLore());
			sb.append(";" + lines.replace("§", "&"));
		} else { 
			sb.append(";null");
		}
		return sb.toString();
	}
	
	//slot;amount;id_item;durability;name;enchant1-levelenchant1,enchant2-levelenchant2;lore1[lore2
	
	public ItemStack desconvert(String name) { 
		String[] split = name.split(";");
		ItemBuilder ib = new ItemBuilder(Material.getMaterial(Integer.valueOf(split[2])))
		.setAmount(Integer.valueOf(split[1]))
		.setDurability(Integer.valueOf(split[3]))
		.setName(split[4].replace("&", "§"));
		if(!split[5].equals("null")) { 
			String[] split_e = split[5].split(",");
			for(String names : split_e) {
				String[] split_e_e = names.split("-");
				ib.setEnchant(getTranslateEnchant(split_e_e[0]), Integer.valueOf(split_e_e[1]));
			}
			if(split[4].startsWith("&f")) { 
				ib.setName("§b§o" + ItemName.valueOf(Material.getMaterial(Integer.valueOf(split[2])), Integer.valueOf(split[3])).getName());
			}
		}
		if(!split[6].equals("null")) { 
			String[] split_l = split[6].replace("&", "§").split("]");
			List<String> list = Arrays.asList(split_l);
			ib.setDescription(list);
		} else { 
			ib.setDescription(Arrays.asList(""));
		}
		return ib.getStack();
	}
}
