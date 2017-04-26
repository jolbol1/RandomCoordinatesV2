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
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.material.MaterialData;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by James on 25/04/2017.
 */
public class BonusChestManager {

    private CoordinatesManager coordinatesManager = new CoordinatesManager();

    public String getFileName(World world) {
        return getBonusChestFile(world).getName();
    }


    public FileConfiguration getBonusChestFile(World world) {
        FileConfiguration fileConfiguration = null;
        for(String key : RandomCoords.getPlugin().bonusChest.getKeys(false)) {
            if(RandomCoords.getPlugin().bonusChest.getStringList(key) != null) {
                for(String worldName: RandomCoords.getPlugin().bonusChest.getStringList(key)) {
                    if(Bukkit.getServer().getWorld(worldName) != null) {
                        if(Bukkit.getServer().getWorld(worldName) == world) {
                            File file = new File(RandomCoords.getPlugin().getDataFolder() + File.separator + "BonusChests", key + ".yml");
                            fileConfiguration = YamlConfiguration.loadConfiguration(file);
                        }
                    }



                }
            }

        }
        return fileConfiguration;

    }

    public List<ItemStack> itemsToAdd(World world) {
        FileConfiguration configuration = getBonusChestFile(world);
        Map<String, Object> itemsPlusData = itemsPlusData(configuration);
        List<ItemStack> items = new ArrayList<>();
        for(String item : itemsPlusData.keySet()) {
            Map<String, Object> itemData = (Map<String, Object>) itemsPlusData.get(item);
            int amount = (int) itemData.get("Amount");
            int odd = 100;
            String itemType = item;
            if(itemType.contains(":")) {
                itemType = item.split(":")[0];
            }
            byte data = 0;
            if(itemData.containsKey("Data")) {
                data = Byte.valueOf(String.valueOf(itemData.get("Data")));
            }
            ItemStack itemStack = new ItemStack(Material.getMaterial(itemType), amount, data);
            ItemMeta itemMeta = itemStack.getItemMeta();
            if(itemData.containsKey("Name")) {
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', (String) itemData.get("Name")));
            }
            if(itemData.containsKey("Lore")) {
                itemMeta.setLore((List<String>) itemData.get("Lore"));
            }
            if(itemData.containsKey("Enchantments")) {
                for (String enchantment : (List<String>) itemData.get("Enchantments")) {
                    String[] enchantmentSplitter = enchantment.split(":");
                    int level = Integer.valueOf(enchantmentSplitter[1]);
                    itemMeta.addEnchant(Enchantment.getByName(enchantmentSplitter[0]), level, true);
                }
            }



            itemStack.setItemMeta(itemMeta);

            if(itemData.containsKey("Author") || itemData.containsKey("Pages")) {
                if(itemStack.getType().equals(Material.WRITTEN_BOOK)) {
                    BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
                    if(itemData.containsKey("Author")) {
                        bookMeta.setAuthor(String.valueOf(itemData.get("Author")));
                    }
                    if(itemData.containsKey("Pages")) {
                        bookMeta.setPages((List<String>) itemData.get("Pages"));
                    }
                    if(itemData.containsKey("Name")) {
                        bookMeta.setTitle(String.valueOf(itemData.get("Name")));
                    }
                    if(itemData.containsKey("Lore")) {
                        bookMeta.setLore((List<String>) itemData.get("Lore"));
                    }

                    itemStack.setItemMeta(bookMeta);
                }
            }

            if(itemData.containsKey("Colour")) {
                if(itemStack.getType().equals(Material.LEATHER_BOOTS) || itemStack.getType().equals(Material.LEATHER_CHESTPLATE) ||
                        itemStack.getType().equals(Material.LEATHER_LEGGINGS) || itemStack.getType().equals(Material.LEATHER_HELMET)) {
                    Color color = (Color) itemData.get("Colour");

                    LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
                    leatherArmorMeta.setColor(color);
                    itemStack.setItemMeta(leatherArmorMeta);

                }
            }

            if(itemData.containsKey("FireworkEffect")) {
                Bukkit.broadcastMessage("Here");
                if(itemStack.getType().equals(Material.FIREWORK)) {
                    Bukkit.broadcastMessage("Firework");
                    FireworkMeta fireworkMeta = (FireworkMeta) itemMeta;
                    fireworkMeta.addEffects((Iterable<FireworkEffect>) itemData.get("FireworkEffect"));
                    itemStack.setItemMeta(fireworkMeta);

                }
            }

            if(itemData.containsKey("SkullOwner")) {
                if(itemStack.getType().equals(Material.SKULL_ITEM)) {
                    SkullMeta skullMeta = (SkullMeta) itemMeta;
                    skullMeta.setOwner(String.valueOf(itemData.get("SkullOwner")));
                    itemStack.setItemMeta(skullMeta);
                }
            }


            if(itemData.containsKey("Odds")) {
                odd = (int) itemData.get("Odds");
            }

            int randomChance = coordinatesManager.getRandomNumber(0, 100);
            if(randomChance < 0) {
                randomChance = randomChance * -1;
            }
            if(randomChance <= odd) {
                items.add(itemStack);
            }


        }

        return items;

    }

