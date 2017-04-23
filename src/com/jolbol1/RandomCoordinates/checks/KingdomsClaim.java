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
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.kingdoms.constants.land.SimpleChunkLocation;
import org.kingdoms.manager.game.GameManagement;



/**
 * Created by JamesShopland on 14/04/15.
 */
public class KingdomsClaim {
    /**
     * Checks if the player is near a Towny town, or within the checking radius.
     * @param l The location to check.
     * @return True or False, Is the location in a Town?
     */
    public boolean KingdomsClaim(final Location l) {
        if (!(Bukkit.getServer().getPluginManager().getPlugin("Kingdoms") == null)) {
            if (RandomCoords.getPlugin().config.getString("Kingdoms").equals("true")) {
                final int X = l.getBlockX();
                final int Y = l.getBlockY();
                final int Z = l.getBlockZ();

                final int r = RandomCoords.getPlugin().config.getInt("CheckingRadius");

                int x = X - r;
                int y = Y - r;
                int z = Z - r;

                final int bx = x;
                final int bz = z;

                String kingdoms;


             //   for (int i = 0; i < r * 2 + 1; i++) {
                    for (int j = 0; j < r * 2 + 1; j++) {
                        for (int k = 0; k < r * 2 + 1; k++) {
                            Chunk chunk = l.getChunk();
                            kingdoms = GameManagement.getLandManager().getOrLoadLand(new SimpleChunkLocation(chunk)).getOwner();


                            if (kingdoms != null) {
                                return false;
                            }


                            //x++;
                            x = x + 16;
                        }
                        z = z + 16;
                        //z++;
                        x = bx;
                    }
            /*        z = bz;
                    x = bx;
                    y++;
                }*/
            }

            return true;
        } else {

            return true;

        }
    }

    /**
     * Is there a Kingdom claim nearby?
     * @param l
     * @return True if kindom nearby, False if not.
     */
    public boolean kingdomClaimNearby(Location l) {
        if (Bukkit.getServer().getPluginManager().getPlugin("Kingdoms") == null) {
            return false;
        }
        if(!RandomCoords.getPlugin().getConfig().getString("Kingdoms").equalsIgnoreCase("true")) {
            return false;
        }


        int radius = RandomCoords.getPlugin().getConfig().getInt("CheckingRadius");
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        int x;
        int y;
        int z;
        String kingdoms;


        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                x = l.getBlockX();
                y = l.getBlockY();
                z = l.getBlockZ();
                Chunk chunk = l.getWorld().getBlockAt(x + (chX * 16), y, z + (chZ * 16)).getChunk();
                kingdoms = GameManagement.getLandManager().getOrLoadLand(new SimpleChunkLocation(chunk)).getOwner();
                if (kingdoms != null) {
                    return true;
                }

            }
        }

        return false;
    }




}
