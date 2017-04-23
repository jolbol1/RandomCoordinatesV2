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
import com.jolbol1.RandomCoordinates.managers.Util.PortalLoaded;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by James on 04/07/2016.
 */
public class PortalEnter extends BukkitRunnable {

    private final Coordinates coordinates = new Coordinates();
    private final MessageManager messages = new MessageManager();
    private final CoordinatesManager coordinatesManager = new CoordinatesManager();

    /**
     * This is the class that checks if a player has entered a portal.
     * This is a Runnable initiated from the OnEnable in the main class.
     */
    @Override
    public void run() {

        if (!RandomCoords.getPlugin().skyBlock.isEmpty() && !RandomCoords.getPlugin().skyBlockSave.getString("SkyBlock").equalsIgnoreCase("none")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (RandomCoords.getPlugin().skyBlock.containsKey(p.getUniqueId())) {
                    if (Material.getMaterial(RandomCoords.getPlugin().skyBlockSave.getString("SkyBlock")) != null) {

                        Material material = Material.getMaterial(RandomCoords.getPlugin().skyBlockSave.getString("SkyBlock"));

                        if (RandomCoords.getPlugin().skyBlock.get(p.getUniqueId()).getBlock().getType().equals(Material.AIR)) {

                            RandomCoords.getPlugin().skyBlock.get(p.getUniqueId()).getBlock().setType(material);
                        } else if (!RandomCoords.getPlugin().skyBlock.get(p.getUniqueId()).getBlock().getType().equals(material)) {
                            RandomCoords.getPlugin().skyBlock.remove(p.getUniqueId());

                        }
                        if (RandomCoords.getPlugin().skyBlockSave.getString("AutoRemove").equalsIgnoreCase("true")) {
                            if (p.getLocation().getWorld().equals(RandomCoords.getPlugin().skyBlock.get(p.getUniqueId()).getWorld())) {
                                if (RandomCoords.getPlugin().skyBlock.get(p.getUniqueId()).distance(p.getLocation().subtract(0, 1, 0)) > 2.5) {
                                    RandomCoords.getPlugin().skyBlock.get(p.getUniqueId()).getBlock().setType(Material.AIR);
                                    RandomCoords.getPlugin().skyBlock.remove(p.getUniqueId());
                                }
                            }
                        }
                    }
                }
            }
        }


            /**
             * For all the players on the world of this portal.
             * Check is the targetWorld is banned.
             * Check if the player is in this portal.
             */
            if(RandomCoords.getPlugin().loadedPortalList == null || RandomCoords.getPlugin().loadedPortalList.size() == 0) {
                return;
            }

            for(PortalLoaded portalLoaded : RandomCoords.getPlugin().loadedPortalList) {
               String portalWorld = portalLoaded.getPortalWorldName();
                Location point1 = portalLoaded.getPoint1();
                Location point2 = portalLoaded.getPoint2();
                int max = portalLoaded.getMax();
                int min = portalLoaded.getMin();
                String toWorldName = portalLoaded.getToWorldName();
                World worldW = Bukkit.getWorld(toWorldName);
                double cost = portalLoaded.getPortalCost();


                for (final Player player : Bukkit.getServer().getWorld(portalWorld).getPlayers()) {

                    if (isInside(player.getLocation(), point1, point2)) {


                        /**
                         * Check firstly if they have the permission to use the portal.
                         */
                        if (RandomCoords.getPlugin().hasPermission(player, "Random.PortalUse") || RandomCoords.getPlugin().hasPermission(player, "Random.Basic") || RandomCoords.getPlugin().hasPermission(player, "Random.*")) {
                            /**
                             * If the player has the money to use the portal, then continue. Else message and return.
                             */
                            if (!coordinates.hasMoney(player, cost)) {
                                //Sends the message to the player that they have not paid for the portal.
                                messages.cost(player, cost);
                                return;
                            }
                            //Uses the coordinates class to finally send the player to the random spot.
                            if(!RandomCoords.getPlugin().config.getString("Experimental").equalsIgnoreCase("true")) {
                                coordinates.finalCoordinates(player, max, min, worldW, CoordType.PORTAL, 0);
                                return;
                            } else {

                                coordinatesManager.randomlyTeleportPlayer(player, worldW, max, min, CoordType.PORTAL, 0);
                                return;
                            }
                        } else {
                            //Messages them that they have no permission to use portals.
                            messages.noPermission(player);
                            return;
                        }
                    }
                }
            }


            }




    /**
     * Checks if a plyer is inside the portal. (MOVE THIS!) DUPED CODE!
     *
     * @param loc The players location.
     * @param l1  Corner 1 of the portal.
     * @param l2  Corner 2 of the portal.
     * @return True or False, are they in the portal.
     */
    public boolean isInside(final Location loc, final Location l1, final Location l2) {
        final int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        final int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        final int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        final int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        final int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        final int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
        final int x = loc.getBlockX();
        final int y = loc.getBlockY();
        final int z = loc.getBlockZ();

        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
    }



}
