package com.br.fullPvp.accounts;

import com.br.fullPvp.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TypeCoin {
	
	REAL("Real(is)", "R$"),
	CASH("Cash(s)", "ⓒ"),
	REPUTACAO("Reputação", "ⓡ");
	
	String name, symbol;
	
	public static String formatExactFormatter(TypeCoin typeCoin, double amount) {
		if(typeCoin.equals(REAL)) { 
			return Utils.getInstance().formatMoney(amount);
		} else if(typeCoin.equals(CASH)) {
			return Utils.getInstance().formatCash(amount);
		} else if(typeCoin.equals(REPUTACAO)) {
			return Utils.getInstance().formatReputation(amount);
		}
		return "???";
	}
}
