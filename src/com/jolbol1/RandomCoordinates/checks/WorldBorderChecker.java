package com.jolbol1.RandomCoordinates.checks;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by JamesShopland on 02/11/14.
 */
public class WorldBorderChecker {


    /**
     * Checks if the location is outside of the WorldBorder world border. (Plugin)
     * @param l The location to check.
     * @return True or false, Is the location outside of the border.
     */
    public boolean WorldBorderCheck(final Location l) {
        if (!(Bukkit.getServer().getPluginManager().getPlugin("WorldBorder") == null)) {
            if (RandomCoords.getPlugin().config.getString("WorldBorder").equalsIgnoreCase("true")) {

                final BorderData border = Config.Border(l.getWorld().getName());
                return border == null || border.insideBorder(l);

            } else {
                return true;
            }

        } else {

            return true;
        }
    }
}