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
import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by JamesShopland on 02/11/14.
 */
public class WorldBorderChecker {


    /**
     * Checks if the location is outside of the WorldBorder world border. (Plugin)
     * @param l The location to check.
     * @return True or false, Is the location outside of the border.
     */
    public boolean WorldBorderCheck(final Location l) {
        if (!(Bukkit.getServer().getPluginManager().getPlugin("WorldBorder") == null)) {
            if (RandomCoords.getPlugin().config.getString("WorldBorder").equalsIgnoreCase("true")) {

                final BorderData border = Config.Border(l.getWorld().getName());
                return border == null || border.insideBorder(l);

            } else {
                return true;
            }

        } else {

            return true;
        }
    }
}