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
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by James on 02/07/2016.
 */
public class Suffocation implements Listener {
    /**
     * Checks if the player is suffocating on teleport, a re-teleports them higher.
     * @param e The entity damage event.
     */
    @EventHandler
    public void OnSuffocation(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (e.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION) {
            return;
        }
        final Player p = (Player) e.getEntity();

        //Are they in the Invul Cooldown initiated in FinalCoordinates of Coordinate Class?
        if (!Cooldown.isInCooldown(p.getUniqueId(), "Invul")) {
            return;
        }
        e.setCancelled(true);
        final int highestY = p.getWorld().getHighestBlockYAt(p.getLocation());
        final double x = p.getLocation().getX();
        final double z = p.getLocation().getZ();
        final double xR = Math.floor(x) + 0.5;
        final double zR = Math.floor(z) + 0.5;
        final Location reTry = new Location(p.getWorld(), xR, highestY + 2, zR);
        p.teleport(reTry);


    }


}
