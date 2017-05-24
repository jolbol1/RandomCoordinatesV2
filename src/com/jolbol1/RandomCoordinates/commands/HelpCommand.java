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

import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by James on 07/07/2016.
 */
public class HelpCommand implements CommandInterface {

    private final String prefix = ChatColor.GOLD + "";
    private final ChatColor good = ChatColor.AQUA;
    private final ChatColor un = ChatColor.DARK_AQUA;
    private final ChatColor desc = ChatColor.YELLOW;


    @Override
    public void onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {

        if (args[0].equalsIgnoreCase("help")) {
            if (args.length == 1 || args.length == 2 && args[1].equalsIgnoreCase("1")) {

                sender.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.GREEN + "Help Page 1/3: Developed by Jolbol1/ImBaffled");
                if (sender.hasPermission("Random.Basic") || sender.hasPermission("Random.Command") || sender.hasPermission("Random.*")) {
                    sender.sendMessage(prefix + good + "/RC" + desc +  " Teleport to somewhere random!");
                }
                if (sender.hasPermission("Random.Warps") || sender.hasPermission("Random.*")) {
                    sender.sendMessage(prefix + good + "/RC Warp" + desc + " Teleport to a random warp!");
                }
                if (sender.hasPermission("Random.Admin.All") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                    sender.sendMessage(prefix + good + "/RC All" + un +  " {World} {Max} {Min}" + desc + " Teleports all players to a random location. {World/Max/Min} are not required.");
                }
                if (sender.hasPermission("Random.Admin.Others") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                    sender.sendMessage(prefix + good + "/RC Player" + un +  " {Name} {World} {Max} {Min}" + desc + " Teleports the specified player to a random location. {World/Max/Min} are not required.");
                }
                if (sender.hasPermission("Random.Admin.Reload") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                    sender.sendMessage(prefix + good + "/RC Reload" + desc +  " Reloads the configuration files.");
                }

            } else if (args.length == 2 && args[1].equalsIgnoreCase("2")) {
                sender.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.GREEN + "Help Page 2/3: Developed by Jolbol1/ImBaffled");
                if (sender.hasPermission("Random.Admin.Warp") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                    sender.sendMessage(prefix + good + "/RC Warp Set" + un +  " {Name}" + desc +  " Sets a warp with {Name} at current location");
                    sender.sendMessage(prefix + good + "/RC Warp Delete" + un +  " {Name}" + desc +  " Deletes the warp with {Name}");
                    sender.sendMessage(prefix + good + "/RC Warp List" + desc +  " Lists all set warps");


                }


                if (sender.hasPermission("Random.Admin.Portals") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                    sender.sendMessage(prefix + good + "/RC Wand" + desc +  " Gives you the Random Wand for portal selection.");
                    sender.sendMessage(prefix + good + "/RC Portal Create" + un + " {Name}" + desc + " Creates a portal from the RandomWand selection with name {Name}");
                    sender.sendMessage(prefix + good + "/RC Portal Delete" + un +  " {Name}" + desc +  " Deletes a portal with name {Name}");
                }
            } else if (args.length == 2 && args[1].equalsIgnoreCase("3")) {
                sender.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.GREEN + "Help Page 3/3: Developed by Jolbol1/ImBaffled");

                if (sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.Admin.Settings") || sender.hasPermission("Random.*")) {
                    sender.sendMessage(prefix + good + "/RC Set" + un + " {World}" + good + " Center" + desc + " Sets the centerpoint for the Max, Min boundary");
                    sender.sendMessage(prefix + good + "/RC Set" + un + " {World}" + good + " Center Remove" + desc + " Removes the center point for {World}");

                    sender.sendMessage(prefix + good + "/RC Set" + un + " {World}" + good + " Max" + un + " {Number}" + desc + " Sets the maximum boundary for random teleporting");
                    sender.sendMessage(prefix + good + "/RC Set" + un + " {World}" + good + " Min" + un + " {Number}" + desc + " Sets the minimum boundary for random teleporting");
                    sender.sendMessage(prefix + good + "/RC Set" + un + " {World}" + good + " Max Global" + desc + " Sets the max to the default in config.");
                    sender.sendMessage(prefix + good + "/RC Set" + un + " {World}" + good + " Min Global"  + desc + " Sets the min to the default in config.");
                }
            } else if(args.length == 2 && args[1].equalsIgnoreCase("4")) {
                if(sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*") || sender.hasPermission("Random.Admin.Chest")) {
                    sender.sendMessage(prefix + good + "/RC chest" + un + " {FileToSave}" + desc + " Saves the content of your inventory to {FileToSave}");
                    sender.sendMessage(prefix + good + "/RC chest" + un + " {FileToSave}" + good + " -i" + desc + " Saves the item you're holding to {FileToSave}");
                    sender.sendMessage(prefix + good + "/RC chest" + un + " {FileToSave}" + good + " -o" + desc + "Overwrites {FileToSave} with the contents of your inventory.");


                }
            }



            else if(args.length == 2) {
                sender.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.RED + "Invalid Page, Must be between 1 and 3!");
            }
        }
    }
}


