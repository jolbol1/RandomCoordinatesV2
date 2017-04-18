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
import com.jolbol1.RandomCoordinates.cooldown.Cooldown;
import com.jolbol1.RandomCoordinates.managers.CoordinatesManager;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashSet;

/**
 * Created by James on 30/03/2017.
 */
public class RadiusCommand implements CommandInterface {

    private CoordinatesManager coordinatesManager = new CoordinatesManager();
    private CommonMethods commonMethods = new CommonMethods();
    private MessageManager messages = new MessageManager();

    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length >= 1 && args[0].equalsIgnoreCase("radius") && (sender.hasPermission("Random.Admin.Radius") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*"))) {

            int radius = 0;
            String worldName = null;
            int max = coordinatesManager.key;
            int min = coordinatesManager.key;
            boolean entities = false;

            Location toCheck = null;

            for(String s : args) {
                if(s.equalsIgnoreCase("-e")) {
                    entities = true;
                } else
                if(Bukkit.getWorld(s) != null && worldName == null) {
                    worldName = s;
                }
                else if(commonMethods.canParseInteger(s)) {
                    if(radius == 0) {
                        radius = Integer.parseInt(s);
                    } else
                    if(max == coordinatesManager.key) {
                        max = Integer.parseInt(s);
                    } else if(min == coordinatesManager.key) {
                        min = Integer.parseInt(s);
                    }
                }

            }
            if(sender instanceof BlockCommandSender) {
                BlockCommandSender blockCommandSender = (BlockCommandSender) sender;
                toCheck = blockCommandSender.getBlock().getLocation();
                if(worldName == null) {
                    worldName = blockCommandSender.getBlock().getWorld().getName();
                }
            }
            else if(sender instanceof Player) {
                Player p = (Player) sender;
                toCheck = p.getLocation();
                if(worldName == null) {
                    worldName = p.getWorld().getName();
                }
            } else {
                messages.notPlayer(sender);
                return;
            }

            if(toCheck == null) {
                messages.incorrectUsage(sender, "/RC Region {radius} {world} {max} {min} -> {} = Optional");
                return;
            }

            Entity[] nearby = getNearbyEntities(toCheck, radius);
            Location randomLocationFirst = coordinatesManager.getSafeRandomLocation(Bukkit.getWorld(worldName), max, min);
            Location randomLocation = randomLocationFirst.subtract(0, 2.5, 0);

            if(randomLocationFirst == null) {
                messages.couldntFind(sender);
                return;
            }
            messages.teleportedAll(sender, worldName);

            for(Entity e : nearby) {
                if(entities == true) {
                    if(e instanceof Player) {
                        e.teleport(randomLocation);
                        messages.teleportedBy(sender, (Player) e);
                        messages.teleportMessage((Player) e, randomLocation);
                        final Cooldown c = new Cooldown(e.getUniqueId(), "Invul", 30);
                        c.start();
                        final Cooldown cT = new Cooldown(e.getUniqueId(), "InvulTime", RandomCoords.getPlugin().config.getInt("InvulTime"));
                        cT.start();
                    } else {
                        e.teleport(randomLocation);
                    }
                } else {
                    if(e instanceof Player) {
                        e.teleport(randomLocation);
                        messages.teleportedBy(sender, (Player) e);
                        messages.teleportMessage((Player) e, randomLocation);

                        final Cooldown c = new Cooldown(e.getUniqueId(), "Invul", 30);
                        c.start();
                        final Cooldown cT = new Cooldown(e.getUniqueId(), "InvulTime", RandomCoords.getPlugin().config.getInt("InvulTime"));
                        cT.start();
                    }
                }
            }









        }


    }

    public Entity[] getNearbyEntities(Location l, int radius) {
        if(radius < 16) {
            return l.getChunk().getEntities();
        }
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntities = new HashSet <Entity> ();

        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
                        radiusEntities.add(e);
                }
            }
        }

        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }



}
