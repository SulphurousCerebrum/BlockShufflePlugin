package com.sulphurouscerebrum.plugins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.logging.Logger;

public class BlockShuffleTask extends BukkitRunnable {
    Logger logger;
    boolean hasRoundEnded;
    Main plugin;
    BlockShuffleTaskHelper helper;
    int currentRoundTime;
    int currentRound;
    int successfulPlayers;
    int counter;
    SendTitle titleSender;

    public BlockShuffleTask(Main plugin){
        this.logger = Bukkit.getLogger();
        this.plugin = plugin;
        this.hasRoundEnded = true;
        this.currentRoundTime = 0;
        this.currentRound = 0;
        this.successfulPlayers = 0;
        this.counter = 100;
        this.titleSender = new SendTitle();
        this.helper = new BlockShuffleTaskHelper(this.plugin, this.currentRound);
    }

    @Override
    public void run() {

        if(counter > 0) {
            if(counter % 20 == 0) {
                for (BlockShufflePlayer player : this.plugin.params.getAvailablePlayers())
                    titleSender.sendTitle(player.player, 5, 10, 5, ChatColor.BLUE + "Game Starting", ChatColor.RED + "" + (counter / 20));
            }

            counter -= 10;
        }

        else {
            if (hasRoundEnded) {
                this.currentRound += 1;
                Bukkit.broadcastMessage("Starting Round : " + ChatColor.BOLD + "" + this.currentRound);
                this.currentRoundTime = 0;
                this.hasRoundEnded = false;
                this.successfulPlayers = 0;
                helper.startRound(this.currentRound);
            } else {
                for (BlockShufflePlayer player : this.plugin.params.getAvailablePlayers()) {
                    if (!player.getHasFoundBlock()) {
                        boolean hasFound = helper.checkPlayer(player);
                        if (hasFound) this.successfulPlayers++;
                    }
                }

                int timeRemaining = this.plugin.params.getRoundTime() - this.currentRoundTime;

                if (this.successfulPlayers == this.plugin.params.getAvailablePlayers().size()) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Everyone Found their block!");
                    this.hasRoundEnded = true;
                } else if (timeRemaining <= 0) {
                    Bukkit.broadcastMessage("\nTime Up!");
                    this.hasRoundEnded = true;
                } else if (timeRemaining <= 200) {
                    if(timeRemaining % 20 == 0)
                        Bukkit.broadcastMessage(ChatColor.RED + "Time Remaining : " + ChatColor.BOLD + (timeRemaining / 20) + " seconds");
                    this.currentRoundTime += 10;
                } else {
                    this.currentRoundTime += 10;
                }

                if (this.hasRoundEnded && this.currentRound == this.plugin.params.getNoOfRounds()) {
                    this.cancel();
                }
            }
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        helper.endGame();
    }
}

class BlockShuffleTaskHelper {

    Main plugin;
    int currentRound;
    public BlockShuffleTaskHelper(Main plugin, int currentRound){
        this.plugin = plugin;
        this.currentRound = currentRound;
    }

    public void startRound(int currentRound){
        this.currentRound = currentRound;
        for(BlockShufflePlayer player : this.plugin.params.getAvailablePlayers()){
            player.setHasFoundBlock(false);
            player.setBlockToBeFound(getRandomBlock());
            Bukkit.getLogger().info("Assigned " + player.getName() + " with " + player.getBlockToBeFound());
            createBoard(player);
        }
    }

    public Material getRandomBlock(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(this.plugin.params.getAvailableBlocks().size());
        return this.plugin.params.getAvailableBlocks().get(randomNumber);
    }

    public void createBoard(BlockShufflePlayer player) {
        Bukkit.getLogger().info("Creating Scoreboard for " + player.getName());
        Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        Objective obj = scoreboard.registerNewObjective("BSObjective", "dummy", "Block Shuffle");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score s1 = obj.getScore("ROUND : " + this.currentRound + "/" + this.plugin.params.getNoOfRounds());
        s1.setScore(5);
        Score s2 = obj.getScore("");
        s2.setScore(4);
        Score s3 = obj.getScore("YOUR SCORE : " + player.getScore());
        s3.setScore(3);
        Score s4 = obj.getScore("");
        s4.setScore(2);
        Score s5 = obj.getScore("YOUR BLOCK : " + player.getBlockToBeFound());
        s5.setScore(1);

        player.player.setScoreboard(scoreboard);
    }

    public boolean checkPlayer(BlockShufflePlayer player) {
        Material standingOn = Objects.requireNonNull(Bukkit.getPlayer(player.getName())).getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
        if(standingOn.equals(player.getBlockToBeFound())) {
            player.setHasFoundBlock(true);
            player.setScore(player.getScore() + 1);
            broadcastSound(Sound.BLOCK_END_PORTAL_SPAWN);
            return true;
        }

        return false;
    }

    public void broadcastSound(Sound sound) {
        for(BlockShufflePlayer player : this.plugin.params.getAvailablePlayers()){
            player.player.playSound(player.player.getLocation(), sound, 1.0f, 1.0f);
        }
    }

    public void endGame(){
        SendTitle titleSender = new SendTitle();
        Bukkit.broadcastMessage("\nScores : \n");
        broadcastSound(Sound.UI_TOAST_CHALLENGE_COMPLETE);
        TreeMap<Integer, String> scores = new TreeMap<>(Collections.reverseOrder());

        for(BlockShufflePlayer player : this.plugin.params.getAvailablePlayers()) {
            Player ply = Bukkit.getPlayer(player.getName());
            scores.put(player.getScore(), player.getName());
            ply.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            titleSender.sendTitle(ply, 10, 30, 10, ChatColor.RED + "" + "Game Over", "");
        }

        Iterator iterator = scores.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            String message = mapEntry.getValue() + " : " + mapEntry.getKey();
            Bukkit.broadcastMessage(message);
        }
        this.plugin.params.setGameRunning(false);
    }
}
