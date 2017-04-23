

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
import com.jolbol1.RandomCoordinates.managers.Util.ArgMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by James on 19/03/2017.
 */
public class Portals implements CommandInterface {


    private final MessageManager messages = new MessageManager();
    private int key = 574272099;
    private final Map selection = RandomCoords.getPlugin().wandSelection;
    private CommonMethods commonMethods = new CommonMethods();


    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length >= 1 && args[0].equalsIgnoreCase("portal") && sender.hasPermission("Random.*") && sender.hasPermission("Random.Admin.*") && sender.hasPermission("Random.Admin.Portals")) {
            //The mode to use. Ranging from max, min, create, delete.

            if(!(sender instanceof Player)) {
                messages.notPlayer(sender);
                return;
            }
            String mode = null;
            //Set max to default if not used otherwise.
            int max = key;
            //Set min to deafault if not changed otherwise.
            int min = key;
            //What to name the portal. This is REQUIRED
            String name = null;
            //The world that the portal will go to. If null, World the player is in is chosen.
            String toWorldName = null;

            Player p = (Player) sender;

            //Go through all the args provided.

            Map modeUsed = commonMethods.processCommonArguments(args, false, true);

            if(modeUsed.containsKey(ArgMode.WORLDNOTEXIST)) {
                messages.invalidWorld(sender,(String) modeUsed.get(ArgMode.WORLDNOTEXIST));
                return;
            }

            if(modeUsed.containsKey(ArgMode.INCORRECT)) {
                if(modeUsed.containsKey(ArgMode.CREATE)) {
                    messages.incorrectUsage(sender, "/RC portal create [portalName] {teleportWorld} {Max} {Min}  ->  {} = Optional");
                } else if(modeUsed.containsKey(ArgMode.DELETE)) {
                    messages.incorrectUsage(sender, "/RC portal delete [portalName]");
                } else if(modeUsed.containsKey(ArgMode.LIST)) {
                    messages.incorrectUsage(sender, "/RC portal list {World}  ->  {} = Optional");
                } else {
                    messages.incorrectUsage(sender, "/Rc portal create/delete/list... -> See /RC Help or Wiki for more details.");
                }
                return;
            }


            if(modeUsed.containsKey(ArgMode.MAX)) {
                max = (int) modeUsed.get(ArgMode.MAX);
            }

            if(modeUsed.containsKey(ArgMode.MIN)) {
                min = (int) modeUsed.get(ArgMode.MIN);
            }

            if(modeUsed.containsKey(ArgMode.PORTAL)) {
                name = (String) modeUsed.get(ArgMode.PORTAL);
            }

            if(modeUsed.containsKey(ArgMode.WORLD)) {
                toWorldName = (String) modeUsed.get(ArgMode.WORLD);
            }


            if(modeUsed.containsKey(ArgMode.LIST)) {
                listPortals(sender, toWorldName);
                return;
            }

            if(toWorldName == null) {
                toWorldName = p.getWorld().getName();
            }


            if(!commonMethods.minToLarge(sender, min, max, Bukkit.getWorld(toWorldName))) {
                return;
            }

            if(modeUsed.containsKey(ArgMode.CREATE) && toWorldName != null && name != null) {
                createPortal(sender, Bukkit.getWorld(toWorldName), name, max , min);
                return;
            }

            if(modeUsed.containsKey(ArgMode.DELETE) && (name != null || toWorldName != null)) {
                if(name != null) {
                    deletePortal(sender, name);
                } else if(toWorldName != null) {
                    deletePortal(sender, toWorldName);
                }
                return;
            }

            messages.incorrectUsage(sender, "/Rc portal create/delete/list... -> See /RC Help or Wiki for more details.");



        }
    }





    /**
     * Creates an instance of the portal in the portals file.
     * @param sender who sent the command
     * @param targetWorld the world the portal takes people too
     * @param pName the portalName
     * @param max the portal max
     * @param min the portal min.
     */
    public void createPortal(CommandSender sender, World targetWorld, String pName, int max, int min) {
        final Location pos1 = (Location) selection.get(PortalMap("pos1", (Player) sender));
        final Location pos2 = (Location) selection.get(PortalMap("pos2", (Player) sender));
        if(pos1 == null || pos2 == null) {
            messages.noSelection(sender);
            return;
        }

        if (pos1.getWorld() != pos2.getWorld()) {
            messages.posInSameWorld(sender);
            return;
        }
        final int p1x = pos1.getBlockX();
        final int p1y = pos1.getBlockY();
        final int p1z = pos1.getBlockZ();
        final int p2x = pos2.getBlockX();
        final int p2y = pos2.getBlockY();
        final int p2z = pos2.getBlockZ();
        final String wName = targetWorld.getName();
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p1x", p1x);
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p1y", p1y);
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p1z", p1z);
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p2x", p2x);
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p2y", p2y);
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p2z", p2z);
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p2z", p2z);
        if(max != key) {
            RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "max", max);
        }
        if(min != key) {
            RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "min", min);
        }
        RandomCoords.getPlugin().portals.set("Portal." + pName + ".PortalWorld", pos1.getBlock().getWorld().getName());
        RandomCoords.getPlugin().portals.set("Portal." + pName + ".world", wName);
        RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().portals, RandomCoords.getPlugin().portalsFile);
        messages.portalCreated(sender, pName, wName);
        RandomCoords.getPlugin().reloadPortals();
    }


    public void deletePortal(CommandSender sender, String name) {
        if (RandomCoords.getPlugin().portals.get("Portal." + name) != null) {
            //Delete the portal.
            RandomCoords.getPlugin().portals.set("Portal." + name, null);
            RandomCoords.getPlugin().reloadPortals();
            messages.portalDeleted(sender, name);
            RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().portals, RandomCoords.getPlugin().portalsFile);
            return;
        } //If portal does not exist.
        else {
            messages.portalNotExist(sender, name);
            return;
        }
    }


    /**
     * Combines position and player name to get a portal map.
     * @param position the players position
     * @param p the player
     * @return position + p.getName
     */
    public String PortalMap(final String position, final Player p) {
        return position + p.getName();
    }

    public void listPortals(CommandSender sender, String toWorldName) {
        //If toWorldName is null.
        if(toWorldName == null) {
            //Get all the portals.
            final ConfigurationSection cs = RandomCoords.getPlugin().portals.getConfigurationSection("Portal");
            messages.portalList(sender);
            //Go through all the portals if there are any, thus send the message.
            if (cs != null) {
                for (final String a : cs.getKeys(false)) {
                    sender.sendMessage(ChatColor.RED + a);
                }
            }
        } //If a world was specified with list, Get only portals for that world.
        else if(commonMethods.doesWorldExist(sender, toWorldName)) {
            final ConfigurationSection cs = RandomCoords.getPlugin().portals.getConfigurationSection("Portal");
            messages.portalList(sender);
            //If portals do exists.
            if (cs != null) {
                for (final String a : cs.getKeys(false)) {
                    //make sure the world is the same as one provided.
                    if(RandomCoords.getPlugin().portals.getString("Portal." + a + ".PortalWorld").equalsIgnoreCase(toWorldName)) {
                        sender.sendMessage(ChatColor.RED + a);
                    }
                }
            }
        } else {
            messages.incorrectUsage(sender, "/RC Portal create/delete/list {portalName} plus other additional flags.");
            return;

        }
    }


}
