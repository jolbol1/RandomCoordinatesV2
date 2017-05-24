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

package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


/**
 * Created by James on 02/07/2016.
 */
public class Reload implements CommandInterface {

    private final MessageManager messages = new MessageManager();


    @Override
    public void onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (args.length == 1) {
            if (RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.Reload") || RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(sender, "Random.*")) {
                if (args[0].equalsIgnoreCase("reload")) {
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().language, "language.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().config, "config.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().limiter, "limiter.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().portals, "portals.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().warps, "warps.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().blacklist, "blacklist.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().skyBlockSave, "SkyBlockSave.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().bonusChest, "BonusChestConfig.yml");
                    messages.reloadMessage(sender);
                    RandomCoords.getPlugin().reloadPortals();

                }
            } else {
                messages.noPermission(sender);

            }
        }
    }



}
