package com.jolbol1.RandomCoordinates.checks;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.managers.DebugManager;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;


/**
 * Created by JamesShopland on 02/11/14.
 */
public class FactionChecker {

    DebugManager debugManager = new DebugManager();

    /**
     * Used to check wether or not the location is in faction land, or within the buffer.
     * @param l The location that we are checking.
     * @return True or False, is it in faction land?
     */
    public boolean FactionCheck(final Location l) {
        int checks = 1;
        if (!(Bukkit.getServer().getPluginManager().getPlugin("Factions") == null)) {
            if (RandomCoords.getPlugin().config.getString("Factions").equals("true")) {
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
                            //Code here
                            final Block b = l.getWorld().getBlockAt(x, y, z);
                            if (!(BoardColl.get().getFactionAt(PS.valueOf(b)).isNone())) {
                                return false;

                            }
                        x++;
                            checks++;
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

    /**
     * Is there faction land within the radius from location
     * @param l
     * @return False if none, True if land found.
     */
    public boolean factionLandNearby(Location l) {
        if (Bukkit.getServer().getPluginManager().getPlugin("Factions") == null) {
            return false;
        }
        if(!RandomCoords.getPlugin().getConfig().getString("Factions").equalsIgnoreCase("true")) {
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
                Block b = l.getWorld().getBlockAt(x + (chX * 16), y, z + (chZ * 16));
                if (!(BoardColl.get().getFactionAt(PS.valueOf(b)).isNone())) {
                    return true;
                }

            }
        }

        return false;
    }


}

