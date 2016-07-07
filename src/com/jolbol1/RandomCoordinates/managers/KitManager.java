package com.jolbol1.RandomCoordinates.managers;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Kit;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.User;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.KeywordReplacer;
import com.earth2me.essentials.textreader.SimpleTextInput;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by James on 07/07/2016.
 */
public class KitManager {


    public void getKit(Player p, Chest c, String name)
    {
        if(Bukkit.getPluginManager().getPlugin("Essentials") == null) {
            return;
        } else {
            IEssentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
            ForkJoinPool.commonPool().execute(new Runnable() {
                public void run() {
                    if (ess != null) {
                        Map<String, Object> kit = ess.getSettings().getKit(name.toLowerCase());
                        User u = ess.getUser(p);
                        List<String> items;
                        try {
                            Kit kitMe = new Kit(name, ess);
                            items = kitMe.getItems();

                            Inventory inv = c.getInventory();


                            inv.addItem(deSerialize(items, u).get());
                        } catch (Exception e) {
                            Bukkit.getServer().getLogger().severe("[RandomCoords] Kit " + name + " does not exist");
                            return;
                        }
                    } else {
                        System.out.println("essentials was null when getting kits!");
                    }
                }
            });
        }
    }

    public CompletableFuture<ItemStack[]> deSerialize(List<String> items, User user)
    {
        Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");

        CompletableFuture<ItemStack[]> finalList = new CompletableFuture<>();
        List<ItemStack> itemList = new ArrayList<ItemStack>();
        IText input = new SimpleTextInput(items);
        IText output = new KeywordReplacer(input, user.getSource(), ess);
        for (String kitItem : output.getLines())
        {
            String[] parts = kitItem.split(" +");
            try
            {
                ItemStack parseStack = ess.getItemDb().get(parts[0], parts.length > 1 ? Integer.parseInt(parts[1]) : 1);
                if (parseStack != null && parseStack.getType() != Material.AIR)
                {
                    MetaItemStack metaStack = new MetaItemStack(parseStack);
                    if (parts.length > 2)
                    {
                        metaStack.parseStringMeta(null, true, parts, 2, ess);
                    }
                    itemList.add(metaStack.getItemStack());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        finalList.complete(itemList.toArray(new ItemStack[itemList.size()]));
        return finalList;
    }






}
