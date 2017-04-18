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

package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.commands.Portals;
import com.jolbol1.RandomCoordinates.commands.WandGive;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

/**
 * Created by James on 04/07/2016.
 */
public class Wand implements Listener {

    private final Map selection = RandomCoords.getPlugin().wandSelection;
    private final MessageManager messages = new MessageManager();
    private final Portals portalCommand = new Portals();
    private final WandGive wandGive = new WandGive();

    /**
     * Gets when the player has clicked using the /RC Wand.
     * @param e The player interact event.
     */
    @EventHandler
    public void onWandClick(final PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR) {
            return;
        }
        final Player p = e.getPlayer();
        if (p.getInventory().getItemInHand().equals(wandGive.wand())) {
            if (RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.Admin.Portals") || RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.*")) {

                if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (selection.get(portalCommand.PortalMap("pos1", p)) != null) {
                        if (selection.get(portalCommand.PortalMap("pos1", p)).equals(e.getClickedBlock().getLocation())) {
                            e.setCancelled(true);
                            return;
                        }
                    }


                    final Block b = e.getClickedBlock();
                    final int x = b.getX();
                    final int y = b.getY();
                    final int z = b.getZ();
                    p.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.GREEN + "Pos 1 Set at " + x + " " + y + " " + z);
                    selection.put(portalCommand.PortalMap("pos1", p), b.getLocation());
                    e.setCancelled(true);
                } else {
                    if (selection.get(portalCommand.PortalMap("pos2", p)) != null) {
                        if (selection.get(portalCommand.PortalMap("pos2", p)).equals(e.getClickedBlock().getLocation())) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                    if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        final Block b = e.getClickedBlock();
                        final int x = b.getX();
                        final int y = b.getY();
                        final int z = b.getZ();
                        p.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.GREEN + "Pos 2 Set at " + x + " " + y + " " + z);
                        selection.put(portalCommand.PortalMap("pos2", p), b.getLocation());
                        e.setCancelled(true);
                    }
                }
            } else {
                messages.noPermission(e.getPlayer());

            }
        }
    }




}
