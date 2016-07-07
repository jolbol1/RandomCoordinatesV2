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

    private Coordinates coordinates = new Coordinates();
    private MessageManager messages = new MessageManager();

    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
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
                            World world = Bukkit.getServer().getWorld(args[1]);
                            int max = 574272099;
                            int min = 574272099;
                            teleportAll(sender, max, min, world);
                        } else {
                            messages.invalidWorld(sender, args[1].toString());
                        }
                    }
                } else if (args.length == 3) {
                    World world;
                    if (Bukkit.getServer().getWorld(args[1]) != null) {
                        world = Bukkit.getServer().getWorld(args[1].toString());
                    } else {
                        messages.invalidWorld(sender, args[1]);
                        return;
                    }
                    int max = 574272099;
                    int min = 574272099;

                    try {
                        max = Integer.valueOf(args[2]);
                    } catch (NumberFormatException nfe) {
                        messages.rcAllUsage(sender);
                    }
                    teleportAll(sender, max, min, world);
                } else if (args.length == 4) {
                    if (Bukkit.getServer().getWorld(args[1]) != null) {
                        World world = Bukkit.getServer().getWorld(args[3]);
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
                        messages.invalidWorld(sender, args[1].toString());
                    }
                } else {
                    messages.incorrectUsage(sender, "/rc all", "/RC All {World} {Max} {Min} - {World/Max/Min} = Not Required!");
                }

            } else {
                messages.noPermission(sender);
            }
        }
    }



    public void teleportAll(CommandSender sender, int max, int min, World world) {
        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            if(world == null) {
                world = p.getWorld();
            }

            //Check if world blacklisted
            for(String worlds : RandomCoords.getPlugin().config.getStringList("BannedWorlds")) {
                if(world.getName().equals(worlds)) {
                   messages.worldBanned(sender);
                    return;

                }
            }
            if(!(sender instanceof ConsoleCommandSender)) {
                Player player = (Player) sender;
                if(p != player ) {
                 coordinates.finalCoordinates(p,max, min, world, CoordType.ALL, 0);
                    messages.teleportedBy(sender, p);
                }
            } else {
                coordinates.finalCoordinates(p,max, min, world, CoordType.ALL, 0);
                messages.teleportedBy(sender, p);
            }

        }
    }


}
