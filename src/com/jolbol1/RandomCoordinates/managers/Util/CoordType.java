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

/**
 * Created by James on 05/07/2016.
 * These Enums enable me to see in what way they initiated the Random Teleport.
 * This helps to set custom prices etc based on each command withinh the command.
 */
public enum CoordType {
    /**
     * This is used when they are teleported by the command /RC
     */
    COMMAND("Command"),
    /**
     * This is used when they are teleported by the command /RC All
     */
    ALL("All"),
    /**
     * This is used when they are teleported by /RC Player {Name}
     */
    PLAYER("Others"),
    /**
     * This is used when they are teleported by a [RandomCoords] sign
     */
    SIGN("Signs"),
    /**
     * This is used when they are teleported on join
     */
    JOIN("Join"),
    /**
     * This is used when they are teleported by using a portal
     */
    PORTAL("Portals"),
    /**
     * This is used when they are teleported by using /RC Warp
     */
    WARPS("Warps"),
    /**
     * Used for custom world teleport via /RC Warp
     */
    WARPWORLD("Warps"),

    JOINWORLD("Join");


    String methodName;
    CoordType(String methodName) {
        this.methodName = methodName;
    }

    public String getName() {
        return methodName;
    }

}


