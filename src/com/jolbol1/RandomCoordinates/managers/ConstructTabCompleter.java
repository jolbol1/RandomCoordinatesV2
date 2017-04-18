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

package com.jolbol1.RandomCoordinates.managers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by James on 05/07/2016.
 */
public class ConstructTabCompleter implements TabCompleter {
    /**
     * The command that handles tab completion in minecraft.
     * @param commandSender The player writing the command.
     * @param command The command itself.
     * @param s Not used.
     * @param args The amount of arguments the player has typed.
     * @return The tab competition list.
     */
    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String s, final String[] args) {
        if (args.length == 1) {
            final List<String> list = new ArrayList<>();
            list.add("set");
            list.add("player");
            list.add("portal");
            list.add("all");
            list.add("wand");
            list.add("warp");

            final List<String> newList = new ArrayList<>();

            newList.addAll(list);


            return newList;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                return Bukkit.getServer().getWorlds().stream().map(World::getName).collect(Collectors.toList());

            } else if (args[0].equalsIgnoreCase("player")) {
                final List<String> players = new ArrayList<>();
                for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
                    final String name = p.getName();
                    players.add(name);
                }
                return players;
            } else if (args[0].equalsIgnoreCase("portal")) {
                final List<String> options = new ArrayList<>();
                options.add("create");
                options.add("delete");
                return options;
            } else {
                return null;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set") && Bukkit.getWorld(args[1]) != null) {
                final List<String> options = new ArrayList<>();
                options.add("center");
                options.add("max");
                options.add("min");
                return options;
            }
        } else {

            return null;
        }
        return null;
    }
}
