package com.br.fullPvp.mines;

import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;

public class PositionManager {
	
	@Getter
	static PositionManager instance = new PositionManager();
	@Getter
	static HashMap<UUID, Position> position = new HashMap<>();
	
	public void add(UUID uniqueId) { 
		if(!position.containsKey(uniqueId)) 
			position.put(uniqueId, new Position());
	}
	
	public void remove(UUID uniqueId) { 
		if(position.containsKey(uniqueId)) 
			position.remove(uniqueId);
	}
	
	public Position getPosition(UUID uniqueId) { 
		if(position.containsKey(uniqueId)) {
			return position.get(uniqueId);
		}
		return null;
	}
}
