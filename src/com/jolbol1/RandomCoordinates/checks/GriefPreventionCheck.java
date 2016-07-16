package com.jolbol1.RandomCoordinates.checks;

import com.jolbol1.RandomCoordinates.RandomCoords;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Created by JamesShopland on 02/11/14.
 */
public class GriefPreventionCheck {


    public boolean griefPrevent(final Location l) {
        if (!(Bukkit.getServer().getPluginManager().getPlugin("GriefPrevention") == null)) {
            if (RandomCoords.getPlugin().config.getString("GriefPrevention").equals("true")) {

                final GriefPrevention gp = GriefPrevention.instance;


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

                            final Block b = l.getWorld().getBlockAt(x, y, z);
                            final Claim myClaim = gp.dataStore
                                    .getClaimAt(b.getLocation(), false, null);
                            if (!(myClaim == null)) {
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

