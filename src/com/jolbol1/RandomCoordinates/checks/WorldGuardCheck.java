package com.jolbol1.RandomCoordinates.checks;

import com.jolbol1.RandomCoordinates.RandomCoords;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by JamesShopland on 11/01/15.
 */
public class WorldGuardCheck {

    public boolean WorldguardCheck(final Location l) {

        //Checks if plugin is installed.
        if (!(Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") == null)) {

            //Checks if WorldGuard is enabled
            if (RandomCoords.getPlugin().config.getString("WorldGuard").equals("true")) {
                final int X = l.getBlockX();
                //  int Y = l.getBlockY();
                final int Z = l.getBlockZ();

                //Calls the BufferZone
                final int r = RandomCoords.getPlugin().config.getInt("CheckingRadius");

                int x = X - r;
                // int y = Y - r;
                int z = Z - r;

                final int bx = x;
                final int bz = z;

                //Checks all coordinates within the buffer zone for a region
                for (int i = 0; i < r * 2 + 1; i++) {
                    for (int j = 0; j < r * 2 + 1; j++) {
                        for (int k = 0; k < r * 2 + 1; k++) {

                            //See if allRegions is enabled.
                            if (RandomCoords.getPlugin().config.getStringList("Regions").contains("allRegions")) {
                                return RandomCoords.getPlugin().getWorldGuard().getRegionManager(l.getWorld()).getApplicableRegions(l).size() == 0;
                            } else {
                                for (final ProtectedRegion reg : RandomCoords.getPlugin().getWorldGuard().getRegionManager(l.getWorld()).getApplicableRegions(l)) {
                                    for (final String regName : RandomCoords.getPlugin().config.getStringList("Regions")) {
                                        //See if region in zone matches ignored regions.
                                        if (reg.getId().equalsIgnoreCase(regName)) {
                                            return false;
                                        }
                                    }
                                }
                            }

                            x++;
                        }
                        z++;
                        x = bx;
                    }
                    z = bz;
                    x = bx;
                    // y++;
                }
            }

            return true;
        } else {

            return true;

        }
    }


}
