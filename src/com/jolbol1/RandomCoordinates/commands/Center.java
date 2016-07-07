package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by James on 04/07/2016.
 */
public class Center implements CommandInterface {

    private MessageManager messages = new MessageManager();


    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.Settings") || RandomCoords.getPlugin().hasPermission(sender, "Random.*")) {
            if (args.length == 3) {
                if (!args[0].equalsIgnoreCase("set")) {
                    return;
                }
                World world;
                if (Bukkit.getServer().getWorld(args[1].toString()) != null) {
                    world = Bukkit.getServer().getWorld(args[1].toString());
                } else {
                    messages.invalidWorld(sender, args[1].toString());
                    return;
                }
                if (args[2].equalsIgnoreCase("center")) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        Location pLoc = p.getLocation();
                        int x = pLoc.getBlockX();
                        int y = pLoc.getBlockY();
                        int z = pLoc.getBlockZ();
                        String wName = world.getName();
                        RandomCoords.getPlugin().config.set(wName + ".Center.X", x);
                        RandomCoords.getPlugin().config.set(wName + ".Center.Y", y);
                        RandomCoords.getPlugin().config.set(wName + ".Center.Z", z);
                        RandomCoords.getPlugin().saveCustomConfig();
                        messages.centerSet(sender);
                        return;
                    }


                } else {
                    messages.incorrectUsage(sender, "/RC set", "/RC set {World} Center/Max/Min {Max/Min}");
                    return;

                }
            } else if (args.length == 4) {
                if (args[2].equalsIgnoreCase("max")) {
                    World world;
                    if (Bukkit.getServer().getWorld(args[1].toString()) != null) {
                        world = Bukkit.getServer().getWorld(args[1].toString());
                    } else {
                        messages.invalidWorld(sender, args[1].toString());
                        return;
                    }
                    int max;
                    if (args[3].equalsIgnoreCase("global")) {
                        RandomCoords.getPlugin().config.set(world.getName() + ".Max", null);
                        RandomCoords.getPlugin().saveCustomConfig();
                        messages.maxRemove(sender, world.getName());

                        return;
                    }
                    try {
                        max = Integer.valueOf(args[3]);
                    } catch (NumberFormatException e) {
                        messages.incorrectUsage(sender, "/RC set", "/rc set {World} Max {Max}");
                        return;
                    }

                    String wName = world.getName();
                    RandomCoords.getPlugin().config.set(wName + ".Max", max);
                    RandomCoords.getPlugin().saveCustomConfig();
                    messages.maxSet(sender, String.valueOf(max), world.getName());
                    return;

                } else if (args[2].equalsIgnoreCase("min")) {
                    World world;
                    if (Bukkit.getServer().getWorld(args[1].toString()) != null) {
                        world = Bukkit.getServer().getWorld(args[1].toString());
                    } else {
                        messages.invalidWorld(sender, args[1].toString());
                        return;
                    }
                    int min;
                    if (args[3].equalsIgnoreCase("global")) {
                        RandomCoords.getPlugin().config.set(world.getName() + ".Min", null);
                        RandomCoords.getPlugin().saveCustomConfig();
                        messages.minRemove(sender, world.getName());
                        return;
                    }
                    try {
                        min = Integer.valueOf(args[3]);
                    } catch (NumberFormatException e) {
                        messages.incorrectUsage(sender, "/RC set", "/rc set {World} Min {Min}");
                        return;
                    }

                    String wName = world.getName();
                    RandomCoords.getPlugin().config.set(wName + ".Min", min);
                    RandomCoords.getPlugin().saveCustomConfig();
                    messages.minSet(sender, String.valueOf(min), world.getName());
                    return;
                } else if (args[2].equalsIgnoreCase("center")) {
                    if (args[3].equalsIgnoreCase("remove")) {
                        World world;
                        if (Bukkit.getServer().getWorld(args[1].toString()) != null) {
                            world = Bukkit.getServer().getWorld(args[1].toString());
                        } else {
                            messages.invalidWorld(sender, args[1].toString());
                            return;
                        }
                        RandomCoords.getPlugin().config.set(world.getName() + ".Center", null);
                        RandomCoords.getPlugin().saveCustomConfig();
                        messages.centerRemove(sender, world.getName());
                    }
                }
            } else {
                messages.incorrectUsage(sender, "/RC set", "/RC set {World} Center/Max/Min {Max/Min}");
                return;
            }
        } else {
            messages.noPermission(sender);
            return;
        }
    }
}
