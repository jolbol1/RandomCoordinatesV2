package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import com.jolbol1.RandomCoordinates.managers.CoordType;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by James on 04/07/2016.
 */
public class Others implements CommandInterface {

    private final Coordinates coordinates = new Coordinates();
    private final MessageManager messages = new MessageManager();

    @Override
    public void onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.Others") || RandomCoords.getPlugin().hasPermission(sender, "Random.*")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("player")) {
                messages.incorrectUsage(sender, "/RC player {World} {Max} {Min} - {World/Max/Min} = Not Required");
                return;
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("player")) {
                final String target = args[1];
                for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (player.getName().equalsIgnoreCase(target)) {

                        //Check if world banned
                        for (final String worlds : RandomCoords.getPlugin().config.getStringList("BannedWorlds")) {
                            if (player.getWorld().getName().equals(worlds)) {
                                messages.worldBanned(sender);
                                return;

                            }
                        }
                        coordinates.finalCoordinates(player, 574272099, 574272099, player.getWorld(), CoordType.PLAYER, 0);
                        messages.teleportedBy(sender, player);
                    }
                }


            } else if (args.length == 3 && args[0].equalsIgnoreCase("player")) {
                final String wName = args[2];
                if (Bukkit.getServer().getWorld(wName) == null) {
                    messages.invalidWorld(sender, wName);
                    return;
                }
                final World world = Bukkit.getServer().getWorld(wName);
                final String target = args[1];

                for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (player.getName().equalsIgnoreCase(target)) {
                        for (final String worlds : RandomCoords.getPlugin().config.getStringList("BannedWorlds")) {
                            if (world.getName().equals(worlds)) {
                                messages.worldBanned(sender);
                                return;

                            }
                        }
                        coordinates.finalCoordinates(player, 574272099, 574272099, world, CoordType.PLAYER, 0);
                        messages.teleportedBy(sender, player);

                    }
                }
            } else if (args.length == 4 && args[0].equalsIgnoreCase("player")) {
                final String wName = args[2];
                if (Bukkit.getServer().getWorld(wName) == null) {
                    messages.invalidWorld(sender, wName);
                    return;
                }
                int max;
                try {
                    max = Integer.valueOf(args[3]);
                } catch (NumberFormatException e) {
                    messages.incorrectUsage(sender, "/RC player {Player} {World} {Max} {Min} - {World/Max/Min} = Not Required");
                    return;
                }
                final World world = Bukkit.getServer().getWorld(wName);
                final String target = args[1];
                for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (player.getName().equalsIgnoreCase(target)) {
                        for (final String worlds : RandomCoords.getPlugin().config.getStringList("BannedWorlds")) {
                            if (world.getName().equals(worlds)) {
                                messages.worldBanned(sender);
                                return;

                            }
                        }
                        coordinates.finalCoordinates(player, max, 574272099, world, CoordType.PLAYER, 0);
                        messages.teleportedBy(sender, player);

                    }
                }
            } else if (args.length == 5) {
                final String wName = args[2];
                if (Bukkit.getServer().getWorld(wName) == null) {
                    messages.invalidWorld(sender, wName);
                    return;
                }
                int min;
                int max;
                try {
                    max = Integer.valueOf(args[3]);
                    min = Integer.valueOf(args[4]);
                } catch (NumberFormatException e) {
                    messages.incorrectUsage(sender, "/RC player {World} {Max} {Min} - {World/Max/Min} = Not Required");
                    return;
                }
                final String target = args[1];
                final World world = Bukkit.getServer().getWorld(wName);
                for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (player.getName().equalsIgnoreCase(target)) {
                        for (final String worlds : RandomCoords.getPlugin().config.getStringList("BannedWorlds")) {
                            if (world.getName().equals(worlds)) {
                                messages.worldBanned(sender);
                                return;

                            }
                        }
                        coordinates.finalCoordinates(player, max, min, world, CoordType.PLAYER, 0);
                    }
                }
            }
        } else {
            messages.noPermission(sender);
        }
    }
}
