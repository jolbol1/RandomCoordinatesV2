package com.jolbol1.RandomCoordinates.checks;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Created by JamesShopland on 02/11/14.
 */
public class Nether {


    private static final int key = 574272099;

    public int netherY(final Location l) {
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
                            if(!(b.getType() == Material.BEDROCK || b.getType() == Material.AIR || b.getType() == Material.LAVA || b.getType() == Material.STATIONARY_LAVA || b1.getType() != Material.AIR || b2.getType() != Material.AIR)) {
                                return y;
                            }



                        }
                    }

                    y++;
                }

        return key;
    }





}

