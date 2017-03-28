package com.jolbol1.RandomCoordinates.event;

/**
 * Created by James on 16/03/2017.
 */
import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.cooldown.Cooldown;
import com.jolbol1.RandomCoordinates.managers.CoordType;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;

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
