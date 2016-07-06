package com.jolbol1.RandomCoordinates.checks;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Created by James on 04/07/2016.
 */
public class End {

    private Location key = null;
    public Location endCoord(Location l) {

                int X = l.getBlockX();
                int Y = l.getBlockY();
                int Z = l.getBlockZ();

                int r = 256;

                int x = X - r;
                int y = Y - r;
                int z = Z - r;

                int bx = x;
                int bz = z;


                for (int i = 0; i < r * 2 + 1; i++) {
                    for (int j = 0; j < r * 2 + 1; j++) {
                        for (int k = 0; k < r * 2 + 1; k++) {
                            //Code here
                            Block b = l.getWorld().getBlockAt(x, y, z);
                            if(b.getType() == Material.ENDER_STONE || b.getType() == Material.END_BRICKS) {
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
        return key;
    }






}
