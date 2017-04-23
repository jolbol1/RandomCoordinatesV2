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
import com.jolbol1.RandomCoordinates.managers.Util.CoordType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by James on 01/07/2016.
 */
public class RandomCommand implements CommandInterface {


    private final Coordinates coordinates = new Coordinates();
    private final CoordinatesManager coordinatesManager = new CoordinatesManager();
    private final MessageManager messages = new MessageManager();


    //The command should be automatically created.
    @Override
    public void onCommand(final CommandSender sender, final Command cmd,
                          final String commandLabel, final String[] args) {
        if (RandomCoords.getPlugin().hasPermission(sender, "Random.Basic") || RandomCoords.getPlugin().hasPermission(sender, "Random.*") || RandomCoords.getPlugin().hasPermission(sender, "Random.Command")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    messages.notPlayer(sender);
                    return;
                }
                final Player p = (Player) sender;
                //   Location start = p.getLocation();
                // double health = p.getHealth();
                for (final String worlds : RandomCoords.getPlugin().config.getStringList("BannedWorlds")) {
                    if (p.getWorld().getName().equals(worlds)) {
                        messages.worldBanned(sender);
                        return;

                    }
                }

                if(!RandomCoords.getPlugin().config.getString("Experimental").equalsIgnoreCase("true")) {
                    coordinates.finalCoordinates(p, coordinatesManager.key, coordinatesManager.key, p.getWorld(), CoordType.COMMAND, 0);
                } else {
                    coordinatesManager.randomlyTeleportPlayer(p, p.getWorld(), coordinatesManager.key, coordinatesManager.key, CoordType.COMMAND, 0);
                }



            }
        } else {
            messages.noPermission(sender);
        }

    }


}


