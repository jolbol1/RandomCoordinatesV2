package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.commands.handler.CommandHandler;
import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by James on 07/07/2016.
 */
public class HelpCommand implements CommandInterface {

    String prefix = ChatColor.GOLD + "- ";
    ChatColor good = ChatColor.RED;
    ChatColor cmdC = ChatColor.GREEN ;



    @Override
    public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length == 1 && args[0].equalsIgnoreCase("help")) {

            sender.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.GREEN + "Help Page: Developed by Jolbol1/ImBaffled") ;
            if(sender.hasPermission("Random.Basic") || sender.hasPermission("Random.Command") || sender.hasPermission("Random.*")){
               new FancyMessage(prefix).then("/RC ").color(cmdC).suggest("/rc").then("- Teleport to somewhere random!").color(good).send(sender);
            }
            if(sender.hasPermission("Random.Warps") || sender.hasPermission("Random.*")) {
                new FancyMessage(prefix).then("/RC Warp ").color(cmdC).suggest("/rc warp").then("- Teleport to a random warp!").color(good).send(sender);
            }
            if(sender.hasPermission("Random.Admin.All") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                new FancyMessage(prefix).then("/RC All {World} {Max} {Min} ").color(cmdC).suggest("/RC all").then(" - Teleports all players to a random location. {World/Max/Min} are not required.").color(good).send(sender);
            }
            if(sender.hasPermission("Random.Admin.Others") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                new FancyMessage(prefix).then("/RC player {Name} {World} {Max} {Min} ").color(cmdC).suggest("/rc player ").then("- Teleports the specified player to a random location. {World/Max/Min} are not required.").color(good).send(sender);
            }
            if(sender.hasPermission("Random.Admin.Reload") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                new FancyMessage(prefix).then("/RC reload ").color(cmdC).suggest("/rc reload").then("- Reloads the configuration files.").color(good).send(sender);
            }
            if(sender.hasPermission("Random.Admin.Warp") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                new FancyMessage(prefix).then("/RC warp set {Name} ").color(cmdC).suggest("/rc warp set ").then("- Sets a warp with {Name} at current location").color(good).send(sender);
                new FancyMessage(prefix).then("/RC warp delete {Name} ").color(cmdC).suggest("/rc warp delete ").then("- Deletes a warp with {Name}").color(good).send(sender);
                new FancyMessage(prefix).then("/RC warp list ").color(cmdC).suggest("/rc warp set ").then("- Lists all warps").color(good).send(sender);
            }

            if(sender.hasPermission("Random.Admin.Portals") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                new FancyMessage(prefix).then("/RC Wand ").color(cmdC).suggest("/RC Wand").then("- Gives you the Random Wand for portal selection.").color(good).send(sender);
                new FancyMessage(prefix).then("/RC portal create {Name} ").color(cmdC).suggest("/RC portal create ").then("- Creates a portal from the RandomWand selection with name {Name}").color(good).send(sender);
                new FancyMessage(prefix).then("/RC portal delete {Name} ").color(cmdC).suggest("/RC portal delete ").then("- Deletes a portal with name {Name}").color(good).send(sender);

            }


            if(sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.Admin.Settings") || sender.hasPermission("Random.*")) {
                new FancyMessage(prefix).then("/RC Set {World} Center ").color(cmdC).suggest("/RC set world center").then("- Sets the centerpoint for the Max, Min boundary").color(good).send(sender);
                new FancyMessage(prefix).then("/RC Set {World} Max {Number} ").color(cmdC).suggest("/RC set world max").then("- Sets the maximum boundary for random teleporting").color(good).send(sender);
                new FancyMessage(prefix).then("/RC Set {World} Min {Number} ").color(cmdC).suggest("/RC set world min").then("- Sets the minimum boundary for random teleporting").color(good).send(sender);
                new FancyMessage(prefix).then("To remove the center, use 'remove' after Center, To use the global max/min, replace {Number} with 'global'").color(good).send(sender);
            }
        }
    }
}
