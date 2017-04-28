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

import com.jolbol1.RandomCoordinates.RandomCoords;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Collection;

/**
 * Created by JamesShopland on 02/11/14.
 */
public class GriefPreventionCheck {

    /**
     * Used to check wether or not the location is in protected land, or within the buffer.
     * @param l The location to check.
     * @return True or False, Are they in protected land.
     */
    public boolean griefPrevent(final Location l) {
        if (!(Bukkit.getServer().getPluginManager().getPlugin("GriefPrevention") == null)) {
            if (RandomCoords.getPlugin().config.getString("GriefPrevention").equals("true")) {

                final GriefPrevention gp = GriefPrevention.instance;


                final int X = l.getBlockX();
                final int Y = l.getBlockY();
                final int Z = l.getBlockZ();

                final int r = RandomCoords.getPlugin().config.getInt("CheckingRadius");

                int x = X - r;
                int y = Y - r;
                int z = Z - r;

                final int bx = x;
                final int bz = z;


                for (int i = 0; i < r * 2 + 1; i++) {
                    for (int j = 0; j < r * 2 + 1; j++) {
                        for (int k = 0; k < r * 2 + 1; k++) {

                            final Block b = l.getWorld().getBlockAt(x, y, z);

                            final Claim myClaim = gp.dataStore
                                    .getClaimAt(b.getLocation(), false, null);

                            if (!(myClaim == null)) {
                                return false;
                            }
                            x++;
                        }
                        z++;
                        x = bx;
                    }
                    z = bz;
                    x = bx;
                    y++;
                }
            }

            return true;
        } else {

            return true;
        }
    }

    public boolean griefPrevNearby(Location l) {
        if (Bukkit.getServer().getPluginManager().getPlugin("GriefPrevention") == null) {
            return false;
        }
        if(!RandomCoords.getPlugin().getConfig().getString("GriefPrevention").equalsIgnoreCase("true")) {
            return false;
        }
        final GriefPrevention gp = GriefPrevention.instance;


        int radius = RandomCoords.getPlugin().getConfig().getInt("CheckingRadius");
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        int x;
        int y;
        int z;


        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {

            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                x = l.getBlockX();
                y = l.getBlockY();
                z = l.getBlockZ();
                Chunk chunk = l.getWorld().getBlockAt(x + (chX * 16), y, z + (chZ * 16)).getChunk();
                Collection<Claim> claims = gp.dataStore.getClaims(chunk.getX(), chunk.getZ());
                if(!claims.isEmpty() && claims != null) {
                    return true;
                }
            }
        }

        return false;
    }


}

