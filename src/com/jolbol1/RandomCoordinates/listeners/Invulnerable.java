package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.cooldown.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by James on 02/07/2016.
 */
public class Invulnerable implements Listener {
    /**
     * Checks if the player is damaged while in InvulTime.
     * @param e The damage event.
     */
    @EventHandler
    public void onAnyDamage(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        final Player p = (Player) e.getEntity();
        if (!Cooldown.isInCooldown(p.getUniqueId(), "InvulTime")) {
            return;
        }
        e.setCancelled(true);
    }


}
