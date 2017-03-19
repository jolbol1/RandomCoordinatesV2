package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Map;
import java.util.UUID;

/**
 * Created by James on 16/03/2017.
 */
public class SkyBlockBreak implements Listener {




    @EventHandler
    public void onBlockExplodeEvent(BlockExplodeEvent e) {
        for(Block b : e.blockList()) {
            blockManager(b);
        }

    }
    @EventHandler
    public void onentExplodevent(EntityExplodeEvent e) {
        for(Block b : e.blockList()) {
            blockManager(b);
        }

    }

    @EventHandler
    public void onBlockBurnEvent(BlockBurnEvent e) {
        blockManager(e.getBlock());

    }
    @EventHandler
    public void onBlockDamageEvent(BlockBreakEvent e) {
        if(RandomCoords.getPlugin().skyBlock.containsValue(e.getBlock().getLocation())) {
            for(Map.Entry<UUID, Location> entry : RandomCoords.getPlugin().skyBlock.entrySet()) {
                if(entry.getValue().equals(e.getBlock().getLocation())) {
                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);
                    RandomCoords.getPlugin().skyBlock.remove(entry.getKey());
                }
            }
        }


    }
    @EventHandler
    public void onBlockPistonRetractEvent(BlockPistonRetractEvent e) {
        for(Block b: e.getBlocks()) {
            blockManager(b);
        }
    }
    @EventHandler
    public void onBlockPistonExtendEvent(BlockPistonExtendEvent e) {

        for(Block b: e.getBlocks()) {
            blockManager(b);
        }
    }
    @EventHandler
    public void onBlockEntityEvent(EntityChangeBlockEvent e) {
        blockManager(e.getBlock());
    }




    public void blockManager(Block b) {

        if(RandomCoords.getPlugin().skyBlock.containsValue(b.getLocation())) {
            for(Map.Entry<UUID, Location> entry : RandomCoords.getPlugin().skyBlock.entrySet()) {
                if(entry.getValue().equals(b.getLocation())) {
                    RandomCoords.getPlugin().skyBlock.remove(entry.getKey());
                }
            }
        }
    }







}
