/*
 *     RandomCoords, Provding the best Bukkit Random Teleport Plugin
 *     Copyright (C) 2014  James Shopland
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

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
    private String prefix() {
        String prefix = RandomCoords.getPlugin().language.getString("Prefix") + ChatColor.RESET;
        String message = ChatColor.translateAlternateColorCodes('&', prefix);
        return message;

    }

    /**
     * Message for if the player has no permission.
     * @param sender Who we're sending the message to.
     */
    public void noPermission(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("NoPermission"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * Message for when the min is greater than the maximum.
     * @param sender Who we're sending the message to.
     */
    public void minTooLarge(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("MinTooLarge"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * The message sent after teleporting them.
     * @param player Who we're sending the message to.
     * @param location The location the player ended up at.
     */
    public void teleportMessage(final Player player, final Location location) {
        final String message = RandomCoords.getPlugin().language.getString("TeleportMessage");
        final String locX = String.valueOf(location.getX());
        final String locY = String.valueOf(location.getBlockY() - 2);
        final String locZ = String.valueOf(location.getZ());
        final String newMessage = message.replaceAll("%coordinates", (locX + ", " + locY + ", "  + locZ));
        player.sendMessage(prefix() + ChatColor.translateAlternateColorCodes('&', newMessage));

    }

    /**
     * Message thats send if the command sender isnt a player.
     * @param sender Who we're sending the message to.
     */
    public void notPlayer(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("NotPlayer"));
        sender.sendMessage(prefix() + message);

    }

    /**
     * The message for when they are still in a cooldown.
     * @param sender Who we're sending the message to.
     * @param Time The time left in the cooldown.
     */
    void cooldownMessage(final CommandSender sender, final int Time) {
        final String message = RandomCoords.getPlugin().language.getString("CooldownMessage");
        final String messageTime = message.replaceAll("%time", String.valueOf(Time));
        sender.sendMessage(prefix() + ChatColor.translateAlternateColorCodes('&', messageTime));
    }

    /**
     * The message sent when reloading the files.
     * @param sender Who we're sending the message to.
     */
    public void reloadMessage(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("ReloadFiles"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * The message that tells them how long until they teleport.
     * @param sender Who we're sending the message to.
     * @param Time The time until they teleport.
     */
    void TeleportingIn(final CommandSender sender, final int Time) {
        final String message = RandomCoords.getPlugin().language.getString("TeleportingIn");
        final String messageFinal = ChatColor.translateAlternateColorCodes('&', message.replaceAll("%time", String.valueOf(Time)));
        final String messages = ChatColor.translateAlternateColorCodes('&', messageFinal);
        sender.sendMessage(prefix() + messages);
    }

    /**
     * Message thats sent when they try to teleport, but are already about to.
     * @param sender Who we're sending the message to.
     * @param Time Time until they teleport.
     */
    void aboutTo(final CommandSender sender, final int Time) {
        final String message = RandomCoords.getPlugin().language.getString("AlreadyAboutTo");
        final String messageFinal = message.replaceAll("%time", String.valueOf(Time));
        sender.sendMessage(prefix() + ChatColor.translateAlternateColorCodes('&', messageFinal));
    }

    /**
     * Message thats sent if no safe location is found.
     * @param sender Who we're sending the message to.
     */
    public void couldntFind(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("TooManyAttempts"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * Message thats sent when the player has reached the limit.
     * @param sender Who we're sending the message to.
     */
    void reachedLimit(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("ReachedLimit"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * Message that is sent when the players teleported on join.
     * @param sender Who we're sending the message to.
     */
    public void onJoin(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("OnJoin"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * Tells the player who they have been teleported by.
     * @param sender Who we're sending the message to.
     * @param target Who teleported the player.
     */
    public void teleportedBy(final CommandSender sender, final Player target) {
        final String originator = sender.getName();
        //String targetName = target.getDisplayName();
        final String message = RandomCoords.getPlugin().language.getString("TeleportedBy");
        final String finalMessage = message.replaceAll("%player", originator);
        target.sendMessage(prefix() + ChatColor.translateAlternateColorCodes('&', finalMessage));

    }

    /**
     * The message thats sent when /RC all is used incorrectly.
     * @param sender Who we're sending the message to.
     */
    public void rcAllUsage(final CommandSender sender) {
        final String correct = ChatColor.RED + "Correct Usage: /RC all {Max} {Min} {World}  - {} = Not required";
        sender.sendMessage(prefix() + correct);

    }

    /**
     * Message thats sent when the world is invalid.
     * @param sender Who we're sending the message to.
     * @param worldName The name of the world we're checking.
     */
    public void invalidWorld(final CommandSender sender, final String worldName) {
        final String message = RandomCoords.getPlugin().language.getString("InvalidWorld");
        final String finalMessage = message.replaceAll("%world", worldName);
        sender.sendMessage(prefix() + ChatColor.translateAlternateColorCodes('&', finalMessage));

    }

    /**
     * Message that is sent when the center of the world is set.
     * @param sender Who we're sending the message to.
     */
    public void centerSet(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("CenterSet"));
        sender.sendMessage(prefix() + message);

    }

    /**
     * Message that is sent when a sign is created.
     * @param sender Who we're sending the message to.
     */
    public void signCreated(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("SignCreated"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * Message that is sent when no selection is made.
     * @param sender Who we're sending the message to.
     */
    public void noSelection(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("NoSelection"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * Message that is sent when a portal is created.
     * @param sender Who we're sending the message to.
     * @param name Name of the portal
     * @param wname Name of the world the portals teleports to.
     */
    public void portalCreated(final CommandSender sender, final String name, final String wname) {
        final String message = RandomCoords.getPlugin().language.getString("PortalCreated");
        final String messageW = message.replaceAll("%world", wname);
        final String finalMessage = messageW.replaceAll("%name", name);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix() + messages);
    }

    /**
     * Message that is sent when a command is used incorrectly.
     * @param sender Who we're sending the message to.
     * @param correct The correct usage of the command.
     */
    public void incorrectUsage(final CommandSender sender, final String correct) {
        final String message = ChatColor.translateAlternateColorCodes('&', "Incorrect Usage: " + correct);
        sender.sendMessage(prefix() + ChatColor.RED + message);
    }

    /**
     * Message thats sent if the portal doesnt exist
     * @param sender Who we're sending the message to.
     * @param name The attempted portal name.
     */
    public void portalNotExist(final CommandSender sender, final String name) {
        final String message = RandomCoords.getPlugin().language.getString("PortalNotExist");
        final String finalMessage = message.replaceAll("%name", name);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix() + messages);
    }

    /**
     * Message that is sent when a portals deleted.
     * @param sender Who we're sending the message to.
     * @param name The name of the portal.
     */
    public void portalDeleted(final CommandSender sender, final String name) {
        final String message = RandomCoords.getPlugin().language.getString("PortalDeleted");
        final String finalMessage = message.replaceAll("%name", name);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix() + messages);
    }

    /**
     * Message thats sent that list all of the portals.
     * @param sender Who we're sending the message to.
     */
    public void portalList(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("PortalList"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * Message that is sent if the RC wand selections are in different worlds.
     * @param sender Who we're sending the message to.
     */
    public void posInSameWorld(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("PositionInSameWorld"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * Message that is sent when the max is set.
     * @param sender Who we're sending the message to.
     * @param max The max that was set.
     * @param world The world the max was set for.
     */
    public void maxSet(final CommandSender sender, final String max, final String world) {
        final String message = RandomCoords.getPlugin().language.getString("MaximumSet");
        final String stageOne = message.replaceAll("%world", world);
        final String finalMessage = stageOne.replaceAll("%max", max);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix() + messages);
    }

    /**
     * Message that is sent when a min is set.
     * @param sender Who we're sending the message to.
     * @param min The min that was set.
     * @param world The world that the min was set for.
     */
    public void minSet(final CommandSender sender, final String min, final String world) {
        final String message = RandomCoords.getPlugin().language.getString("MinimumSet");
        final String stageOne = message.replaceAll("%world", world);
        final String finalMessage = stageOne.replaceAll("%min", min);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix() + messages);
    }

    /**
     * Message that is sent when a max is removed for world.
     * @param sender Who we're sending the message to.
     * @param world The world the max was removed for.
     */
    public void maxRemove(final CommandSender sender, final String world) {
        final String message = RandomCoords.getPlugin().language.getString("MaxRemove");
        final String finalMessage = message.replaceAll("%world", world);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix() + messages);
    }

    /**
     * Message that is sent when a min is removed for world.
     * @param sender Who we're sending the message to.
     * @param world The world the min was removed for.
     */
    public void minRemove(final CommandSender sender, final String world) {
        final String message = RandomCoords.getPlugin().language.getString("MinRemove");
        final String finalMessage = message.replaceAll("%world", world);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix() + messages);
    }

    /**
     * Message that is sent when the center for a world is removed.
     * @param sender Who we're sending the message to.
     * @param world The world the center was removed for.
     */
    public void centerRemove(final CommandSender sender, final String world) {
        final String message = RandomCoords.getPlugin().language.getString("CenterRemove");
        final String finalMessage = message.replaceAll("%world", world);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix() + messages);
    }

    /**
     * Message that is sent when a TP is cancelled due to movement.
     * @param sender Who we're sending the message to.
     */
    void youMoved(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("MovedTooFar"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * Message that is sent when a TP is cancelled due to PVP.
     * @param sender Who we're sending the message to.
     */
    void tookDamage(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("CantUseInCombat"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * Message that is sent when the world is banned.
     * @param sender Who we're sending the message to.
     */
    public void worldBanned(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("WorldBanned"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * Message that is sent when a warp is set.
     * @param sender Who we're sending the message to.
     * @param name The name of the warp.
     */
    public void warpSet(final CommandSender sender, final String name) {
        final String message = RandomCoords.getPlugin().language.getString("WarpSet");
        final String finalMessage = message.replaceAll("%name", name);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix() + messages);
    }

    /**
     * Message that is sent when a warps is deleted.
     * @param sender Who we're sending the message to.
     * @param name The name of the warp deleted.
     */
    public void warpDelete(final CommandSender sender, final String name) {
        final String message = RandomCoords.getPlugin().language.getString("WarpDelete");
        final String finalMessage = message.replaceAll("%name", name);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix() + messages);
    }

    /**
     * Message that is sent when there is no Warps.
     * @param sender Who we're sending the message to.
     */
    void noWarps(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("NoWarps"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * Message that is sent when there is no warps for the world.
     * @param sender Who we're sending the message to.
     * @param world The world theres no warps for.
     */
    void noWarpsWorld(final CommandSender sender, String world) {

        final String message = RandomCoords.getPlugin().language.getString("NoWarpsWorld");
        final String finalMessage = ChatColor.translateAlternateColorCodes('&', message.replaceAll("%world", world));

        sender.sendMessage(prefix() + finalMessage);
    }

    /**
     * Message that is sent when there is no such command.
     * @param sender Who we're sending the message to.
     */
    public void noCommand(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("NoSuchCommand"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * Message that is sent when the player is charged.
     * @param sender Who we're sending the message to.
     * @param cost The amount they were charged.
     */
    public void charged(final CommandSender sender, final double cost) {
        final String message = RandomCoords.getPlugin().language.getString("Charged");
        final String costMessage = message.replaceAll("%cost", String.valueOf(cost));
        final String messages = ChatColor.translateAlternateColorCodes('&', costMessage);
        sender.sendMessage(prefix() + messages);
    }

    /**
     * The message thats sent telling them the cost.
     * @param sender Who we're sending the message to.
     * @param cost The amount they will be charged.
     */
    public void cost(final CommandSender sender, final double cost) {
        final String message = RandomCoords.getPlugin().language.getString("Cost");
        final String costMessage = message.replaceAll("%cost", String.valueOf(cost));
        final String messages = ChatColor.translateAlternateColorCodes('&', costMessage);
        sender.sendMessage(prefix() + messages);
    }

    /**
     * The message that is sent if the warp does not exist.
     * @param sender Who we're sending the message to.
     */
    public void warpNotExist(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("WarpNotExist"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * Message sent when player receives the /RC wand.
     * @param sender Who we're sending the message to.
     */
    public void wandGiven(final CommandSender sender) {
        final String message = ChatColor.translateAlternateColorCodes('&', RandomCoords.getPlugin().language.getString("WandGive"));
        sender.sendMessage(prefix() + message);
    }

    /**
     * Message that is when a player /RC all's people/
     * @param sender Who we're sending the message to.
     * @param world The world they telepoted too.
     */
    public void teleportedAll(final CommandSender sender, final String world) {
        final String message = RandomCoords.getPlugin().language.getString("TeleportedAll");
        final String finalMessage = message.replaceAll("%world", world);
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix() + messages);
    }

    public void teleportedAll(final CommandSender sender) {
        final String message = RandomCoords.getPlugin().language.getString("TeleportedAll");
        final String finalMessage = message.replaceAll("%world", "their world");
        final String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix() + messages);
    }

    public void playerNotExist(final CommandSender sender) {
        final String message = RandomCoords.getPlugin().language.getString("PlayerNotExist");
        final String messages = ChatColor.translateAlternateColorCodes('&', message);
        sender.sendMessage(prefix() + messages);
    }

    public void teleportedPlayer(final CommandSender sender, Player target) {
        String message = RandomCoords.getPlugin().language.getString("TeleportedPlayer");
        String finalMessage = message.replaceAll("%player", target.getName());
        String messages = ChatColor.translateAlternateColorCodes('&', finalMessage);
        sender.sendMessage(prefix() + messages);
    }

    public void regionNotExist(CommandSender sender, String region) {
        String message = RandomCoords.getPlugin().language.getString("RegionNotExist");
        String finalMess = message.replaceAll("%region", region);
        String messages = ChatColor.translateAlternateColorCodes('&', finalMess);
        sender.sendMessage(prefix() + messages);

    }


    public void teleportedWithinRegion(CommandSender sender, String region) {
        String message = RandomCoords.getPlugin().language.getString("TeleportedInRegion");
        String finalMess = message.replaceAll("%region", region);
        String messages = ChatColor.translateAlternateColorCodes('&', finalMess);
        sender.sendMessage(prefix() + messages);

    }


    public void inventoryContentsSaved(CommandSender sender, String fileName, boolean overwritten) {
        if(!overwritten) {
            String message = RandomCoords.getPlugin().language.getString("InventorySaved");
            String finalMess = message.replaceAll("%file", fileName);
            String messages = ChatColor.translateAlternateColorCodes('&', finalMess);
            sender.sendMessage(prefix() + messages);
        } else {
            String message = RandomCoords.getPlugin().language.getString("InventorySavedOverride");
            String finalMess = message.replaceAll("%file", fileName);
            String messages = ChatColor.translateAlternateColorCodes('&', finalMess);
            sender.sendMessage(prefix() + messages);
        }
    }

    public void itemSaved(CommandSender sender, String fileName) {

            String message = RandomCoords.getPlugin().language.getString("ItemSaved");
            String finalMess = message.replaceAll("%file", fileName);
            String messages = ChatColor.translateAlternateColorCodes('&', finalMess);
            sender.sendMessage(prefix() + messages);


    }


}
