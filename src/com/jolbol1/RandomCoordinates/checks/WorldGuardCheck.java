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
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JamesShopland on 11/01/15.
 */
public class WorldGuardCheck {
    /**
     * Checks if the player is in, or near the specified WorldGuard regions.
     * @param l The location to check.
     * @return True or False, Is the location in one of these regions.
     */
    public boolean WorldguardCheck(final Location l) {

        //Checks if plugin is installed.
        if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {

            //Checks if WorldGuard is enabled
            if (RandomCoords.getPlugin().config.getString("WorldGuard").equals("true")) {

                final int X = l.getBlockX();
                //  int Y = l.getBlockY();
                final int Z = l.getBlockZ();

                //Calls the BufferZone
                final int r = RandomCoords.getPlugin().config.getInt("CheckingRadius");


                //Checks all coordinates within the buffer zone for a region

                            //See if allRegions is enabled.
                            if (RandomCoords.getPlugin().config.getStringList("Regions").contains("allRegions")) {

                                return RandomCoords.getPlugin().getWorldGuard().getRegionManager(l.getWorld()).getApplicableRegions(l).size() == 0;
                            } else {
                                RegionContainer container = RandomCoords.getPlugin().getWorldGuard().getRegionContainer();
                                RegionManager regions = container.get(l.getWorld());
                                int cub = RandomCoords.getPlugin().config.getInt("CheckingRadius");
                                BlockVector p1 = new BlockVector(l.getX() + cub, 0.0D, l.getZ() + cub);
                                BlockVector p2 = new BlockVector(l.getX() - cub, 256.0D, l.getZ() - cub);
                                ProtectedRegion cuboid = new ProtectedCuboidRegion("RandomCoordsREGION-DO-NOT-USE", p1, p2);
                                ProtectedRegion regionRadius = cuboid;
                                ApplicableRegionSet set = regions.getApplicableRegions(regionRadius);
                                if(set.size() == 0 ) {
                                    return true;
                                }
                                List<String> regionsToCheck = new ArrayList<>();

                                for(String name : RandomCoords.getPlugin().config.getStringList("Regions")) {

                                        if (set.getRegions().contains(regions.getRegion(name))) {
                                            regionsToCheck.add(name);
                                        }

                                }
                                return regionsToCheck == null || regionsToCheck.size() == 0;


                            }



            }

            return true;
        } else {

            return true;

        }
    }





}
