package com.jolbol1.RandomCoordinates.checks;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Created by JamesShopland on 02/11/14.
 */
public class FactionChecker {


    public boolean FactionCheck(Location l) {
        if (!(Bukkit.getServer().getPluginManager().getPlugin("Factions") == null)) {
            if (RandomCoords.getPlugin().config.getString("Factions").equals("true")) {
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
                            //Code here
                            Block b = l.getWorld().getBlockAt(x, y, z);
                            if (!(BoardColl.get().getFactionAt(PS.valueOf(b)).isNone())) {
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

