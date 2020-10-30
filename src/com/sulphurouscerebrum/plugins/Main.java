package com.sulphurouscerebrum.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.md_5.bungee.api.ChatColor;


public class Main extends JavaPlugin implements Listener{	
	
//	Check if game is running
	public boolean isRunning = false;
	
//	List of active players
	public List<BlockShufflePlayer> players;
	
//	Get all the available blocks and store it in a HashMap
	public List<String> availableBlocks;
	
	
//	Hold round number, round time and current round time in ticks
	int rounds;
	int roundTime;
	int currentRound;
	int currentRoundTime;
	
//	Hold if round has ended
	boolean roundHasEnded;
	
//	Hold amount of food to be given in the beginning
	int foodToBeGiven;
	
	public void onEnable() {
		
//		Set Command Handler and Tab Handler
		getCommand("blockshuffle").setExecutor(new BlockShuffleCommands(this));
		getCommand("blockshuffle").setTabCompleter(new BlockShuffleTabCompleter());
		
		getServer().getPluginManager().registerEvents(this,  this);
		
		saveDefaultConfig();
		
//		Get info from the config file
		loadConfigFile();
		
//		Initialise Assignment Class
		BlockShuffleAssign assignClass = new BlockShuffleAssign(this);
		
		players = new ArrayList<BlockShufflePlayer>();
		roundHasEnded = true;
		currentRoundTime = 0;
		currentRound = 0;
		
//		Runs every 10 ticks with 0 tick initial delay
		BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if(isRunning) {
                	
//                	If round not going on, assign a new block to all players
                	if(roundHasEnded) {
                		currentRound++;
                		Bukkit.broadcastMessage(ChatColor.BOLD + "" + "\nROUND : " + currentRound);
                		assignClass.assignBlocks();
                		roundHasEnded = false;
                	}
                	else {
//                		To check how many players found their block
                		int playersCompleted = 0;
                		
//                		Iterate through all players and do checks
                		for(BlockShufflePlayer player : players) {
                			
//                			If player is done with their block, continue
                			if(player.challengeCompleted) {
                				++playersCompleted;
                				continue;
                			}
               			
//                			Get the block on which the Player is standing on
                			if(Bukkit.getPlayer(player.name).getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(player.blockToBeFound)){
                				player.challengeCompleted = true;
                				String toBeDisplayed = ChatColor.GREEN + "" + ChatColor.BOLD + "" + player.name;
                				toBeDisplayed = toBeDisplayed + "" + ChatColor.GREEN + "" + " found their block";
                				Bukkit.broadcastMessage(toBeDisplayed);
                				player.score += 1;
                				++playersCompleted;
                			}
                			
                		}
                		
//                		All players found their block
                		if(playersCompleted == players.size()) {
                			Bukkit.broadcastMessage("\nEveryone Found their Block!");
                			roundHasEnded = true;
                			currentRoundTime = 0;
                			if(currentRound == rounds) {
                				displayScoreboard();
                				isRunning = false;
                				currentRound = 0;
                			}
                		}
                		
                		if(currentRoundTime < roundTime) {              			
//                			Start alerts when 10 seconds left
                			if(currentRoundTime >= roundTime - 200) {
                				if((roundTime - currentRoundTime) % 20 == 0) {
                					Bukkit.broadcastMessage(ChatColor.RED + "" + "Countdown : " + ChatColor.BOLD + "" + (roundTime - currentRoundTime) / 20);
                				}
                			}
                			
//                			Add 10 ticks
                			currentRoundTime += 10;
                		}
                		else {
                			Bukkit.broadcastMessage(ChatColor.BOLD + "" + "Time Up!");
                			
//                			Add respective scores
                			for(BlockShufflePlayer player : players) {	
                				if(!player.challengeCompleted) {
                					Bukkit.getPlayer(player.name).sendMessage(ChatColor.RED + "You could not find your block!");
                				}
                			}
                			
//                			Check if game ended
                			roundHasEnded = true;
                			currentRoundTime = 0;
                			if(currentRound == rounds) {
                				displayScoreboard();
                				isRunning = false;
                				currentRound = 0;
                			}
                		}
                	}
                }
            }
        }, 0L, 10L);
	
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player ply = event.getPlayer();
		BlockShufflePlayer toBeRemoved = null;
		for(BlockShufflePlayer player : players) {
			if(player.name == ply.getName()) {
				toBeRemoved = player;
			}
		}
		
		if(toBeRemoved != null) {
			players.remove(toBeRemoved);
			Bukkit.broadcastMessage(ChatColor.RED + "" + ply.getName() + " left. F's in the chat");
			
			if(players.size() < 2) {
				Bukkit.broadcastMessage("\n" + ChatColor.RED + "Not enough players to continue the game. Stopping");
				players.clear();
				roundHasEnded = true;
				currentRound = 1;
				currentRoundTime = 0;
				isRunning = false;
			}
		}	
		
	}
	
	public void loadConfigFile() {
		availableBlocks = new ArrayList<String>();
		
		List<String> blocks = new ArrayList<String>();
		blocks = getConfig().getStringList("block-list"); 
		
//		Place everything in List
		for(String block : blocks) {
			if(checkMaterialValidity(block)) {
				availableBlocks.add(block);
				Bukkit.getLogger().info("Loaded " + block);
			}
			
			else {
				Bukkit.getLogger().info("Material " + block + " is not valid. Skipping");
			}
			
		}
		
		if(availableBlocks.isEmpty()) {
			Bukkit.getLogger().info("No blocks were added from the config.yml file. Game cannot start");
		}
		
		Bukkit.getLogger().info("Total of " + availableBlocks.size() + " blocks were added");
		
//		Load number of rounds and round time
		rounds = getConfig().getInt("parameters.rounds");
		roundTime = getConfig().getInt("parameters.roundTime");
		
		if(rounds < 1) {
			Bukkit.getLogger().info("Number of rounds cannot be less than 1. Defaulting to 1");
			rounds = 1;
		}
		
		if(roundTime < 200) {
			Bukkit.getLogger().info("Round time cannot be less than 10 seconds. Defaulting to 1 minute");
			roundTime = 1200;
		}
		
		Bukkit.getLogger().info("Number of rounds : " + rounds);
		Bukkit.getLogger().info("Round time : " + roundTime);
		
//		Get amount of food to be given
		foodToBeGiven = getConfig().getInt("parameters.giveFood");
		if(foodToBeGiven < 0) {
			Bukkit.getLogger().info("Invalid food amount. Defaulting to 16");
			foodToBeGiven = 16;
		}
//		If amount is more than maximum inventory space
		if(foodToBeGiven > 2304) {
			foodToBeGiven = 2304;
		}
		
		Bukkit.getLogger().info("Amount of food to be given : " + foodToBeGiven);
	}
	
	public boolean checkMaterialValidity(String material) {
		Material m = Material.getMaterial(material);
		
		if(m == null) return false;
		else return true;
	}
		
	public void displayScoreboard() {
		Bukkit.broadcastMessage(ChatColor.GOLD + "\n\nGame Over");
		Bukkit.broadcastMessage("\nHere are the results!");
		Bukkit.broadcastMessage("\n");
		
//		Create ScoreBoard in sorted order
		TreeMap<Integer, String> scores = new TreeMap<Integer, String>(Collections.reverseOrder());
		for(BlockShufflePlayer player : players) {
			scores.put(player.score, player.name);
		}
		
		for(Map.Entry<Integer, String> m : scores.entrySet()) {
			String name = (String) m.getValue();
			int score = (int) m.getKey();
			Bukkit.broadcastMessage(ChatColor.GREEN + "" + name + "      " + ChatColor.YELLOW + "" + score);
		}
		
		Bukkit.broadcastMessage("\nGG");
	}

	public void onDisable() {
		
	}
	
}
