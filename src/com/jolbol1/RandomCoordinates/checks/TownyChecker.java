package com.jolbol1.RandomCoordinates.checks;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Created by JamesShopland on 14/04/15.
 */
public class TownyChecker {

    public boolean TownyCheck(Location l) {
        if (!(Bukkit.getServer().getPluginManager().getPlugin("Towny") == null)) {
            if (RandomCoords.getPlugin().config.getString("Towny").equals("true")) {
                int X = l.getBlockX();
                int Y = l.getBlockY();
                int Z = l.getBlockZ();

                int r = RandomCoords.getPlugin().config.getInt("CheckingRadius");

                int x = X - r;
                int y = Y - r;
                int z = Z - r;

                int bx = x;
                int bz = z;


                for (int i = 0; i < r * 2 + 1; i++) {
                    for (int j = 0; j < r * 2 + 1; j++) {
                        for (int k = 0; k < r * 2 + 1; k++) {
                            Block b = l.getWorld().getBlockAt(x, y, z);
                            TownBlock tb = TownyUniverse.getTownBlock(b.getLocation());
                            if(tb != null) {
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


}
