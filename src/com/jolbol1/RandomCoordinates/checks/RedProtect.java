package com.jolbol1.RandomCoordinates.checks;

import br.net.fabiozumbi12.RedProtect.API.RedProtectAPI;
import br.net.fabiozumbi12.RedProtect.Region;
import com.jolbol1.RandomCoordinates.RandomCoords;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

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


                for (int i = 0; i < r * 2 + 1; i++) {
                    for (int j = 0; j < r * 2 + 1; j++) {
                        for (int k = 0; k < r * 2 + 1; k++) {
                            Region reg = RedProtectAPI.getRegion(l);
                            if (reg != null) {
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
