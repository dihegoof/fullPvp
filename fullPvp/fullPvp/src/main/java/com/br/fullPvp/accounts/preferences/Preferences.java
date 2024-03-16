package com.br.fullPvp.accounts.preferences;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import com.br.fullPvp.Main;
import com.br.fullPvp.mysql.SqlQuerys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Preferences {
	
	UUID uniqueId;
	boolean adminMode, staffChat, receiveTell, receiveDonate, receiveReports, receiveWarns, fly, scoreBoard, requestsClan, requestsTpa;

	public Preferences(UUID uniqueId) { 
		this.uniqueId = uniqueId;
		this.adminMode = false;
		this.staffChat = true;
		this.receiveTell = true;
		this.receiveDonate = true;
		this.receiveReports = true;
		this.receiveWarns = true;
		this.fly = false;
		this.scoreBoard = true;
		this.requestsClan = true;
		this.requestsTpa = true;		
	}
	
	public void save() { 
		try {
			PreparedStatement stmt = null;
			if(exists()) { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.PREFERENCES_UPDATE.getQuery());
				stmt.setBoolean(1, isAdminMode());
				stmt.setBoolean(2, isStaffChat());
				stmt.setBoolean(3, isReceiveTell());
				stmt.setBoolean(4, isReceiveDonate());
				stmt.setBoolean(5, isReceiveReports());
				stmt.setBoolean(6, isReceiveWarns());
				stmt.setBoolean(7, isFly());
				stmt.setBoolean(8, isScoreBoard());
				stmt.setBoolean(9, isRequestsClan());
				stmt.setBoolean(10, isRequestsTpa());
				stmt.setString(11, getUniqueId().toString());
			} else { 
				stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.PREFERENCES_INSERT.getQuery());
				stmt.setString(1, getUniqueId().toString());
				stmt.setBoolean(2, isAdminMode());
				stmt.setBoolean(3, isStaffChat());
				stmt.setBoolean(4, isReceiveTell());
				stmt.setBoolean(5, isReceiveDonate());
				stmt.setBoolean(6, isReceiveReports());
				stmt.setBoolean(7, isReceiveWarns());
				stmt.setBoolean(8, isFly());
				stmt.setBoolean(9, isScoreBoard());
				stmt.setBoolean(10, isRequestsClan());
				stmt.setBoolean(11, isRequestsTpa());
			}
			stmt.executeUpdate();
			Main.debug("Prefêrencias de " + getUniqueId() + " salva!");
		} catch (Exception e) {
			Main.debug("Ocorreu um erro ao salvar prefêrencias de " + getUniqueId() + "!", e.getLocalizedMessage());
		}
	}
	
	public boolean exists() {
		try {
			PreparedStatement stmt = Main.getMySql().getConn().prepareStatement(SqlQuerys.PREFERENCES_SELECT.getQuery());
			stmt.setString(1, getUniqueId().toString());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
}