    public Map<String, Object> itemsPlusData(FileConfiguration bonusChestFile) {
        Map<String, Object> itemsPlusData = new LinkedHashMap<>();
        for(String key : bonusChestFile.getKeys(false)) {
            Bukkit.broadcastMessage(key);
            itemsPlusData.put(key, parseItem(bonusChestFile, key));
        }
        return itemsPlusData;



    }

    public Map<String,Object> parseItem(FileConfiguration fileItems, String materialName) {
        Map<String, Object> itemData = new HashMap<>();
        Bukkit.broadcastMessage(materialName);
        if(fileItems.getInt(materialName + ".Odds") != 0) {
            itemData.put("Odds", fileItems.getInt(materialName + ".Odds"));
        }

        if(fileItems.getString(materialName + ".Name") != null) {
            itemData.put("Name",fileItems.getString(materialName + ".Name")  );
        }

        if(fileItems.getInt(materialName + ".Amount") != 0) {
            itemData.put("Amount", fileItems.getInt(materialName + ".Amount"));
        }

        if(fileItems.getStringList(materialName + ".Lore") != null) {
            List<String> lore = new ArrayList<>();
            for(String name : fileItems.getStringList(materialName + ".Lore")) {
                lore.add(ChatColor.translateAlternateColorCodes('&', name));
            }
            itemData.put("Lore", lore);
        }

        if(fileItems.get(materialName + ".Data") != null) {
            itemData.put("Data", fileItems.getString(materialName + ".Data"));
        }

        if(fileItems.getStringList(materialName + ".Enchantments") != null) {
            itemData.put("Enchantments", fileItems.getStringList(materialName + ".Enchantments"));

        }

        if(fileItems.getString(materialName + ".Author") != null && materialName.contains("WRITTEN_BOOK")) {
            itemData.put("Author", fileItems.getString(materialName + ".Author"));
        }

        if(fileItems.get(materialName + ".Pages") != null && materialName.contains("WRITTEN_BOOK")) {
            itemData.put("Pages", fileItems.get(materialName + ".Pages"));
        }

        if(materialName.contains("LEATHER_HELMET") || materialName.contains("LEATHER_BOOTS") || materialName.contains("LEATHER.LEGGINGS") || materialName.contains("LEATHER_CHESTPLATE")) {
            itemData.put("Colour", fileItems.get(materialName + ".Colour"));
        }

        if(materialName.contains("FIREWORK")) {
            itemData.put("FireworkEffect", fileItems.get(materialName + ".FireworkEffect"));
        }

        if(materialName.contains("SKULL_ITEM")) {
            itemData.put("SkullOwner", fileItems.get(materialName + ".SkullOwner"));
        }



        return itemData;

    }

    public void itemStackToFile(String fileName, ItemStack item) {
        File file = new File(RandomCoords.getPlugin().getDataFolder() + File.separator + "BonusChests", fileName + ".yml");
        FileConfiguration yamlFile = RandomCoords.getPlugin().setupFile(file);
        ItemMeta itemMeta = item.getItemMeta();
        String itemName = item.getType().name();
        String displayName = itemMeta.getDisplayName();
        int amount = item.getAmount();
        Map<Enchantment, Integer> enchantments = itemMeta.getEnchants();
            int i = 0;
            for(String key : yamlFile.getKeys(false)) {
                if(key.contains(itemName)) {
                    i++;
                }
            }
            if(i == 0) {
                yamlFile.createSection(itemName);

            } else if(i > 0) {
                itemName = itemName + ":" + i;
            }
            yamlFile.set(itemName + ".Name", displayName);
        yamlFile.set(itemName + ".Amount", amount);
        yamlFile.set(itemName + ".Lore", itemMeta.getLore());

        yamlFile.set(itemName + ".Data", item.getData().getData());
        List<String> enchanting = new ArrayList<>();
        for(Enchantment enchantment : enchantments.keySet()) {
            String name = enchantment.getName();
            int level = enchantments.get(enchantment);
            enchanting.add(name + ":" + level);
        }

        if(item.getType().equals(Material.WRITTEN_BOOK)) {
            BookMeta bookMeta = (BookMeta) itemMeta;
            yamlFile.set(itemName + ".Author",  bookMeta.getAuthor().toString() );
            yamlFile.set(itemName + ".Pages", bookMeta.getPages().toArray());
            yamlFile.set(itemName + ".Name", bookMeta.getTitle());
        }

        if(item.getType().equals(Material.LEATHER_LEGGINGS) || item.getType().equals(Material.LEATHER_BOOTS) ||
        item.getType().equals(Material.LEATHER_CHESTPLATE) || item.getType().equals(Material.LEATHER_HELMET)) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
            yamlFile.set(itemName + ".Colour", leatherArmorMeta.getColor());
        }

        if(item.getType().equals(Material.FIREWORK)) {
            FireworkMeta fireworkMeta = (FireworkMeta) itemMeta;
            yamlFile.set(itemName + ".FireworkEffect", fireworkMeta.getEffects());

        }

        if(item.getType().equals(Material.SKULL_ITEM) ) {
            SkullMeta skullMeta = (SkullMeta) itemMeta;
            yamlFile.set(itemName + ".SkullOwner", skullMeta.getOwner());
        }


        yamlFile.set(itemName + ".Enchantments", enchanting);

            RandomCoords.getPlugin().saveFile(yamlFile, file);



        }













    }




