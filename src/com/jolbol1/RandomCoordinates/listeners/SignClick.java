package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.managers.CoordType;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by James on 04/07/2016.
 */
public class SignClick implements Listener {

   private MessageManager messages = new MessageManager();
    private Coordinates coordinates = new Coordinates();

    @EventHandler
    public void onClickEvent(PlayerInteractEvent e) {
        double cost = 0;
        String line1 = null;
            if(e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {
                    Sign sign = (Sign) e.getClickedBlock().getState();
                if(sign.getLine(0).equalsIgnoreCase(ChatColor.GREEN + "[RandomCoords]")) {
                    if((RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.Admin.Sign") || RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.*")) && e.getAction() == Action.LEFT_CLICK_BLOCK) {
                        e.setCancelled(false);
                        return;
                    }
                    if (RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.SignUse")) {
                        World world = sign.getWorld();
                        if(sign.getLine(1) != null) {
                            line1 = sign.getLine(1).replaceAll("\uF701", "");
                        }
                        if (line1.length() != 0 ) {
                            if (e.getPlayer().getServer().getWorld(sign.getLine(1).replaceAll("\uF701", "")) == null) {
                                messages.invalidWorld(e.getPlayer(), sign.getLine(1));
                            } else {
                                world = e.getPlayer().getServer().getWorld(sign.getLine(1).replaceAll("\uF701", ""));
                            }


                        }
                        if(sign.getLine(2).length() != 0 || sign.getLine(2) != null ) {
                            try {
                                cost = Double.valueOf(sign.getLine(2).replaceAll("\uF701", ""));
                            } catch (NumberFormatException Ne) {
                                Bukkit.getServer().getLogger().severe(sign.getLine(2).replaceAll("\uF701", "") + " is not a price!");
                            }

                        }
                        if(RandomCoords.getPlugin().hasMoney(e.getPlayer(), cost)) {
                            coordinates.finalCoordinates(e.getPlayer(), 574272099, 574272099, world, CoordType.SIGN, cost);
                        } else {
                            messages.cost(e.getPlayer(), cost);
                            return;
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
