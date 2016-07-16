package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import java.util.Set;

/**
 * Created by James on 04/07/2016.
 */
public class BlockPortal implements Listener {

    @EventHandler
    public void blockPhysics(final BlockPhysicsEvent e) {
        if(RandomCoords.getPlugin().portals.get("Portal")== null) { return; }
        final Set<String> portals = RandomCoords.getPlugin().portals.getConfigurationSection("Portal").getKeys(false);
        for (final String name : portals) {


            final String world = RandomCoords.getPlugin().portals.getString("Portal." + name + ".world");
            if(Bukkit.getServer().getWorld(world) == null) {
                Bukkit.getServer().getLogger().severe(world + " is an invalid world, Change this portal!");
                return;

            }
            final String portalWorld = RandomCoords.getPlugin().portals.getString("Portal." + name + ".PortalWorld");

            if(Bukkit.getServer().getWorld(portalWorld) == null) {
                Bukkit.getServer().getLogger().severe(portalWorld + "no longer exists");
                return;

            }
            final World w = Bukkit.getServer().getWorld(portalWorld);
            final int p1y = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1y");
            final int p1z = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1z");
            final int p1x = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1x");

            final int p2y = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2y");
            final int p2z = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2z");
            final int p2x = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2x");
            final Location l1 = new Location(w, p1x, p1y, p1z);//NOPMD
            final Location l2 = new Location(w, p2x, p2y, p2z);//NOPMD

                if(isInside(e.getBlock().getLocation(), l1, l2)) {
                    e.setCancelled(true);
                    return;
                }


        }
    }

    //Written by desht
    private boolean isInside(final Location loc, final Location l1, final Location l2) {
        final int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        final int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        final int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        final int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        final int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        final int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
        final int x = loc.getBlockX();
        final int y = loc.getBlockY();
        final int z = loc.getBlockZ();

        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
    }

}




