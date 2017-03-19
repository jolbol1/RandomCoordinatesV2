package com.jolbol1.RandomCoordinates.listeners;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.managers.CoordType;
import com.jolbol1.RandomCoordinates.managers.Coordinates;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by James on 04/07/2016.
 */
public class onJoin implements Listener {

    private final Coordinates coordinates = new Coordinates();
    private final MessageManager messages = new MessageManager();

    /**
     * The event that checks wheter to teleport the player on join.
     * @param e The player join event.
     */
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        /**
         * If the onJoin config option is false, then dont run the code.
         */
        if (RandomCoords.getPlugin().config.getString("OnJoin").equalsIgnoreCase("false")) {
            return;
        }
        /**
         * If the player has the permission to be teleported onJoin then do so. Else, Dont. :P
         */
        if (RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.OnJoin") || RandomCoords.getPlugin().hasPermission(e.getPlayer(), "Random.*")) {
            //Grabs an instance of the player who is joining.
            final Player p = e.getPlayer();
            /**
             * Checks if the player has played before, If so dont run the code.
             */
            if (p.hasPlayedBefore()) {
                return;
            }
            //Get the command that should be run on join. Plans to change this to a list.
            //Initiate the coordinates function which will handle the random teleport. Notice the secret key to get default values.
            coordinates.finalCoordinates(p, 574272099, 574272099, p.getWorld(), CoordType.JOIN, 0);
            //Message them to let them know that they have been teleported.
            messages.onJoin(p);


        }

    }


}
