package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Set;

/**
 * Created by James on 04/07/2016.
 */
public class InNetherPortal implements Listener {

    private final PortalEnter pe = new PortalEnter();

    @EventHandler
    public void onPlayerTP(final PlayerTeleportEvent e) {
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            return;
        }
        if (RandomCoords.getPlugin().portals == null) {
            return;
        }
        if (RandomCoords.getPlugin().portals.getConfigurationSection("Portal") == null) {
            return;
        }
        if (RandomCoords.getPlugin().portals.getConfigurationSection("Portal").getKeys(false) == null) {
            return;
        }
        final Set<String> portals = RandomCoords.getPlugin().portals.getConfigurationSection("Portal").getKeys(false);
        for (final String name : portals) {

            final String world = RandomCoords.getPlugin().portals.getString("Portal." + name + ".world");
            if (Bukkit.getServer().getWorld(world) == null) {
                Bukkit.getServer().getLogger().severe(world + " is an invalid world, Change this portal!");
                return;

            }
            final String portalWorld = RandomCoords.getPlugin().portals.getString("Portal." + name + ".PortalWorld");
            if (Bukkit.getServer().getWorld(portalWorld) == null) {
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


            //  World worldW = Bukkit.getServer().getWorld(world);
            if (pe.isInside(e.getPlayer().getLocation(), l1, l2)) {
                e.setCancelled(true);
                return;
            }


        }


    }

 /*   public boolean inAll(final Location location) {
        if (RandomCoords.getPlugin().portals.get("Portal") == null) {
            return false;
        }
        final ConfigurationSection cs = RandomCoords.getPlugin().portals.getConfigurationSection("Portal.");
        for (final String name : cs.getKeys(false)) {
            final String world = RandomCoords.getPlugin().portals.getString("Portal." + name + ".world");
            if (Bukkit.getServer().getWorld(world) == null) {
                Bukkit.getServer().getLogger().severe(world + " is an invalid world, Change this portal!");
                return false;

            }
            final String portalWorld = RandomCoords.getPlugin().portals.getString("Portal." + name + ".PortalWorld");
            if (Bukkit.getServer().getWorld(portalWorld) == null) {
                Bukkit.getServer().getLogger().severe(portalWorld + "no longer exists");
                return false;

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


        //    World worldW = Bukkit.getServer().getWorld(world);
            return pe.isInside(location, l1, l2);

        }


        return false;
    }*/
}
