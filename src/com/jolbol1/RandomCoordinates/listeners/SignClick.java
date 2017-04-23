package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.CoordinatesManager;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import com.jolbol1.RandomCoordinates.managers.Util.CoordType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

/**
 * Created by James on 04/07/2016.
 */
public class SignClick implements Listener {

    private final MessageManager messages = new MessageManager();
    private final Coordinates coordinates = new Coordinates();
    private final CoordinatesManager coordinatesManager = new CoordinatesManager();

    /**
     * Gets when a player clicks a sign, and if its a RC sign.
     * @param e The player interact event.
     */
    @EventHandler
    public void onClickEvent(final PlayerInteractEvent e) {
        double cost = 0;
        String line1 = null;
        if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {
                final Sign sign = (Sign) e.getClickedBlock().getState();
                if (sign.getLine(0).equalsIgnoreCase(ChatColor.GREEN + "[RandomCoords]")) {
                    if ((RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.Admin.Sign") || RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.*")) && e.getAction() == Action.LEFT_CLICK_BLOCK) {
                        e.setCancelled(false);
                        return;
                    }
                    if (RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.SignUse") || RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.Basic")) {
                        World world = sign.getWorld();
                        if (sign.getLine(1) != null) {
                            line1 = sign.getLine(1).replaceAll("\uF701", "");
                        }
                        if ((line1 != null ? line1.length() : 0) != 0) {
                            if (e.getPlayer().getServer().getWorld(sign.getLine(1).replaceAll("\uF701", "")) == null) {
                                messages.invalidWorld(e.getPlayer(), sign.getLine(1));
                            } else {
                                world = e.getPlayer().getServer().getWorld(sign.getLine(1).replaceAll("\uF701", ""));
                            }


                        }
                        if (sign.getLine(2).replaceAll("\uF701", "").length() != 0 && sign.getLine(2).replaceAll("\uF701", "") != null && !Objects.equals(sign.getLine(2).replaceAll("\uF701", ""), "") && !Objects.equals(sign.getLine(2).replaceAll("\uF701", ""), " ")) {
                            try {
                                cost = Double.valueOf(sign.getLine(2).replaceAll("\uF701", ""));
                            } catch (NumberFormatException Ne) {
                                Bukkit.getServer().getLogger().severe(sign.getLine(2).replaceAll("\uF701", "") + " is not a price!");
                            }

                        }
                        if (coordinates.hasMoney(e.getPlayer(), cost)) {
                            if(!RandomCoords.getPlugin().config.getString("Experimental").equalsIgnoreCase("true")) {
                                coordinates.finalCoordinates(e.getPlayer(), 574272099, 574272099, world, CoordType.SIGN, cost);
                            } else {
                                coordinatesManager.randomlyTeleportPlayer(e.getPlayer(), world, coordinatesManager.key, coordinatesManager.key, CoordType.SIGN, cost);
                            }
                        } else {
                            messages.cost(e.getPlayer(), cost);
                        }

                    } else {
                        messages.noPermission(e.getPlayer());
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

}
