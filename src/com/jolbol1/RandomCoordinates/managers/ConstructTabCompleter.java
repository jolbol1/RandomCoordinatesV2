package com.jolbol1.RandomCoordinates.managers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by James on 05/07/2016.
 */
public class ConstructTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final  String s, final String[] args) {
        if(args.length == 1) {
            final List<String> list = new ArrayList<>();
            list.add("set");
            list.add("player");
            list.add("portal");
            list.add("all");
            list.add("wand");
            list.add("warp");

            final List<String> newList = new ArrayList<>();

            newList.addAll(list);


            return newList;
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("set")) {
                final List<String> worlds = Bukkit.getServer().getWorlds().stream().map(World::getName).collect(Collectors.toList());
                return worlds;

            } else if(args[0].equalsIgnoreCase("player")) {
                final List<String> players = new ArrayList<>();
                for(final Player p : Bukkit.getServer().getOnlinePlayers()) {
                    final String name = p.getName();
                    players.add(name);
                }
                return players;
            } else if(args[0].equalsIgnoreCase("portal")) {
                final List<String> options = new ArrayList<>();
                options.add("create");
                options.add("delete");
                return options;
            } else {
                return null;
            }
        } else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("set") && Bukkit.getWorld(args[1]) != null) {
                final List<String> options = new ArrayList<>();
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
