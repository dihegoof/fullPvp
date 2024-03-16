package com.br.fullPvp.links;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.br.fullPvp.Main;
import com.br.fullPvp.links.Link.TypeLinks;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.Getter;
import lombok.Setter;

public class LinkManager {
	
	@Getter
	static LinkManager instance = new LinkManager();
	@Getter
	@Setter
	static Link link = null;
	
	public void loadLinks() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.LINK_SELECT.getQuery());
			stmt.setInt(1, 1);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) { 
				setLink(new Link(1, rs.getString("prefix"), rs.getString("website"), rs.getString("shop"), rs.getString("discord"), rs.getString("address"), rs.getString("youtubechannel"), rs.getString("twitter"), rs.getString("motd")));
			} else { 
				setLink(new Link(1, TypeLinks.PREFIX.getValue(), TypeLinks.WEBSITE.getValue(), TypeLinks.SHOP.getValue(), TypeLinks.DISCORD.getValue(), TypeLinks.ADDRESS.getValue(), TypeLinks.YOUTUBE_CHANNEL.getValue(), TypeLinks.TWITTER.getValue(), TypeLinks.MOTD.getValue()));
			}
			Main.debug("Links carregados!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar os links!", e.getLocalizedMessage());
		}
	}
	
	public void saveLinks() { 
		getLink().save();
	}
}
