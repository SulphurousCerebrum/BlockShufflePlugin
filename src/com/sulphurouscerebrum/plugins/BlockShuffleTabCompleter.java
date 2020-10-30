package com.sulphurouscerebrum.plugins;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class BlockShuffleTabCompleter implements TabCompleter {
	
	List<String> list = new ArrayList<String>();
	
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		
	   if(list.isEmpty()) {
		   list.add("start");
		   list.add("stop");
		   list.add("info");
		   list.add("setRounds");
		   list.add("setRoundTime");
		   list.add("setFoodAmount");
	   }
	   
	   List<String> suggestions = new ArrayList<String>();
	   if(args.length == 1) {
		   for(String a : list) {
			   if(a.toLowerCase().startsWith(args[0].toLowerCase())) {
				   suggestions.add(a);
			   }
		   }
		   return suggestions;
	   }
	    
	    return null;
	}
}