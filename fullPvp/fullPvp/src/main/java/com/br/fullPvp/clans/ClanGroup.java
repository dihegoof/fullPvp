package com.br.fullPvp.clans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ClanGroup {
	
	LEADDER(3, "Líder"),
	RECRUIT(2, "Recruta"),
	MEMBER(1, "Membro");
	
	int id;
	String name;

}
