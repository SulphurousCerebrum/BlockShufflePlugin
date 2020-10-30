package com.sulphurouscerebrum.plugins;

import org.bukkit.Material;

public class BlockShufflePlayer {
	String name;
	boolean challengeCompleted;
	Material blockToBeFound;
	int score;
	
	public BlockShufflePlayer(String n) {
		name = n;
		challengeCompleted = false;
		blockToBeFound = null;
		score = 0;
	}
	
	public void setCompleted(boolean com) {
		challengeCompleted = com;
	}
	
	public boolean getCompleted() {
		return challengeCompleted;
	}
	
	public void setBlock(Material block) {
		blockToBeFound = block;
	}
	
	public Material getBlock() {
		return blockToBeFound;
	}
}
