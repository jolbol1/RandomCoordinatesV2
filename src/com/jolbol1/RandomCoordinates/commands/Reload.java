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
                    RandomCoords.getPlugin().reloadLanguageFile();
                    RandomCoords.getPlugin().reloadConfigFile();
                    messages.reloadMessage(sender);

                }
            } else {
                messages.noPermission(sender);

            }
        }
    }
}
