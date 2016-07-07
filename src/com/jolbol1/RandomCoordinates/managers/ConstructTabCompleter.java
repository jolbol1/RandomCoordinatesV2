package com.jolbol1.RandomCoordinates.managers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 05/07/2016.
 */
public class ConstructTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("set");
            list.add("player");
            list.add("portal");
            list.add("all");
            list.add("wand");
            list.add("warp");
            if (list == null) {
                List<String> empty = new ArrayList<>();
                list = empty;
            }

            List<String> newList = new ArrayList<>();

            for (String site : list) {
                newList.add(site);


            }


            return newList;
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("set")) {
                List<String> worlds = new ArrayList<>();
                for(World world : Bukkit.getServer().getWorlds()) {
                    worlds.add(world.getName());
                }
                return worlds;

            } else if(args[0].equalsIgnoreCase("player")) {
                List<String> players = new ArrayList<>();
                for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                    String name = p.getName();
                    players.add(name);
                }
                return players;
            } else if(args[0].equalsIgnoreCase("portal")) {
                List<String> options = new ArrayList<>();
                options.add("create");
                options.add("delete");
                return options;
            } else {
                return null;
            }
        } else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("set") && Bukkit.getWorld(args[1].toString()) != null) {
                List<String> options = new ArrayList<>();
                options.add("center");
                options.add("max");
                options.add("min");
                return options;
            }
        }  else {

            return null;
        }
        return null;
    }
}
