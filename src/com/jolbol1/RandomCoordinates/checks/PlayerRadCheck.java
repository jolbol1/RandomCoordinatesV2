/*
 *     RandomCoords, Provding the best Bukkit Random Teleport Plugin
 *     Copyright (C) 2014  James Shopland
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.jolbol1.RandomCoordinates.checks;

import com.jolbol1.RandomCoordinates.RandomCoords;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
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


    /**
     * Are there players nearby?
     * @param l
     * @return True if yes, False if no
     */
    public boolean areThereNearbyPlayers(Location l) {
        if(!RandomCoords.getPlugin().getConfig().getString("AvoidPlayers").equalsIgnoreCase("true")) {
            return false;
        }

        int radius = RandomCoords.getPlugin().getConfig().getInt("CheckingRadius");
        if(radius < 16) {
            for(Entity e : l.getChunk().getEntities()) {

                if(e instanceof Player) {
                    return true;
                }

            }

        }
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;

        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
                        if(e instanceof Player){
                            return true;
                        }
                }
            }
        }

        return false;
    }



}
