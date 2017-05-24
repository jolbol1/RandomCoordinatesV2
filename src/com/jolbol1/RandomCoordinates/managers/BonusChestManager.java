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
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

/**
 * Created by James on 25/04/2017.
 */
public class BonusChestManager {

    //private CoordinatesManager coordinatesManager = new CoordinatesManager();
    private final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();


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


    public List<ItemStack> itemsToAdd(Player player, World world) {
        FileConfiguration configuration = getBonusChestFile(world);

        Map<String, Object> itemsPlusData = itemsPlusData(configuration, player);
        List<ItemStack> items = new ArrayList<>();
        Set<String> itemsPlusTheData = itemsPlusData.keySet();

        if(itemsPlusTheData.contains("Essentials")) {
            for(String kit : configuration.getStringList("Essentials")) {
                KitManager kitManager = new KitManager();
                for(ItemStack itemStack : kitManager.getEssentialsKits(player, kit)) {

                    items.add(itemStack);

                }

            }
        }

        for(String item : itemsPlusTheData) {
            String itemType = item;
            if (itemType.contains(":")) {
                itemType = item.split(":")[0];
            }
            if (Material.getMaterial(itemType) != null) {
                Map<String, Object> itemData = (Map<String, Object>) itemsPlusData.get(item);
                int amount = (int) itemData.get("Amount");
                int odd = 100;

                byte data = 0;
                if (itemData.containsKey("Data")) {
                    data = Byte.valueOf(String.valueOf(itemData.get("Data")));
                }
                if (Material.getMaterial(itemType) == null) {
                    Bukkit.getServer().getLogger().log(Level.WARNING, itemType + " is not a valid item.");
                    return null;
                }
                ItemStack itemStack = new ItemStack(Material.getMaterial(itemType), amount, data);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemData.containsKey("Name")) {
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', (String) itemData.get("Name")));
                }
                if (itemData.containsKey("Lore")) {
                    itemMeta.setLore((List<String>) itemData.get("Lore"));
                }
                if (itemData.containsKey("Enchantments")) {
                    for (String enchantment : (List<String>) itemData.get("Enchantments")) {
                        String[] enchantmentSplitter = enchantment.split(":");
                        int level = Integer.valueOf(enchantmentSplitter[1]);
                        itemMeta.addEnchant(Enchantment.getByName(enchantmentSplitter[0]), level, true);
                    }
                }

                if(itemData.containsKey("Durability")) {
                    short durab = Short.valueOf(String.valueOf(itemData.get("Durability")));
                    if(durab < Material.getMaterial(itemType).getMaxDurability()) {
                        int durabilityMod = itemStack.getType().getMaxDurability() - durab;
                        itemStack.setDurability(Short.valueOf(String.valueOf(durabilityMod)));
                    }
                }


                itemStack.setItemMeta(itemMeta);

                if (itemData.containsKey("Author") || itemData.containsKey("Pages")) {
                    if (itemStack.getType().equals(Material.WRITTEN_BOOK)) {
                        BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
                        if (itemData.containsKey("Author")) {
                            bookMeta.setAuthor(String.valueOf(itemData.get("Author")));
                        }
                        if (itemData.containsKey("Pages")) {
                            bookMeta.setPages((List<String>) itemData.get("Pages"));
                        }
                        if (itemData.containsKey("Name")) {
                            bookMeta.setTitle(String.valueOf(itemData.get("Name")));
                        }
                        if (itemData.containsKey("Lore")) {
                            bookMeta.setLore((List<String>) itemData.get("Lore"));
                        }

                        itemStack.setItemMeta(bookMeta);
                    }

                }

                if (itemData.containsKey("Colour")) {
                    if (itemStack.getType().equals(Material.LEATHER_BOOTS) || itemStack.getType().equals(Material.LEATHER_CHESTPLATE) ||
                            itemStack.getType().equals(Material.LEATHER_LEGGINGS) || itemStack.getType().equals(Material.LEATHER_HELMET)) {
                        Color color = (Color) itemData.get("Colour");

                        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
                        leatherArmorMeta.setColor(color);
                        itemStack.setItemMeta(leatherArmorMeta);

                    }
                }

                if (itemData.containsKey("FireworkEffect")) {
                    if (itemStack.getType().equals(Material.FIREWORK)) {
                        FireworkMeta fireworkMeta = (FireworkMeta) itemMeta;
                        fireworkMeta.addEffects((Iterable<FireworkEffect>) itemData.get("FireworkEffect"));
                        itemStack.setItemMeta(fireworkMeta);

                    }
                }

                if (itemData.containsKey("SkullOwner")) {
                    if (itemStack.getType().equals(Material.SKULL_ITEM)) {
                        SkullMeta skullMeta = (SkullMeta) itemMeta;
                        skullMeta.setOwner(String.valueOf(itemData.get("SkullOwner")));
                        itemStack.setItemMeta(skullMeta);
                    }
                }


                if (itemData.containsKey("Odds")) {
                    odd = (int) itemData.get("Odds");
                }

                int randomChance = threadLocalRandom.nextInt(0, 100);
                if (randomChance < 0) {
                    randomChance = randomChance * -1;
                }
                if (randomChance <= odd) {
                    items.add(itemStack);
                }


            } else {
                if(!item.contains("Essentials")) {
                    Bukkit.getServer().getLogger().log(Level.WARNING, itemType + " is not a valid item.");
                }
            }
        }


