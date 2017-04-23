/*
 *     RandomCoords, Provding the best Bukkit Random Teleport Plugin
 *     Copyright (C) 2014  James Shopland
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.CoordinatesManager;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import com.jolbol1.RandomCoordinates.managers.Util.ArgMode;
import com.jolbol1.RandomCoordinates.managers.Util.CoordType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by James on 05/07/2016.
 */
public class WarpsNew implements CommandInterface {

    private final MessageManager messages = new MessageManager();
    private final Coordinates coordinates = new Coordinates();
    private final CoordinatesManager coordinatesManager = new CoordinatesManager();

    @Override
    public void onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if(args.length >= 1 && args[0].equalsIgnoreCase("warp")) {
            String mode = null;
            String name = null;
            String playerName = null;
            String worldName = null;
            Map modesUsed = processArguments(args);

            //Did they do /RC warp... Create?
            if(modesUsed.containsKey(ArgMode.CREATE)) {
                //Do they have the permission to create.
                if(sender.hasPermission("Random.Admin.Warp") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                    //Does modesUsed contain an unprocessed argument? (The name of the warp)
                    if(modesUsed.containsKey(ArgMode.INCORRECT)) {
                        //Make sure they are a player.
                        if(sender instanceof Player) {
                            //Create the warp.
                            createWarp((String) modesUsed.get(ArgMode.INCORRECT), (Player) sender, sender);
                            return;
                        } else {
                            messages.notPlayer(sender);
                            return;
                        }


                    } else {
                        messages.incorrectUsage(sender, "/RC warp create {warpName}");
                    }





                } else {
                    messages.noPermission(sender);
                    return;
                }
            }
            else if(modesUsed.containsKey(ArgMode.DELETE)) {
                if(sender.hasPermission("Random.Admin.Warp") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                    if(modesUsed.containsKey(ArgMode.INCORRECT)) {
                        deleteWarp((String) modesUsed.get(ArgMode.INCORRECT), sender);
                        return;
                    } else {
                        messages.incorrectUsage(sender, "/RC delete {warpName}");
                        return;
                    }




                } else {
                    messages.noPermission(sender);
                    return;
                }
            }
            else if(modesUsed.containsKey(ArgMode.LIST)) {
                if(sender.hasPermission("Random.Admin.Warp") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                    if(modesUsed.containsKey(ArgMode.WORLD)) {
                        listWarps(sender, (String) modesUsed.get(ArgMode.WORLD));
                        return;
                    } else {
                        listWarps(sender, null);
                        return;
                    }




                } else {
                    messages.noPermission(sender);
                }

            } else if(modesUsed.containsKey(ArgMode.WARP)) {
                if(!(sender instanceof Player)) {
                    messages.playerNotExist(sender);
                    return;
                }
                if(!sender.hasPermission("Random.Warps") && !sender.hasPermission("Random.Admin.*") && !sender.hasPermission("Random.*") && !sender.hasPermission("Random.Basic")) {
                    messages.noPermission(sender);
                    return;
                }
                Player p = (Player) sender;
                if(!RandomCoords.getPlugin().config.getString("Experimental").equalsIgnoreCase("true")) {
                    coordinates.finalCoordinates(p, 574272099, 574272099, p.getWorld(), CoordType.WARPS, 0);
                    return;
                } else {
                    coordinatesManager.randomlyTeleportPlayer(p, p.getWorld(), coordinatesManager.key, coordinatesManager.key, CoordType.WARPS, 0);
                    return;
                }
            } else {
                if(modesUsed.containsKey(ArgMode.INCORRECT)) {
                    messages.incorrectUsage(sender, "/RC warp {world} -> {} = Optional");
                    return;
                }
                if(modesUsed.containsKey(ArgMode.WORLD)) {
                    if(!sender.hasPermission("Random.Warps.Worlds") && !sender.hasPermission("Random.*")) {
                        messages.noPermission(sender);
                        return;
                    }
                    worldName = (String) modesUsed.get(ArgMode.WORLD);
                }
                if(modesUsed.containsKey(ArgMode.PLAYER)) {
                    if(!sender.hasPermission("Random.Admin.Others") && !sender.hasPermission("Random.Admin.*") && !sender.hasPermission("Random.*")) {
                        messages.noPermission(sender);
                        return;
                    }
                    playerName = (String) modesUsed.get(ArgMode.PLAYER);
                }


                if(playerName == null && worldName != null) {
                    if(!(sender instanceof Player)) {
                        messages.notPlayer(sender);
                        return;
                    }
                    Player p = (Player) sender;
                        if(!RandomCoords.getPlugin().config.getString("Experimental").equalsIgnoreCase("true")) {
                            coordinates.finalCoordinates(p, 574272099, 574272099, Bukkit.getWorld(worldName), CoordType.WARPWORLD, 0);
                            return;
                        } else {
                            coordinatesManager.randomlyTeleportPlayer(p, Bukkit.getWorld(worldName), coordinatesManager.key, coordinatesManager.key, CoordType.WARPWORLD, 0);
                            return;
                        }
                } else if(playerName != null && worldName == null) {
                    if(!sender.hasPermission("Random.Admin.Warp") && !sender.hasPermission("Random.Admin.*") && !sender.hasPermission("Random.*")) {
                        messages.noPermission(sender);
                        return;
                    }
                    Player p = Bukkit.getPlayer(playerName);
                    worldName = p.getWorld().getName();
                    if(!RandomCoords.getPlugin().config.getString("Experimental").equalsIgnoreCase("true")) {
                        coordinates.finalCoordinates(p, 574272099, 574272099, Bukkit.getWorld(worldName), CoordType.WARPS, 0);
                        return;
                    } else {
                        coordinatesManager.randomlyTeleportPlayer(p, Bukkit.getWorld(worldName), coordinatesManager.key, coordinatesManager.key, CoordType.WARPS, 0);
                        return;
                    }
                } else if(playerName != null && worldName != null) {
                    if(!sender.hasPermission("Random.Admin.Warp") && !sender.hasPermission("Random.Admin.*") && !sender.hasPermission("Random.*")) {
                        messages.noPermission(sender);
                        return;
                    }
                    Player p = Bukkit.getPlayer(playerName);
                    if(!RandomCoords.getPlugin().config.getString("Experimental").equalsIgnoreCase("true")) {
                        coordinates.finalCoordinates(p, 574272099, 574272099, Bukkit.getWorld(worldName), CoordType.WARPWORLD, 0);
                        return;
                    } else {
                        coordinatesManager.randomlyTeleportPlayer(p, Bukkit.getWorld(worldName), coordinatesManager.key, coordinatesManager.key, CoordType.WARPWORLD, 0);
                        return;
                    }
                }


            }





        }
    }

    public void createWarp(String name, Player p, CommandSender sender) {
        final String warpName = name;
        final Location location = p.getLocation();
        final double xD = location.getX();
        final double yD = location.getBlockY();
        final double zD = location.getBlockZ();
        final double x = Math.floor(xD) + 0.5;
        final double y = Math.floor(yD);
        final double z = Math.floor(zD) + 0.5;
        final World world = location.getWorld();
        RandomCoords.getPlugin().warps.set("Warps." + warpName + ".X", x);
        RandomCoords.getPlugin().warps.set("Warps." + warpName + ".Y", y);
        RandomCoords.getPlugin().warps.set("Warps." + warpName + ".Z", z);
        RandomCoords.getPlugin().warps.set("Warps." + warpName + ".World", world.getName());
        RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().warps, RandomCoords.getPlugin().warpFile);
        messages.warpSet(sender, warpName);
    }

    public void deleteWarp(String warpName, CommandSender sender) {
        if (RandomCoords.getPlugin().warps.getString("Warps." + warpName) != null) {
            RandomCoords.getPlugin().warps.set("Warps." + warpName, null);
            RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().warps, RandomCoords.getPlugin().warpFile);
            messages.warpDelete(sender, warpName);
        } else {
            messages.warpNotExist(sender);
        }
    }


    public Map<ArgMode, Object> processArguments(String[] args) {
        Map<ArgMode, Object> modes = new HashMap<ArgMode, Object>();
        int i = 0;
        String playerName= null;
        String worldName = null;
        for(String s : args) {
            if(i != 0) {
                if (s.equalsIgnoreCase("create") || s.equalsIgnoreCase("set")) {
                    modes.put(ArgMode.CREATE, null);
                } else if (s.equalsIgnoreCase("list")) {
                    modes.put(ArgMode.LIST, null);

                } else if (s.equalsIgnoreCase("delete") || s.equalsIgnoreCase("remove")) {
                    modes.put(ArgMode.DELETE, null);
                } else if (Bukkit.getPlayer(s) != null && playerName == null && !modes.containsKey(ArgMode.CREATE) && !modes.containsKey(ArgMode.DELETE) && !modes.containsKey(ArgMode.LIST))  {
                    playerName = s;
                    modes.put(ArgMode.PLAYER, s);
                } else if (Bukkit.getWorld(s) != null && worldName == null&& !modes.containsKey(ArgMode.CREATE) && !modes.containsKey(ArgMode.DELETE)) {
                    worldName = s;
                    modes.put(ArgMode.WORLD, worldName);
                } else {
                    //Could also be the warp name.
                    modes.put(ArgMode.INCORRECT, s);
                }
            }
            else if(i == 0 && args.length == 1) {
                modes.put(ArgMode.WARP, null);
            }
            i++;




        }
        return modes;
    }

    public void listWarps(CommandSender sender, String worldName) {
        if(worldName != null && Bukkit.getWorld(worldName) != null) {
            final Set<String> warps = RandomCoords.getPlugin().warps.getConfigurationSection("Warps.").getKeys(false);
            sender.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.GREEN + "Warps for " + worldName + ":");

            for (final String warpName : warps) {
                if(RandomCoords.getPlugin().warps.getString("Warps." + warpName + ".World").equalsIgnoreCase(worldName)) {
                    sender.sendMessage(ChatColor.GREEN + warpName);
                }
            }
        } else if(worldName == null) {
            final Set<String> warps = RandomCoords.getPlugin().warps.getConfigurationSection("Warps.").getKeys(false);
            sender.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.GREEN + "Warps:");

            for (final String warpName : warps) {
                sender.sendMessage(ChatColor.GREEN + warpName);
            }
        }
    }

}
