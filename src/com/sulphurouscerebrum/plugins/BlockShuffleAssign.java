package com.sulphurouscerebrum.plugins;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BlockShuffleAssign {
	
	Main plugin;
	
	public BlockShuffleAssign(Main plugin) {
		this.plugin = plugin;
	}
	
	public void assignBlocks() {
		
		for(BlockShufflePlayer player : plugin.players) {
			player.setCompleted(false);
			Material assignedBlock = getRandomBlock();
			player.setBlock(assignedBlock);
			Player play = Bukkit.getPlayer(player.name);
			play.sendMessage("Your block is : " + assignedBlock.name());
		}
	}
	
	public Material getRandomBlock() {
		
		Material assignedBlock = null;
		Random rand = new Random();
		
//		Generate random number and get it from list
		while(assignedBlock == null) {
			int randomNumber = rand.nextInt(plugin.availableBlocks.size());
			Material m = Material.getMaterial(plugin.availableBlocks.get(randomNumber));
			assignedBlock = m;
		}
		return assignedBlock;
	}
	
	
//	For weighted games. Under development
	
//	@SuppressWarnings("rawtypes")
//	public Material getRandomBlock() {
//		
//		Material assignedBlock = null;
//		Random rand = new Random();
//		int weight;
//		String blockName;
//		
////		Iterate through the HashMap and find the first element which has greater weight than chosen randomNumber.
////		If nothing selected, repeat.
//		while(assignedBlock == null) {
//			int randomNumber = rand.nextInt(plugin.weightedSum);
//						
//			for(Map.Entry block : plugin.availableBlocks.entrySet()) {
//				weight = (int) block.getValue();
//				if(weight > randomNumber) {
//					blockName = (String) block.getKey();
//					assignedBlock = Material.getMaterial(blockName);
//					break;
//				}
//			}
//		}
//		return assignedBlock;
//	}
}
