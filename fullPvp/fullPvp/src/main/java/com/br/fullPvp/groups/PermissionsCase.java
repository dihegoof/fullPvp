package com.br.fullPvp.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PermissionsCase {
	
	String permission;
	long time;
	
	public boolean isPermanent() { 
		return time == -1;
	}
}
