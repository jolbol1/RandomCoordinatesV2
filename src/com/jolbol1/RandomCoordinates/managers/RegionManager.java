/*
 *     RandomCoords, Provding the best Bukkit Random Teleport Plugin
 *     Copyright (C) 2014  James Shopland
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.jolbol1.RandomCoordinates.managers;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by James on 23/03/2017.
 */
public class RegionManager {

    private MessageManager messages = new MessageManager();
    private Coordinates coordinates = new Coordinates();


    public Map<String, String> allRegionList(CommandSender sender) {
        if (!worldGuardInstalled(sender)) {
            return null;
        }
        Map regionList = new HashMap<String, World>();
        for (World w : Bukkit.getServer().getWorlds()) {
            if (RandomCoords.getPlugin().getWorldGuard().getRegionManager(w) != null) {
                com.sk89q.worldguard.protection.managers.RegionManager regionManager = RandomCoords.getPlugin().getWorldGuard().getRegionManager(w);
                if (regionManager.getRegions().values() != null || regionManager.getRegions() != null || !regionManager.getRegions().isEmpty()) {
                    for (ProtectedRegion region : regionManager.getRegions().values()) {
                        regionList.put(region.getId(), w.getName());

                    }

                }


            }
        }
        if (regionList.isEmpty() || regionList.values() == null || regionList == null) {
            return null;
        }

        return regionList;


    }

    public boolean worldGuardInstalled(CommandSender sender) {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null) {
            sender.sendMessage("WG not supported.");
            return false;
        }

        return true;
    }

    public Selection getSelectionFromRegion(Player player, String regionName, World world) {

        WorldEditPlugin worldEdit = null;
        try {
            worldEdit = RandomCoords.getPlugin().getWorldGuard().getWorldEdit();
        } catch (CommandException e) {
            e.printStackTrace();
            return null;
        }

        com.sk89q.worldguard.protection.managers.RegionManager regionManager = RandomCoords.getPlugin().getWorldGuard().getRegionManager(world);

        ProtectedRegion region = regionManager.getRegion(regionName);

        // Set selection
        if (region instanceof ProtectedCuboidRegion) {
            ProtectedCuboidRegion cuboid = (ProtectedCuboidRegion) region;
            Vector pt1 = cuboid.getMinimumPoint();
            Vector pt2 = cuboid.getMaximumPoint();
            CuboidSelection selection = new CuboidSelection(world, pt1, pt2);
            return selection;

        } else if (region instanceof ProtectedPolygonalRegion) {
            ProtectedPolygonalRegion poly2d = (ProtectedPolygonalRegion) region;
            Polygonal2DSelection selection = new Polygonal2DSelection(
                    world, poly2d.getPoints(),
                    poly2d.getMinimumPoint().getBlockY(),
                    poly2d.getMaximumPoint().getBlockY());
            //worldEdit.setSelection(player, selection);
            return selection;

        } else {
            messages.incorrectUsage(player, "/RC region [regionName] {player} -> {} = Optional");
            return null;
        }


    }

    public List<Block> blockList(Player player, Selection selection) {
        ArrayList<Block> blocks = new ArrayList<Block>();

        World world = selection.getWorld();

        if (selection instanceof Polygonal2DSelection) {
            Polygonal2DRegion poly = null;
            try {
                poly = (Polygonal2DRegion) selection.getRegionSelector().getRegion();
            } catch (IncompleteRegionException e) {
                e.printStackTrace();
            }

            for (BlockVector block : poly) {
                Location l = new Location(world, block.getX(), block.getY(), block.getZ());
                Block b = l.getBlock();
                blocks.add(b);
                // Do something with the block
            }
        } else if (selection instanceof CuboidSelection) {
            CuboidRegion cub = null;
            try {
                cub = (CuboidRegion) selection.getRegionSelector().getRegion();
            } catch (IncompleteRegionException e) {
                e.printStackTrace();
            }
            for (BlockVector block : cub) {
                Location l = new Location(world, block.getX(), block.getY(), block.getZ());
                Block b = l.getBlock();
                blocks.add(b);
                // Do something with the block
            }


        }

        return blocks;
    }


    public void teleportRandomlyInRegion(Player p, World w, Selection selection) {
        List<Block> blocks = blockList(p, selection);
        long seed = System.nanoTime();
        Collections.shuffle(blocks, new Random(seed));

        Block b = blocks.get(coordinates.getRandomNumberInRange(0, blocks.size() - 1, p));
        int y = b.getLocation().getWorld().getHighestBlockYAt(b.getLocation());
        Location finalLoc = new Location(b.getWorld(), b.getX() + 0.5, y, b.getZ() + 0.5);
        p.teleport(finalLoc);


    }
}
