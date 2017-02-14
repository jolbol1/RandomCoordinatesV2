package com.jolbol1.RandomCoordinates.checks;

import com.jolbol1.RandomCoordinates.RandomCoords;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.kingdoms.constants.land.SimpleChunkLocation;
import org.kingdoms.manager.game.GameManagement;



/**
 * Created by JamesShopland on 14/04/15.
 */
public class KingdomsClaim {
    /**
     * Checks if the player is near a Towny town, or within the checking radius.
     * @param l The location to check.
     * @return True or False, Is the location in a Town?
     */
    public boolean KingdomsClaim(final Location l) {
        if (!(Bukkit.getServer().getPluginManager().getPlugin("Kingdoms") == null)) {
            if (RandomCoords.getPlugin().config.getString("Kingdoms").equals("true")) {
                final int X = l.getBlockX();
                final int Y = l.getBlockY();
                final int Z = l.getBlockZ();

                final int r = RandomCoords.getPlugin().config.getInt("CheckingRadius");

                int x = X - r;
                int y = Y - r;
                int z = Z - r;

                final int bx = x;
                final int bz = z;

                String kingdoms;


                for (int i = 0; i < r * 2 + 1; i++) {
                    for (int j = 0; j < r * 2 + 1; j++) {
                        for (int k = 0; k < r * 2 + 1; k++) {
                            Chunk chunk = l.getChunk();
                            kingdoms = GameManagement.getLandManager().getOrLoadLand(new SimpleChunkLocation(chunk)).getOwner();


                            if (kingdoms != null) {
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
