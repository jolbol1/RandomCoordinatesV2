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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by James on 05/07/2016.
 */
public class Warp implements CommandInterface {

    private final MessageManager messages = new MessageManager();
    private final Coordinates coordinates = new Coordinates();

    @Override
    public void onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if(args.length >= 1 && args[0].equalsIgnoreCase("warp") && sender.hasPermission("Random.*") && sender.hasPermission("Random.Admin.*") && sender.hasPermission("Random.Admin.Warp")) {
            String mode = null;
            String name = null;
            String playerName = null;
            String worldName = null;
            for(String s : args) {

                if(!s.equalsIgnoreCase("warp")) {
                     if(s.contains("name:")) {
                        name = s.replace("name:", "");
                    } else if(s.equalsIgnoreCase("create") || s.equalsIgnoreCase("set")) {

                        if(mode == null) {
                            mode = "create";
                        }
                    } else if(s.equalsIgnoreCase("delete") || s.equalsIgnoreCase("remove")) {
                        if(mode == null) {
                            mode = "delete";
                        }
                    } else if(s.equalsIgnoreCase("list")) {
                        mode = "list";
                    } else if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(s))) {
                         playerName = s;
                     } else if(name == null) {
                        name = s;
                    } else if(name != null) {
                         worldName = s;
                     } else if(s.contains(":")) {
                            messages.incorrectUsage(sender, "/RC Warp create/delete/list {Name} or use flags!");
                            return;
                     }




                }
            }

            if(mode == null) {
                mode = "teleport";
            }

            if(mode.equalsIgnoreCase("create")) {
                if(sender instanceof ConsoleCommandSender) {
                    messages.notPlayer(sender);
                    return;
                }
                if(!(sender instanceof Player)) {
                    messages.notPlayer(sender);
                    return;
                }
                if(name != null) {
                    createWarp(name, (Player) sender, sender);
                    return;
                } else {
                    messages.incorrectUsage(sender, "/RC Warp create {Name} to create warp where you are standing");
                }
            } else if(mode.equalsIgnoreCase("delete")) {
                if(name != null) {
                    deleteWarp(name, sender);
                } else {
                    messages.incorrectUsage(sender, "/RC Warp delete {Name} to delete the warp with name {Name}");

                }
            } else if(mode.equalsIgnoreCase("list")) {
                if(name == null) {
                    final Set<String> warps = RandomCoords.getPlugin().warps.getConfigurationSection("Warps.").getKeys(false);
                    sender.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.GREEN + "Warps:");

                    for (final String warpName : warps) {
                        sender.sendMessage(ChatColor.GREEN + warpName);
                    }
                } else if(name != null && Bukkit.getWorld(name) != null) {
                    final Set<String> warps = RandomCoords.getPlugin().warps.getConfigurationSection("Warps.").getKeys(false);
                    sender.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.GREEN + "Warps for " + name + ":");

                    for (final String warpName : warps) {
                        if(RandomCoords.getPlugin().warps.getString("Warps." + warpName + ".World").equalsIgnoreCase(name)) {
                            sender.sendMessage(ChatColor.GREEN + warpName);
                        }
                    }
                }
            } else if(mode.equalsIgnoreCase("teleport")) {
                CoordType coordType;

                if(RandomCoords.getPlugin().config.getString("WarpCrossWorld").equalsIgnoreCase("true")){
                    coordType = CoordType.WARPS;
                } else {
                    coordType = CoordType.WARPWORLD;
                }
                if(name == null && playerName == null) {
                    if(sender instanceof ConsoleCommandSender) {
                        messages.notPlayer(sender);
                        return;
                    }
                        Player p = (Player) sender;
                        coordinates.finalCoordinates(p, 574272099, 574272099, p.getWorld(), coordType, 0);
                        return;

                } else if(name != null && Bukkit.getWorld(name) != null && playerName == null) {
                    if(sender instanceof ConsoleCommandSender) {
                        messages.notPlayer(sender);
                        return;
                    }
                    Player p = (Player) sender;
                    coordinates.finalCoordinates(p, 574272099, 574272099, Bukkit.getWorld(name), coordType, 0);
                    return;
                } else if(playerName != null && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(playerName)) && name != null) {

                    Player p = Bukkit.getPlayer(playerName);
                    messages.teleportedPlayer(sender, p);
                    coordinates.finalCoordinates(p, 574272099, 574272099, Bukkit.getWorld(name), coordType, 0);
                    return;

                } else if(playerName != null && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(playerName)) && name == null) {
                    Player p = Bukkit.getPlayer(playerName);
                    messages.teleportedPlayer(sender, p);
                    coordinates.finalCoordinates(p, 574272099, 574272099, p.getWorld(), coordType, 0);
                    return;
                }

                else {
                    messages.incorrectUsage(sender, "/RC Warp {Player/World}");
                    return;
                }
            }







        }
    }

    public void createWarp(String name, Player p, CommandSender sender) {
        final String warpName = name;
        final Location location = p.getLocation();
        final double xD = location.getX();
        final double yD = location.getBlockY();
        final double zD = location.getBlockZ();
        final double x = Math.floor(xD) + 0.5;
        final double y = Math.floor(yD);
        final double z = Math.floor(zD) + 0.5;
        final World world = location.getWorld();
        RandomCoords.getPlugin().warps.set("Warps." + warpName + ".X", x);
        RandomCoords.getPlugin().warps.set("Warps." + warpName + ".Y", y);
        RandomCoords.getPlugin().warps.set("Warps." + warpName + ".Z", z);
        RandomCoords.getPlugin().warps.set("Warps." + warpName + ".World", world.getName());
        RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().warps, RandomCoords.getPlugin().warpFile);
        messages.warpSet(sender, warpName);
    }

    public void deleteWarp(String warpName, CommandSender sender) {
        if (RandomCoords.getPlugin().warps.getString("Warps." + warpName) != null) {
            RandomCoords.getPlugin().warps.set("Warps." + warpName, null);
            RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().warps, RandomCoords.getPlugin().warpFile);
            messages.warpDelete(sender, warpName);
        } else {
            messages.warpNotExist(sender);
        }
    }

}
