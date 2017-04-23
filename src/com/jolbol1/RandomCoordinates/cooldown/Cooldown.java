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

package com.jolbol1.RandomCoordinates.cooldown;

/**
 * Created by James on 02/07/2016.
 */

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Cooldown {

    private static final Map<String, Cooldown> cooldowns = new ConcurrentHashMap<>();
    private final int timeInSeconds;
    private final UUID id;
    private final String cooldownName;
    private long start;

    /**
     * Creates a cooldown.
     * @param id Players UUID
     * @param cooldownName The name of the Cooldown.
     * @param timeInSeconds How long the cooldown should last.
     */
    public Cooldown(final UUID id, final String cooldownName, final int timeInSeconds) {
        this.id = id;
        this.cooldownName = cooldownName;
        this.timeInSeconds = timeInSeconds;
    }

    /**
     * Checks if the player is in the cooldown.
     * @param id The players UUID.
     * @param cooldownName The name of the cooldown we're checking they're in.
     * @return True or False, are they in the cooldown.
     */
    public static boolean isInCooldown(final UUID id, final String cooldownName) {
        if (getTimeLeft(id, cooldownName) >= 1) {
            return true;
        } else {
            stop(id, cooldownName);
            return false;
        }
    }

    /**
     * Stops the cooldown.
     * @param id The players UUID.
     * @param cooldownName The name of the cooldown.
     */
    private static void stop(final UUID id, final String cooldownName) {
        Cooldown.cooldowns.remove(id + cooldownName);
    }

    /**
     * Gets an instance of the cooldown.
     * @param id The players UUID
     * @param cooldownName The cooldown name.
     * @return The instance of the cooldown.
     */
    private static Cooldown getCooldown(final UUID id, final String cooldownName) {
        return cooldowns.get(id.toString() + cooldownName);
    }

    /**
     * Gets the time left in a cooldown.
     * @param id The players UUID.
     * @param cooldownName The name of the cooldown.
     * @return The time left.
     */
    public static int getTimeLeft(final UUID id, final String cooldownName) {
        final Cooldown cooldown = getCooldown(id, cooldownName);
        int f = -1;
        if (cooldown != null) {
            final long now = System.currentTimeMillis();
            final long cooldownTime = cooldown.start;
            final int r = (int) (now - cooldownTime) / 1000;
            f = (r - cooldown.timeInSeconds) * (-1);
        }
        return f;
    }

    /**
     * Starts the cooldowns.
     */
    public void start() {
        this.start = System.currentTimeMillis();
        cooldowns.put(this.id.toString() + this.cooldownName, this);
    }

}

