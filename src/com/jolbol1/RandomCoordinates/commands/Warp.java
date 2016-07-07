package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import com.jolbol1.RandomCoordinates.managers.CoordType;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by James on 05/07/2016.
 */
public class Warp implements CommandInterface {

    private MessageManager messages = new MessageManager();
    private Coordinates coordinates = new Coordinates();

    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
       if(sender instanceof ConsoleCommandSender) { messages.notPlayer(sender); return; }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("warp")) {
                if (RandomCoords.getPlugin().hasPermission(sender, "Random.Warps") || RandomCoords.getPlugin().hasPermission(sender, "Random.*")) {
                    Player p = (Player) sender;
                    coordinates.finalCoordinates(p, 574272099, 574272099, p.getWorld(), CoordType.WARPS, 0);
                    return;
                } else {
                    messages.noPermission(sender);
                    return;

                }
            }
        }


        Player p = (Player) sender;
        if(RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.Warp") || RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(sender, "Random.*")) {
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("warp") && args[1].equalsIgnoreCase("set") && args[2] != null) {
                    String warpName = args[2].toString();
                    Location location = p.getLocation();
                    double xD = location.getX();
                    double yD = location.getBlockY();
                    double zD = location.getBlockZ();
                    double x = Math.floor(xD) + 0.5;
                    double y = Math.floor(yD);
                    double z = Math.floor(zD) + 0.5;
                    World world = location.getWorld();
                    RandomCoords.getPlugin().warps.set("Warps." + warpName + ".X", x);
                    RandomCoords.getPlugin().warps.set("Warps." + warpName + ".Y", y);
                    RandomCoords.getPlugin().warps.set("Warps." + warpName + ".Z", z);
                    RandomCoords.getPlugin().warps.set("Warps." + warpName + ".World", world.getName());
                    RandomCoords.getPlugin().saveWarps();
                    messages.warpSet(sender, warpName);
                    return;
                } else if (args[0].equalsIgnoreCase("warp") && args[1].equalsIgnoreCase("delete") && args[2] != null) {
                    String warpName = args[2].toString();
                    if(RandomCoords.getPlugin().warps.getString("Warps." + warpName) != null) {
                        RandomCoords.getPlugin().warps.set("Warps." + warpName, null);
                        RandomCoords.getPlugin().saveWarps();
                        messages.warpDelete(sender, warpName);
                        return;
                    } else {
                        messages.warpNotExist(sender);
                        return;
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("warp") && args[1].equalsIgnoreCase("list")) {
                    Set<String> warps = RandomCoords.getPlugin().warps.getConfigurationSection("Warps.").getKeys(false);
                    sender.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.GREEN + "Warps:");

                    for (String name : warps) {
                        sender.sendMessage(ChatColor.GREEN + name);
                    }
                } else if(args[0].equalsIgnoreCase("warp") && Bukkit.getServer().getWorld(args[1].toString()) != null) {
                    World world = Bukkit.getServer().getWorld(args[1].toString());
                    if (RandomCoords.getPlugin().hasPermission(sender, "Random.Warps") || RandomCoords.getPlugin().hasPermission(sender, "Random.*")) {
                        coordinates.finalCoordinates(p, 574272099, 574272099, world, CoordType.WARPS, 0);
                        return;
                    } else {
                        messages.noPermission(sender);
                        return;

                    }
                }

            }
        } else {
            messages.noPermission(sender);
            return;

        }
    }
}
