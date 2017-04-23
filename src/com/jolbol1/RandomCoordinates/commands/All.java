
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
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by James on 19/03/2017.
 */
public class All implements CommandInterface {

    private Coordinates coordinates = new Coordinates();
    private CommonMethods commonMethods = new CommonMethods();
    private MessageManager messages = new MessageManager();
    private final CoordinatesManager coordinatesManager = new CoordinatesManager();

    /**
     * As of v0.3.0 Commands have been redone. They are now very flexible and allow for multiple flags.
     */
    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        //Is the command being run All?, Do they have the permission?
        if(args.length >= 1 && args[0].equalsIgnoreCase("all") && (sender.hasPermission("Random.Admin.All") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*"))) {

            //The World To Teleport Them To - null as its not needed
            String toWorldName = null;
            //The World To Get The Players From - Null as its not needed
            String fromWorldName= null;
            //Set max to deafult. Will change if provided
            int max = commonMethods.key;
            //Set min to default, Will change if provided.
            int min = commonMethods.key;

            //Is the argument just /RC All
            if(args.length == 1) {
                teleportAll(sender, max, min, toWorldName, fromWorldName);
                return;
            }  // Else if there are more arguments.
            else if(args.length > 1) {
                //Run through all the arguments
                Map modesUsed = commonMethods.processCommonArguments(args);

                if(modesUsed.containsKey(ArgMode.WORLDNOTEXIST)) {
                    if(! ((String) modesUsed.get(ArgMode.WORLDNOTEXIST)).contains("from:") && Bukkit.getWorld((String) modesUsed.get(ArgMode.WORLDNOTEXIST)) == null) {
                        messages.invalidWorld(sender, (String) modesUsed.get(ArgMode.WORLDNOTEXIST));
                        return;
                    }
                }

                if(modesUsed.containsKey(ArgMode.INCORRECT)) {
                    messages.incorrectUsage(sender, "/RC all [toWorld] {fromWorld} {Max} {Min} -> {} = Optional");
                    return;
                }




                if(modesUsed.containsKey(ArgMode.MAX)) {
                    int customMax = (int) modesUsed.get(ArgMode.MAX);
                    max = customMax;
                }

                if(modesUsed.containsKey(ArgMode.MIN)) {
                    int customMin = (int) modesUsed.get(ArgMode.MIN);
                    min = customMin;
                }

                if(modesUsed.containsKey(ArgMode.WORLD)) {
                    toWorldName = (String) modesUsed.get(ArgMode.WORLD);
                }

                for(String arg : args) {
                    if(Bukkit.getWorld(arg) != null && toWorldName != null && arg != toWorldName) {
                        fromWorldName = arg;
                    }

                    else if (arg.contains("from:") && Bukkit.getWorld(arg.replace("from:", "")) != null) {
                        fromWorldName = arg.replace("from:", "");
                    }

                }



                //Teleport all the players, Using the values set above.
                if(toWorldName != null && Bukkit.getWorld(toWorldName) != null) {
                    if (!commonMethods.minToLarge(sender, min, max, Bukkit.getWorld(toWorldName))) {
                        return;
                    }
                } else {
                    if(!commonMethods.minToLarge(sender, min, max)) {
                        return;
                    }
                }
                teleportAll(sender, max, min, toWorldName, fromWorldName);
                return;
            }










        }
    }


    /**
     * Gets all players on a server, Except the sender.
     * @param sender - Who should be exempt from this?
     * @return All players.
     */
    public Collection<Player> allPlayers(CommandSender sender) {
        Collection<Player> players = new ArrayList<>();
        for(Player p: Bukkit.getOnlinePlayers()) {
            if(!p.equals(sender)) {
                players.add(p);
            }
        }
        return players;
    }



    /**
     * Teleports all players randomly
     * @param sender - who sent the command
     * @param max - The max that we will teleport all these players too
     * @param min - The min that we will teleport all these players too
     * @param toWorldName - The world we want to teleport them too.
     * @param fromWorldName - The world we want to grab the players from.
     */
    public void teleportAll(CommandSender sender, int max, int min, String toWorldName, String fromWorldName) {

        //Is the toWorldName provided? If so, Set the message to sender as "their world"
        //If it is provided, Set the world name in message to toWorldName
        if(toWorldName == null) {
            messages.teleportedAll(sender, "their world");
        } else {
            messages.teleportedAll(sender, toWorldName);
        }

        //If a from world has been provided, But its an invalid world, Message and return.
        if(fromWorldName != null && !commonMethods.doesWorldExist(sender, fromWorldName)) {
            messages.invalidWorld(sender, fromWorldName);
            return;
        }

        //Parse over all the player from allPlayers() function above.
        for(Player p: allPlayers(sender)) {

            //First set the worldTo to the players world.
            World worldTo = p.getWorld();

            //If toWorldName has been provided in the command.
            if(toWorldName != null) {
                //If the toWorldname provided exists, set worldTo to this world.
                if(commonMethods.doesWorldExist(sender, toWorldName)) {
                    worldTo = Bukkit.getWorld(toWorldName);
                } else {
                    //Otherwise, Its an invalid world. - Checked in commmand but added for API reasons...
                    messages.invalidWorld(sender, toWorldName);
                    return;
                }
            }
           //Make sure the world isnt banned, If it is message and return.
            if(!commonMethods.isWorldBanned(worldTo)) {
                messages.worldBanned(sender);
                return;
            }

            //If from world name is null, or if the players world is equal to fromWorldName, Message and teleport.

            if(fromWorldName == null || p.getWorld().equals(Bukkit.getWorld(fromWorldName))) {
                messages.teleportedBy(sender, p);
                if(!RandomCoords.getPlugin().config.getString("Experimental").equalsIgnoreCase("true")) {
                    coordinates.finalCoordinates(p, max, min, worldTo, CoordType.ALL, 0);
                } else {
                    coordinatesManager.randomlyTeleportPlayer(p, worldTo, max, min, CoordType.ALL, 0);
                }
            }
        }


    }

}
