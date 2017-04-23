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
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.logging.Level;

/**
 * Created by James on 23/03/2017.
 */
public class RegionCommand implements CommandInterface {

    private MessageManager messages = new MessageManager();
    private Coordinates coordinates = new Coordinates();
    private com.jolbol1.RandomCoordinates.managers.RegionManager regionManager;

    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length >= 1 && args[0].equalsIgnoreCase("region")) {

            if(Bukkit.getPluginManager().getPlugin("WorldGuard") == null || Bukkit.getPluginManager().getPlugin("WorldEdit") == null) {
                Bukkit.getServer().getLogger().log(Level.SEVERE, "WorldGuard/WorldEdit are not installed. This feature cannot be used.");
                messages.incorrectUsage(sender, "WorldGuard/WorldEdit not installed.");
                return;
            }

            regionManager = new com.jolbol1.RandomCoordinates.managers.RegionManager();

            Player p = null;
            String region = null;
            Map<String, String> regionList = regionManager.allRegionList(sender);
            if(regionList == null || regionList.values() == null || regionList.isEmpty()) {
                messages.incorrectUsage(sender, "No WG regions exist!");
                return;
            }
            int i = 0;
            for(String s : args) {
                if(i != 0) {

                    if(regionList.containsKey(s) && region == null) {
                        region = s;
                    } else if(s.contains("region:") && regionList.containsKey(s.replace("region:", ""))) {
                        region = s.replace("region:", "");
                    } else if(s.contains("player:") && Bukkit.getPlayer(s.replace("player:", "")) != null) {
                        p = Bukkit.getPlayer(s.replace("player:", ""));
                    }

                    else if(Bukkit.getPlayer(s) != null && p == null) {
                        p = Bukkit.getPlayer(s);
                    } else if(region != null && p == null) {
                        if(sender instanceof  Player) {
                            p = (Player) sender;
                        } else {
                            messages.notPlayer(sender);
                            return;
                        }
                    } else {
                        messages.regionNotExist(sender, s);
                        return;
                    }



                }

               i++;
            }

           // teleportRandomlyInRegion(sender, p, region, regionList.get(region));
            if(p == null) {
                if(sender.hasPermission("Random.Region.Command") || sender.hasPermission("Random.*")) {
                    p = (Player) sender;
                } else {
                    messages.noPermission(sender);
                    return;
                }
            } else if(p != null) {

                if(!(sender.hasPermission("Random.Region.Others") || sender.hasPermission("Random.Region.*") || sender.hasPermission("Random.*"))) {
                    messages.noPermission(sender);
                    return;
                }


            }


            if(region != null && p != null) {
                if(p != sender && (sender.hasPermission("Random.Region.Others") || sender.hasPermission("Random.Region.*") || sender.hasPermission("Random.*"))) {

                    messages.teleportedPlayer(sender, p);
                    messages.teleportedBy(sender, p);
                 regionManager.teleportRandomlyInRegion(p, Bukkit.getWorld(regionList.get(region)), regionManager.getSelectionFromRegion(p, region, Bukkit.getWorld(regionList.get(region))));
                    messages.teleportedWithinRegion(p, region);
                } else if(p == sender ){

                    if(p.hasPermission("Random.Region.Command")  || p.hasPermission("Random.*")) {

                        if (p.hasPermission("Random.Regions." + region) || p.hasPermission("Random.Regions.*") || p.hasPermission("Random.*")) {
                            regionManager.teleportRandomlyInRegion(p, Bukkit.getWorld(regionList.get(region)), regionManager.getSelectionFromRegion(p, region, Bukkit.getWorld(regionList.get(region))));
                            messages.teleportedWithinRegion(sender, region);
                        } else {
                            messages.noPermission(sender);
                            return;
                        }
                    } else {
                        messages.noPermission(sender);
                        return;
                    }
                } else {
                    messages.noPermission(sender);
                    return;
                }
            } else {
                if(region != null && !regionList.containsKey(region)) {
                    messages.regionNotExist(sender, region);
                }
                messages.incorrectUsage(sender, "/RC region [regionName] {player} -> {} = Optional");
                return;
            }

        }
    }



}
