package com.sulphurouscerebrum.plugins;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class BlockShuffleTabCompleter implements TabCompleter {

    Main plugin;
    public BlockShuffleTabCompleter(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        List<String> list = new ArrayList<>();

        if(args.length == 1) {
            list.add("start");
            list.add("stop");
            list.add("info");
            list.add("add");
            list.add("remove");
            list.add("list");
            list.add("set");

            List<String> suggestions = new ArrayList<>();
            for(String a : list) {
                if(a.toLowerCase().startsWith(args[0].toLowerCase())){
                    suggestions.add(a);
                }
            }

            return suggestions;
        }

        else if(args.length == 2) {

            List<String> suggestions = new ArrayList<>();

            if(args[0].equalsIgnoreCase("add")) {
                for(Player p : Bukkit.getOnlinePlayers()){
                    list.add(p.getName());
                }

                for(String a : list) {
                    if(a.toLowerCase().startsWith((args[1].toLowerCase()))) suggestions.add(a);
                }
                return suggestions;
            }

            else if(args[0].equalsIgnoreCase("remove")) {
                for(BlockShufflePlayer p : this.plugin.params.getAvailablePlayers()){
                    list.add(p.getName());
                }

                for(String a : list) {
                    if(a.toLowerCase().startsWith((args[1].toLowerCase()))) suggestions.add(a);
                }
                return suggestions;
            }

            else if(args[0].equalsIgnoreCase(("set"))) {
                list.add("noOfRounds");
                list.add("roundTime");
                list.add("foodAmount");

                for(String a : list) {
                    if(a.toLowerCase().startsWith(args[1].toLowerCase())) suggestions.add(a);
                }

                return suggestions;
            }

        }
        return null;
    }
}
