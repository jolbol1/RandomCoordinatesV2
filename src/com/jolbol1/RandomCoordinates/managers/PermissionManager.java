package com.jolbol1.RandomCoordinates.managers;

import com.jolbol1.RandomCoordinates.RandomCoords;
import org.bukkit.command.CommandSender;

/**
 * Created by James on 01/07/2016.
 */
public class PermissionManager {

    private final MessageManager messages = new MessageManager();

    public boolean hasPermission(CommandSender sender, String permission){
        if(sender.hasPermission(permission)) {
            return true;
        } else {
            messages.noPermission(sender);
            return false;
        }

    }


}
