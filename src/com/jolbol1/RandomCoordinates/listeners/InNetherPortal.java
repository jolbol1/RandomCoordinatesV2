package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Set;

/**
 * Created by James on 04/07/2016.
 */
public class InNetherPortal implements Listener {

    private PortalEnter pe = new PortalEnter();

    @EventHandler
    public void onPlayerTP(PlayerTeleportEvent e) {
        if(e.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) { return; }
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
            if(pe.isInside(e.getPlayer().getLocation(), l1, l2)) {
                e.setCancelled(true);
                return;
            }


        }



    }

    public boolean inAll(Location location) {
        if (RandomCoords.getPlugin().portals.get("Portal") == null) {
            return false;
        }
        ConfigurationSection cs = RandomCoords.getPlugin().portals.getConfigurationSection("Portal.");
        for (String name : cs.getKeys(false)) {

            int p1y = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1y");
            int p1z = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1z");
            int p1x = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1x");

            int p2y = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2y");
            int p2z = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2z");
            int p2x = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2x");
            String portalWorld = RandomCoords.getPlugin().portals.getString("Portal." + name + ".PortalWorld");
            String world = RandomCoords.getPlugin().portals.getString("Portal." + name + ".world");
            if (Bukkit.getServer().getWorld(world) == null) {
                Bukkit.getServer().getLogger().severe(world + " is an invalid world, Change this portal!");
                return false;

            }
            if (Bukkit.getServer().getWorld(portalWorld) == null) {
                Bukkit.getServer().getLogger().severe(portalWorld + "no longer exists");
                return false;

            }
            World w = Bukkit.getServer().getWorld(portalWorld);
            Location l1 = new Location(w, p1x, p1y, p1z);
            Location l2 = new Location(w, p2x, p2y, p2z);


            World worldW = Bukkit.getServer().getWorld(world);
            if (pe.isInside(location, l1, l2)) {
                return true;
            } else {
                return false;
            }

        }


        return false;
    }
}
