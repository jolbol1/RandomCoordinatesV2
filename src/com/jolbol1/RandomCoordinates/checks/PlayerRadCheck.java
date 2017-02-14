package com.jolbol1.RandomCoordinates.checks;

import com.jolbol1.RandomCoordinates.RandomCoords;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;


/**
 * Created by JamesShopland on 24/12/14.
 */
public class PlayerRadCheck {
    /**
     * Checks if a player is nearby the location, and within the checking radius.
     * @param l The location to check.
     * @return True or False, Are they near a player.
     */
    public boolean isPlayerNear(final Location l) {
        final int X = l.getBlockX();
        final int Y = l.getBlockY();
        final int Z = l.getBlockZ();

        final int r = RandomCoords.getPlugin().config.getInt("CheckingRadius");

        int x = X - r;
        int y = Y - r;
        int z = Z - r;

        final int bx = x;
        final int bz = z;

        if (RandomCoords.getPlugin().config.getString("AvoidPlayers").equals("true")) {

            for (int i = 0; i < r * 2 + 1; i++) {
                for (int j = 0; j < r * 2 + 1; j++) {
                    for (int k = 0; k < r * 2 + 1; k++) {
                        //noinspection LoopStatementThatDoesntLoop
                        for (final Player p : Bukkit.getOnlinePlayers()) {
                            final Block b = l.getWorld().getBlockAt(x, y, z);
                            return p.getLocation().getBlock().getRelative(BlockFace.DOWN) != b;
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
    }
}
