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

package com.jolbol1.RandomCoordinates.commands.handler;


import com.jolbol1.RandomCoordinates.commands.CommonMethods;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.CoordinatesManager;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import com.jolbol1.RandomCoordinates.managers.Util.ArgMode;
import com.jolbol1.RandomCoordinates.managers.Util.CoordType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//The class will implement CommandExecutor.
public class CommandHandler implements CommandExecutor {

    //This is where we will store the commands
    private static final Map<String, CommandInterface> commands = new ConcurrentHashMap<>();
    private final MessageManager messages = new MessageManager();
    private CommonMethods commonMethods = new CommonMethods();
    private Coordinates coordinates = new Coordinates();
    private CoordinatesManager coordinatesManager = new CoordinatesManager();

    //Register method. When we register commands in our onEnable() we will use this.
    public void register(final String name, final CommandInterface cmd) {

        //When we register the command, this is what actually will put the command in the hashmap.
        commands.put(name, cmd);
    }

    //This will be used to check if a string exists or not.
    private boolean exists(final String name) {

        //To actually check if the string exists, we will return the hashmap
        return commands.containsKey(name);
    }

    //Getter method for the Executor.
    private CommandInterface getExecutor(final String name) {

        //Returns a command in the hashmap of the same name.
        return commands.get(name);
    }

    //This will be a template. All commands will have this in common.
    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {

        //For example, in each command, it will check if the sender is a player and if not, send an error message.


        //If there aren't any arguments, what is the command name going to be? For this example, we are going to call it /example.
        //This means that all commands will have the base of /example.
        if (args.length == 0) {
            getExecutor("rc").onCommand(sender, cmd, commandLabel, args);
            return true;
        }

        //What if there are arguments in the command? Such as /example args
        if (args.length > 0) {

            if(Bukkit.getPlayer(args[0]) != null) {
                Player p = Bukkit.getPlayer(args[0]);
                Map modeUsed = commonMethods.processCommonArguments(args, true);
                int max = commonMethods.key;
                int min = commonMethods.key;
                String toWorldName = null;

                if(modeUsed.containsKey(ArgMode.WORLDNOTEXIST)) {
                    messages.invalidWorld(sender, (String) modeUsed.get(ArgMode.WORLDNOTEXIST));
                    return true;
                }

                if(modeUsed.containsKey(ArgMode.INCORRECT)) {
                    messages.incorrectUsage(sender, "/RC [playerName] {toWorld} {Max} {Min} -> {} = Optional.");
                    return true;
                }


                if(modeUsed.containsKey(ArgMode.MAX)) {
                    max = (int) modeUsed.get(ArgMode.MAX);
                }

                if(modeUsed.containsKey(ArgMode.MIN)) {
                    min = (int) modeUsed.get(ArgMode.MIN);
                }

                if(modeUsed.containsKey(ArgMode.WORLD)) {
                    toWorldName = (String) modeUsed.get(ArgMode.WORLD);
                }

                if(toWorldName == null) {
                    toWorldName = p.getWorld().getName();
                }

                if(!commonMethods.minToLarge(sender, min, max, Bukkit.getWorld(toWorldName))) {
                    return true;
                }

                if(sender.hasPermission("Random.Admin.Others") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                    messages.teleportedBy(sender, p);
                    //Send the teleported player message to the sender.
                    messages.teleportedPlayer(sender, p);

                    //coordinates.finalCoordinates(p, max, min, Bukkit.getWorld(toWorldName), CoordType.PLAYER, 0);
                    coordinatesManager.randomlyTeleportPlayer(p, Bukkit.getWorld(toWorldName), max, min, CoordType.PLAYER, 0);

                    return true;
                } else {
                    messages.noPermission(sender);
                    return true;
                }





            }

            //If that argument exists in our registration in the onEnable();
            if (exists(args[0])) {

                //Get The executor with the name of args[0]. With our example, the name of the executor will be args because in
                //the command /example args, args is our args[0].
                getExecutor(args[0]).onCommand(sender, cmd, commandLabel, args);
                return true;
            } else {

                //We want to send a message to the sender if the command doesn't exist.

                messages.noCommand(sender);
                return true;
            }
        }

        return false;
    }

}