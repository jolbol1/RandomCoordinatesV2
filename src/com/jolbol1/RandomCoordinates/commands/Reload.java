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

    MessageManager messages = new MessageManager();

    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length == 1) {
            if(RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.Reload") || RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(sender, "Random.*")) {
            if(args[0].equalsIgnoreCase("reload")) {
                RandomCoords.getPlugin().reloadLanguageFile();
                RandomCoords.getPlugin().reloadConfigFile();
                messages.reloadMessage(sender);

            }
        } else {
                messages.noPermission(sender);
                return;

            }
        }
    }
}
