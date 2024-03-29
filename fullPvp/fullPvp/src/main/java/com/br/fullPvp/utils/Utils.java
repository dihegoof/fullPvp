package com.br.fullPvp.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.br.fullPvp.accounts.TypeCoin;
import com.br.fullPvp.links.Link.TypeLinks;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class Utils {
	
	@Getter
	static Utils instance = new Utils();
	static final String[] suffix = new String[] { "", "k", "m", "b", "t" };
	static final int MAX_LENGTH = 4;

	public String formatMoney(double value) { 
		 return TypeCoin.REAL.getSymbol() + new DecimalFormat("#,###.#").format(value);
	}
	
	public String formatCash(double value) { 
		 return TypeCoin.CASH.getSymbol() + new DecimalFormat("###.#").format(value);
	}
	
	public String formatReputation(double value) { 
		 return TypeCoin.REPUTACAO.getSymbol() + new DecimalFormat("###.#").format(value);
	}
	
	public String formatPercent(double value) { 
		return "%" + String.valueOf(value).replace(".0", "");
	}
	
	public void sendMessage(CommandSender player, boolean skipLine, String... messages) {
		if(skipLine)
			player.sendMessage("");
		for(String message : messages)
			player.sendMessage(message);
		if(skipLine)
			player.sendMessage("");
	}

	public void sendBroadcastMessage(boolean skipLine, String... messages) {
		if(skipLine)
			Bukkit.broadcastMessage("");
		for(String message : messages)
			Bukkit.broadcastMessage(message);
		if(skipLine)
			Bukkit.broadcastMessage("");
	}

	public void sintaxCommand(CommandSender player, String... messages) {
		player.sendMessage("");
		player.sendMessage("§cLista de comandos:");
		player.sendMessage("");
		for(String message : messages)
			player.sendMessage(message);
		player.sendMessage("");
	}
	
	public String formatNumber(double number) {
		String r = new DecimalFormat("##0E0").format(number);
		r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
		while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
			r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
		}
		return r;
	}

	public boolean getBooleanByInteger(Integer id) {
		if (id == 0) {
			return false;
		}
		return true;
	}
	
	public boolean validateName(String username) {
		Pattern pattern = Pattern.compile("[a-zA-Z0-9_]{1,16}");
		Matcher matcher = pattern.matcher(username);
		return matcher.matches();
	}

	public String decode64(String decode) {
		try {
			return new String(Base64.getDecoder().decode(decode.getBytes()), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String encode64(String encode) {
		try {
			return Base64.getEncoder().encodeToString(encode.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<String> getFormattedLore(String text) {
		List<String> lore = new ArrayList<>();
		String[] split = text.split(" ");
		text = "";

		for (int i = 0; i < split.length; ++i) {
			if (ChatColor.stripColor(text).length() > 25 || ChatColor.stripColor(text).endsWith(".")
					|| ChatColor.stripColor(text).endsWith("!")) {
				lore.add("§7" + text);
				if (text.endsWith(".") || text.endsWith("!")) {
					lore.add("");
				}
				text = "";
			}
			String toAdd = split[i];
			if (toAdd.contains("\n")) {
				toAdd = toAdd.substring(0, toAdd.indexOf("\n"));
				split[i] = split[i].substring(toAdd.length() + 1);
				lore.add("§7" + text + ((text.length() == 0) ? "" : " ") + toAdd);
				text = "";
				--i;
			} else {
				text += ((text.length() == 0) ? "" : " ") + toAdd;
			}
		}
		lore.add("§7" + text);

		return lore;
	}

	public String formatData(long time) {
		String dateFinal = "";
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
		dateFinal = String.valueOf(format.format(time).substring(0, 10));
		dateFinal += " às " + String.valueOf(format.format(time).substring(11, 19));
		return dateFinal;
	}

	public void setValue(String field, Class<?> clazz, Object instance, Object value) {
		try {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			f.set(instance, value);
		} catch (Exception exception) {

		}
	}

	public Object getValue(String field, Class<?> clazz, Object instance) {
		try {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			return f.get(instance);
		} catch (Exception exception) {

		}
		return null;
	}

	public void setValue(String field, Object instance, Object value) {
		try {
			Field f = instance.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(instance, value);
		} catch (Exception exception) {

		}
	}

	public Object getValue(String field, Object instance) {
		try {
			Field f = instance.getClass().getDeclaredField(field);
			f.setAccessible(true);
			return f.get(instance);
		} catch (Exception exception) {

		}
		return null;
	}
	
	public String createArgs(int index, String[] args, String defaultArgs, boolean color) {
		StringBuilder sb = new StringBuilder();
		for (int i = index; i < args.length; i++)
			sb.append(args[i]).append((i + 1 >= args.length ? "" : " "));
		if (sb.length() == 0)
			sb.append(defaultArgs);
		return color ? sb.toString().replace("&", "§") : sb.toString();
	}

	public boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public boolean isDouble(String string) {
		try {
			Double.parseDouble(string);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public boolean isTime(String time) {
		try {
			TimeManager.getInstance().getTime(time);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean isCoin(String coin) {
		try {
			TypeCoin.valueOf(coin);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean isTypeLink(String typeLink) { 
		boolean has = false;
		for(TypeLinks t : TypeLinks.values()) { 
			if(t.getName().equalsIgnoreCase(typeLink)) {
				has = true;
			}
		}
		return has;
	}
	
	public boolean isUUID(String string) {
		try {
			UUID.fromString(string);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public String formatTime(int i) {
		if (i >= 60) {
			int minutes = i / 60;
			int seconds = i - minutes * 60;
			if (seconds == 0) {
				if (minutes > 1) {
					return minutes + " minutos";
				} else {
					return minutes + " minuto";
				}
			}
			String min = "minuto";
			String second = "segundo";
			if (minutes > 1)
				min = min + "s";
			if (seconds > 1)
				second = second + "s";
			return minutes + " " + min + " e " + seconds + " " + second;
		}
		if (i > 1)
			return i + " segundos";
		return i + " segundo";
	}

	public String toTime(int i) {
		int minutes = i / 60;
		int seconds = i % 60;

		if (minutes > 0) {
			return minutes + "m" + (seconds > 0 ? " " + seconds + "s" : "");
		} else {
			return seconds + "s";
		}
	}

	public String compareTime(long end) {
		return compareTime(System.currentTimeMillis(), end);
	}

	public String compareTime(long current, long end) {
		long ms = end - current;
		int seconds = (int) (ms / 1000) % 60;
		int minutes = (int) (ms / (1000 * 60)) % 60;
		int hours = (int) (ms / (1000 * 60 * 60) % 24);
		int days = (int) (ms / (1000 * 60 * 60 * 24));

		return (days > 0 ? days + " dias " : "") + (hours > 0 ? hours + " horas " : "")
				+ (minutes > 0 ? minutes + " minutos " : "") + (seconds > 0 ? seconds + " segundos" : "");
	}

	public String compareSimpleTime(long end) {
		return compareSimpleTime(System.currentTimeMillis(), end);
	}

	public String compareSimpleTime(long current, long end) {
		long ms = end - current;
		int seconds = (int) (ms / 1000) % 60;
		int minutes = (int) (ms / (1000 * 60)) % 60;
		int hours = (int) (ms / (1000 * 60 * 60) % 24);
		int days = (int) (ms / (1000 * 60 * 60 * 24));

		String retur = (days > 0 ? days + "d, " : "") + (hours > 0 ? hours + "h, " : "")
				+ (minutes > 0 ? minutes + "m, " : "") + (seconds > 0 ? seconds + "s," : "");
		return retur.substring(0, retur.length() - 1) + ".";
	}

	public UUID fromRenato(String uuid) {
		return UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-"
				+ uuid.substring(16, 20) + "-" + uuid.substring(20, 32));
	}

	public String captalize(String toCaptalize) {
		return toCaptalize.toLowerCase().substring(0, 1).toUpperCase() + toCaptalize.substring(1);
	}

	public int countItem(Inventory inventory, Material material) {
		int amount = 0;

		for (ItemStack items : inventory.getContents())
			if (items != null && items.getType().equals(material))
				amount++;

		return amount;
	}

	public String formatOldTime(Integer i) {
		int minutes = i.intValue() / 60;
		int seconds = i.intValue() % 60;
		String disMinu = (minutes < 10 ? "" : "") + minutes;
		String disSec = (seconds < 10 ? "0" : "") + seconds;
		String formattedTime = disMinu + ":" + disSec;
		return formattedTime;
	}

	public int findItem(Inventory inventory, Material material) {
		int slot = 0;

		for (int i = 0; i < inventory.getContents().length; i++) {
			if (inventory.getItem(i) != null && inventory.getItem(i).getType().equals(material)) {
				slot = i;
			}
		}

		return slot;
	}
	
	public void updateTab(Player player, String cima, String baixo) {
		Title.getInstance().sendTabTitle(player, cima, baixo);
	}

	public void box(String title, String... paragraph) {
		ArrayList<String> buffer = new ArrayList<String>();
		String at = "";
		int side1 = (int) Math.round(25 - ((title.length() + 4) / 2d));
		int side2 = (int) (26 - ((title.length() + 4) / 2d));
		at += '+';
		for (int t = 0; t < side1; t++)
			at += '-';
		at += "{ " + title + " }";
		for (int t = 0; t < side2; t++)
			at += '-';
		at += '+';
		buffer.add(at);
		at = "";
		buffer.add("|                                                   |");
		for (String s : paragraph) {
			at += "| ";
			int left = 49;
			for (int t = 0; t < s.length(); t++) {
				at += s.charAt(t);
				left--;
				if (left == 0) {
					at += " |";
					buffer.add(at);
					at = "";
					at += "| ";
					left = 49;
				}
			}
			while (left-- > 0)
				at += ' ';
			at += " |";
			buffer.add(at);
			at = "";
		}
		buffer.add("|                                                   |");
		buffer.add("+---------------------------------------------------+");
		System.out.println(" ");
		for (String line : buffer.toArray(new String[buffer.size()])) {
			System.out.println(line);
		}
		System.out.println(" ");
	}

	public char randomChar() {
		return (char) ('a' + new Random().nextInt(25));
	}

	public char nextChar(int id) {
		return (char) ('a' + id);
	}

	public List<Block> getNearbyBlocks(Location location, int radius) {
		List<Block> blocks = new ArrayList<Block>();
		for (int X = location.getBlockX() - radius; X <= location.getBlockX() + radius; X++) {
			for (int Y = location.getBlockY() - radius; Y <= location.getBlockY() + radius; Y++) {
				for (int Z = location.getBlockZ() - radius; Z <= location.getBlockZ() + radius; Z++) {
					Block block = location.getWorld().getBlockAt(X, Y, Z);
					if (!block.isEmpty())
						blocks.add(block);
				}
			}
		}
		return blocks;
	}
	
	public Enchantment getTranslateEnchant(String name) { 
		Enchantment enchant = null;
		if(name.toUpperCase().equalsIgnoreCase("DAMAGE_ALL") || name.toUpperCase().equalsIgnoreCase("DAMAGE_ALL") || name.toUpperCase().equalsIgnoreCase("AFIAÇÃO")) { 
			enchant = Enchantment.DAMAGE_ALL;
		} else if(name.toUpperCase().equalsIgnoreCase("KNOCKBACK") || name.toUpperCase().equalsIgnoreCase("REPULSÃO")) { 
			enchant = Enchantment.KNOCKBACK;
		} else if(name.toUpperCase().equalsIgnoreCase("DAMAGEARTHROPODS") || name.toUpperCase().equalsIgnoreCase("DAMAGE_ARTHROPODS") || name.toUpperCase().equalsIgnoreCase("RUÍNA DOS ARTRÓPODES")) { 
			enchant = Enchantment.DAMAGE_ARTHROPODS;
		} else if(name.toUpperCase().equalsIgnoreCase("DIG_SPEED") || name.toUpperCase().equalsIgnoreCase("DIGSPEED") || name.toUpperCase().equalsIgnoreCase("EFFICIENCY") || name.toUpperCase().equalsIgnoreCase("EFICIÊNCIA")) { 
			enchant = Enchantment.DIG_SPEED;
		} else if(name.toUpperCase().equalsIgnoreCase("DURABILITY") || name.toUpperCase().equalsIgnoreCase("UNBREAKING") || name.toUpperCase().equalsIgnoreCase("INQUEBRÁVEL")) { 
			enchant = Enchantment.DURABILITY;
		} else if(name.toUpperCase().equalsIgnoreCase("FIRE_ASPECT") || name.toUpperCase().equalsIgnoreCase("FIREASPECTY") || name.toUpperCase().equalsIgnoreCase("ASPECTO FLAMEJANTE")) { 
			enchant = Enchantment.FIRE_ASPECT;
		} else if(name.toUpperCase().equalsIgnoreCase("LOOT_BONUS_BLOCKS") || name.toUpperCase().equalsIgnoreCase("LOOTBONUSBLOCKS") || name.toUpperCase().equalsIgnoreCase("FORTUNE") || name.toUpperCase().equalsIgnoreCase("FORTUNA")) { 
			enchant = Enchantment.LOOT_BONUS_BLOCKS;
		} else if(name.toUpperCase().equalsIgnoreCase("LOOT_BONUS_MOBS") || name.toUpperCase().equalsIgnoreCase("LOOTBONUSMOBS") || name.toUpperCase().equalsIgnoreCase("PILHAGEM")) { 
			enchant = Enchantment.LOOT_BONUS_MOBS;
		} else if(name.toUpperCase().equalsIgnoreCase("PROTECTION_ENVIRONMENTAL") || name.toUpperCase().equalsIgnoreCase("PROTECTIONENVIRONMENTAL") || name.toUpperCase().equalsIgnoreCase("PROTEÇÃO")) { 
			enchant = Enchantment.PROTECTION_ENVIRONMENTAL;
		} else if(name.toUpperCase().equalsIgnoreCase("PROTECTION_EXPLOSIONS") || name.toUpperCase().equalsIgnoreCase("PROTECTIONEXPLOSIONS") || name.toUpperCase().equalsIgnoreCase("PROTEÇÃO CONTRA EXPLOSÃO")) { 
			enchant = Enchantment.PROTECTION_EXPLOSIONS;
		} else if(name.toUpperCase().equalsIgnoreCase("PROTECTION_FIRE") || name.toUpperCase().equalsIgnoreCase("PROTECTIONFIRE") || name.toUpperCase().equalsIgnoreCase("PROTEÇÃO CONTRA FOGO")) { 
			enchant = Enchantment.PROTECTION_FIRE;
		} else if(name.toUpperCase().equalsIgnoreCase("PROTECTION_PROJECTILE") || name.toUpperCase().equalsIgnoreCase("PROTECTIONPROJECTILE") || name.toUpperCase().equalsIgnoreCase("PROTEÇÃO CONTRA PROJÉTEIS")) { 
			enchant = Enchantment.PROTECTION_PROJECTILE;
		} else if(name.toUpperCase().equalsIgnoreCase("SILK_TOUCH") || name.toUpperCase().equalsIgnoreCase("SILKTOUCH") || name.toUpperCase().equalsIgnoreCase("TOQUE SUAVE")) { 
			enchant = Enchantment.SILK_TOUCH;
		} else if(name.toUpperCase().equalsIgnoreCase("ARROW_FIRE") || name.toUpperCase().equalsIgnoreCase("ARROWFIRE") || name.toUpperCase().equalsIgnoreCase("CHAMA")) { 
			enchant = Enchantment.ARROW_FIRE;
		} else if(name.toUpperCase().equalsIgnoreCase("ARROW_DAMAGE") || name.toUpperCase().equalsIgnoreCase("ARROWDAMAGE") || name.toUpperCase().equalsIgnoreCase("FORÇA")) { 
			enchant = Enchantment.ARROW_DAMAGE;
		} else if(name.toUpperCase().equalsIgnoreCase("ARROW_INFINITY") || name.toUpperCase().equalsIgnoreCase("ARROWINFINITY") || name.toUpperCase().equalsIgnoreCase("INFINIDADE")) { 
			enchant = Enchantment.ARROW_INFINITE;
		} else if(name.toUpperCase().equalsIgnoreCase("ARROW_KNOCKBACK") || name.toUpperCase().equalsIgnoreCase("ARROWKNOCKBACK") || name.toUpperCase().equalsIgnoreCase("IMPACTO")) { 
			enchant = Enchantment.ARROW_KNOCKBACK;
		}
		return enchant;
	}

	public long parseDateDiff(String time, boolean future) throws Exception {
		Pattern timePattern = Pattern.compile(
				"(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?",
				2);
		Matcher m = timePattern.matcher(time);
		int years = 0;
		int months = 0;
		int weeks = 0;
		int days = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		boolean found = false;
		while (m.find()) {
			if ((m.group() != null) && (!m.group().isEmpty())) {
				for (int i = 0; i < m.groupCount(); i++) {
					if ((m.group(i) != null) && (!m.group(i).isEmpty())) {
						found = true;
						break;
					}
				}
				if (found) {
					if ((m.group(1) != null) && (!m.group(1).isEmpty())) {
						years = Integer.parseInt(m.group(1));
					}
					if ((m.group(2) != null) && (!m.group(2).isEmpty())) {
						months = Integer.parseInt(m.group(2));
					}
					if ((m.group(3) != null) && (!m.group(3).isEmpty())) {
						weeks = Integer.parseInt(m.group(3));
					}
					if ((m.group(4) != null) && (!m.group(4).isEmpty())) {
						days = Integer.parseInt(m.group(4));
					}
					if ((m.group(5) != null) && (!m.group(5).isEmpty())) {
						hours = Integer.parseInt(m.group(5));
					}
					if ((m.group(6) != null) && (!m.group(6).isEmpty())) {
						minutes = Integer.parseInt(m.group(6));
					}
					if ((m.group(7) == null) || (m.group(7).isEmpty())) {
						break;
					}
					seconds = Integer.parseInt(m.group(7));

					break;
				}
			}
		}
		if (!found) {
			throw new Exception("Illegal Date");
		}
		if (years > 20) {
			throw new Exception("Illegal Date");
		}
		Calendar c = new GregorianCalendar();
		if (years > 0) {
			c.add(1, years * (future ? 1 : -1));
		}
		if (months > 0) {
			c.add(2, months * (future ? 1 : -1));
		}
		if (weeks > 0) {
			c.add(3, weeks * (future ? 1 : -1));
		}
		if (days > 0) {
			c.add(5, days * (future ? 1 : -1));
		}
		if (hours > 0) {
			c.add(11, hours * (future ? 1 : -1));
		}
		if (minutes > 0) {
			c.add(12, minutes * (future ? 1 : -1));
		}
		if (seconds > 0) {
			c.add(13, seconds * (future ? 1 : -1));
		}
		return c.getTimeInMillis();
	}
}