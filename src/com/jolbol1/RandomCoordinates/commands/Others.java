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

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by James on 04/07/2016.
 */
public class Others implements CommandInterface {

    private final Coordinates coordinates = new Coordinates();
    private final MessageManager messages = new MessageManager();
    private int key = 574272099;

    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        //Is the command being run All?, Do they have the permission?
        if(args.length >= 1 && args[0].equalsIgnoreCase("player") && (sender.hasPermission("Random.Admin.Others") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*"))) {

            String toWorldName = null;
            int max = key;
            int min = key;
            Player p = null;
            if(args.length == 1) {
                messages.incorrectUsage(sender, "/RC player {Player} {World} {Max} {Min}");
                return;
            } else if(args.length > 1) {
                for(String s : args) {
                    if (!s.equalsIgnoreCase("player")) {
                        if (s instanceof String) {
                            if(allPlayers(sender).contains(Bukkit.getPlayer(s))) {
                                p = Bukkit.getPlayer(s);

                            } else if(s.contains("player:")) {
                                String name = s.replace("player:", "");
                                if(allPlayers(sender).contains(Bukkit.getPlayer(name))) {
                                    p = Bukkit.getPlayer(name);
                                } else {
                                    messages.playerNotExist(sender);
                                    return;
                                }
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
                            } else if (p != null) {
                                if(toWorldName == null) {
                                    toWorldName = s;
                                }


                            } else {
                                if(s.contains(":")) {
                                    messages.incorrectUsage(sender, "/RC All {World}/{Max} or use flags!");
                                    return;
                                }

                            }
                        }
                    }
                }



                if(p == null) {
                    messages.playerNotExist(sender);
                    return;
                }

                if(toWorldName != null && !doesWorldExist(sender, toWorldName)) {
                    messages.invalidWorld(sender, toWorldName);
                    return;
                }

                if(toWorldName == null) {
                    toWorldName = p.getWorld().getName();
                }

                if((min > max && max != key && min != key)) {
                    messages.minTooLarge(sender);
                    return;
                } else if(max != key &&  min == key) {
                    int minimum = coordinates.getMin(Bukkit.getWorld(toWorldName));
                    if(max > minimum) {
                        messages.minTooLarge(sender);
                        return;
                    }
                } else if(max == key &&  min != key) {
                    int maximum = coordinates.getMax(Bukkit.getWorld(toWorldName));
                    if(min > maximum) {
                        messages.minTooLarge(sender);
                        return;
                    }
                }

                messages.teleportedBy(sender, p);
                messages.teleportedPlayer(sender, p);
                coordinates.finalCoordinates(p, max, min, Bukkit.getWorld(toWorldName), CoordType.PLAYER, 0);
                return;
            }










        }
    }

    public boolean isWorldBanned(World world) {
        if(RandomCoords.getPlugin().config.getStringList("BannedWorlds").contains(world.getName())) {
            return false;
        }
        return true;
    }

    public boolean doesWorldExist(CommandSender sender, String worldName) {


        if(Bukkit.getWorld(worldName) == null) {
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

    public Collection<Player> allPlayers(CommandSender sender) {
        Collection<Player> players = new ArrayList<>();
        for(Player p: Bukkit.getOnlinePlayers()) {
                players.add(p);

        }
        return players;
    }


}
