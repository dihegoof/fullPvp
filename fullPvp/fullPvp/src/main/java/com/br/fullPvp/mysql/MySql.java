package com.br.fullPvp.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.br.fullPvp.Main;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MySql {
	
	String address, user, password, database;
	Connection conn;

	public MySql(String address, String user, String password, String database) {
		this.address = address;
		this.user = user;
		this.password = password;
		this.database = database;
	}
	
	public void open() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + getAddress() + ":3306/" + getDatabase(), getUser(), getPassword());
			if(conn == null) {
				this.conn = connection;
				Main.debug("Conectado com sucesso!");
			}
		} catch (Exception e) {
			Main.debug("Erro de conexão!");
		}
	}
	
	public void close() { 
		try {
			if(conn != null) {
				this.conn.close();
				Main.debug("Desconectado com sucesso!");
			}
		} catch (Exception e) {
			Main.debug("Erro de conexão!");
		}
	}
	
	public void createTable() { 
		List<String> querys = Arrays.asList(
				SqlQuerys.ACCOUNT_CREATE_TABLE.getQuery(), 
				SqlQuerys.GROUP_CREATE_TABLE.getQuery(), 
				SqlQuerys.STATUS_CREATE_TABLE.getQuery(), 
				SqlQuerys.PREFERENCES_CREATE_TABLE.getQuery(), 
				SqlQuerys.RANK_CREATE_TABLE.getQuery(), 
				SqlQuerys.LINK_CREATE_TABLE.getQuery(),
				SqlQuerys.WARP_CREATE_TABLE.getQuery(), 
				SqlQuerys.TAG_CREATE_TABLE.getQuery(),
				SqlQuerys.PREFERENCES_SERVER_CREATE_TABLE.getQuery(),
				SqlQuerys.CLAN_CREATE_TABLE.getQuery(),
				SqlQuerys.CLAN_STATUS_CREATE_TABLE.getQuery());
		try {
			PreparedStatement stmt = null;
			for(String names : querys) { 
				stmt = Main.getMySql().getConn().prepareStatement(names);
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			Main.debug("Ocorreu um erro ao criar as tabelas!");
		}
	}
}
