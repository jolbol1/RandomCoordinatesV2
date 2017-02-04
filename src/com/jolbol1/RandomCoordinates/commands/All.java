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
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Created by James on 04/07/2016.
 */
public class All implements CommandInterface {

    private final Coordinates coordinates = new Coordinates();
    private final MessageManager messages = new MessageManager();

    @Override
    public void onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (args.length >= 1) {
            if (!args[0].equalsIgnoreCase("all")) {
                return;
            }

            if (RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.All") || RandomCoords.getPlugin().hasPermission(sender, "Random.*")) {
                if (args.length == 1) {
                    teleportAll(sender, 574272099, 574272099, null);
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("all")) {
                        if (Bukkit.getServer().getWorld(args[1]) != null) {
                            final World world = Bukkit.getServer().getWorld(args[1]);
                            final int max = 574272099;
                            final int min = 574272099;
                            teleportAll(sender, max, min, world);
                        } else {
                            messages.invalidWorld(sender, args[1]);
                        }
                    }
                } else if (args.length == 3) {
                    World world;
                    if (Bukkit.getServer().getWorld(args[1]) != null) {
                        world = Bukkit.getServer().getWorld(args[1]);
                    } else {
                        messages.invalidWorld(sender, args[1]);
                        return;
                    }
                    int max = 574272099;
                    final int min = 574272099;

                    try {
                        max = Integer.valueOf(args[2]);
                    } catch (NumberFormatException nfe) {
                        messages.rcAllUsage(sender);
                    }
                    teleportAll(sender, max, min, world);
                } else if (args.length == 4) {
                    if (Bukkit.getServer().getWorld(args[1]) != null) {
                        final World world = Bukkit.getServer().getWorld(args[3]);
                        int max = 574272099;
                        int min = 574272099;
                        try {
                            max = Integer.valueOf(args[2]);
                            min = Integer.valueOf(args[3]);
                        } catch (NumberFormatException nfe) {
                            messages.rcAllUsage(sender);
                        }
                        teleportAll(sender, max, min, world);
                    } else {
                        messages.invalidWorld(sender, args[1]);
                    }
                } else {
                    messages.incorrectUsage(sender, "/RC All {World} {Max} {Min} - {World/Max/Min} = Not Required!");
                }

            } else {
                messages.noPermission(sender);
            }
        }
    }

    /**
     * The function used within /RC All, to teleport all the players.
     * @param sender Who used the command
     * @param max The specified Max coordinate
     * @param min The specified Min coordinate
     * @param world The world to teleport all in.
     */
    private void teleportAll(final CommandSender sender, final int max, final int min, World world) {
        for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
            String worldName;
            if (world == null) {
                world = p.getWorld();
                worldName = "the world they were in.";
            } else {
                worldName = world.getName();
            }

            //Check if world blacklisted

            for (final String worlds : RandomCoords.getPlugin().config.getStringList("BannedWorlds")) {
                if (world.getName().equals(worlds)) {
                    messages.worldBanned(sender);
                    return;

                }
            }
            if (!(sender instanceof ConsoleCommandSender)) {
                messages.teleportedAll(sender, worldName);
                final Player player = (Player) sender;
                if (p != player) {
                    coordinates.finalCoordinates(p, max, min, world, CoordType.ALL, 0);
                    messages.teleportedBy(sender, p);
                }
            } else {
                messages.teleportedAll(sender, worldName);
                coordinates.finalCoordinates(p, max, min, world, CoordType.ALL, 0);
                messages.teleportedBy(sender, p);
            }

        }
    }


}
