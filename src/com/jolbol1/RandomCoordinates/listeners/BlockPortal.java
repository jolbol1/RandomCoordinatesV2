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
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import java.util.Set;

/**
 * Created by James on 04/07/2016.
 * <p>
 * THIS IS AN UNUSED CLASS / LISTENER, Apparently not...
 */
public class BlockPortal implements Listener {

    private final PortalEnter pe = new PortalEnter();

    /**
     * Blocks physics within the RC Portals. This is to allow for Portal blocks and flowing water.
     * @param e The physics event.
     */
    @EventHandler
    public void blockPhysics(final BlockPhysicsEvent e) {
        final Material mat = e.getBlock().getType();
        /**
         * If the material thats causing the physics event is one of these, Execute the next code, Else allow physics normally.
         */
        if(mat == Material.WATER || mat == Material.STATIONARY_WATER || mat == Material.LAVA || mat == Material.STATIONARY_LAVA || mat == Material.PORTAL || mat == Material.ENDER_PORTAL) {

            /**
             * If There are not any portals, Allow natural physics.
              */
            if (RandomCoords.getPlugin().portals.get("Portal") == null) {
            return;
        }

        //Gets the list of portals from the portal file.
        final Set<String> portals = RandomCoords.getPlugin().portals.getConfigurationSection("Portal").getKeys(false);
            /**
             * Check all of these portals to see if the block that had an update is in one of them.
             */
            for (final String name : portals) {
                //Gets the world that the portal goes to.
                final String world = RandomCoords.getPlugin().portals.getString("Portal." + name + ".world");

                /**
                 * If the wolrd doesnt exist, Allow natural physics and log that no such world occurs for said portal.
                  */
                if (Bukkit.getServer().getWorld(world) == null) {
                    //Log the fact that the world is non existent
                    Bukkit.getServer().getLogger().severe(world + " is an invalid world, Change this portal!");
                    return;

            }

            //Gets the world that the portal is in.
            final String portalWorld = RandomCoords.getPlugin().portals.getString("Portal." + name + ".PortalWorld");

                /**
                 * If the world the portal is in is null, Again log it and return.
                  */
                if (Bukkit.getServer().getWorld(portalWorld) == null) {
                    //Log the fact that this world no longer exitst.
                    Bukkit.getServer().getLogger().severe(portalWorld + "no longer exists");
                    return;
                }

                //Grabs the world that the portal is in.
                final World w = Bukkit.getServer().getWorld(portalWorld);
                /**
                 * Grab one corner of the portal.
                 */
                final int p1y = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1y");
                final int p1z = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1z");
                final int p1x = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1x");
                /**
                 * Grab the other corner of the portal.
                 */
                final int p2y = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2y");
                final int p2z = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2z");
                final int p2x = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2x");

                /**
                 * Use these value as a method to get the location of the corner.
                 */
                final Location l1 = new Location(w, p1x, p1y, p1z);//NOPMD
                final Location l2 = new Location(w, p2x, p2y, p2z);//NOPMD

                /**.
                 * If the location that we have got is inside a portal, Cancel the physics.
                 */
                if (pe.isInside(e.getBlock().getLocation(), l1, l2)) {
                    //Cancels the physics event.
                    e.setCancelled(true);
                    return;
            }
        }

        }
    }




}




