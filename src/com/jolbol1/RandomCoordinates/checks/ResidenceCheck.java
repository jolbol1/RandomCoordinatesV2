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

package com.jolbol1.RandomCoordinates.checks;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.CuboidArea;
import com.jolbol1.RandomCoordinates.RandomCoords;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by James on 22/04/2017.
 */
public class ResidenceCheck {


    /**
     * Checks if there is a residence claim within the radius from location.
     * Due to the way residence works, Im going to check each corner of the chunk
     * instead of every block. This means it may not be 100% effective but the odds
     * of a claim bein small enough to avoid each corner is high.
     * @return
     */
    public boolean isChunkProtected(Location location)
    {
        if(Bukkit.getServer().getPluginManager().getPlugin("Residence") == null) {
            return false;
        }

        if(!RandomCoords.getPlugin().getConfig().getString("Residence").equalsIgnoreCase("true")) {
            return false;
        }
        for (ClaimedResidence residence : Residence.getInstance().getResidenceManager().getResidences().values()) {
            if ((!residence.isSubzone()) && (location.getWorld().getName().equals(residence.getWorld()))) {
                for (CuboidArea area : residence.getAreaMap().values()) {
                    if(isInside(location, area.getHighLoc(), area.getLowLoc())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInside(final Location loc, final Location l2, final Location l1) {
        int radius = RandomCoords.getPlugin().getConfig().getInt("CheckingRadius");
        final int x1 = Math.min(l1.getBlockX() - radius , l2.getBlockX() - radius);
        final int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        final int z1 = Math.min(l1.getBlockZ() - radius, l2.getBlockZ() - radius);
        final int x2 = Math.max(l1.getBlockX() + radius, l2.getBlockX() + radius);
        final int y2 = Math.max(l1.getBlockY(), l2.getBlockY() + 256);
        final int z2 = Math.max(l1.getBlockZ() + radius, l2.getBlockZ() + radius);
        final int x = loc.getBlockX();
        final int y = loc.getBlockY();
        final int z = loc.getBlockZ();
        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
    }



}
