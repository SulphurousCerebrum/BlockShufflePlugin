package com.sulphurouscerebrum.plugins;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class BlockShuffleParams {
    @SuppressWarnings("FieldCanBeLocal")
    private BukkitTask task;
    private int noOfRounds;
    private int roundTime;
    private int initialFoodAmount;
    private boolean isGameRunning;
    private List<Material> availableBlocks;
    @SuppressWarnings("FieldMayBeFinal")
    private List<BlockShufflePlayer> availablePlayers;

    public BlockShuffleParams(){
        noOfRounds = 5;
        roundTime = 6000;
        initialFoodAmount = 16;
        isGameRunning = false;
        availablePlayers = new ArrayList<>();
    }

    public int getNoOfRounds() {
        return this.noOfRounds;
    }

    public void setNoOfRounds(int noOfRounds){
        this.noOfRounds = noOfRounds;
    }

    public int getRoundTime(){
        return this.roundTime;
    }

    public void setRoundTime(int roundTime){
        this.roundTime = roundTime;
    }

    public int getInitialFoodAmount(){
        return this.initialFoodAmount;
    }

    public void setInitialFoodAmount(int initialFoodAmount){
        this.initialFoodAmount = initialFoodAmount;
    }

    public List<Material> getAvailableBlocks(){
        return this.availableBlocks;
    }

    public void setAvailableBlocks(List<Material> availableBlocks){
        this.availableBlocks = availableBlocks;
    }

    public void setTask(BukkitTask task){
        this.task = task;
    }

    public BukkitTask getTask(){
        return this.task;
    }

    public boolean getIsGameRunning(){
        return this.isGameRunning;
    }

    public void setGameRunning(boolean isGameRunning){
        this.isGameRunning = isGameRunning;
    }

    public List<BlockShufflePlayer> getAvailablePlayers(){
        return this.availablePlayers;
    }

    public boolean addAvailablePlayer(String playerString) {
        for(BlockShufflePlayer player : availablePlayers){
            if(player.getName().equalsIgnoreCase(playerString)) {
                return false;
            }
        }
        BlockShufflePlayer player = new BlockShufflePlayer(Bukkit.getPlayer(playerString));
        availablePlayers.add(player);
        return true;
    }

    public boolean removeAvailablePlayer(String playerString){
        int indexToBeRemoved = -1;
        for(BlockShufflePlayer player : availablePlayers) {
            if(player.getName().equalsIgnoreCase(playerString)) {
                indexToBeRemoved = availablePlayers.indexOf(player);
                break;
            }
        }

        if(indexToBeRemoved >= 0) {
            availablePlayers.remove(indexToBeRemoved);
            return true;
        }
        else return false;
    }
}

