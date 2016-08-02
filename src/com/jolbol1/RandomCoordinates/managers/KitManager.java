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


    public void getKit(final Player p, final Chest c, final String name)
    {
        if(Bukkit.getPluginManager().getPlugin("Essentials") == null) {
        } else {
            final IEssentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
            ForkJoinPool.commonPool().execute(() -> {
                if (ess != null) {
                  //  Map<String, Object> kit = ess.getSettings().getKit(name.toLowerCase());
                    final User u = ess.getUser(p);
                    List<String> items;
                    try {
                        final Kit kitMe = new Kit(name, ess);
                        items = kitMe.getItems(u);

                        final Inventory inv = c.getInventory();


                        inv.addItem(deSerialize(items, u).get());
                    } catch (Exception e) {
                        RandomCoords.logger.severe("Essnetials unable to deserialize kit (RandomCoords)");

                    }
                } else {
                    RandomCoords.logger.severe("Essnetials was null when getting kit. (RandomCoords)");
                }
            });
        }
    }

    private CompletableFuture<ItemStack[]> deSerialize(final List<String> items, final User user)
    {
        final Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");

        final CompletableFuture<ItemStack[]> finalList = new CompletableFuture<>();
        final List<ItemStack> itemList = new ArrayList<>();
        final IText input = new SimpleTextInput(items);
        final IText output = new KeywordReplacer(input, user.getSource(), ess);
        for (final String kitItem : output.getLines())
        {
            final String[] parts = kitItem.split(" +");
            try
            {
                final ItemStack parseStack = ess.getItemDb().get(parts[0], parts.length > 1 ? Integer.parseInt(parts[1]) : 1);
                if (parseStack != null && parseStack.getType() != Material.AIR)
                {
                    final MetaItemStack metaStack = new MetaItemStack(parseStack);//NOPMD
                    if (parts.length > 2)
                    {
                        metaStack.parseStringMeta(null, true, parts, 2, ess);
                    }
                    itemList.add(metaStack.getItemStack());
                }
            }
            catch (Exception e)
            {
                RandomCoords.logger.log(Level.SEVERE, "There was an error serializing the essentials kit! (RandomCoords)");
            }
        }
        finalList.complete(itemList.toArray(new ItemStack[itemList.size()]));
        return finalList;
    }






}
