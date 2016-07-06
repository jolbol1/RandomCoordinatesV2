package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by James on 04/07/2016.
 */
public class PortalCommand implements CommandInterface {


    private MessageManager messages = new MessageManager();
    private Map selection = RandomCoords.getPlugin().wandSelection;

    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.Portals") || RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(sender, "Random.*")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("portal")) {
                    messages.incorrectUsage(sender, "/rc portal", "/RC Portal Create {Name} {World} or /RC Portal Delete {Name}");
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("portal") && args[1].equalsIgnoreCase("create")) {
                    if (sender instanceof ConsoleCommandSender) {
                        messages.notPlayer(sender);
                        return;
                    }
                    messages.incorrectUsage(sender, "/rc portal create", "/RC Portal Create {Name} {World}");
                    return;
                } else if (args[0].equalsIgnoreCase("portal") && args[1].equalsIgnoreCase("list")) {
                    ConfigurationSection cs = RandomCoords.getPlugin().portals.getConfigurationSection("Portal");
                    messages.portalList(sender);
                    for (String a : cs.getKeys(false)) {
                        sender.sendMessage(ChatColor.RED + a);
                    }
                } else {
                    if (args[0].equalsIgnoreCase("portal")) {
                        messages.incorrectUsage(sender, "/rc portal", "/RC Portal Create {Name} {World} or /RC Portal Delete {Name}");

                    }
                }
            }
            if (args.length >= 3) {
                if (args[0].equalsIgnoreCase("portal") && args[1].equalsIgnoreCase("create") && args[2] != null) {

                    if (sender instanceof ConsoleCommandSender) {
                        messages.notPlayer(sender);
                        return;
                    }
                    Player p = (Player) sender;
                    World world = p.getWorld();
                    if (args.length == 4) {
                        if (args[3] != null) {
                            if (p.getServer().getWorld(args[3].toString()) == null) {
                                messages.invalidWorld(sender, args[3].toString());
                                return;
                            } else {
                                world = p.getServer().getWorld(args[3].toString());
                            }
                        }
                    }
                    if (selection.get(RandomCoords.getPlugin().PortalMap("pos1", p)) == null || selection.get(RandomCoords.getPlugin().PortalMap("pos2", p)) == null) {
                        messages.noSelection(sender);
                        return;
                    } else if (selection.get(RandomCoords.getPlugin().PortalMap("pos1", p)) != null && selection.get(RandomCoords.getPlugin().PortalMap("pos1", p)) != null) {
                        Location pos1 = (Location) selection.get(RandomCoords.getPlugin().PortalMap("pos1", p));
                        Location pos2 = (Location) selection.get(RandomCoords.getPlugin().PortalMap("pos2", p));
                        if (pos1.getWorld() != pos2.getWorld()) {
                            messages.posInSameWorld(sender);
                            return;
                        }
                        int p1x = pos1.getBlockX();
                        int p1y = pos1.getBlockY();
                        int p1z = pos1.getBlockZ();
                        int p2x = pos2.getBlockX();
                        int p2y = pos2.getBlockY();
                        int p2z = pos2.getBlockZ();
                        String wName = world.getName();
                        String pName = args[2].toString();
                        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p1x", p1x);
                        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p1y", p1y);
                        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p1z", p1z);
                        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p2x", p2x);
                        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p2y", p2y);
                        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p2z", p2z);
                        RandomCoords.getPlugin().portals.set("Portal." + pName + ".PortalWorld", pos1.getBlock().getWorld().getName());
                        RandomCoords.getPlugin().portals.set("Portal." + pName + ".world", wName);
                        RandomCoords.getPlugin().savePortals();
                        messages.portalCreated(sender, pName, wName);


                    }


                } else {
                    if (args[0].equalsIgnoreCase("portal") && args[1].equalsIgnoreCase("delete") && args[2] != null) {
                        String pName = args[2].toString();
                        if (RandomCoords.getPlugin().portals.get("Portal." + pName) != null) {
                            RandomCoords.getPlugin().portals.set("Portal." + pName, null);
                            messages.portalDeleted(sender, pName);
                            RandomCoords.getPlugin().savePortals();
                            return;
                        } else {
                            messages.portalNotExist(sender, pName);
                            return;
                        }
                    }
                    messages.incorrectUsage(sender, "/rc portal create", "/RC Portal Create {Name} {World}");
                }

            }
        } else {
            messages.noPermission(sender);
            return;
        }
    }
}
