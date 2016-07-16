package com.jolbol1.RandomCoordinates.managers;


import com.jolbol1.RandomCoordinates.RandomCoords;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by James on 01/07/2016.
 */
public class MessageManager {
   //
    private final String prefix = ChatColor.GOLD + "[RandomCoords] ";
    private final ChatColor good = ChatColor.GREEN;



    public void noPermission(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("NoPermission"));
        sender.sendMessage(prefix + message);
    }

    public void minTooLarge(final CommandSender sender) {
        String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("MinTooLarge"));
        sender.sendMessage(prefix + message);
    }

    public void teleportMessage(final Player player, final Location location) {
        final String message = RandomCoords.getPlugin().language.getString("TeleportMessage");
        final String locX =  String.valueOf(location.getX());
        final String locY =  String.valueOf(location.getBlockY());
        final String locZ = String.valueOf(location.getZ());
        final String newMessage = message.replaceAll("%coordinates", ChatColor.GREEN + (locX + ", " + good + locY + ", " + good +  locZ));
        player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', newMessage));

    }

    public void notPlayer(final CommandSender sender) {
        String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("NotPlayer"));
        sender.sendMessage(prefix + message);

    }

    public void cooldownMessage(final CommandSender sender, final int Time) {
        final String message = RandomCoords.getPlugin().language.getString("CooldownMessage");
        final String messageTime = message.replaceAll("%time", String.valueOf(Time));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messageTime));
    }

    public void reloadMessage(final CommandSender sender) {
        String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("ReloadFiles"));
        sender.sendMessage(prefix + message);
    }

    public void TeleportingIn(final CommandSender sender, final int Time) {
        final String message = RandomCoords.getPlugin().language.getString("TeleportingIn");
        final String messageFinal = ChatColor.translateAlternateColorCodes('&', message.replaceAll("%time", String.valueOf(Time)));
        String messages = ChatColor.translateAlternateColorCodes('&', messageFinal);
        sender.sendMessage(prefix + messages);
    }

    public void aboutTo(final CommandSender sender, final int Time) {
        final String message = RandomCoords.getPlugin().language.getString("AlreadyAboutTo");
        final String messageFinal = message.replaceAll("%time", String.valueOf(Time));
        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messageFinal));
    }

    public void couldntFind(final CommandSender sender) {
        String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("TooManyAttempts"));
        sender.sendMessage(prefix + message);
    }

    public void reachedLimit(final CommandSender sender){
        String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("ReachedLimit"));
        sender.sendMessage(prefix + message);
    }

    public void onJoin(final CommandSender sender) {
        String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("OnJoin"));
        sender.sendMessage(prefix + message);
    }

    public void teleportedBy(final CommandSender sender, final Player target) {
        final String originator = sender.getName();
     //String targetName = target.getDisplayName();
        final String message = RandomCoords.getPlugin().language.getString("TeleportedBy");
        final String finalMessage = message.replaceAll("%player", originator);
        target.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', finalMessage));

    }

    public void rcAllUsage(final CommandSender sender) {
        String correct = ChatColor.RED +  "Correct Usage: /RC all {Max} {Min} {World}  - {} = Not required";
        sender.sendMessage(prefix + correct);

    }

    public void invalidWorld(final CommandSender sender, final String worldName) {
        final String message = RandomCoords.getPlugin().language.getString("InvalidWorld");
        final String finalMessage = message.replaceAll("%world", worldName);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', finalMessage));

    }

    public void centerSet(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("CenterSet"));
        sender.sendMessage(prefix + message);

    }
    public void signCreated(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("SignCreated"));
        sender.sendMessage(prefix + message);
    }

    public void noSelection(final CommandSender sender) {
        String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("NoSelection"));
        sender.sendMessage(prefix + message);
    }

    public void portalCreated(final CommandSender sender, final String name, final String wname) {
        final String message = RandomCoords.getPlugin().language.getString("PortalCreated");
        final String messageW = message.replaceAll("%world", wname);
        final String finalMessage = messageW.replaceAll("%name", name);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix + messages);
    }

    public void incorrectUsage(final CommandSender sender, final String command, final  String correct) {
        final String message = ChatColor.translateAlternateColorCodes('&', "Incorrect Usage: " + correct);
        sender.sendMessage(prefix + ChatColor.RED  + message);
    }

    public void portalNotExist(final CommandSender sender, final String name) {
        final String message = RandomCoords.getPlugin().language.getString("PortalNotExist");
        final String finalMessage = message.replaceAll("%name", name);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix + messages);
    }

    public void portalDeleted(final CommandSender sender, final String name) {
        final String message = RandomCoords.getPlugin().language.getString("PortalDeleted");
        final String finalMessage = message.replaceAll("%name", name);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix + messages);
    }

    public void portalList(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("PortalList"));
        sender.sendMessage(prefix + message);
    }

    public void posInSameWorld(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("PositionInSameWorld"));
        sender.sendMessage(prefix + message);
    }

    public void maxSet(final CommandSender sender, final String max, final  String world) {
        final String message = RandomCoords.getPlugin().language.getString("MaximumSet");
        final String stageOne = message.replaceAll("%world", world);
        final String finalMessage = stageOne.replaceAll("%max", max);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix + messages);
    }

    public void minSet(final CommandSender sender, final String min, final String world) {
        final String message = RandomCoords.getPlugin().language.getString("MinimumSet");
        final String stageOne = message.replaceAll("%world", world);
        final String finalMessage = stageOne.replaceAll("%min", min);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix + messages);
    }

    public void maxRemove(final CommandSender sender, final String world) {
        final String message = RandomCoords.getPlugin().language.getString("MaxRemove");
        final String finalMessage = message.replaceAll("%world", world);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix + messages);
    }

    public void minRemove(final CommandSender sender, final  String world) {
        final String message = RandomCoords.getPlugin().language.getString("MinRemove");
        final String finalMessage = message.replaceAll("%world", world);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix + messages);
    }

    public void centerRemove(final CommandSender sender, final String world) {
        final String message = RandomCoords.getPlugin().language.getString("CenterRemove");
        final String finalMessage = message.replaceAll("%world", world);
        final String messages = ChatColor.translateAlternateColorCodes('&',finalMessage);
        sender.sendMessage(prefix + messages);
    }


    public void youMoved(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("MovedTooFar"));
        sender.sendMessage(prefix + message);
    }


    public void tookDamage(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("CantUseInCombat"));
        sender.sendMessage(prefix + message);
    }


    public void worldBanned(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("WorldBanned"));
        sender.sendMessage(prefix + message);
    }

    public void warpSet(final CommandSender sender, final String name) {
        final String message = RandomCoords.getPlugin().language.getString("WarpSet");
        final String finalMessage = message.replaceAll("%name", name);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix + messages);
    }

    public void warpDelete(final CommandSender sender, final String name) {
        final String message = RandomCoords.getPlugin().language.getString("WarpDelete");
        final String finalMessage = message.replaceAll("%name", name);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix + messages);
    }

    public void noWarps(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("NoWarps"));
        sender.sendMessage(prefix + message);
    }

    public void noCommand(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("NoSuchCommand"));
        sender.sendMessage(prefix + message);
    }

    public void charged(final CommandSender sender, final double cost) {
        final String message = RandomCoords.getPlugin().language.getString("Charged");
        final String costMessage = message.replaceAll("%cost", String.valueOf(cost));
        final String messages = ChatColor.translateAlternateColorCodes('&', costMessage);
        sender.sendMessage(prefix + messages);
    }

    public void cost(final CommandSender sender, final double cost) {
        final String message = RandomCoords.getPlugin().language.getString("Cost");
        final String costMessage = message.replaceAll("%cost", String.valueOf(cost));
        final String messages = ChatColor.translateAlternateColorCodes('&', costMessage);
        sender.sendMessage(prefix + message);
    }

    public void warpNotExist(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("WarpNotExist"));
        sender.sendMessage(prefix + message);
    }

    public void wandGiven(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("WandGive"));
        sender.sendMessage(prefix + message);
    }




}
