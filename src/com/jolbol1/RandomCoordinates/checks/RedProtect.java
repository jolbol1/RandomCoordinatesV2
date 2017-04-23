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

import br.net.fabiozumbi12.RedProtect.API.RedProtectAPI;
import br.net.fabiozumbi12.RedProtect.Region;
import com.jolbol1.RandomCoordinates.RandomCoords;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by JamesShopland on 14/04/15.
 */
public class RedProtect {
    /**
     * Checks if the player is near a Towny town, or within the checking radius.
     * @param l The location to check.
     * @return True or False, Is the location in a Town?
     */
    public boolean RedProtect(final Location l) {
        if (!(Bukkit.getServer().getPluginManager().getPlugin("RedProtect") == null)) {
            if (RandomCoords.getPlugin().config.getString("RedProtect").equals("true")) {
                final int X = l.getBlockX();
                final int Y = l.getBlockY();
                final int Z = l.getBlockZ();

                final int r = RandomCoords.getPlugin().config.getInt("CheckingRadius");

                int x = X - r;
                int y = Y - r;
                int z = Z - r;

                final int bx = x;
                final int bz = z;


              //  for (int i = 0; i < r * 2 + 1; i++) {
                    for (int j = 0; j < r * 2 + 1; j++) {
                        for (int k = 0; k < r * 2 + 1; k++) {
                            Region reg = RedProtectAPI.getRegion(l);
                            if (reg != null) {
                                return false;
                            }


                           // x++;
                            x = x + 16;
                        }
                       // z++;
                        z = z + 16;
                        x = bx;
                    }
                /*    z = bz;
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
     * Is there a redProtect claim nearby?
     * @param l
     * @return True if yes, False if no.
     */
    public boolean redProtectClaimNearby(Location l) {
        if (Bukkit.getServer().getPluginManager().getPlugin("RedProtect") == null) {
            return false;
        }
        if(!RandomCoords.getPlugin().getConfig().getString("RedProtect").equalsIgnoreCase("true")) {
            return false;
        }


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
                Region reg = RedProtectAPI.getRegion(l.getWorld().getBlockAt(x + (chX * 16), y, z + (chZ * 16)).getLocation());
                if (reg != null) {
                    return true;
                }

            }
        }

        return false;
    }


}
