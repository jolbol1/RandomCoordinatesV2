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
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import com.jolbol1.RandomCoordinates.managers.BonusChestManager;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

/**
 * Created by James on 26/04/2017.
 */
public class BonusCommand implements CommandInterface {
    private MessageManager messageManager = new MessageManager();
    private BonusChestManager bonusChestManager = new BonusChestManager();

    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length >= 1 && args[0].equalsIgnoreCase("chest") && (sender.hasPermission("Random.Admin.Chest") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*"))) {
            if(!(sender instanceof Player)) {
                messageManager.notPlayer(sender);
                return;
            }
            Player player = (Player) sender;
            if(args.length == 1) {
                messageManager.incorrectUsage(sender, "/RC chest {ChestName} - {ChestName} <- Chest you want to add items to. (YML File)");
            }
            if(args.length == 2) {
                String fileName = args[1].toString();
                for(ItemStack itemStack : player.getInventory().getContents()) {
                    if(itemStack != null) {
                        bonusChestManager.itemStackToFile(fileName, itemStack);
                    }
                }
                messageManager.inventoryContentsSaved(sender, fileName, false);
            }
            if(args.length == 3) {
                if (args[2].equalsIgnoreCase("-o")) {

                    String fileName = args[1].toString();
                    File file = new File(RandomCoords.getPlugin().getDataFolder() + File.separator + "BonusChests", fileName + ".yml");
                    file.delete();
                    for (ItemStack itemStack : player.getInventory().getContents()) {
                        if (itemStack != null) {
                            bonusChestManager.itemStackToFile(fileName, itemStack);
                        }
                    }

                    messageManager.inventoryContentsSaved(sender, fileName, true);


                } else if(args[2].equalsIgnoreCase("-i")) {
                    String fileName = args[1].toString();
                    bonusChestManager.itemStackToFile(fileName, player.getItemInHand());
                    messageManager.itemSaved(sender, fileName);


                } else if(args[2].equalsIgnoreCase("-d")) {
                    String fileName = args[1].toString();
                    for(ItemStack itemStack : player.getInventory().getContents()) {
                        if(itemStack != null) {
                            bonusChestManager.itemStackToFile(fileName, itemStack, true);
                        }
                    }
                    messageManager.inventoryContentsSaved(sender, fileName, false);
                }
            }



        }
    }
}
