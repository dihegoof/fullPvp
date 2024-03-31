package com.br.fullPvp.clans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ClanMember {
	
	String name;
	ClanGroup clanGroup;
	long joinIn;
	
}
