
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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by James on 04/07/2016.
 */
public class Others implements CommandInterface {

    private final Coordinates coordinates = new Coordinates();
    private final MessageManager messages = new MessageManager();
    private CommonMethods commonMethods = new CommonMethods();
    private final CoordinatesManager coordinatesManager = new CoordinatesManager();

    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        //Is the command being run Player?, Do they have the permission?
        if(args.length >= 1 && args[0].equalsIgnoreCase("player") && (sender.hasPermission("Random.Admin.Others") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*"))) {

            //The world name they want the player to go too. Null if not specified
            String toWorldName = null;
            //The max coordinate the player can teleport too. Set to default if not provided.
            int max = commonMethods.key;
            //The min coordintae the player can teleport too. Set to default if not provided.
            int min = commonMethods.key;
            //The player that we are teleporting, Null until specified.
            Player p = null;
            //Is the command is just /RC player. Send incorrect usage message.
            if(args.length == 1) {
                //Send Message
                messages.incorrectUsage(sender, "/RC player {Player} {World} {Max} {Min}");
                return;
            } //If the command longer than just /RC player {args}
            else if(args.length > 1) {
                //Filter over all of the arguments.
                Map modesUsed = commonMethods.processCommonArguments(args, true);

                if(modesUsed.containsKey(ArgMode.WORLDNOTEXIST) && ! ((String) modesUsed.get(ArgMode.WORLDNOTEXIST)).contains("player:")) {

                    if(modesUsed.containsKey(ArgMode.WORLD)) {
                        messages.playerNotExist(sender);
                        return;
                    } else {
                        messages.invalidWorld(sender, (String) modesUsed.get(ArgMode.WORLDNOTEXIST));
                        return;
                    }
                }

                if(modesUsed.containsKey(ArgMode.INCORRECT)) {
                    messages.incorrectUsage(sender, "/RC player [playerName] {toWorld} {Max} {Min} -> {} = Optional.");
                    return;
                }

                if(modesUsed.containsKey(ArgMode.PLAYER)) {
                    p =(Player) modesUsed.get(ArgMode.PLAYER);
                }

                if(modesUsed.containsKey(ArgMode.MAX)) {
                    max = (int) modesUsed.get(ArgMode.MAX);
                }

                if(modesUsed.containsKey(ArgMode.MIN)) {
                    min = (int) modesUsed.get(ArgMode.MIN);
                }

                if(modesUsed.containsKey(ArgMode.WORLD)) {
                    toWorldName = (String) modesUsed.get(ArgMode.WORLD);
                }

                for(String s : args) {
                    if(s.contains("player:") && Bukkit.getPlayer(s.replace("player:", "")) != null) {
                        p = Bukkit.getPlayer(s.replace("player:", ""));
                    }
                }

                if(p == null) {
                    messages.playerNotExist(sender);
                    return;
                }

                if(toWorldName == null && p != null) {
                    toWorldName = p.getWorld().getName();
                }

                if(!commonMethods.minToLarge(sender, min, max, Bukkit.getWorld(toWorldName))) {
                    return;
                }



                //Send the teleported by message to target player/
                messages.teleportedBy(sender, p);
                //Send the teleported player message to the sender.
                messages.teleportedPlayer(sender, p);
                //Teleport them using the provided args.
                if(!RandomCoords.getPlugin().config.getString("Experimental").equalsIgnoreCase("true")) {
                    coordinates.finalCoordinates(p, max, min, Bukkit.getWorld(toWorldName), CoordType.PLAYER, 0);
                } else {
                    coordinatesManager.randomlyTeleportPlayer(p, Bukkit.getWorld(toWorldName), max, min, CoordType.PLAYER, 0);
                }
                return;
            }










        }
    }



}
