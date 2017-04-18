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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Set;

/**
 * Created by James on 04/07/2016.
 */
public class InNetherPortal implements Listener {

    private final PortalEnter pe = new PortalEnter();

    /**
     * Checks if the player is a nether portal, and if that portal is a RC portal.
     * @param e The teleport event.
     */
    @EventHandler
    public void onPlayerTP(final PlayerTeleportEvent e) {

        /**
         * Checks if the teleport cause was a nether portal. If yes, proceed with code.
         */
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            return;
        }
        /**
         * Checks if the plugin actually has any portals.
         */
        if (RandomCoords.getPlugin().portals == null) {
            return;
        }
        /**
         * Checks if the file itself has any portals in it.
         */
        if (RandomCoords.getPlugin().portals.getConfigurationSection("Portal") == null) {
            return;
        }
        /**
         * Checks yet again if there are any portals
         */
        if (RandomCoords.getPlugin().portals.getConfigurationSection("Portal").getKeys(false) == null) {
            return;
        }

        //Gets a set of all of the portals.
        final Set<String> portals = RandomCoords.getPlugin().portals.getConfigurationSection("Portal").getKeys(false);

        /**
         * For all of these portals, check whether or not the teleport is within these portals.
         */
        for (final String name : portals) {

            //Get the world that this portal teleports to.
            final String world = RandomCoords.getPlugin().portals.getString("Portal." + name + ".world");
            /**
             * If this world is null, Return and log the fact.
             */
            if (Bukkit.getServer().getWorld(world) == null) {
                //Log the fact that this world does not exist.
                Bukkit.getServer().getLogger().severe(world + " is an invalid world, Change this portal!");
                return;

            }
            // Get the world that the portal is actually in.
            final String portalWorld = RandomCoords.getPlugin().portals.getString("Portal." + name + ".PortalWorld");
            /**
             * If this is also null, Seldom Use. Then cancel the code.
             */
            if (Bukkit.getServer().getWorld(portalWorld) == null) {
                //Log the fact that this world does not exist.
                Bukkit.getServer().getLogger().severe(portalWorld + "no longer exists");
                return;

            }

            //Get an instance of the actual world.
            final World w = Bukkit.getServer().getWorld(portalWorld);
            /**
             * Get one corner of the portal.
             */
            final int p1y = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1y");
            final int p1z = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1z");
            final int p1x = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1x");
            /**
             * Get the other corner of the portal.
             */
            final int p2y = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2y");
            final int p2z = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2z");
            final int p2x = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2x");
            /**
             * Use these values to get the corners as locations.
             */
            final Location l1 = new Location(w, p1x, p1y, p1z);//NOPMD
            final Location l2 = new Location(w, p2x, p2y, p2z);//NOPMD

            /**
             * If the location is from within the portal, then cancel the teleport event.
             */
            if (pe.isInside(e.getPlayer().getLocation(), l1, l2)) {
                //Cancels the teleport event.
                e.setCancelled(true);
                return;
            }


        }


    }


}
