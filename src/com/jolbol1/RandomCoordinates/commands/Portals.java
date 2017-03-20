package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
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

import java.util.Map;

/**
 * Created by James on 19/03/2017.
 */
public class Portals implements CommandInterface {


    private final MessageManager messages = new MessageManager();
    private int key = 574272099;
    private final Map selection = RandomCoords.getPlugin().wandSelection;


    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length >= 1 && args[0].equalsIgnoreCase("portal") && sender.hasPermission("Random.*") && sender.hasPermission("Random.Admin.*") && sender.hasPermission("Random.Admin.Portals")) {
            String mode = null;
            int max = key;
            int min = key;
            String name = null;
            String toWorldName = null;
            for(String s : args) {

                if(!s.equalsIgnoreCase("portal")) {
                   if (s.contains("to:")) {
                        toWorldName = s.replace("to:", "");
                    } else if (s.contains("max:")) {
                        if (!canParseInteger(s.replace("max:", ""))) {
                            break;
                        }
                        max = Integer.parseInt(s.replace("max:", ""));
                    } else if(s.contains("name:")) {
                       name = s.replace("name:", "");
                   }  else if (s.contains("min:")) {
                        if (!canParseInteger(s.replace("min:", ""))) {
                            break;
                        }
                        min = Integer.parseInt(s.replace("min:", ""));
                    } else if (canParseInteger(s)) {
                        if(max == key) {
                            max = Integer.valueOf(s);
                        } else if(min == key) {
                            min = Integer.valueOf(s);
                        }
                   }  else if (doesWorldExist(sender, s)) {
                        if(toWorldName == null) {
                            toWorldName = s;
                        }
                    } else if(s.equalsIgnoreCase("create")) {
                       if(mode == null) {
                           mode = "create";
                       }
                   } else if(s.equalsIgnoreCase("delete")) {
                       if(mode == null) {
                           mode = "delete";
                       }
                   } else if(s.equalsIgnoreCase("list")) {
                       mode = "list";
                   } else if(name == null) {
                       name = s;
                   }

                   else {
                        if(s.contains(":")) {
                            messages.incorrectUsage(sender, "/RC All {World}/{Max} or use flags!");
                            return;
                        } else {
                            messages.invalidWorld(sender, s);
                            return;
                        }

                    }


                }
            }
            if(mode.equalsIgnoreCase("create")) {
                if(sender instanceof ConsoleCommandSender) {
                    messages.notPlayer(sender);
                    return;
                }
                if(name != null) {
                    createPortal(sender, Bukkit.getWorld(toWorldName), name, max, min);
                }
            } else if(mode.equalsIgnoreCase("delete")) {
                if(name != null) {
                    if (RandomCoords.getPlugin().portals.get("Portal." + name) != null) {
                        RandomCoords.getPlugin().portals.set("Portal." + name, null);
                        messages.portalDeleted(sender, name);
                        RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().portals, RandomCoords.getPlugin().portalsFile);
                        return;
                    } else {
                        messages.portalNotExist(sender, name);
                        return;
                    }
                } else if(toWorldName != null) {
                    if (RandomCoords.getPlugin().portals.get("Portal." + toWorldName) != null) {
                        RandomCoords.getPlugin().portals.set("Portal." + toWorldName, null);
                        messages.portalDeleted(sender, toWorldName);
                        RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().portals, RandomCoords.getPlugin().portalsFile);
                        return;
                    } else {
                        messages.portalNotExist(sender, toWorldName);
                        return;
                    }
                }
            } else if(mode.equalsIgnoreCase("list")) {
                if(toWorldName == null) {
                    final ConfigurationSection cs = RandomCoords.getPlugin().portals.getConfigurationSection("Portal");
                    messages.portalList(sender);
                    if (cs != null) {
                        for (final String a : cs.getKeys(false)) {
                            sender.sendMessage(ChatColor.RED + a);
                        }
                    }
                } else if(doesWorldExist(sender, toWorldName)) {
                    final ConfigurationSection cs = RandomCoords.getPlugin().portals.getConfigurationSection("Portal");
                    messages.portalList(sender);
                    if (cs != null) {
                        for (final String a : cs.getKeys(false)) {
                            if(RandomCoords.getPlugin().portals.getString("Portal." + a + ".PortalWorld").equalsIgnoreCase(toWorldName)) {
                                sender.sendMessage(ChatColor.RED + a);
                            }
                        }
                    }
                }
            }






        }
    }


    public boolean doesWorldExist(CommandSender sender, String worldName) {


        if(Bukkit.getWorld(worldName) == null) {
            return false;
        }
        return true;


    }

    public boolean canParseInteger(String maximum) {
        try {
            Integer.parseInt(maximum);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public void createPortal(CommandSender sender, World targetWorld, String pName, int max, int min) {
        final Location pos1 = (Location) selection.get(PortalMap("pos1", (Player) sender));
        final Location pos2 = (Location) selection.get(PortalMap("pos2", (Player) sender));
        if(pos1 == null || pos2 == null) {
            messages.noSelection(sender);
            return;
        }

        if (pos1.getWorld() != pos2.getWorld()) {
            messages.posInSameWorld(sender);
            return;
        }
        final int p1x = pos1.getBlockX();
        final int p1y = pos1.getBlockY();
        final int p1z = pos1.getBlockZ();
        final int p2x = pos2.getBlockX();
        final int p2y = pos2.getBlockY();
        final int p2z = pos2.getBlockZ();
        final String wName = targetWorld.getName();
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p1x", p1x);
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p1y", p1y);
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p1z", p1z);
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p2x", p2x);
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p2y", p2y);
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p2z", p2z);
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "p2z", p2z);
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "max", max);
        RandomCoords.getPlugin().portals.set("Portal." + pName + "." + "min", min);
        RandomCoords.getPlugin().portals.set("Portal." + pName + ".PortalWorld", pos1.getBlock().getWorld().getName());
        RandomCoords.getPlugin().portals.set("Portal." + pName + ".world", wName);
        RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().portals, RandomCoords.getPlugin().portalsFile);
        messages.portalCreated(sender, pName, wName);
    }


    public String PortalMap(final String position, final Player p) {
        return position + p.getName();
    }


}
