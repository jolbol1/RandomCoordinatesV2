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

package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.CoordinatesManager;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import com.jolbol1.RandomCoordinates.managers.Util.CoordType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by James on 04/07/2016.
 */
public class onJoin implements Listener {

    private final Coordinates coordinates = new Coordinates();
    private final MessageManager messages = new MessageManager();
    private final CoordinatesManager coordinatesManager = new CoordinatesManager();

    /**
     * The event that checks wheter to teleport the player on join.
     * @param e The player join event.
     */
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {

        //ONLY SEND UPDATE MESSAGE TO OP's
        if(e.getPlayer().isOp()) {
            if(RandomCoords.getPlugin().updateNeeded == true) {
                e.getPlayer().setDisplayName(ChatColor.GOLD + "[RandomCoords]" + ChatColor.RED + " A new version is now available on Spigot. http://bit.ly/RandomTeleportSpigot");
            }
        }



        /**
         * If the onJoin config option is false, then dont run the code.
         */
        if (RandomCoords.getPlugin().config.getString("OnJoin").equalsIgnoreCase("false")) {
            return;
        }
        /**
         * If the player has the permission to be teleported onJoin then do so. Else, Dont. :P
         */
        if (RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.OnJoin") || RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.*")) {
            //Grabs an instance of the player who is joining.
            final Player p = e.getPlayer();
            /**
             * Checks if the player has played before, If so dont run the code.
             */
            if (p.hasPlayedBefore()) {
                return;
            }
            //Get the command that should be run on join. Plans to change this to a list.
            //Initiate the coordinates function which will handle the random teleport. Notice the secret key to get default values.
            //coordinates.finalCoordinates(p, 574272099, 574272099, p.getWorld(), CoordType.JOIN, 0);
            coordinatesManager.randomlyTeleportPlayer(p, p.getWorld(), coordinatesManager.key, coordinatesManager.key, CoordType.JOIN, 0);
            //Message them to let them know that they have been teleported.
            messages.onJoin(p);


        }

    }


}
