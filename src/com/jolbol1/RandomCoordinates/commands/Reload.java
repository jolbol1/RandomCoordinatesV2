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

package com.jolbol1.RandomCoordinates.commands;
import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.checks.GriefPreventionCheck;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import com.jolbol1.RandomCoordinates.managers.BonusChestManager;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;


/**
 * Created by James on 02/07/2016.
 */
public class Reload implements CommandInterface {

    private final MessageManager messages = new MessageManager();

    @Override
    public void onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (args.length == 1) {
            if (RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.Reload") || RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(sender, "Random.*")) {
                if (args[0].equalsIgnoreCase("reload")) {
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().language, "language.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().config, "config.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().limiter, "limiter.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().portals, "portals.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().warps, "warps.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().blacklist, "blacklist.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().skyBlockSave, "SkyBlockSave.yml");
                    messages.reloadMessage(sender);
                    RandomCoords.getPlugin().reloadPortals();

                }
            } else {
                messages.noPermission(sender);

            }
        } else if(args.length > 1 && args[1].equalsIgnoreCase("bonus")) {
            Player player = (Player) sender;
            BonusChestManager bonusChestManager = new BonusChestManager();
            Block chestBlock = player.getLocation().add(1, 0, 1).getBlock();
            chestBlock.setType(Material.CHEST);
            final Chest chest = (Chest) chestBlock.getState();
            Bukkit.broadcastMessage(bonusChestManager.getFileName(player.getWorld()));
            chest.setCustomName(bonusChestManager.getFileName(player.getWorld()));
            //Get the inventory of this chest.
            final Inventory chestInv = chest.getInventory();
            int i = 0;
            for(ItemStack itemStack : bonusChestManager.itemsToAdd(player.getWorld())) {
                chestInv.addItem(itemStack);
                i++;
                if(i > 26) {
                    Block doubleChest = chestBlock.getLocation().add(1, 0, 0).getBlock();
                    doubleChest.setType(Material.CHEST);
                    Chest chestTwo = (Chest) doubleChest.getState();
                    Inventory chestTwoInv = chestTwo.getInventory();
                    chestTwoInv.addItem(itemStack);
                } else if(i > 53) {
                    Bukkit.getLogger().log(Level.WARNING, "Too many items to fit in chest");
                }
            }

        } else if(args.length > 1 && args[1].equalsIgnoreCase("save")) {
            Player player = (Player) sender;
            BonusChestManager bonusChestManager = new BonusChestManager();
            bonusChestManager.itemStackToFile("TestChest", player.getItemInHand());
        }
    }



}
