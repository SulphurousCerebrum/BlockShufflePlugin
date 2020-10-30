package com.sulphurouscerebrum.plugins;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlockShuffleCommands implements CommandExecutor{

	public Main plugin;
	
	public BlockShuffleCommands(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length <=0) {
			return false;
		}
		
		if(label.equalsIgnoreCase("blockshuffle")) {	
			
			if(args[0].equalsIgnoreCase("start")) {
				
//				Check if game is already running and a dumbass is trying to start it again
				if(plugin.isRunning) {
					sender.sendMessage("Game is already running. First stop current session!");
					return true;
				}
				
//				Check if there are any available blocks from config.yml file
				if(plugin.availableBlocks.isEmpty()) {
					sender.sendMessage("No valid blocks were found in config.yml file. Game cannot start");
					return true;
				}
				
//				Start the game
				boolean didStart = start(plugin);
				if(!didStart) {
					sender.sendMessage("Not enough players to start the game (Minimum 2)");
					sender.sendMessage("Check if all players are in survival mode");
					return true;
				}
				
				Bukkit.getServer().broadcastMessage("Added these players successfully: ");
				for(BlockShufflePlayer player : plugin.players) {
					Bukkit.getServer().broadcastMessage(player.name);
				}
				
				Bukkit.getServer().broadcastMessage("\nStarting Game ...\n");
				
				return true;
			}
			
			if(args[0].equalsIgnoreCase("stop")) {
				
//				Check if dumbass is OP
				if(!plugin.isRunning) {
					sender.sendMessage("No current games running");
					return true;
				}
				stop(plugin);
				sender.sendMessage("Game Stopped");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("info")) {
				sender.sendMessage("Number of rounds : " + plugin.rounds);
				sender.sendMessage("Time each round (in ticks): " + plugin.roundTime);
				sender.sendMessage("Amount of food to be given : " + plugin.foodToBeGiven);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("setrounds")) {
				if(plugin.isRunning) {
					sender.sendMessage("You can't do that now. A game is currently running!");
				}
				else {
					try {
						int noOfRounds = Integer.parseInt(args[1]);
						if(noOfRounds < 1) {
							sender.sendMessage("Number of rounds cannot be less than 1. Defaulting to 1");
							noOfRounds = 1;
						}
						plugin.rounds = noOfRounds;
						sender.sendMessage("Set Number of rounds to : " + noOfRounds);
					}
					catch(Exception e) {
						return false;
					}
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("setroundtime")) {
				if(plugin.isRunning) {
					sender.sendMessage("You can't do that now. A game is currently running!");
				}
				else {
					try {
						int roundTime = Integer.parseInt(args[1]);
						if(roundTime <= 200) {
							sender.sendMessage("Round has to last more than 200 ticks (10 seconds). Defaulting to 1200 (60 seconds");
							roundTime = 1200;
						}
						plugin.roundTime = roundTime;
						sender.sendMessage("Set Round Time to : " + roundTime);
					}
					catch(Exception e) {
						return false;
					}
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("setfoodamount")) {
				if(plugin.isRunning) {
					sender.sendMessage("You can't do that now. A game is currently running!");
				}
				else {
					try {
						int foodAmount = Integer.parseInt(args[1]);
						if(foodAmount < 0) {
							sender.sendMessage("Food amount cannot be less than 0. Defaulting to 16");
							foodAmount = 16;
						}
						if(foodAmount > 2304) {
							sender.sendMessage("Food amount exceeds maximum possible amount. Defaulting to 2304");
							foodAmount = 2304;
						}
						plugin.foodToBeGiven = foodAmount;
						sender.sendMessage("Set Food amount to : " + foodAmount);
					}
					catch(Exception e) {
						return false;
					}
				}
				return true;
			}
		}
		
		return false;
	}
	
	public boolean start(Main plugin) {
		
		plugin.players.clear();
		
//    	Add players who are in survival mode to the game
//		Also clear their inventories and check if to give food
//		Allows other players to spectate
		
    	for(Player survivalPlayers : Bukkit.getServer().getOnlinePlayers()) {
    		if(survivalPlayers.getGameMode().equals(GameMode.SURVIVAL)) {
    			BlockShufflePlayer player = new BlockShufflePlayer(survivalPlayers.getName());
    			plugin.players.add(player);
    			survivalPlayers.getInventory().clear();
    			survivalPlayers.getInventory().addItem(new ItemStack(Material.COOKED_PORKCHOP, plugin.foodToBeGiven));
    		}
    	}
    	
//    	Check for minimum players
    	if(plugin.players.size() < 2) {
    		return false;
    	}
    	
    	plugin.isRunning = true;
    	return true;
	}
	
	public void stop(Main plugin) {
		
//		Clear list of players
		plugin.players.clear();
		plugin.roundHasEnded = true;
		plugin.currentRound = 1;
		plugin.currentRoundTime = 0;
		plugin.isRunning = false;
		
	}
	
}
