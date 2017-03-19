package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.managers.CoordType;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.Set;

/**
 * Created by James on 04/07/2016.
 */
public class PortalEnter extends BukkitRunnable {

    private final Coordinates coordinates = new Coordinates();
    private final MessageManager messages = new MessageManager();

    /**
     * This is the class that checks if a player has entered a portal.
     * This is a Runnable initiated from the OnEnable in the main class.
     */
    @Override
    public void run() {

        if(!RandomCoords.getPlugin().skyBlock.isEmpty() && !RandomCoords.getPlugin().skyBlockSave.getString("SkyBlock").equalsIgnoreCase("none")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (RandomCoords.getPlugin().skyBlock.containsKey(p.getUniqueId())) {
                    if (Material.getMaterial(RandomCoords.getPlugin().skyBlockSave.getString("SkyBlock")) != null) {

                        Material material = Material.getMaterial(RandomCoords.getPlugin().skyBlockSave.getString("SkyBlock"));

                        if (RandomCoords.getPlugin().skyBlock.get(p.getUniqueId()).getBlock().getType().equals(Material.AIR)) {

                            RandomCoords.getPlugin().skyBlock.get(p.getUniqueId()).getBlock().setType(material);
                        } else if(!RandomCoords.getPlugin().skyBlock.get(p.getUniqueId()).getBlock().getType().equals(material)) {
                            RandomCoords.getPlugin().skyBlock.remove(p.getUniqueId());

                        }
                        if(RandomCoords.getPlugin().skyBlockSave.getString("AutoRemove").equalsIgnoreCase("true")) {
                            if(p.getLocation().getWorld().equals(RandomCoords.getPlugin().skyBlock.get(p.getUniqueId()).getWorld())) {
                                if (RandomCoords.getPlugin().skyBlock.get(p.getUniqueId()).distance(p.getLocation().subtract(0, 1, 0)) > 2.5) {
                                    RandomCoords.getPlugin().skyBlock.get(p.getUniqueId()).getBlock().setType(Material.AIR);
                                    RandomCoords.getPlugin().skyBlock.remove(p.getUniqueId());
                                }
                            }
                        }
                    }
                }
            }
        }


        /**
         * If there is no portals, Return.
         */
        if (RandomCoords.getPlugin().portals.get("Portal") == null) {
            return;
        }

        //Gets a list of the portals now that we know its not null.
        final Set<String> portals = RandomCoords.getPlugin().portals.getConfigurationSection("Portal").getKeys(false);
        /**
         * For all of these portals, Check if anyone online is within the portal.
         */
        for (final String name : portals) {

            //Gets the world that the portal will go to.
            final String world = RandomCoords.getPlugin().portals.getString("Portal." + name + ".world");
            /**
             * If this is null then return and log.
             */
            if (Bukkit.getServer().getWorld(world) == null) {
                //Log the fact that the world is non existent.
                Bukkit.getServer().getLogger().severe(world + " is an invalid world, Change this portal!");
                return;

            }
            //Get the world that the portal is in.
            final String portalWorld = RandomCoords.getPlugin().portals.getString("Portal." + name + ".PortalWorld");
            /**
             * Checks if the world the portal is in is null, If so return and log.
             */
            if (Bukkit.getServer().getWorld(portalWorld) == null) {
                //Log the fact that the world is null.
                Bukkit.getServer().getLogger().severe(portalWorld + "no longer exists");
                return;

            }
            //Get the world that the portal is in.
            final World w = Bukkit.getServer().getWorld(portalWorld);
            /**
             * Get one corner of the portal.
             */
            final int p1y = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1y");
            final int p1z = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1z");
            final int p1x = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p1x");

            /**
             * Get another corner of the portal.
             */
            final int p2y = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2y");
            final int p2z = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2z");
            final int p2x = RandomCoords.getPlugin().portals.getInt("Portal." + name + ".p2x");
            /**
             * Get the locations of these corners.
             */
            final Location l1 = new Location(w, p1x, p1y, p1z);//NOPMD
            final Location l2 = new Location(w, p2x, p2y, p2z);//NOPMD
            /**
             * For all the players on the world of this portal.
             * Check is the targetWorld is banned.
             * Check if the player is in this portal.
             */
            for (final Player player : Bukkit.getServer().getWorld(portalWorld).getPlayers()) {
                //Gets the world that the portal is going to.
                final World worldW = Bukkit.getServer().getWorld(world);
                /**
                 * If the player is inside of the portal, Then initiate the teleport process if need be.
                 */
                if (isInside(player.getLocation(), l1, l2)) {
                    /**
                     * For the list of worlds, Check if its banned thus return.
                     */
                    for (final String worlds : RandomCoords.getPlugin().config.getStringList("BannedWorlds")) {
                        /**
                         * If the world is in the banned list return and message.
                         */
                        if (worldW.getName().equals(worlds)) {
                            //Send the message that the world is banned.
                            messages.worldBanned(player);
                            return;

                        }
                    }
                    /**
                     * Check firstly if they have the permission to use the portal.
                     */
                    if (RandomCoords.getPlugin().hasPermission(player, "Random.PortalUse") || RandomCoords.getPlugin().hasPermission(player, "Random.Basic") || RandomCoords.getPlugin().hasPermission(player, "Random.*")) {
                        /**
                         * If the player has the money to use the portal, then continue. Else message and return.
                         */
                        if (!coordinates.hasMoney(player, RandomCoords.getPlugin().config.getDouble("PortalCost"))) {
                            //Sends the message to the player that they have not paid for the portal.
                            messages.cost(player, RandomCoords.getPlugin().config.getDouble("PortalCost"));
                            return;
                        }
                        //Uses the coordinates class to finally send the player to the random spot.
                        coordinates.finalCoordinates(player, 574272099, 574272099, worldW, CoordType.PORTAL, 0);
                    } else {
                        //Messages them that they have no permission to use portals.
                        messages.noPermission(player);
                        return;
                    }
                    return;
                }

            }

        }
    }

    /**
     * Checks if a plyer is inside the portal. (MOVE THIS!) DUPED CODE!
      * @param loc The players location.
     * @param l1 Corner 1 of the portal.
     * @param l2 Corner 2 of the portal.
     * @return True or False, are they in the portal.
     */
    public boolean isInside(final Location loc, final Location l1, final Location l2) {
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
