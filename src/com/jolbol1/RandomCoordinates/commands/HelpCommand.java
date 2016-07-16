package com.jolbol1.RandomCoordinates.commands;

import com.jolbol1.RandomCoordinates.commands.handler.CommandInterface;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by James on 07/07/2016.
 */
public class HelpCommand implements CommandInterface {

    private final String prefix = ChatColor.GOLD + "- ";
    private final ChatColor good = ChatColor.RED;
    private final ChatColor cmdC = ChatColor.GREEN ;



    @Override
    public void onCommand(final CommandSender sender, final Command  cmd, final String commandLabel, final String[] args) {
        if(args.length == 1 && args[0].equalsIgnoreCase("help")) {

            sender.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.GREEN + "Help Page: Developed by Jolbol1/ImBaffled") ;
            if(sender.hasPermission("Random.Basic") || sender.hasPermission("Random.Command") || sender.hasPermission("Random.*")){
              sender.sendMessage(prefix + good + "/RC - Teleport to somewhere random!");
            }
            if(sender.hasPermission("Random.Warps") || sender.hasPermission("Random.*")) {
                sender.sendMessage(prefix + good + "/RC Warp - Teleport to a random warp!");
            }
            if(sender.hasPermission("Random.Admin.All") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                sender.sendMessage(prefix + good + "/RC All {World} {Max} {Min} - Teleports all players to a random location. {World/Max/Min} are not required.");
            }
            if(sender.hasPermission("Random.Admin.Others") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
               sender.sendMessage(prefix + good + "/RC Player {Name} {Wolrd} {Max} {Min} - Teleports the specified player to a random location. {World/Max/Min} are not required.");
            }
            if(sender.hasPermission("Random.Admin.Reload") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                sender.sendMessage(prefix + good + "/RC Reload - Reloads the configuration files.");
            }
            if(sender.hasPermission("Random.Admin.Warp") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                sender.sendMessage(prefix + good + "/RC Warp Set {Name} - Sets a warp with {Name} at current location");
                sender.sendMessage(prefix + good + "/RC Warp Delete {Name} - Deletes the warp with {Name}");
                sender.sendMessage(prefix + good + "/RC Warp List - Lists all set warps");


            }

            if(sender.hasPermission("Random.Admin.Portals") || sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.*")) {
                sender.sendMessage(prefix + good + "/RC Wand - Gives you the Random Wand for portal selection.");
                sender.sendMessage(prefix + good + "/RC Portal Create {Name} - Creates a portal from the RandomWand selection with name {Name}");
                sender.sendMessage(prefix + good + "/RC Portal Delete {Name} - Deletes a portal with name {Name}");
            }


            if(sender.hasPermission("Random.Admin.*") || sender.hasPermission("Random.Admin.Settings") || sender.hasPermission("Random.*")) {
                sender.sendMessage(prefix + good + "/RC Set {World} Center - Sets the centerpoint for the Max, Min boundary");
                sender.sendMessage(prefix + good + "/RC Set {World} Max {Number} - Sets the maximum boundary for random teleporting");
                sender.sendMessage(prefix + good + "/RC Set {World} Min {Number} - Sets the minimum boundary for random teleporting");
                sender.sendMessage(prefix + good + "To remove the center, use 'remove' after Center, To use the global max/min, replace {Number} with 'global'" );
            }
        }
    }
}
