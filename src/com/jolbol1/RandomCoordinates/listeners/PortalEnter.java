package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.managers.CoordType;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

/**
 * Created by James on 04/07/2016.
 */
public class PortalEnter extends BukkitRunnable {

    private Coordinates coordinates = new Coordinates();
    private MessageManager messages = new MessageManager();

    @Override
    public void run() {
        if (RandomCoords.getPlugin().portals.get("Portal") == null) {
            return;
        }
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
            for (Player player : Bukkit.getServer().getWorld(portalWorld).getPlayers()) {
                    World worldW = Bukkit.getServer().getWorld(world);
                    for (String worlds : RandomCoords.getPlugin().config.getStringList("BannedWorlds")) {
                        if (worldW.getName().equals(worlds)) {
                            messages.worldBanned(player);
                            return;

                        }
                    }
                    if (isInside(player.getLocation(), l1, l2)) {
                        if (RandomCoords.getPlugin().hasPermission(player, "Random.PortalUse") || RandomCoords.getPlugin().hasPermission(player, "Random.Basic") || RandomCoords.getPlugin().hasPermission(player, "Random.*")) {
                          if(!RandomCoords.getPlugin().hasMoney(player, RandomCoords.getPlugin().config.getDouble("PortalCost"))) {
                              messages.cost(player, RandomCoords.getPlugin().config.getDouble("PortalCost"));
                              return;
                          }
                            coordinates.finalCoordinates(player, 574272099, 574272099, worldW, CoordType.PORTAL, 0);
                        } else {
                            messages.noPermission(player);
                            return;
                        }
                        return;
                    }

                }

        }
    }

    //Written by desht
    public boolean isInside(Location loc, Location l1, Location l2) {
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
