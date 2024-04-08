package com.br.fullPvp.mines;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;
import com.br.fullPvp.utils.SerializeLocation;
import com.br.fullPvp.utils.TimeManager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("deprecation")
@AllArgsConstructor
@Getter
@Setter
public class Mine {

	String name, timeToReset;
	long time;
	boolean enable, enableHolo;
	Location pos1, pos2, locHolo;
	List<Block> blocks;
	List<Composition> composition;
	
	public void save() { 
		try {
			PreparedStatement stmt = null;
			ArrayList<String> composition = new ArrayList<>();
			for(Composition c : getComposition()) { 
				composition.add(c.getId() + ";" + c.getDurability() + ";" + c.getPercent());
			}
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.MINE_UPDATE.getQuery());
				stmt.setString(1, getName());
				stmt.setString(2, getTimeToReset());
				stmt.setBoolean(3, isEnable());
				stmt.setBoolean(4, isEnableHolo());
				stmt.setString(5, SerializeLocation.getInstance().serializeLocation(getPos1(), false));
				stmt.setString(6, SerializeLocation.getInstance().serializeLocation(getPos2(), false));
				stmt.setString(7, isEnableHolo() ? SerializeLocation.getInstance().serializeLocation(getLocHolo(), true) : "world;0;0;0;0;0");
				stmt.setString(8, composition.isEmpty() ? "null" : composition.toString().replace("[", "").replace("]", ""));
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.MINE_INSERT.getQuery());
				stmt.setString(1, getTimeToReset());
				stmt.setBoolean(2, isEnable());
				stmt.setString(3, SerializeLocation.getInstance().serializeLocation(getPos1(), false));
				stmt.setString(4, SerializeLocation.getInstance().serializeLocation(getPos2(), false));
				stmt.setBoolean(5, isEnableHolo());
				stmt.setString(6, isEnableHolo() ? SerializeLocation.getInstance().serializeLocation(getLocHolo(), true) : "world;0;0;0;0;0");
				stmt.setString(7, composition.isEmpty() ? "null" : composition.toString().replace("[", "").replace("]", ""));
				stmt.setString(8, getName());
			}
			stmt.executeUpdate();
			Main.debug("Mina " + getName() + " salva!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar mina " + getName() + "!", e.getLocalizedMessage());
		}
	}	
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.MINE_SELECT.getQuery());
			stmt.setString(1, getName());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void delete() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.MINE_DELETE.getQuery());
			stmt.setString(1, getName());
			stmt.executeUpdate();
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao deletar mina " + getName() + "!", e.getLocalizedMessage());
		}
	}
	
	public boolean containsBlock(int idBlock, int durability) { 
		boolean has = false;
		for(Composition c : getComposition()) { 
			if(c.getId() == idBlock && c.getDurability() == durability) { 
				has = true;
			}
		}
		return has;
	}
	
	public boolean isComplete() { 
		boolean has = false;
		int total = 0;
		for(Composition c : getComposition()) { 
			total += c.getPercent();
		}
		if(total == 100) { 
			has = true;
		}
		return has;
	}
	
	public int remaing() { 
		int remaing = 100;
		for(Composition c : getComposition()) { 
			remaing -= c.getPercent();
		}
		return remaing;
	}

	public void add(ItemStack block, int percent) {
		getComposition().add(new Composition(block.getTypeId(), block.getDurability(), percent));
	}
	
	public void remove(ItemStack block) { 
		for(Composition c : getComposition()) { 
			if(c.getId() == block.getTypeId() && c.getDurability() == block.getDurability()) { 
				getComposition().remove(c);
			}
		}
	}

	public void reset() {
        int x1 = (int)Math.min(getPos1().getX(), getPos2().getX());
        int x2 = (int)Math.max(getPos1().getX(), getPos2().getX()) + 1;
        int y1 = (int)Math.min(getPos1().getY(), getPos2().getY());
        int y2 = (int)Math.max(getPos1().getY(), getPos2().getY()) + 1;
        int z1 = (int)Math.min(getPos1().getZ(), getPos2().getZ());
        int z2 = (int)Math.max(getPos1().getZ(), getPos2().getZ()) + 1;
        World world = getPos1().getWorld();
        Random random = new Random();
        List<Block> list = new ArrayList<>();
        for(int x = x1; x < x2; ++x) {
            for(int y = y1; y < y2; ++y) {
                for(int z = z1; z < z2; ++z) {
                    Location loc = new Location(world, (double)x, (double)y, (double)z);
                    Integer porce = random.nextInt(100);
                    int cumulativeProbability = 0, data = 0;
                    int materialId = 0;
                    for(Composition c : getComposition()) { 
                    	data = c.getId();
                    	int porcent = c.getPercent();
                    	cumulativeProbability += porcent;
                    	if(cumulativeProbability >= porce) { 
                    		materialId = c.getId();
                    		break;
                    	}
                    }
                    loc.getBlock().setTypeIdAndData(materialId, (byte) data, true);
                    list.add(loc.getBlock());
                }
            }
        }
    	long timeFuture = 0;
    	if(isEnable()) {
    		timeFuture = TimeManager.getInstance().getTime(getTimeToReset());
    	}
        this.time = timeFuture;
        this.blocks = list;
	}
}
