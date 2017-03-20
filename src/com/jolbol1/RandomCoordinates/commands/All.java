package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import com.jolbol1.RandomCoordinates.managers.CoordType;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Created by James on 19/03/2017.
 */
public class All implements CommandInterface {

    private Coordinates coordinates = new Coordinates();
    private MessageManager messages = new MessageManager();
    private int key = 574272099;

    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        //Is the command being run All?, Do they have the permission?
        if(args.length >= 1 && args[0].equalsIgnoreCase("all") && (sender.hasPermission("Random.Admin.All") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*"))) {

            String toWorldName = null;
            String fromWorldName= null;
            int max = key;
            int min = key;
            if(args.length == 1) {
                teleportAll(sender, max, min, toWorldName, fromWorldName);
                return;
            } else if(args.length > 1) {
                for(String s : args) {
                    if (!s.equalsIgnoreCase("all")) {
                        if (s instanceof String) {
                            if (s.contains("from:")) {
                                fromWorldName = s.replace("from:", "");
                            } else if (s.contains("to:")) {
                                toWorldName = s.replace("to:", "");
                            } else if (s.contains("max:")) {
                                if (!canParseInteger(s.replace("max:", ""))) {
                                    break;
                                }
                                max = Integer.parseInt(s.replace("max:", ""));
                            } else if (s.contains("min:")) {
                                if (!canParseInteger(s.replace("min:", ""))) {
                                    break;
                                }
                                min = Integer.parseInt(s.replace("min:", ""));
                            } else if (canParseInteger(s)) {
                                if(max == key) {
                                    max = Integer.valueOf(s);
                                } else if(min == key) {
                                    min = Integer.valueOf(s);
                                }
                            } else if (doesWorldExist(sender, s)) {
                                if(toWorldName == null) {
                                    toWorldName = s;
                                } else
                                if(fromWorldName == null) {
                                    fromWorldName = s;
                                }
                            } else {
                                if(s.contains(":")) {
                                    messages.incorrectUsage(sender, "/RC All {World}/{Max} or use flags!");
                                } else {
                                    messages.invalidWorld(sender, s);
                                }
                                return;
                            }
                        }
                    }
                }
                messages.teleportedAll(sender, toWorldName);
                teleportAll(sender, max, min, toWorldName, fromWorldName);
                return;
            }










        }
    }

    public Collection<Player> playersInWorld(CommandSender sender, World world) {
        Collection<Player> players = new ArrayList<>();
        for(Player p: Bukkit.getOnlinePlayers()) {
            if(p.getWorld().equals(world) && !p.equals(sender)) {
                players.add(p);
            }
        }
        return players;
    }


    public Collection<Player> allPlayers(CommandSender sender) {
        Collection<Player> players = new ArrayList<>();
        for(Player p: Bukkit.getOnlinePlayers()) {
            if(!p.equals(sender)) {
                players.add(p);
            }
        }
        return players;
    }

    public boolean isWorldBanned(World world) {
        if(RandomCoords.getPlugin().config.getStringList("BannedWorlds").contains(world.getName())) {
            return false;
        }
        return true;
    }

    public boolean doesWorldExist(CommandSender sender, String worldName) {

        World w = Bukkit.getWorld(worldName);
        if(w == null) {
            return false;
        }
        return true;


    }

    public boolean canParseInteger(String maximum) {
        try {
            Integer.parseInt(maximum);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }




    public void teleportAll(CommandSender sender, int max, int min, String toWorldName, String fromWorldName) {

        if(toWorldName == null) {
            messages.teleportedAll(sender, "their world");
        } else {
            messages.teleportedAll(sender, toWorldName);
        }

        for(Player p: allPlayers(sender)) {

            World worldTo = p.getWorld();

            if(toWorldName != null) {
                if(doesWorldExist(sender, toWorldName)) {
                    worldTo = Bukkit.getWorld(toWorldName);
                } else {
                    messages.invalidWorld(sender, toWorldName);
                    return;
                }
            }

            if(!isWorldBanned(worldTo)) {
                messages.worldBanned(sender);
                return;
            }
            if(fromWorldName != null && !doesWorldExist(sender, fromWorldName)) {
                messages.invalidWorld(sender, fromWorldName);
                return;
            }

            if(fromWorldName == null || p.getWorld().equals(Bukkit.getWorld(fromWorldName))) {
                messages.teleportedBy(sender, p);
                coordinates.finalCoordinates(p, max, min, worldTo, CoordType.ALL, 0);
            }
        }


    }













}
