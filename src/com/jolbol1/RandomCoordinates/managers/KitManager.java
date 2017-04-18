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

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Kit;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.User;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.KeywordReplacer;
import com.earth2me.essentials.textreader.SimpleTextInput;
import com.jolbol1.RandomCoordinates.RandomCoords;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;

/**
 * Created by James on 07/07/2016.
 */
class KitManager {

    /**
     * Grabs the kit from the config name.
     * @param p The player we're getting the kit for.
     * @param c The Chest that is being generated.
     * @param name The name of the kit.
     */
    public void getKit(final Player p, final Chest c, final String name) {
        if (Bukkit.getPluginManager().getPlugin("Essentials") == null) {
        } else {
            final IEssentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
            ForkJoinPool.commonPool().execute(() -> {
                if (ess != null) {
                    //  Map<String, Object> kit = ess.getSettings().getKit(name.toLowerCase());
                    final User u = ess.getUser(p);
                    List<String> items;
                    try {
                        final Kit kitMe = new Kit(name, ess);
                        items = kitMe.getItems();

                        final Inventory inv = c.getInventory();


                        inv.addItem(deSerialize(items, u).get());
                    } catch (Exception e) {
                        e.printStackTrace();
                        RandomCoords.logger.severe("Essnetials unable to deserialize kit (RandomCoords)");

                    }
                } else {
                    RandomCoords.logger.severe("Essnetials was null when getting kit. (RandomCoords)");
                }
            });
        }
    }

    /**
     * Gets the essentials kit from the config as ItemStacks.
     * @param items The items in the kit.
     * @param user The player in the form of Essentials User.
     * @return The ItemStacks.
     */
    private CompletableFuture<ItemStack[]> deSerialize(final List<String> items, final User user) {
        final Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");

        final CompletableFuture<ItemStack[]> finalList = new CompletableFuture<>();
        final List<ItemStack> itemList = new ArrayList<>();
        final IText input = new SimpleTextInput(items);
        final IText output = new KeywordReplacer(input, user.getSource(), ess);
        for (final String kitItem : output.getLines()) {
            final String[] parts = kitItem.split(" +");
            try {
                final ItemStack parseStack = ess.getItemDb().get(parts[0], parts.length > 1 ? Integer.parseInt(parts[1]) : 1);
                if (parseStack != null && parseStack.getType() != Material.AIR) {
                    final MetaItemStack metaStack = new MetaItemStack(parseStack);//NOPMD
                    if (parts.length > 2) {
                        metaStack.parseStringMeta(null, true, parts, 2, ess);
                    }
                    itemList.add(metaStack.getItemStack());
                }
            } catch (Exception e) {
                RandomCoords.logger.log(Level.SEVERE, "There was an error serializing the essentials kit! (RandomCoords)");
            }
        }
        finalList.complete(itemList.toArray(new ItemStack[itemList.size()]));
        return finalList;
    }


}
