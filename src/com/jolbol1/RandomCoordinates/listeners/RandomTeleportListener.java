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
import com.jolbol1.RandomCoordinates.event.RandomTeleportEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Created by James on 16/03/2017.
 */
public class RandomTeleportListener implements Listener {


    /**
     *Event that is called when a player RandomlyTeleports
     * This is to handle the SkyBlock feature.
     * This even considers the players location as the one before teleporting and e.location as the one they are going to.
     * @param e
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onRandomTeleport(RandomTeleportEvent e) {


        Location skyBlock = new Location(e.location().getWorld(), e.location().getX(), e.location().getY() - 2, e.location().getZ());

    if(!RandomCoords.getPlugin().skyBlockSave.getString("SkyBlock").equalsIgnoreCase("none")) {
        if (Material.getMaterial(RandomCoords.getPlugin().skyBlockSave.getString("SkyBlock")) != null) {
            if(RandomCoords.getPlugin().skyBlockSave.getStringList("SkyBlockWorlds").contains(e.getPlayer().getWorld().getName())) {
                Material material = Material.getMaterial(RandomCoords.getPlugin().skyBlockSave.getString("SkyBlock"));

                if (RandomCoords.getPlugin().skyBlock.containsKey(e.getPlayer().getUniqueId())) {
                    if (RandomCoords.getPlugin().skyBlockSave.getString("AutoRemove").equalsIgnoreCase("true")) {
                        RandomCoords.getPlugin().skyBlock.get(e.getPlayer().getUniqueId()).getBlock().setType(Material.AIR);
                    }
                }
                int defY = RandomCoords.getPlugin().skyBlockSave.getInt("DefaultY");
                skyBlock.setY(defY);
                skyBlock.getBlock().setType(material);
                RandomCoords.getPlugin().skyBlock.put(e.getPlayer().getUniqueId(), skyBlock.getBlock().getLocation());
            }
        }
    }




    }



}
