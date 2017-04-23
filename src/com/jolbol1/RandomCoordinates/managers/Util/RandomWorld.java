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

package com.jolbol1.RandomCoordinates.managers.Util;

import com.jolbol1.RandomCoordinates.RandomCoords;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by James on 28/03/2017.
 */
public class RandomWorld {
    World world;
    int max = RandomCoords.getPlugin().config.getInt("MaxCoordinate");
    int min = RandomCoords.getPlugin().config.getInt("MinCoordinate");
    Location center;
    boolean banned = false;

    public RandomWorld(World world) {
     this.world = world;
    }

    public RandomWorld(World world, int max, int min, Location center) {
        this.world = world;
        this.max = max;
        this.min = min;
        this.center = world.getSpawnLocation().add(0.5, 0, 0.5);
        this.banned = RandomCoords.getPlugin().config.getList("BannedWorlds").contains(world.getName());
    }


    public World getWorld(){
        return world;
    }

    public String getWorldName() {
        return world.getName();
    }

    public int getMax(){
        if (RandomCoords.getPlugin().config.get(world.getName() + ".Max") != null) {
            max = RandomCoords.getPlugin().config.getInt(world.getName() + ".Max");
        }
        return max;
    }

    public int getMin(){
        if (RandomCoords.getPlugin().config.get(world.getName() + ".Min") != null) {
            min = RandomCoords.getPlugin().config.getInt(world.getName() + ".Min");
        }
        return min;
    }

    public Location getCenter(){
        if (RandomCoords.getPlugin().config.get(world.getName() + ".Center") != null) {
            double x = RandomCoords.getPlugin().config.getDouble(world.getName() + ".Center.X");
            double y = RandomCoords.getPlugin().config.getDouble(world.getName() + ".Center.Y");
            double z = RandomCoords.getPlugin().config.getDouble(world.getName() + ".Center.Z");

            center = new Location(world, x , y, z);
        }
        if(center == null) {
            center = new Location(world, 0.5, 0, 0.5);
        }

        return center;
    }

    public boolean isBanned(){
        return banned;
    }

    public int getCenterX() {
        return getCenter().getBlockX();
    }

    public int getCenterZ() {
        return getCenter().getBlockZ();
    }

    public int getCenterY() {
        return  getCenter().getBlockY();
    }


}