        return items;

    }

    public Map<String, Object> itemsPlusData(FileConfiguration bonusChestFile, Player player) {
        Map<String, Object> itemsPlusData = new LinkedHashMap<>();
        if(bonusChestFile == null) {
            Bukkit.getServer().getLogger().log(Level.WARNING, "Bonus Chest is on, but, no chest setup in BonusChestConfig.yml");
        } else {
            for (String key : bonusChestFile.getKeys(false)) {
                itemsPlusData.put(key, parseItem(bonusChestFile, key, player));
            }
        }
        return itemsPlusData;



    }

    public Map<String,Object> parseItem(FileConfiguration fileItems, String materialName) {
        return parseItem(fileItems, materialName, null);
    }

    public Map<String,Object> parseItem(FileConfiguration fileItems, String materialName, Player player) {
        Map<String, Object> itemData = new HashMap<>();
        if(fileItems.getInt(materialName + ".Odds") != 0) {
            itemData.put("Odds", fileItems.getInt(materialName + ".Odds"));
        }

        if(fileItems.getString(materialName + ".Name") != null) {
            if(player == null) {
                itemData.put("Name", ChatColor.translateAlternateColorCodes('&',fileItems.getString(materialName + ".Name")));
            } else {
                String text = fileItems.getString(materialName + ".Name").replaceAll("&p", ChatColor.stripColor(player.getName()));
                itemData.put("Name", ChatColor.translateAlternateColorCodes('&', text));
            }
        }

        if(fileItems.get(materialName + ".Amount") != null) {
            int amount = 1;
            Object obj = fileItems.get(materialName + ".Amount");
            if(String.valueOf(obj).contains(":")) {
                String[] random = fileItems.getString(materialName + ".Amount").split(":");
                amount = randomWithin(random[1], random[0], materialName);


            } else {
                amount = fileItems.getInt(materialName + ".Amount");
            }
            itemData.put("Amount", amount);
        }

        if(fileItems.get(materialName + ".Durability") != null) {
            int durability = 1;
            Object obj = fileItems.get(materialName + ".Durability");
            if(String.valueOf(obj).contains(":")) {
                String[] random = fileItems.getString(materialName + ".Durability").split(":");
                durability = randomWithin(random[1], random[0], materialName);

            } else {
                durability = fileItems.getInt(materialName + ".Durability");
            }
            itemData.put("Durability", durability);
        }

        if(fileItems.getStringList(materialName + ".Lore") != null) {
            if(player == null) {
                List<String> lore = new ArrayList<>();
                for (String name : fileItems.getStringList(materialName + ".Lore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', name));
                }
                itemData.put("Lore", lore);
            } else {
                List<String> lore = new ArrayList<>();
                for (String name : fileItems.getStringList(materialName + ".Lore")) {
                    String nameReplace = name.replaceAll("&p", ChatColor.stripColor(player.getName()));
                    lore.add(ChatColor.translateAlternateColorCodes('&', nameReplace));
                }
                itemData.put("Lore", lore);
            }
        }

        if(fileItems.get(materialName + ".Data") != null) {
            int amount = 0;
            Object obj = fileItems.get(materialName + ".Data");
            if(String.valueOf(obj).contains(":")) {
                String[] split = String.valueOf(obj).split(":");
                amount = randomWithin(split[1], split[0], materialName);
            } else {
                amount = fileItems.getInt(materialName + ".Data");
            }


            itemData.put("Data", amount);
        }

        if(fileItems.getStringList(materialName + ".Enchantments") != null) {
            itemData.put("Enchantments", fileItems.getStringList(materialName + ".Enchantments"));

        }

        if(fileItems.getString(materialName + ".Author") != null && materialName.contains("WRITTEN_BOOK")) {
            if(player == null) {
                itemData.put("Author", fileItems.getString(materialName + ".Author"));
            } else {
                itemData.put("Author", fileItems.getString(materialName + ".Author").replaceAll("&p", player.getName()));
            }
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
            if(player == null) {
                itemData.put("SkullOwner", fileItems.get(materialName + ".SkullOwner"));
            } else {
                String name = ChatColor.stripColor(player.getName());
                itemData.put("SkullOwner", String.valueOf(fileItems.get(materialName + ".SkullOwner")).replaceAll("&p", name));

            }
        }



        return itemData;

    }

    public void itemStackToFile(String fileName, ItemStack item) {
        itemStackToFile(fileName, item, false);
    }

    public void itemStackToFile(String fileName, ItemStack item, boolean durability) {
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
        if(durability) {
            yamlFile.set(itemName + ".Durability", item.getDurability());
        }

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

    public void spawnBonusChest(Player player) {
        //Get the location of the chest and set it to a chest.
        Location chestLocation = new Location(player.getWorld(), player.getLocation().getX() + 1, player.getLocation().getY() - 2, player.getLocation().getZ() + 1);
        Location chestTwo = new Location(player.getWorld(), player.getLocation().getX() + 2, player.getLocation().getY() - 2, player.getLocation().getZ() + 1);

        if((chestLocation.getY() + 3) > chestLocation.getWorld().getHighestBlockAt(chestLocation).getY() || (chestLocation.getY() - 3) < chestLocation.getWorld().getHighestBlockAt(chestLocation).getY()) {
            int chestHeight = chestLocation.getWorld().getHighestBlockAt(chestLocation).getY();
            chestLocation.setY(chestHeight);
            chestTwo.setY(chestHeight);
        }
        Block chestBlock = chestLocation.getBlock();
        chestBlock.setType(Material.CHEST);
        Location airLoc = chestLocation.add(0,1,0);
        airLoc.getBlock().setType(Material.AIR);
        //Get the Chest
        final Chest chest = (Chest) chestBlock.getState();
        //Get the inventory of this chest.
        final Inventory chestInv = chest.getInventory();
        int i = 0;
        List<ItemStack> itemsToAdd =  itemsToAdd(player, player.getWorld());
        List<Integer> singleChestShuffle = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26);
        Collections.shuffle(singleChestShuffle);
        List<Integer> doubleChestShuffle = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53);
        Collections.shuffle(doubleChestShuffle);


        Inventory doubleChestInventory = null;

        if(itemsToAdd.size() > 27) {
            Block doubleChest = chestTwo.getBlock();
            doubleChest.setType(Material.CHEST);
            chestTwo.add(0,1,0).getBlock().setType(Material.AIR);
            Chest chest1 = (Chest) doubleChest.getState();
            Inventory inventory = chest1.getInventory();
            DoubleChest doubleChest1 = (DoubleChest) inventory.getHolder();
            doubleChestInventory = doubleChest1.getInventory();
        }

        String chestName = getBonusChestFileName(player.getWorld());
        if(chestName != null) {
            chest.setCustomName(ChatColor.DARK_RED + chestName);
        }

        for(ItemStack item : itemsToAdd) {
            if(itemsToAdd.size() <= 27) {
                chestInv.setItem(singleChestShuffle.get(i), item);
                i++;
            } else if(itemsToAdd.size() <= 54) {

                    doubleChestInventory.setItem(doubleChestShuffle.get(i), item);
                    i++;

            }



             else {
                Bukkit.getServer().getLogger().log(Level.SEVERE, chestName + "contains too many items. Only 54 will be added.");
            }




        }





    }



    //File configuration .getName() wont work. So I use this instead.
    public String getBonusChestFileName(World world) {
        for(String key : RandomCoords.getPlugin().bonusChest.getKeys(false)) {
            if(RandomCoords.getPlugin().bonusChest.getStringList(key) != null) {
                for(String worldName: RandomCoords.getPlugin().bonusChest.getStringList(key)) {
                    if(Bukkit.getServer().getWorld(worldName) != null) {
                        if(Bukkit.getServer().getWorld(worldName) == world) {
                            File file = new File(RandomCoords.getPlugin().getDataFolder() + File.separator + "BonusChests", key + ".yml");
                            return file.getName().replaceAll("_", " ").replaceAll(".yml", "");
                        }
                    }



                }
            }

        }
        return null;

    }

    public int randomWithin(String upperS, String lowerS, String materialName) {
        int lower = 1;
        int upper = 1;
        try {
            lower = Integer.parseInt(lowerS);
            upper = Integer.parseInt(upperS);
        } catch (NumberFormatException e) {
            Bukkit.getLogger().log(Level.WARNING, "Could not parse random bounds for material: " + materialName);
        }
        if(lower > upper) {
            Bukkit.getLogger().log(Level.WARNING, "The lower bound is larger than the upper bound for material: " + materialName);
        }
        int amount = threadLocalRandom.nextInt((upper - lower) + 1) + lower;
        return amount;
    }




    }




