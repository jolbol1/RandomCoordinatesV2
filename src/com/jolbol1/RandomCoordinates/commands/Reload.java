package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by James on 02/07/2016.
 */
public class Reload implements CommandInterface {

    private final MessageManager messages = new MessageManager();

    @Override
    public void onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (args.length == 1) {
            if (RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.Reload") || RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(sender, "Random.*")) {
                if (args[0].equalsIgnoreCase("reload")) {
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().language, "language.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().config, "config.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().limiter, "limiter.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().portals, "portals.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().warps, "warps.yml");
                    RandomCoords.getPlugin().reloadFile(RandomCoords.getPlugin().blacklist, "blacklist.yml");
                    messages.reloadMessage(sender);

                }
            } else {
                messages.noPermission(sender);

            }
        }
    }
}
