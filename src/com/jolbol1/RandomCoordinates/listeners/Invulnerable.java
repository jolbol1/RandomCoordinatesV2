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

import com.jolbol1.RandomCoordinates.cooldown.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by James on 02/07/2016.
 */
public class Invulnerable implements Listener {
    /**
     * Checks if the player is damaged while in InvulTime.
     * @param e The damage event.
     */
    @EventHandler
    public void onAnyDamage(final EntityDamageEvent e) {
        /**
         * If the damage was not caused to a player, then dont run the code.
         */
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        //Get the player the damage was caused to.
        final Player p = (Player) e.getEntity();
        /**
         * Are they in the Invul cooldown? If no, return.
         */
        if (!Cooldown.isInCooldown(p.getUniqueId(), "InvulTime")) {
            return;
        }

        //Otherwise cancel the event.
        e.setCancelled(true);
    }


}
