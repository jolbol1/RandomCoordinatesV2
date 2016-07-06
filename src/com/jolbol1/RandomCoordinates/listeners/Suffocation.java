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

    @EventHandler
    public void OnSuffocation(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player)) { return; }
        if(e.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION ) { return; }
        Player p = (Player) e.getEntity();

        //Are they in the Invul Cooldown initiated in FinalCoordinates of Coordinate Class?
        if(!Cooldown.isInCooldown(p.getUniqueId(), "Invul")) { return; }
        e.setCancelled(true);
        int highestY = p.getWorld().getHighestBlockYAt(p.getLocation());
        Location reTry = new Location(p.getWorld(), p.getLocation().getBlockX(), highestY + 2, p.getLocation().getBlockZ());
        p.teleport(reTry);
        p.sendMessage("SuffocationFailed!");

    }



}
