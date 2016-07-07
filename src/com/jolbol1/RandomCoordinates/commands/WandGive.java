package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 04/07/2016.
 */
public class WandGive implements CommandInterface {

    private MessageManager messages = new MessageManager();

    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("wand")){
                if(RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.Portals") || RandomCoords.getPlugin().hasPermission(sender, "Random.Admin.*") || RandomCoords.getPlugin().hasPermission(sender, "Random.*")) {
                if(sender instanceof Player) {
                    Player p = (Player) sender;

                    p.getInventory().addItem(RandomCoords.getPlugin().wand());
                    messages.wandGiven(sender);
                }
            } else {
                    messages.noPermission(sender);
                    return;
                }
            }

        }
    }
}
