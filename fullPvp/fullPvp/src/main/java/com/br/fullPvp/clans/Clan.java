package com.br.fullPvp.clans;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.br.fullPvp.Main;
import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.TypeCoin;
import com.br.fullPvp.clans.status.ClanStats;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Clan {
	
	String name, tag, leadder, motto;
	List<ClanMember> members;
	List<String> climbed, invites, allies, enemies;
	double real;
	long createdIn;
	boolean pvp;
	
	ClanStats status;

	public Clan(String name, String tag, String leadder) {
		this.name = name;
		this.tag = tag;
		this.leadder = leadder;
		this.motto = "Clã criado recentemente";
		this.members = new ArrayList<>();
		this.climbed = new ArrayList<>();
		this.invites = new ArrayList<>();
		this.allies = new ArrayList<>();
		this.enemies = new ArrayList<>();
		this.real = 0.0D;
		this.createdIn = System.currentTimeMillis();
		this.pvp = false;
		this.status = new ClanStats(name, 0.0D, 0.0D, 0.0D);
	}
	
	public void save() { 
		try {
			PreparedStatement stmt = null;
			List<String> list = new ArrayList<>();
			for(ClanMember c : getMembers()) { 
				list.add(c.getName() + ";" + c.getClanGroup().toString() + ";" + c.getJoinIn());
			}
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.CLAN_UPDATE.getQuery());
				stmt.setString(1, getTag());
				stmt.setString(2, getMotto());
				stmt.setString(3, list.isEmpty() ? "null" : list.toString().replace("[", "").replace("]", ""));
				Main.debug("Members > " + getMembers().toString());
				stmt.setString(4, getClimbed().isEmpty() ? "null" : getClimbed().toString().replace("[", "").replace("]", ""));
				stmt.setString(5, getInvites().isEmpty() ? "null" : getInvites().toString().replace("[", "").replace("]", ""));
				stmt.setString(6, getAllies().isEmpty() ? "null" : getAllies().toString().replace("[", "").replace("]", ""));
				stmt.setString(7, getEnemies().isEmpty() ? "null" : getEnemies().toString().replace("[", "").replace("]", ""));
				stmt.setDouble(8, getReal());
				stmt.setLong(9, getCreatedIn());
				stmt.setBoolean(10, isPvp());
				stmt.setString(11, getName());
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.CLAN_INSERT.getQuery());
				stmt.setString(1, getName());
				stmt.setString(2, getTag());
				stmt.setString(3, getLeadder());
				stmt.setString(4, getMotto());
				stmt.setString(5, list.isEmpty() ? "null" : list.toString().replace("[", "").replace("]", ""));
				stmt.setString(6, getClimbed().isEmpty() ? "null" : getClimbed().toString().replace("[", "").replace("]", ""));
				stmt.setString(7, getInvites().isEmpty() ? "null" : getInvites().toString().replace("[", "").replace("]", ""));
				stmt.setString(8, getAllies().isEmpty() ? "null" : getAllies().toString().replace("[", "").replace("]", ""));
				stmt.setString(9, getEnemies().isEmpty() ? "null" : getEnemies().toString().replace("[", "").replace("]", ""));
				stmt.setDouble(10, getReal());
				stmt.setLong(11, getCreatedIn());
				stmt.setBoolean(12, isPvp());
			}
			stmt.executeUpdate();
			Main.debug("Clan " + getName() + " salvo, preparando para salvar status!");
			getStatus().save();
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar clan " + getName() + "!", e.getLocalizedMessage());
		}
	}	
	
	public void loadStatus() { 
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.CLAN_STATUS_SELECT.getQuery());
			stmt.setString(1, getName());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				setStatus(new ClanStats(getName(), rs.getDouble("kills"), rs.getDouble("deaths"), rs.getDouble("killstreak")));
			}
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar status do clan " + getName() + "!", e.getLocalizedMessage());
		}
	}
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.CLAN_SELECT.getQuery());
			stmt.setString(1, getName());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void delete() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.CLAN_DELETE.getQuery());
			stmt.setString(1, getName());
			stmt.executeUpdate();
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao deletar clan " + getName() + "!", e.getLocalizedMessage());
		}
	}
	
	public List<Account> getAccountMembers() { 
		List<Account> members = new ArrayList<>();
		for(ClanMember c : getMembers()) { 
			if(AccountManager.getInstance().get(c.getName()) != null) { 
				members.add(AccountManager.getInstance().get(c.getName()));
			}
		}
		return members;
	}
	
	public int getMembersOnline() { 
		int amount = 0;
		for(Account a : getAccountMembers()) { 
			if(a.isOnline()) { 
				amount++;
			}
		}
		return amount;
	}
	
	public void sendMessage(boolean skipLine, String messages) {
		for(Account a : getAccountMembers()) { 
			a.sendMessage(skipLine, "§7[" + messages + "]");
		}
	}
	
	public boolean hasEconomy(TypeCoin typeCoin, double amount) { 
		if(typeCoin.equals(TypeCoin.REAL)) { 
			return getReal() >= amount;			
		}
		return false;
	}
	
	public void remove(TypeCoin typeCoin, double amount) {
		if(typeCoin.equals(TypeCoin.REAL)) { 
			setReal(!hasEconomy(typeCoin, amount) ? 0 : getReal() - amount);
		}
	}

	public void add(TypeCoin typeCoin, double amount) {
		if(typeCoin.equals(TypeCoin.REAL)) { 
			setReal(getReal() + amount);
		}
	}

	public boolean hasMember(String nickName) {
		return AccountManager.getInstance().get(nickName).getClanName().equals(getName());
	}

	public void removeMember(String nickName) {
		for(ClanMember c : getMembers()) { 
			if(c.getName().equals(nickName)) { 
				getMembers().remove(c);
			}
		}
	}

	public void changeGroup(String nickName, ClanGroup member) {
		for(ClanMember c : getMembers()) { 
			if(c.getName().equals(nickName)) { 
				c.setClanGroup(member);
			}
		}
	}

	public void join(String nickName) {
		getMembers().add(new ClanMember(nickName, ClanGroup.MEMBER, System.currentTimeMillis()));
	}
}
