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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Created by James on 04/07/2016.
 */
public class End {
    /**
     * Takes in the a random location for the end, Then finds the nearest island within 256.
     * @param l Random location within the end.
     * @return A Location on the nearest end island.
     */
    public Location endCoord(final Location l) {

        final int X = l.getBlockX();
        final int Y = l.getBlockY();
        final int Z = l.getBlockZ();

        final int r = 256;

        int x = X - r;
        int y = Y - r;
        int z = Z - r;

        final int bx = x;
        final int bz = z;


        for (int i = 0; i < r * 2 + 1; i++) {
            for (int j = 0; j < r * 2 + 1; j++) {
                for (int k = 0; k < r * 2 + 1; k++) {

                    final Block b = l.getWorld().getBlockAt(x, y, z);
                    if (b.getType() == Material.ENDER_STONE || b.getType() == Material.END_BRICKS) {
                        return b.getLocation().add(0, 2.5, 0);
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
        return null;
    }


}
