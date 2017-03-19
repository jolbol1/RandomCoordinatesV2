package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.event.RandomTeleportEvent;
import com.jolbol1.RandomCoordinates.managers.CoordType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

/**
 * Created by James on 16/03/2017.
 */
public class RandomTeleportListener implements Listener {


    /**
     *Event that is called when a player RandomlyTeleports
     * This is to handle the SkyBlock feature.
     * This even considers the players location as the one before teleporting and e.location as the one they are going to.
     * @param e
     */
    @EventHandler
    public void onRandomTeleport(RandomTeleportEvent e) {


        Location skyBlock = new Location(e.location().getWorld(), e.location().getX(), e.location().getY() - 2, e.location().getZ());

    if(!RandomCoords.getPlugin().skyBlockSave.getString("SkyBlock").equalsIgnoreCase("none")) {
        if (Material.getMaterial(RandomCoords.getPlugin().skyBlockSave.getString("SkyBlock")) != null) {
            if(RandomCoords.getPlugin().skyBlockSave.getStringList("SkyBlockWorlds").contains(e.getPlayer().getWorld().getName())) {
                Material material = Material.getMaterial(RandomCoords.getPlugin().skyBlockSave.getString("SkyBlock"));

                if (RandomCoords.getPlugin().skyBlock.containsKey(e.getPlayer().getUniqueId())) {
                    if (RandomCoords.getPlugin().skyBlockSave.getString("AutoRemove").equalsIgnoreCase("true")) {
                        RandomCoords.getPlugin().skyBlock.get(e.getPlayer().getUniqueId()).getBlock().setType(Material.AIR);
                    }
                }
                skyBlock.getBlock().setType(material);
                RandomCoords.getPlugin().skyBlock.put(e.getPlayer().getUniqueId(), skyBlock.getBlock().getLocation());
            }
        }
    }



    }



}
