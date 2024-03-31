package com.br.fullPvp.accounts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.br.fullPvp.Main;
import com.br.fullPvp.accounts.preferences.Preferences;
import com.br.fullPvp.accounts.status.Status;
import com.br.fullPvp.clans.Clan;
import com.br.fullPvp.clans.ClanGroup;
import com.br.fullPvp.clans.ClanManager;
import com.br.fullPvp.clans.ClanMember;
import com.br.fullPvp.groups.Group;
import com.br.fullPvp.groups.GroupManager;
import com.br.fullPvp.groups.PermissionsCase;
import com.br.fullPvp.mysql.SqlQuerys;
import com.br.fullPvp.ranks.RankManager;
import com.br.fullPvp.ranks.Requeriments;
import com.br.fullPvp.tags.Tag;
import com.br.fullPvp.tags.TagManager;
import com.br.fullPvp.utils.TagUpdate;
import com.br.fullPvp.warps.Warp;
import com.br.fullPvp.warps.WarpManager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Account {
	
	UUID uniqueId;
	String nickName, rankName, groupName, lastGroupName, clanName, address, lastAddress, tagUsing;
	long timeGroup, firstLogin, lastSee;
	double real, cash, reputation;
	boolean online;
	List<PermissionsCase> permissions;
	List<String> tags;
	Player player;
	Status status;
	Preferences preferences;
	
	Warp warp;
	
	BukkitTask teleportWarp = null;
	
	public Account(UUID uniqueId, String nickName, Player player) { 
		this.uniqueId = uniqueId;
		this.nickName = nickName;
		this.rankName = RankManager.getInstance().rankDefaulted() != null ? RankManager.getInstance().rankDefaulted().getName() : "NRE";
		this.groupName = GroupManager.getInstance().groupDefaulted() != null ? GroupManager.getInstance().groupDefaulted().getName() : "NRE";
		this.lastGroupName = "NRE";
		this.clanName = "NRE";
		this.address = player.getAddress().getHostString();
		this.lastAddress = player.getAddress().getHostString();
		this.tagUsing = "NRE";
		this.timeGroup = -1L;
		this.firstLogin = System.currentTimeMillis();
		this.lastSee = -1L;
		this.real = 0.0D;
		this.cash = 0.0D;
		this.reputation = 0.0D;
		this.online = false;
		this.permissions = new ArrayList<>();
		this.tags = new ArrayList<>();
		this.player = player;
		this.status = new Status(uniqueId, 0, 0, 0.0);
		this.preferences = new Preferences(uniqueId);
		
		this.warp = null;
	}
	
	public void save() { 
		try {
			PreparedStatement stmt = null;
			List<String> list = new ArrayList<>();
			for(PermissionsCase p : getPermissions()) { 
				list.add(p.getPermission() + ";" + p.getTime());
			}
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.ACCOUNT_UPDATE.getQuery());
				stmt.setString(1, getNickName());
				stmt.setString(2, getRankName());
				stmt.setString(3, getGroupName());
				stmt.setString(4, getLastGroupName());
				stmt.setString(5, getClanName());
				stmt.setString(6, getAddress());
				stmt.setString(7, getLastAddress());
				stmt.setString(8, getTagUsing());
				stmt.setLong(9, getTimeGroup());
				stmt.setLong(10, getLastSee());
				stmt.setDouble(11, getReal());
				stmt.setDouble(12, getCash());
				stmt.setDouble(13, getReputation());
				stmt.setString(14, list.isEmpty() ? "null" : list.toString().replace("[", "").replace("]", ""));
				stmt.setString(15, getTags().isEmpty() ? "null" : getTags().toString().replace("[", "").replace("]", ""));
				stmt.setString(16, getUniqueId().toString());
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.ACCOUNT_INSERT.getQuery());
				stmt.setString(1, getUniqueId().toString());
				stmt.setString(2, getNickName());
				stmt.setString(3, getRankName());
				stmt.setString(4, getGroupName());
				stmt.setString(5, getLastGroupName());
				stmt.setString(6, getClanName());
				stmt.setString(7, getAddress());
				stmt.setString(8, getLastAddress());
				stmt.setString(9, getTagUsing());
				stmt.setLong(10, getTimeGroup());
				stmt.setLong(11, getFirstLogin());
				stmt.setLong(12, getLastSee());
				stmt.setDouble(13, getReal());
				stmt.setDouble(14, getCash());
				stmt.setDouble(15, getReputation());
				stmt.setString(16, getTags().isEmpty() ? "null" : getTags().toString().replace("[", "").replace("]", ""));
				stmt.setString(17, list.isEmpty() ? "null" : list.toString().replace("[", "").replace("]", ""));
			}
			stmt.executeUpdate();
			Main.debug("Conta de " + getNickName() + " salva, preparando para salvar Prefêrencias e Status!");
			getStatus().save();
			getPreferences().save();
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar conta de " + getNickName() + "!", e.getLocalizedMessage());
		}
	}
	
	public void loadStatus() { 
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.STATUS_SELECT.getQuery());
			stmt.setString(1, getUniqueId().toString());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) { 
				setStatus(new Status(getUniqueId(), rs.getDouble("kills"), rs.getDouble("deaths"), (rs.getDouble("kills") / rs.getDouble("deaths"))));
			}
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar status de " + getNickName() + "!", e.getLocalizedMessage());
		}
	}
	
	public void loadPreferences() { 
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.PREFERENCES_SELECT.getQuery());
			stmt.setString(1, getUniqueId().toString());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) { 
				setPreferences(new Preferences(getUniqueId(), rs.getBoolean("adminmode"), rs.getBoolean("staffchat"), rs.getBoolean("receivetell"), rs.getBoolean("receivedonate"), rs.getBoolean("receivereports"), rs.getBoolean("receivewarns"), rs.getBoolean("fly"), rs.getBoolean("scoreboard"), rs.getBoolean("requestsclan"), rs.getBoolean("requeststpa")));
			}
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao carregar preferências de " + getNickName() + "!", e.getLocalizedMessage());
		}
	}
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.ACCOUNT_SELECT.getQuery());
			stmt.setString(1, getUniqueId().toString());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean hasEconomy(TypeCoin typeCoin, double amount) { 
		if(typeCoin.equals(TypeCoin.REAL)) { 
			return getReal() >= amount;			
		} else if(typeCoin.equals(TypeCoin.CASH)) { 
			return getCash() >= amount;
		} else if(typeCoin.equals(TypeCoin.REPUTACAO)) { 
			return getReputation() >= amount;
		}
		return false;
	}
	
	public double get(TypeCoin typeCoin) { 
		if(typeCoin.equals(TypeCoin.REAL)) { 
			return getReal();		
		} else if(typeCoin.equals(TypeCoin.CASH)) { 
			return getCash();
		} else if(typeCoin.equals(TypeCoin.REPUTACAO)) { 
			return getReputation();
		}
		return 0.0;
	}

	public void remove(TypeCoin typeCoin, double amount) {
		if(typeCoin.equals(TypeCoin.REAL)) { 
			this.real = this.real < amount ? 0 : this.real - amount;
		} else if(typeCoin.equals(TypeCoin.CASH)) { 
			this.cash = this.cash < amount ? 0 : this.cash - amount;
		} else if(typeCoin.equals(TypeCoin.REPUTACAO)) { 
			this.reputation = this.reputation < amount ? 0 : this.reputation - amount;
		}
	}

	public void add(TypeCoin typeCoin, double amount) {
		if(typeCoin.equals(TypeCoin.REAL)) { 
			this.real += amount;
		} else if(typeCoin.equals(TypeCoin.CASH)) { 
			this.cash += amount;
		} else if(typeCoin.equals(TypeCoin.REPUTACAO)) { 
			this.reputation += amount;
		}
	}
	
	public void sendMessage(boolean skipLine, String... messages) {
		if(getPlayer() == null) return;
		if(skipLine)
			getPlayer().sendMessage("");
		for(String message : messages)
			getPlayer().sendMessage(message);
		if(skipLine)
			getPlayer().sendMessage("");
	}

	public boolean hasPermission(String permission) {
		boolean has = false;
		if(GroupManager.getInstance().get(getGroupName()) != null && GroupManager.getInstance().get(getGroupName()).hasPermission(permission)) { 
			has = true;
		} else if(getPlayer() != null && getPlayer().isOp()) { 
			has = true;
		} else { 
			for(PermissionsCase p : getPermissions()) { 
				if(p.getPermission().equalsIgnoreCase(permission)) { 
					if(p.getTime() == -1) { 
						has = true;
					} else if(p.getTime() > System.currentTimeMillis()) { 
						has = true;
					}
				}
			}
		}
		return has;
	}
	
	public boolean isGroupPermanent() { 
		return getTimeGroup() == -1;
	}
	
	public boolean hasLastGroup() { 
		return !getLastGroupName().equals("NRE");
	}
	
	public boolean hasOtherTag() { 
		return !getTagUsing().equals("NRE");
	}
	
	public boolean allowUp() {
		boolean has = false;
		for(Requeriments r : RankManager.getInstance().get(getRankName()).nextRank().getRequirements()) { 
			if(get(r.getTypeCoin()) >= r.getValue()) { 
				has = true;
				Main.debug("Player > " + getNickName() + " has " + get(r.getTypeCoin()) + ", Price > " + r.getTypeCoin() + "(" + r.getValue() + ")");
			} else { 
				has = false;
			}
		}
		return has;
	}
	
	public void updatePrefix() { 
		if(isOnline()) { 
			Group group = GroupManager.getInstance().get(getGroupName());
			if(group != null) {
				Tag tag = TagManager.getInstance().get(getTagUsing());
				if(tag == null && hasOtherTag()) return;
				Clan clan = null;
				if(hasClan()) { 
					clan = ClanManager.getInstance().get(getClanName());
					if(clan == null) return;
				}
				TagUpdate.getInstance().setTag(getNickName(), (!hasOtherTag() ? (group.getPrefix().length() == 2 ? group.getPrefix() : group.getPrefix() + " ") : (tag.getPrefix().length() == 2 ? tag.getPrefix() : tag.getPrefix() + " ")), (clan != null ? " §7[" + clan.getTag() + "]" : ""), group.getPriority());
			}
		}
	}
	
	public boolean isInventoryEmpty() { 
		for(ItemStack is : getPlayer().getInventory().getContents()) { 
			if(is != null) { 
				return false;
			}
		}
		return true;
	}
	
	public int slotsFree() { 
		int slotsFree = 0;
		for(ItemStack is : getPlayer().getInventory().getContents()) { 
			if(is == null) { 
				slotsFree +=1;
			}
		}
		return slotsFree;
	}
	
	public boolean usingTagGroup() { 
		return getTagUsing().equalsIgnoreCase("NRE");
	}
	
	public void updateFly() { 
		if(getWarp() != null) { 
			getPlayer().setAllowFlight(!getWarp().isFly() ? false : true);
			getPlayer().setFlying(!getWarp().isFly() ? false : true);
		}
	}
	
	public boolean inWarp(Warp warp) { 
		return getWarp() != null && getWarp().getName().equals(warp.getName());
	}
	
	public boolean inSomeWarp() {
		return getWarp() != null;
	}

	public void startTeleport() {
		teleportWarp = new BukkitRunnable() {
			
			int timeWaiting = hasPermission(Permissions.VANTAGE_WARP.getPermission()) ? 3 : 5;
			
			@Override
			public void run() {
				if(timeWaiting != 0) { 
					if(!AccountManager.getLastWarp().containsKey(getUniqueId())) {
						cancel();
					}
				} else { 
					if(AccountManager.getLastWarp().containsKey(getUniqueId())) { 
						updateFly();
						getWarp().teleport(getPlayer());
						AccountManager.getLastWarp().remove(getUniqueId());
						sendMessage(false, "§aTeleportado!");
					}
				}
				Main.debug("Second > " + timeWaiting);
				timeWaiting--;
			}
		}.runTaskTimer(Main.getPlugin(), 20L, 20L);
	}
	
	public void teleportSpawn() { 
		if(WarpManager.getInstance().existsSpawn()) { 
			if(hasPermission(Permissions.SPAWN_VIP.getPermission())) { 
				if(WarpManager.getInstance().existsSpawnVip()) { 
					setWarp(WarpManager.getInstance().get("Spawnvip"));
					WarpManager.getInstance().get("Spawnvip").teleport(getPlayer());
					return;
				}
			}
			setWarp(WarpManager.getInstance().get("Spawn"));
			WarpManager.getInstance().get("Spawn").teleport(getPlayer());
		}
	}

	public List<Tag> getTagsReal() {
		List<Tag> tags = new ArrayList<>();
		for(String t : getTags()) { 
			if(TagManager.getInstance().get(t) != null) { 
				tags.add(TagManager.getInstance().get(t));
			}
		}
		return tags;
	}

	public boolean hasClan() {
		return !getClanName().equals("NRE");
	}
	
	public boolean hasGroupClan(ClanGroup clanGroup) { 
		for(ClanMember c : ClanManager.getInstance().get(getClanName()).getMembers()) { 
			if(c.getName().equals(getNickName())) { 
				return c.getClanGroup().getId() >= clanGroup.getId();
			}
		}
		return false;
	}
	
	public void add(String permission, long time) {
		getPermissions().add(new PermissionsCase(permission, time));
	}

	public void remove(String permission) {
		for(PermissionsCase p : getPermissions()) { 
			if(p.getPermission().equals(permission)) { 
				getPermissions().remove(p);
			}
		}
	}
}
