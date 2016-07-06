package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Set;

/**
 * Created by James on 04/07/2016.
 */
public class BlockPortal implements Listener {

    private PortalEnter pe = new PortalEnter();
    private InNetherPortal inNetherPortal = new InNetherPortal();
    @EventHandler
    public void blockPhysics(BlockPhysicsEvent e) {
        Set<String> portals = RandomCoords.getPlugin().portals.getConfigurationSection("Portal").getKeys(false);
        for (String name : portals) {

            int p1y = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1y");
            int p1z = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1z");
            int p1x = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1x");

            int p2y = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2y");
            int p2z = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2z");
            int p2x = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2x");
            String portalWorld = RandomCoords.getPlugin().portals.getString("Portal." + name + ".PortalWorld");
            String world = RandomCoords.getPlugin().portals.getString("Portal." + name + ".world");
            if(Bukkit.getServer().getWorld(world) == null) {
                Bukkit.getServer().getLogger().severe(world + " is an invalid world, Change this portal!");
                return;

            }
            if(Bukkit.getServer().getWorld(portalWorld) == null) {
                Bukkit.getServer().getLogger().severe(portalWorld + "no longer exists");
                return;

            }
            World w = Bukkit.getServer().getWorld(portalWorld);
            Location l1 = new Location(w, p1x, p1y, p1z);
            Location l2 = new Location(w, p2x, p2y, p2z);

                World worldW = Bukkit.getServer().getWorld(world);
                if(isInside(e.getBlock().getLocation(), l1, l2)) {
                    e.setCancelled(true);
                    return;
                }


        }
    }

    //Written by desht
    private boolean isInside(Location loc, Location l1, Location l2) {
        int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
    }

}




