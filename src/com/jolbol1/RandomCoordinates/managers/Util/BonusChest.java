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

import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by James on 25/04/2017.
 */
public class BonusChest {

    String name;
    List<World> worlds;
    List<ItemStack> items;
    public BonusChest(String name, List<World> worlds, List<ItemStack> stackList) {
        this.name = name;
        this.worlds = worlds;
        this.items = stackList;

    }

    public String getName() {
        return name;
    }

    public List<World> getWorlds() {
        return worlds;
    }

    public List<ItemStack> getItems() {
        return items;
    }




}
