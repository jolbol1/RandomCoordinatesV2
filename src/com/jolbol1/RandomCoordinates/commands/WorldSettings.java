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
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import com.jolbol1.RandomCoordinates.managers.Util.ArgMode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by James on 19/03/2017.
 */
public class WorldSettings implements CommandInterface {

    private final MessageManager messages = new MessageManager();
    private final Coordinates coordinates = new Coordinates();
    private final CommonMethods commonMethods = new CommonMethods();
    private int key = 574272099;

    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length >= 1 && args[0].equalsIgnoreCase("set") && sender.hasPermission("Random.*") && sender.hasPermission("Random.Admin.*") && sender.hasPermission("Random.Admin.Settings")) {

            //Player p = (Player) sender;
            //Set max to default if not provided.
            int max = key;
            //Set min to default if not provided.
            int min = key;
            //Set worldName to none - This is REQUIRED
            String worldName = null;
            //Set center to null
            Location center = null;
            //Set mode to null
            String mode = null;
            //Are we deleteing?
            boolean delete = false;
            //Go through all the args and assign the appropriate values.
            Map modeUsed = commonMethods.processCommonArguments(args);

            if(modeUsed.containsKey(ArgMode.WORLD) && Bukkit.getWorld((String) modeUsed.get(ArgMode.WORLD)) != null) {
                worldName = (String) modeUsed.get(ArgMode.WORLD);
            } else {
                messages.incorrectUsage(sender, "/RC set [worldName] {center} {Max} {Min} {delete/remove/global} -> {} = Optional. ");
                return;
            }

            for(String s : args) {
                if(s.equalsIgnoreCase("delete") || s.equalsIgnoreCase("global") || s.equalsIgnoreCase("remove")) {
                    delete = true;
                }

                if(s.equalsIgnoreCase("center")) {
                    if(!(sender instanceof Player)) {
                        messages.notPlayer(sender);
                    }
                    Player p = (Player) sender;
                    center = p.getLocation();
                }
            }


            if(center != null && delete == true)
            {
                messages.centerRemove(sender, worldName);
                RandomCoords.getPlugin().config.set(worldName + ".Center", null);
                center = null;
            }
            if(modeUsed.containsKey(ArgMode.MAX)) {
                if(delete == true) {
                    messages.maxRemove(sender, worldName);
                    RandomCoords.getPlugin().config.set(worldName + ".Max", null);

                } else if(modeUsed.get(ArgMode.MAX) != null) {
                    messages.maxSet(sender, String.valueOf(modeUsed.get(ArgMode.MAX)), worldName);
                    RandomCoords.getPlugin().config.set(worldName + ".Max", modeUsed.get(ArgMode.MAX));

                }
            }

            if(modeUsed.containsKey(ArgMode.MIN)) {
                if(delete == true) {
                    messages.minRemove(sender, worldName);
                    RandomCoords.getPlugin().config.set(worldName + ".Min", null);

                } else if(modeUsed.get(ArgMode.MIN) != null) {
                    messages.minSet(sender, String.valueOf(modeUsed.get(ArgMode.MIN)), worldName);

                    RandomCoords.getPlugin().config.set(worldName + ".Min", modeUsed.get(ArgMode.MIN));


                }
            }

            if(center != null) {
                messages.centerSet(sender);
                RandomCoords.getPlugin().config.set(worldName + ".Center.X", center.getBlockX() + 0.5);
                RandomCoords.getPlugin().config.set(worldName + ".Center.Y", center.getBlockY());
                RandomCoords.getPlugin().config.set(worldName + ".Center.Z", center.getBlockZ() + 0.5);
            }



            RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().config, RandomCoords.getPlugin().configFile);

        }
    }





}
