package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import com.jolbol1.RandomCoordinates.managers.CoordType;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by James on 01/07/2016.
 */
public class RandomCommand implements CommandInterface {



    private final Coordinates coordinates = new Coordinates();
    private final MessageManager messages = new MessageManager();




    //The command should be automatically created.
    @Override
    public void onCommand(final CommandSender sender, final Command cmd,
                          final String commandLabel, final String[] args) {
        if(RandomCoords.getPlugin().hasPermission(sender, "Random.Basic") || RandomCoords.getPlugin().hasPermission(sender, "Random.*") || RandomCoords.getPlugin().hasPermission(sender, "Random.Command")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    messages.notPlayer(sender);
                    return;
                }
                final Player p = (Player) sender;
             //   Location start = p.getLocation();
               // double health = p.getHealth();
                for (final String worlds : RandomCoords.getPlugin().config.getStringList("BannedWorlds")) {
                    if (p.getWorld().getName().equals(worlds)) {
                        messages.worldBanned(sender);
                        return;

                    }
                }

                coordinates.finalCoordinates((Player) sender, 574272099, 574272099, ((Player) sender).getWorld(), CoordType.COMMAND, 0);

            }
        } else {
            messages.noPermission(sender);
        }

        }










    }


