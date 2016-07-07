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

                coordinates.finalCoordinates((Player) sender, 574272099, 574272099, ((Player) sender).getWorld(), CoordType.COMMAND, 0);

            }
        } else {
            messages.noPermission(sender);
            return;
        }

        }










    }


