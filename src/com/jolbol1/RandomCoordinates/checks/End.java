package com.jolbol1.RandomCoordinates.checks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Created by James on 04/07/2016.
 */
public class End {

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
                            //Code here
                            final Block b = l.getWorld().getBlockAt(x, y, z);
                            if(b.getType() == Material.ENDER_STONE ) {
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
