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

package com.jolbol1.RandomCoordinates.event;

/**
 * Created by James on 16/03/2017.
 */

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.Util.CoordType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class RandomTeleportEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Location location;
    private CoordType coordType;
    private int cooldown;
    private boolean cancelled;
    private Coordinates coordinates = new Coordinates();

    public RandomTeleportEvent(Player player, Location location, CoordType coordType, int cooldown) {
        this.player = player;
        this.location = location;
        this.coordType = coordType;
        this.cooldown = cooldown;

        switch (coordType) {
            case ALL:
                RandomCoords.getPlugin().allTeleport++;
                break;
            case JOIN:
                RandomCoords.getPlugin().onJoin++;
                break;
            case JOINWORLD:
                RandomCoords.getPlugin().onJoin++;
                break;
            case WARPS:
                RandomCoords.getPlugin().warpTeleport++;
                break;
            case WARPWORLD:
                RandomCoords.getPlugin().warpTeleport++;
                break;
            case COMMAND:
                RandomCoords.getPlugin().commandTeleport++;
                break;
            case PLAYER:
                RandomCoords.getPlugin().otherTeleport++;
                break;
            case PORTAL:
                RandomCoords.getPlugin().portalTeleport++;
                break;
            case SIGN:
                RandomCoords.getPlugin().signTeleport++;
                break;

        }

    }

    public Player getPlayer() {
        return player;
    }

    public CoordType coordType(){
        return coordType;
    }

    public int cooldown(){
        return cooldown;
    }

    public Location location() {
        return location;
    }



    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
