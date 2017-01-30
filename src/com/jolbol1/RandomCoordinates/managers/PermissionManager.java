package com.jolbol1.RandomCoordinates.managers;

import org.bukkit.command.CommandSender;

/**
 * Created by James on 01/07/2016.
 */
class PermissionManager {

    private final MessageManager messages = new MessageManager();

    public boolean hasPermission(final CommandSender sender, final String permission) {
        if (sender.hasPermission(permission)) {
            return true;
        } else {
            messages.noPermission(sender);
            return false;
        }

    }


}
