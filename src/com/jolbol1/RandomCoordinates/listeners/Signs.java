package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by James on 04/07/2016.
 */
public class Signs implements Listener {

    private MessageManager messages = new MessageManager();
    private Coordinates coordinates = new Coordinates();

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (e.getLine(0) == null) {
            return;
        }
        if (!(e.getLine(0).equalsIgnoreCase("[RandomCoords]"))) {
            return;
        }
        if(RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.Admin.Sign") || RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.*")) {
            if (e.getLine(1).length() == 0 || e.getLine(1) == null) {
                e.setLine(0, ChatColor.GREEN + "[RandomCoords]");
                messages.signCreated(e.getPlayer());
            } else if(e.getLine(2).length() == 0 || e.getLine(2) == null) {
                String wName = e.getLine(1).replaceAll("\uF701", "");
                if (wName.length() == 0 || wName == null || wName == "") {
                    return;
                }
                if (e.getPlayer().getServer().getWorld(wName) == null) {
                    messages.invalidWorld(e.getPlayer(), wName);
                    return;
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
