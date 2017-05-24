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
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.CoordinatesManager;
import com.jolbol1.RandomCoordinates.managers.Util.CoordType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

/**
 * Created by James on 06/02/2017.
 */
public class PlayerSwitchWorld implements Listener {

    private final Coordinates coordinates = new Coordinates();
    private final CoordinatesManager coordinatesManager = new CoordinatesManager();

    @EventHandler
    public void onPlayerSwitch(final PlayerTeleportEvent e) {
        if(RandomCoords.getPlugin().config.getStringList("OnJoinWorlds") == null )  { return; }
        if(e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL || e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            return;
        }
        final List<String> list = RandomCoords.getPlugin().config.getStringList("OnJoinWorlds");
        if(list.isEmpty()) { return; }
        if(e.getTo().getWorld() == e.getFrom().getWorld()) { return; }
        if(list.contains(e.getTo().getWorld().getName())) {
            if(e.getTo().getWorld().getPlayers().contains(e.getPlayer())) { return; }
            final Player p = e.getPlayer();
            //coordinates.finalCoordinates(p, 574272099, 574272099, e.getTo().getWorld(), CoordType.JOINWORLD, 0);
            coordinatesManager.randomlyTeleportPlayer(p, e.getTo().getWorld(), coordinatesManager.key, coordinatesManager.key, CoordType.JOINWORLD, 0);

        }


    }



}
