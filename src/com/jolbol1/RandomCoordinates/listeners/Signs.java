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
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 * Created by James on 04/07/2016.
 */
public class Signs implements Listener {

    private final MessageManager messages = new MessageManager();

    /**
     * Detects when a player creates a RC sign.
     * @param e The sign change event.
     */
    @EventHandler
    public void onSignChange(final SignChangeEvent e) {
        if (e.getLine(0) == null) {
            return;
        }
        if (!(e.getLine(0).equalsIgnoreCase("[RandomCoords]"))) {
            return;
        }
        if (RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.Admin.Sign") || RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.*")) {
            if (e.getLine(1).length() == 0 || e.getLine(1) == null) {
                e.setLine(0, ChatColor.GREEN + "[RandomCoords]");
                messages.signCreated(e.getPlayer());
            } else if (e.getLine(2).length() == 0 || e.getLine(2) == null) {
                final String wName = e.getLine(1).replaceAll("\uF701", "");
                if (wName.length() == 0 || wName.equals("")) {
                    return;
                }
                if (e.getPlayer().getServer().getWorld(wName) == null) {
                    messages.invalidWorld(e.getPlayer(), wName);
                } else {
                    e.setLine(0, ChatColor.GREEN + "[RandomCoords]");
                    messages.signCreated(e.getPlayer());
                }
            } else {
                e.setLine(0, ChatColor.GREEN + "[RandomCoords]");
                messages.signCreated(e.getPlayer());
            }
        } else {
            messages.noPermission(e.getPlayer());

        }


    }


}
