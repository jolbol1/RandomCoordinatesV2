package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.managers.CoordType;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by James on 04/07/2016.
 */
public class onJoin implements Listener {

    private Coordinates coordinates = new Coordinates();
    private MessageManager messages = new MessageManager();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(RandomCoords.getPlugin().config.getString("OnJoin").equalsIgnoreCase("false")) { return; }
        if(RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.OnJoin") || RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.*")) {
            if (p.hasPlayedBefore()) {
                return;
            }
            String command = RandomCoords.getPlugin().config.getString("OnJoinCommand");
            coordinates.finalCoordinates(p, 574272099, 574272099, p.getWorld(), CoordType.JOIN, 0);
            messages.onJoin(p);
            if (command.equalsIgnoreCase("none")) {
                return;
            }
            p.getServer().dispatchCommand(p, command);
        }

    }



}