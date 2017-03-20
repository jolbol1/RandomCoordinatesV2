package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by James on 19/03/2017.
 */
public class WorldSettings implements CommandInterface {

    private final MessageManager messages = new MessageManager();
    private final Coordinates coordinates = new Coordinates();
    private int key = 574272099;

    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length >= 1 && args[0].equalsIgnoreCase("set") && sender.hasPermission("Random.*") && sender.hasPermission("Random.Admin.*") && sender.hasPermission("Random.Admin.Settings" )) {

            //Player p = (Player) sender;
            int max = key;
             int min = key;
             String worldName = null;
             Location center = null;
            String mode = null;
            boolean delete = false;
            for(String s : args) {
                if (!s.equalsIgnoreCase("set")) {
                    if (Bukkit.getWorld(s) != null) {
                        worldName = s;
                    } else if (s.equalsIgnoreCase("center")) {
                        if(!(sender instanceof Player)) {
                            messages.notPlayer(sender);
                            return;
                        }
                        Player p = (Player) sender;
                        center = p.getLocation();
                        mode = "center";
                    } else if (s.equalsIgnoreCase("max")) {
                        mode = "max";
                    } else if (s.equalsIgnoreCase("min")) {
                        mode = "min";
                    } else if(s.contains("max:")) {
                        mode = "max";
                        if(canParseInteger(s.replace("max:", ""))) {
                            max = Integer.parseInt(s.replace("max:", ""));
                        }
                    } else if(s.contains("max:")) {
                        mode = "max";
                        if(canParseInteger(s.replace("max:", ""))) {
                            max = Integer.parseInt(s.replace("max:", ""));
                        }
                    } else if(s.contains("min:")) {
                        mode = "min";
                        if(canParseInteger(s.replace("min:", ""))) {
                            min = Integer.parseInt(s.replace("min:", ""));
                        }
                    } else if (s.equalsIgnoreCase("remove") || s.equalsIgnoreCase("global") || s.equalsIgnoreCase("delete")) {
                        delete = true;
                    } else if (canParseInteger(s)) {
                        if(mode != null) {
                            if (mode.equalsIgnoreCase("max")) {
                                max = Integer.valueOf(s);
                            } else if (mode.equalsIgnoreCase("min")) {
                                min = Integer.parseInt(s);
                            }
                        } else if (max == key) {
                            max = Integer.parseInt(s);
                        } else {
                            min = Integer.parseInt(s);
                        }
                    } else {
                        worldName = s;
                    }

                }
            }

            if(max == key && min == key && center == null && mode == null && worldName == null) {
                messages.incorrectUsage(sender, "/RC set {World} {Max/Min/Center} {Value}");
                return;
            }

            if(max != key && min != key) {
                if(max < min) {
                    messages.minTooLarge(sender);
                    return;
                }
            }



                if(worldName == null || Bukkit.getWorld(worldName) == null) {
                    String w;
                    if(worldName == null) {
                        w = "Specified World";
                    } else {
                        w = worldName;
                    }
                    messages.invalidWorld(sender, w);
                    return;
                }

            if(max != key && min == key) {
                int minimum = coordinates.getMin(Bukkit.getWorld(worldName));
                if(max < minimum) {
                    messages.minTooLarge(sender);
                    return;
                }
            }

            if(max == key && min != key) {
                int maximum = coordinates.getMax(Bukkit.getWorld(worldName));
                if(maximum < min) {
                    messages.minTooLarge(sender);
                    return;
                }
            }


                if(delete == true) {
                    if(max == key && mode.equalsIgnoreCase("max") && center == null && min == key) {
                        messages.maxRemove(sender, worldName);
                        RandomCoords.getPlugin().config.set(worldName + ".Max", null);
                        return;
                    }
                    if(max == key && mode.equalsIgnoreCase("min") && center == null && min == key) {
                        messages.minRemove(sender, worldName);
                        RandomCoords.getPlugin().config.set(worldName + ".Min", null);
                        return;
                    }
                    if(max == key && mode.equalsIgnoreCase("center") && center != null && min == key) {
                        messages.centerRemove(sender, worldName);
                        RandomCoords.getPlugin().config.set(worldName + ".Center", null);
                        return;
                    }
                    if(max == key) {
                        messages.maxRemove(sender, worldName);
                        RandomCoords.getPlugin().config.set(worldName + ".Max", null);
                    }
                    if(min == key) {
                        messages.minRemove(sender, worldName);
                        RandomCoords.getPlugin().config.set(worldName + ".Min", null);

                    }
                    if(center != null) {
                        messages.centerRemove(sender, worldName);
                        RandomCoords.getPlugin().config.set(worldName + ".Center", null);
                    }
                    return;

                }

                if(mode.equalsIgnoreCase("max") && max == key) {
                    messages.incorrectUsage(sender, "/RC set {World} Max {Number}");
                    return;
                }
                if(mode.equalsIgnoreCase("min") && min == key) {
                    messages.incorrectUsage(sender, "/RC set {World} Min {Number}");
                    return;
                }

                if (min != key) {
                    messages.minSet(sender, String.valueOf(min), worldName);
                    RandomCoords.getPlugin().config.set(worldName + ".Min", min);
                }
                if (max != key) {
                    messages.maxSet(sender, String.valueOf(max), worldName);
                    RandomCoords.getPlugin().config.set(worldName + ".Max", max);
                }
                if (center != null) {
                    messages.centerSet(sender);
                    RandomCoords.getPlugin().config.set(worldName + ".Center.X", center.getBlockX());
                    RandomCoords.getPlugin().config.set(worldName + ".Center.Y", center.getBlockY());
                    RandomCoords.getPlugin().config.set(worldName + ".Center.Z", center.getBlockZ());


                }


            }
            RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().config, RandomCoords.getPlugin().configFile);

        }




    public boolean canParseInteger(String maximum) {
        try {
            Integer.parseInt(maximum);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

}
