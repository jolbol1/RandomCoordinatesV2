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

    private final MessageManager messages = new MessageManager();


    @Override
    public void onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.Settings") || RandomCoords.getPlugin().hasPermission(sender, "Random.*")) {
            if (args.length == 3) {
                if (!args[0].equalsIgnoreCase("set")) {
                    return;
                }
                World world;
                if (Bukkit.getServer().getWorld(args[1]) != null) {
                    world = Bukkit.getServer().getWorld(args[1]);
                } else {
                    messages.invalidWorld(sender, args[1]);
                    return;
                }
                if (args[2].equalsIgnoreCase("center")) {
                    if (sender instanceof Player) {
                        final Player p = (Player) sender;
                        final Location pLoc = p.getLocation();
                        final int x = pLoc.getBlockX();
                        final int y = pLoc.getBlockY();
                        final int z = pLoc.getBlockZ();
                        final String wName = world.getName();
                        RandomCoords.getPlugin().config.set(wName + ".Center.X", x);
                        RandomCoords.getPlugin().config.set(wName + ".Center.Y", y);
                        RandomCoords.getPlugin().config.set(wName + ".Center.Z", z);
                        RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().config, RandomCoords.getPlugin().configFile);
                        messages.centerSet(sender);
                    }


                } else {
                    messages.incorrectUsage(sender, "/RC set {World} Center/Max/Min {Max/Min}");

                }
            } else if (args.length == 4) {
                if (args[2].equalsIgnoreCase("max")) {
                    World world;
                    if (Bukkit.getServer().getWorld(args[1]) != null) {
                        world = Bukkit.getServer().getWorld(args[1]);
                    } else {
                        messages.invalidWorld(sender, args[1]);
                        return;
                    }

                    if (args[3].equalsIgnoreCase("global")) {
                        RandomCoords.getPlugin().config.set(world.getName() + ".Max", null);
                        RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().config, RandomCoords.getPlugin().configFile);
                        messages.maxRemove(sender, world.getName());

                        return;
                    }
                    int max;
                    try {
                        max = Integer.valueOf(args[3]);
                    } catch (NumberFormatException e) {
                        messages.incorrectUsage(sender, "/rc set {World} Max {Max}");
                        return;
                    }

                    final String wName = world.getName();
                    RandomCoords.getPlugin().config.set(wName + ".Max", max);
                    RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().config, RandomCoords.getPlugin().configFile);
                    messages.maxSet(sender, String.valueOf(max), world.getName());

                } else if (args[2].equalsIgnoreCase("min")) {
                    World world;
                    if (Bukkit.getServer().getWorld(args[1]) != null) {
                        world = Bukkit.getServer().getWorld(args[1]);
                    } else {
                        messages.invalidWorld(sender, args[1]);
                        return;
                    }
                    if (args[3].equalsIgnoreCase("global")) {
                        RandomCoords.getPlugin().config.set(world.getName() + ".Min", null);
                        RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().config, RandomCoords.getPlugin().configFile);
                        messages.minRemove(sender, world.getName());
                        return;
                    }
                    int min;
                    try {
                        min = Integer.valueOf(args[3]);
                    } catch (NumberFormatException e) {
                        messages.incorrectUsage(sender, "/rc set {World} Min {Min}");
                        return;
                    }

                    final String wName = world.getName();
                    RandomCoords.getPlugin().config.set(wName + ".Min", min);
                    RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().config, RandomCoords.getPlugin().configFile);
                    messages.minSet(sender, String.valueOf(min), world.getName());
                } else if (args[2].equalsIgnoreCase("center") && args[3].equalsIgnoreCase("remove")) {
                    World world;
                    if (Bukkit.getServer().getWorld(args[1]) != null) {
                        world = Bukkit.getServer().getWorld(args[1]);
                    } else {
                        messages.invalidWorld(sender, args[1]);
                        return;
                    }
                    RandomCoords.getPlugin().config.set(world.getName() + ".Center", null);
                    RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().config, RandomCoords.getPlugin().configFile);
                    messages.centerRemove(sender, world.getName());

                }
            } else {
                messages.incorrectUsage(sender, "/RC set {World} Center/Max/Min {Max/Min}");
            }
        } else {
            messages.noPermission(sender);
        }
    }
}
