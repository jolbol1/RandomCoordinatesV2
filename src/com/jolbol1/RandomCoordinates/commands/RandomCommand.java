package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import com.jolbol1.RandomCoordinates.cooldown.Cooldown;
import com.jolbol1.RandomCoordinates.managers.CoordType;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_10_R1.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Random;
import java.util.UUID;

/**
 * Created by James on 01/07/2016.
 */
public class RandomCommand implements CommandInterface {

    private RandomCoords plugin;


    private Coordinates coordinates = new Coordinates();
    private MessageManager messages = new MessageManager();




    //The command should be automatically created.
    @Override
    public void onCommand(CommandSender sender, Command cmd,
                          String commandLabel, String[] args) {
        if(RandomCoords.getPlugin().hasPermission(sender, "Random.Basic") || RandomCoords.getPlugin().hasPermission(sender, "Random.*") || RandomCoords.getPlugin().hasPermission(sender, "Random.Command")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    messages.notPlayer(sender);
                    return;
                }
                Player p = (Player) sender;
                Location start = p.getLocation();
                double health = p.getHealth();
                for (String worlds : RandomCoords.getPlugin().config.getStringList("BannedWorlds")) {
                    if (p.getWorld().getName().equals(worlds)) {
                        messages.worldBanned(sender);
                        return;

                    }
                }

                int cooldown = RandomCoords.getPlugin().config.getInt("CooldownTime");
                int timeBefore = RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport");
                if(RandomCoords.getPlugin().config.getDouble("CommandCost") != 0) {
                  if(!RandomCoords.getPlugin().hasMoney(p, RandomCoords.getPlugin().config.getDouble("CommandCost"))) {
                      messages.cost(p, RandomCoords.getPlugin().config.getDouble("CommandCost"));
                      return;
                  }
                }
                if (Cooldown.isInCooldown(((Player) sender).getUniqueId(), "TimeBefore")) {
                    messages.aboutTo(sender, Cooldown.getTimeLeft(((Player) sender).getUniqueId(), "TimeBefore"));
                    return;
                }
                if (cooldown != 0) {
                    if (!Cooldown.isInCooldown(((Player) sender).getUniqueId(), "Command")) {
                        if (!Cooldown.isInCooldown(((Player) sender).getUniqueId(), "TimeBefore")) {
                            Cooldown cTb = new Cooldown(((Player) sender).getUniqueId(), "TimeBefore", RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport"));
                            cTb.start();
                            messages.TeleportingIn(sender, timeBefore);
                        }
                        Cooldown c = new Cooldown(((Player) sender).getUniqueId(), "Command", cooldown + timeBefore);
                        c.start();
                        BukkitScheduler s = RandomCoords.getPlugin().getServer().getScheduler();
                        s.scheduleSyncDelayedTask(RandomCoords.getPlugin().getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                if (RandomCoords.getPlugin().config.getString("StopOnMove").equalsIgnoreCase("true")) {
                                    if (start.distance(p.getLocation()) > 1) {
                                        messages.youMoved(sender);
                                        return;
                                    }
                                }
                                if (RandomCoords.getPlugin().config.getString("StopOnCombat").equalsIgnoreCase("true")) {
                                    if (health > p.getHealth()) {
                                        messages.tookDamage(sender);
                                        return;
                                    }
                                }
                                coordinates.finalCoordinates(p, 574272099, 574272099, p.getWorld(), CoordType.COMMAND, 0);
                            }
                        }, timeBefore * 20L);

                    } else {
                        int secondsLeft = Cooldown.getTimeLeft(((Player) sender).getUniqueId(), "Command");
                        messages.cooldownMessage(sender, secondsLeft);
                        return;
                    }


                } else {
                    if (!Cooldown.isInCooldown(((Player) sender).getUniqueId(), "TimeBefore")) {
                        Cooldown cTb = new Cooldown(((Player) sender).getUniqueId(), "TimeBefore", RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport"));
                        cTb.start();
                        messages.TeleportingIn(sender, timeBefore);
                        BukkitScheduler s = RandomCoords.getPlugin().getServer().getScheduler();



                        s.scheduleSyncDelayedTask(RandomCoords.getPlugin().getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                if (RandomCoords.getPlugin().config.getString("StopOnMove").equalsIgnoreCase("true")) {
                                    if (start.distance(p.getLocation()) > 1) {
                                        messages.youMoved(sender);
                                        return;
                                    }
                                }
                                if (RandomCoords.getPlugin().config.getString("StopOnCombat").equalsIgnoreCase("true")) {
                                    if (health > p.getHealth()) {
                                        messages.tookDamage(sender);
                                        return;
                                    }
                                }
                                coordinates.finalCoordinates((Player) sender, 574272099, 574272099, ((Player) sender).getWorld(), CoordType.COMMAND, 0);
                            }
                        }, timeBefore * 20L);
                    } else {
                        messages.aboutTo(sender, Cooldown.getTimeLeft(((Player) sender).getUniqueId(), "TimeBefore"));
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


