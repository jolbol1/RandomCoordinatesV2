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


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Created by JamesShopland on 02/11/14.
 */
public class Nether {

    /**
     * Secret key used to get default Max and Min
     */
    private static final int key = 574272099;

    /**
     * !OLD SYSTEM! SO BAD! WTF!
     * Takes the location and aims to find a 2 block gap that the player can teleport to on the Y value.
     * @param l Takes in the initial location
     * @return The new Y level.
     */
    public int getSafeNetherY(final Location l) {
        final int X = l.getBlockX();
        final int Y = 126;
        final int Z = l.getBlockZ();

        final int r = 126;

        int y = Y - r;



        for (int i = 0; i < r * 2 + 1; i++) {
            for (int j = 0; j < r * 2 + 1; j++) {
                for (int k = 0; k < r * 2 + 1; k++) {
                    final Block b = l.getWorld().getBlockAt(X, y, Z);
                    final Block b1 = l.getWorld().getBlockAt(b.getLocation().add(0, 1, 0));
                    final Block b2 = l.getWorld().getBlockAt(b1.getLocation().add(0, 1, 0));
                    if (!(b.getType() == Material.BEDROCK || b.getType() == Material.AIR || b.getType() == Material.LAVA || b.getType() == Material.STATIONARY_LAVA || b1.getType() != Material.AIR || b2.getType() != Material.AIR)) {
                        return y;
                    }


                }
            }

            y++;
        }

        return key;
    }



    public int getSafeYNether(Location location) {
        int x = location.getBlockX();
        int z = location.getBlockZ();

        int air = 0;

        for(int y = 2; y < 126; y++) {
            if(location.getWorld().getBlockAt(x, y, z).getType() == Material.AIR) {
                if(y - air == 1) {
                    if (location.getWorld().getBlockAt(x, air - 1, z).getType() == Material.NETHERRACK) {
                        return air;
                    }
                }
                air = y;
            }
        }

        return 0;






    }


}

