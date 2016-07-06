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


    //Check if Random Location is in the WorldBorder
    public boolean WorldBorderCheck(Location l) {
        if (!(Bukkit.getServer().getPluginManager().getPlugin("WorldBorder") == null)) {
            if (RandomCoords.getPlugin().config.getString("WorldBorder").equalsIgnoreCase("true")) {

                BorderData border = Config.Border(l.getWorld().getName());
                return border == null || border.insideBorder(l);

            } else {
                return true;
            }

        } else {

            return true;
        }
    }
}