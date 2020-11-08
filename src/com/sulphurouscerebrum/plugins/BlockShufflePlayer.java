package com.sulphurouscerebrum.plugins;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BlockShufflePlayer {
    Player player;
    int score;
    Material blockToBeFound;
    boolean hasFoundBlock;

    public BlockShufflePlayer(Player player){
        this.player = player;
        score = 0;
        blockToBeFound = null;
        hasFoundBlock = false;
    }

    public String getName(){
        return this.player.getName();
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public boolean getHasFoundBlock(){
        return this.hasFoundBlock;
    }

    public void setHasFoundBlock(boolean hasFoundBlock){
        this.hasFoundBlock = hasFoundBlock;
    }

    public Material getBlockToBeFound(){
        return blockToBeFound;
    }

    public void setBlockToBeFound(Material blockToBeFound) {
        this.blockToBeFound = blockToBeFound;
    }
}
