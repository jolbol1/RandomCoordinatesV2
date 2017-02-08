package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.managers.CoordType;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.Bukkit;
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
    private final MessageManager messages = new MessageManager();

    @EventHandler
    public void onPlayerSwitch(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if(RandomCoords.getPlugin().config.getStringList("OnJoinWorlds") == null )  { return; }
        List<String> list = RandomCoords.getPlugin().config.getStringList("OnJoinWorlds");
        if(list.isEmpty()) return;
        if(e.getTo().getWorld() == e.getFrom().getWorld()) { return; }
        if(list.contains(e.getTo().getWorld().getName())) {
            if(e.getTo().getWorld().getPlayers().contains(e.getPlayer())) { return; }
            coordinates.finalCoordinates(p, 574272099, 574272099, e.getTo().getWorld(), CoordType.JOINWORLD, 0);

        }


    }



}
