package com.jolbol1.RandomCoordinates.managers;


import com.jolbol1.RandomCoordinates.RandomCoords;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Created by James on 01/07/2016.
 */
public class MessageManager {

    private final String prefix = ChatColor.GOLD + "[RandomCoords] ";
    private final ChatColor bad = ChatColor.RED;
    private final ChatColor good = ChatColor.GREEN;



    public void noPermission(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("NoPermission"))).send(sender);
    }

    public void minTooLarge(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("MinTooLarge"))).send(sender);
    }

    public void teleportMessage(Player player, Location location) {
        String message = RandomCoords.getPlugin().language.getString("TeleportMessage");
        String locX =  String.valueOf(location.getX());
        String locY =  String.valueOf(location.getBlockY());
        String locZ = String.valueOf(location.getZ());
        String newMessage = message.replaceAll("%coordinates", ChatColor.GREEN + (locX + ", " + good + locY + ", " + good +  locZ));
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&', newMessage)).tooltip(good + "By RandomCoords plugin").send(player);
    }

    public void notPlayer(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("NotPlayer"))).send(sender);
    }

    public void cooldownMessage(CommandSender sender, int Time) {
        String message = RandomCoords.getPlugin().language.getString("CooldownMessage");
        String messageTime = message.replaceAll("%time", String.valueOf(Time));
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',messageTime)).tooltip(good + " You have " + String.valueOf(Time) + " Seconds Left!").send(sender);
    }

    public void reloadMessage(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("ReloadFiles"))).send(sender);
    }

    public void TeleportingIn(CommandSender sender, int Time) {
        String message = RandomCoords.getPlugin().language.getString("TeleportingIn");
        String messageFinal = ChatColor.translateAlternateColorCodes('&', message.replaceAll("%time", String.valueOf(Time)));
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',messageFinal)).tooltip(good + "To a random location!").send(sender);
    }

    public void aboutTo(CommandSender sender, int Time) {
        String message = RandomCoords.getPlugin().language.getString("AlreadyAboutTo");
        String messageFinal = message.replaceAll("%time", String.valueOf(Time));
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',messageFinal)).tooltip(good + "You're about to randomly teleport!").send(sender);
    }

    public void couldntFind(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("TooManyAttempts"))).send(sender);
    }

    public void reachedLimit(CommandSender sender){
        String limit = String.valueOf(RandomCoords.getPlugin().config.getInt("Limit"));
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("ReachedLimit"))).tooltip(good + "The limit was " + limit).send(sender);
    }

    public void onJoin(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("OnJoin"))).tooltip(good + "By RandomCoords Plugin!").send(sender);
    }

    public void teleportedBy(CommandSender sender, Player target) {
     String originator = sender.getName();
     String targetName = target.getDisplayName();
     String message = RandomCoords.getPlugin().language.getString("TeleportedBy");
     String finalMessage = message.replaceAll("%player", originator);
     new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',finalMessage)).tooltip(good + "By RandomCoords Plugin!").send(target);
    }

    public void rcAllUsage(CommandSender sender) {
        new FancyMessage(prefix).then("Correct Usage: /RC all {Max} {Min} {World}  - {} = Not required").suggest("/rc all").send(sender);
    }

    public void invalidWorld(CommandSender sender, String worldName) {
        String message = RandomCoords.getPlugin().language.getString("InvalidWorld");
        String finalMessage = message.replaceAll("%world", worldName);
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',finalMessage)).send(sender);
    }

    public void centerSet(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("CenterSet"))).send(sender);

    }

    public void signCreated(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("SignCreated"))).send(sender);
    }

    public void noSelection(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("NoSelection"))).suggest("/rc wand").send(sender);
    }

    public void portalCreated(CommandSender sender, String name, String wname) {
        String message = RandomCoords.getPlugin().language.getString("PortalCreated");
        String messageW = message.replaceAll("%world", wname);
        String finalMessage = messageW.replaceAll("%name", name);
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',finalMessage)).send(sender);
    }

    public void incorrectUsage(CommandSender sender, String command, String correct) {
        new FancyMessage(prefix).then("Incorrect Usage: " + correct).suggest(command).send(sender);
    }

    public void portalNotExist(CommandSender sender, String name) {
        String message = RandomCoords.getPlugin().language.getString("PortalNotExist");
        String finalMessage = message.replaceAll("%name", name);
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',finalMessage)).send(sender);
    }

    public void portalDeleted(CommandSender sender, String name) {
        String message = RandomCoords.getPlugin().language.getString("PortalDeleted");
        String finalMessage = message.replaceAll("%name", name);
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',finalMessage)).send(sender);
    }

    public void portalList(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("PortalList"))).send(sender);
    }

    public void posInSameWorld(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("PositionInSameWorld"))).send(sender);
    }

    public void maxSet(CommandSender sender, String max, String world) {
        String message = RandomCoords.getPlugin().language.getString("MaximumSet");
        String stageOne = message.replaceAll("%world", world);
        String finalMessage = stageOne.replaceAll("%max", max);

        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',finalMessage)).send(sender);
    }

    public void minSet(CommandSender sender, String min, String world) {
        String message = RandomCoords.getPlugin().language.getString("MinimumSet");
        String stageOne = message.replaceAll("%world", world);
        String finalMessage = stageOne.replaceAll("%min", min);

        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',finalMessage)).send(sender);
    }

    public void maxRemove(CommandSender sender, String world) {
        String message = RandomCoords.getPlugin().language.getString("MaxRemove");
        String finalMessage = message.replaceAll("%world", world);
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',finalMessage)).send(sender);
    }

    public void minRemove(CommandSender sender, String world) {
        String message = RandomCoords.getPlugin().language.getString("MinRemove");
        String finalMessage = message.replaceAll("%world", world);
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',finalMessage)).send(sender);
    }

    public void centerRemove(CommandSender sender, String world) {
        String message = RandomCoords.getPlugin().language.getString("CenterRemove");
        String finalMessage = message.replaceAll("%world", world);
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',finalMessage)).send(sender);
    }


    public void youMoved(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("MovedTooFar"))).send(sender);
    }


    public void tookDamage(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("CantUseInCombat"))).send(sender);
    }


    public void worldBanned(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("WorldBanned"))).send(sender);
    }

    public void warpSet(CommandSender sender, String name) {
        String message = RandomCoords.getPlugin().language.getString("WarpSet");
        String finalMessage = message.replaceAll("%name", name);
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',finalMessage)).send(sender);
    }

    public void warpDelete(CommandSender sender, String name) {
        String message = RandomCoords.getPlugin().language.getString("WarpDelete");
        String finalMessage = message.replaceAll("%name", name);
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',finalMessage)).send(sender);
    }

    public void noWarps(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("NoWarps"))).send(sender);
    }

    public void noCommand(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("NoSuchCommand"))).send(sender);
    }

    public void charged(CommandSender sender, double cost) {
        String message = RandomCoords.getPlugin().language.getString("Charged");
        String costMessage = message.replaceAll("%cost", String.valueOf(cost));
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',costMessage)).send(sender);
    }

    public void cost(CommandSender sender, double cost) {
        String message = RandomCoords.getPlugin().language.getString("Cost");
        String costMessage = message.replaceAll("%cost", String.valueOf(cost));

        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',costMessage)).send(sender);
    }

    public void warpNotExist(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&',RandomCoords.getPlugin().language.getString("WarpNotExist"))).send(sender);
    }

    public void wandGiven(CommandSender sender) {
        new FancyMessage(prefix).then(ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("WandGive"))).send(sender);
    }




}
