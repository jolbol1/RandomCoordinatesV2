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

/**
 * Created by James on 28/03/2017.
 */
 public class PortalLoaded {
    String portalName;
    Location point1;
    Location point2;
    int max;
    int min;
    String toWorldName;
    String portalWorldName;
    double portalCost = RandomCoords.getPlugin().config.getDouble("PortalCost");

    public PortalLoaded(String portalName, Location point1, Location point2, int max, int min, String toWorldName, String portalWorldName) {
        this.portalName = portalName;
        this.point1 = point1;
        this.point2 = point2;
        this.max = max;
        this.min = min;
        this.toWorldName = toWorldName;
        this.portalWorldName = portalWorldName;



    }


    public Location getPoint1() {
        return point1;
    }

    public Location getPoint2(){
        return point2;
    }

    public int getMax(){
        return max;
    }

    public int getMin(){
        return min;
    }

    public String getToWorldName() {
        return toWorldName;
    }

    public String getPortalWorldName() {
        return portalWorldName;
    }

    public double getPortalCost() {
        return portalCost;
    }









}
