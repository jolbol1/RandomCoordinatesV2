package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.cooldown.Cooldown;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by James on 02/07/2016.
 */
public class Suffocation implements Listener {
    /**
     * Checks if the player is suffocating on teleport, a re-teleports them higher.
     * @param e The entity damage event.
     */
    @EventHandler
    public void OnSuffocation(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (e.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION) {
            return;
        }
        final Player p = (Player) e.getEntity();

        //Are they in the Invul Cooldown initiated in FinalCoordinates of Coordinate Class?
        if (!Cooldown.isInCooldown(p.getUniqueId(), "Invul")) {
            return;
        }
        e.setCancelled(true);
        final int highestY = p.getWorld().getHighestBlockYAt(p.getLocation());
        double x = p.getLocation().getX();
        double z = p.getLocation().getZ();
        double xR = Math.floor(x) + 0.5;
        double zR = Math.floor(z) + 0.5;
        final Location reTry = new Location(p.getWorld(), xR, highestY + 2, zR);
        p.teleport(reTry);
        return;

    }


}
