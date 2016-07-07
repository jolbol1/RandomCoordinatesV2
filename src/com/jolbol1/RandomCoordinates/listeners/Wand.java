package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import com.sk89q.worldedit.event.platform.BlockInteractEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

/**
 * Created by James on 04/07/2016.
 */
public class Wand implements Listener {

    private Map selection = RandomCoords.getPlugin().wandSelection;
    private MessageManager messages = new MessageManager();


    @EventHandler
    public void onWandClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR) { return; }
        if(p.getInventory().getItemInMainHand().equals(RandomCoords.getPlugin().wand())) {
            if (RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.Admin.Portals") || RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.*")) {

                if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (selection.get(PortalMap("pos1", p)) != null) {
                        if (selection.get(PortalMap("pos1", p)).equals(e.getClickedBlock().getLocation())) {
                            e.setCancelled(true);
                            return;
                        }
                    }


                    Block b = e.getClickedBlock();
                    int x = b.getX();
                    int y = b.getY();
                    int z = b.getZ();
                    p.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.GREEN + "Pos 1 Set at " + x + " " + y + " " + z);
                    selection.put(PortalMap("pos1", p), b.getLocation());
                    e.setCancelled(true);
                } else {
                    if (selection.get(PortalMap("pos2", p)) != null) {
                        if (selection.get(PortalMap("pos2", p)).equals(e.getClickedBlock().getLocation())) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                    if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        Block b = e.getClickedBlock();
                        int x = b.getX();
                        int y = b.getY();
                        int z = b.getZ();
                        p.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.GREEN + "Pos 2 Set at " + x + " " + y + " " + z);
                        selection.put(PortalMap("pos2", p), b.getLocation());
                        e.setCancelled(true);
                    }
                }
            } else {
                messages.noPermission(e.getPlayer());

            }
        }
    }

    private String PortalMap(String position, Player p) {
        return RandomCoords.getPlugin().PortalMap(position, p);
    }





}
