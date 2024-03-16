package com.br.fullPvp.links;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Link {
	
	@Getter
	@AllArgsConstructor
	public enum TypeLinks { 
		
		PREFIX("Prefixo", "§b§lFULLPVP"),
		WEBSITE("Site", "www.fullpvp.com.br"),
		SHOP("Loja", "www.loja.fullpvp.com.br"),
		DISCORD("Discord", "dc.join/fullpvp"),
		ADDRESS("Ip", "jogar.fullpvp.com.br"),
		YOUTUBE_CHANNEL("Youtube", "you.be/c/fullpvp"),
		TWITTER("Twitter", "@fullpvp"),
		MOTD("Motd", "§b§lFULLPVP §7Venha jogar conosco!|§dPouco com Deus é muito, jovem!");
		
		String name, value;
		
		public static TypeLinks get(String args) { 
			for(TypeLinks t : values()) { 
				if(t.getName().equalsIgnoreCase(args)) { 
					return t;
				}
			}
			return null;
		}
	}
	
	
	int id;
	String prefix, website, shop, discord, address, youtubeChannel, twitter, motd;  

	public void save() { 
		try {
			PreparedStatement stmt = null;
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.LINK_UPDATE.getQuery());
				stmt.setString(1, getPrefix());
				stmt.setString(2, getWebsite());
				stmt.setString(3, getShop());
				stmt.setString(4, getDiscord());
				stmt.setString(5, getAddress());
				stmt.setString(6, getYoutubeChannel());
				stmt.setString(7, getTwitter());
				stmt.setString(8, getMotd());
				stmt.setInt(9, 1);
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.LINK_INSERT.getQuery());
				stmt.setInt(1, 1);
				stmt.setString(2, getPrefix());
				stmt.setString(3, getWebsite());
				stmt.setString(4, getShop());
				stmt.setString(5, getDiscord());
				stmt.setString(6, getAddress());
				stmt.setString(7, getYoutubeChannel());
				stmt.setString(8, getTwitter());
				stmt.setString(9, getMotd());
			}
			stmt.executeUpdate();
			Main.debug("Links salvos!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar links!", e.getLocalizedMessage());
		}
	}	
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.LINK_SELECT.getQuery());
			stmt.setInt(1, 1);
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void exactLink(String typeLink, String value) { 
		if(typeLink.equals(TypeLinks.PREFIX.getName())) { 
			setPrefix(value);
		} else if(typeLink.equals(TypeLinks.WEBSITE.getName())) { 
			setWebsite(value);
		} else if(typeLink.equals(TypeLinks.SHOP.getName())) { 
			setShop(value);
		} else if(typeLink.equals(TypeLinks.DISCORD.getName())) { 
			setDiscord(value);
		} else if(typeLink.equals(TypeLinks.ADDRESS.getName())) { 
			setAddress(value);
		} else if(typeLink.equals(TypeLinks.YOUTUBE_CHANNEL.getName())) { 
			setYoutubeChannel(value);
		} else if(typeLink.equals(TypeLinks.TWITTER.getName())) { 
			setTwitter(value);
		} else if(typeLink.equals(TypeLinks.MOTD.getName())) { 
			setMotd(value);
		}
	}
}
