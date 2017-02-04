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
        /**
         * If the damage was not caused to a player, then dont run the code.
         */
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        //Get the player the damage was caused to.
        final Player p = (Player) e.getEntity();
        /**
         * Are they in the Invul cooldown? If no, return.
         */
        if (!Cooldown.isInCooldown(p.getUniqueId(), "InvulTime")) {
            return;
        }

        //Otherwise cancel the event.
        e.setCancelled(true);
    }


}
