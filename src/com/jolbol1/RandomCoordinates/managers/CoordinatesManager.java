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
import com.jolbol1.RandomCoordinates.checks.*;
import com.jolbol1.RandomCoordinates.cooldown.Cooldown;
import com.jolbol1.RandomCoordinates.event.RandomTeleportEvent;
import com.jolbol1.RandomCoordinates.managers.Util.CoordType;
import com.jolbol1.RandomCoordinates.managers.Util.RandomWorld;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

/**
 * Created by James on 01/07/2016.
 */
public class CoordinatesManager {

    //Setups the Random Generator. Using SecureRandom for more randomness.
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    //Creates an instance of the message manager class.
    private final MessageManager messages = new MessageManager();
    // Creates an instance of the Faction Checker class.
    private final FactionChecker fc = new FactionChecker();
    //Creates an instance of the Grief Prevention class
    private final GriefPreventionCheck gpc = new GriefPreventionCheck();
    //Creates an instance of the Grief Prevention class.
    private final PlayerRadCheck prc = new PlayerRadCheck();
    //Creates an instance of the Town Checker.
    private final TownyChecker tc = new TownyChecker();
    //Creates an instance of the WorldBorder checker.
    private final WorldBorderChecker wbc = new WorldBorderChecker();
    //Creates an instance of the WorldGuard checker.
    private WorldGuardCheck wgc = null;
    //Creates an instance of the Nether coordinate generator.
    private final Nether nether = new Nether();
    //Creates an instance of the End Island finder.
    private final End end = new End();
    //Grabs the max attempts value from the config.
    private final int maxAttempts = RandomCoords.getPlugin().config.getInt("MaxAttempts");
    //Load up RedProtect
    private final RedProtect redProtect = new RedProtect();
    //Load up KingdomsClaim
    private final KingdomsClaim kingdomsClaim = new KingdomsClaim();
    //Grab an instance of the debug manager.
    private final DebugManager debugManager = new DebugManager();

    private final BonusChestManager bonusChestManager = new BonusChestManager();

    private final ResidenceCheck residenceCheck = new ResidenceCheck();

    public int key = 574272099;

    private FileConfiguration config = RandomCoords.getPlugin().config;



    /**
     * Returns wheter WG is installed or not.
     * @param l The location to check.
     * @return True if location is safe /WG not installed. False if not safe.
     */
    private boolean worldGuardEnabled(Location l) {
        if(RandomCoords.getPlugin().getWorldGuard() != null) {
            if(wgc == null) {
                wgc = new WorldGuardCheck();
            }
            return wgc.WorldguardCheck(l);
        } else {
            return true;
        }
    }

    /**
     * Gets a random coordinate relative to the world center.
     * @param world The world we're getting the coordinate in.
     * @return  The random location.
     */
    public Location getRelativeRandomLocation(World world, int minDefault, int max) {
        //Set up the randomWorld.
        RandomWorld randomWorld = new RandomWorld(world);
        //The max for the world. Change if custom, If not get default.
        if(max == key) {
            max = randomWorld.getMax();
        }
        //The default world minimum
        if(minDefault == key) {
            minDefault = randomWorld.getMin();
        }
        //minOne/minTwo will either be 0 or minDefault. (This stops a cross bug, Ask jolbol1 if you do not understand.)
        List<Integer> minimumList = getRandomMin(minDefault);
        int minOne = minimumList.get(0);
        int minTwo = minimumList.get(1);
        //Gets the worlds center
        Location center = randomWorld.getCenter();
        //Creates the randomLoaction.
        Location randomLocation = center.add(getRandomNumber(minOne, max), 0, getRandomNumber(minTwo, max));

        //Get the highest block.
        double y = getSafeY(randomLocation);

        if(isWithinCircle(randomLocation, max, randomWorld.getCenter())) {
            return null;
        }


        randomLocation.setY(y);
        return randomLocation;

    }

    //Turns the minimum either into
    private List<Integer> getRandomMin(int minDefault) {
        List<Integer> minimums = new ArrayList<Integer>();
        minimums.add(0);
        minimums.add(minDefault);
        Collections.shuffle(minimums);
        return minimums;
    }


    public int getRandomNumber(final int min, final int max) {

        //Returns a random number
        if(config.getString("RandomOrg").equalsIgnoreCase("true")) {
            return Integer.parseInt(getRandomFromOrg(min, max));
        }

        return (random.nextInt((max - min) + 1) + min) * modulus();
    }

    //Function to select 1 or -1 to generate positive and negative coordinates, Kinda a reverse modulus but whatever.

    /**
     * Gets a random number thats either -1 or 1.
     * @return Randomly, Either 1 or -1.
     */
    private int modulus() {
        //Grabs a random boolean, then returns -1 or 1 based on the
        return  (random.nextBoolean() ? -1 : 1);
    }

    /**
     * Returns whether or not the location is safe or not.
     * @param location The location to check
     * @return False if unsafe, True if safe.
     */
    private boolean isTheLocationSafe(final Location location) {

        if(location == null) {
            return false;
        }

        if(location.getY() == 0) {
            return false;
        }


        //Get the block at the specified location
        Block blockAtLocation = location.subtract(0, 2, 0).getBlock();
        //Get the block at the surface.
        Block surfaceBlock = blockAtLocation.getRelative(BlockFace.DOWN);
        //Get the blacklisted list.
        List<String> blacklisted = RandomCoords.getPlugin().blacklist.getStringList("Blacklisted");

        if(RandomCoords.getPlugin().config.getString("ChunkLoader").equalsIgnoreCase("true")) {
            boolean exit = false;
            while (!exit) {
                //The boolean thats used to cancel or continue the loop. If chunk loader is off, set as true. If chunk is generated set as trie.
                exit = generateChunk(location);
            }
        }
        //Parse over all the blocks in the blacklist.
        for(String materialName : blacklisted) {
            //Make sure the material exists.
            if(Material.getMaterial(materialName) != null) {
                //Get the blacklisted material
                Material material = Material.getMaterial(materialName);
                //Is the block blacklisted. If so return false.
                if(surfaceBlock.getType().equals(material)) {
                    return false;
                }

            }
            else if(Biome.valueOf(materialName) != null) {
                if(surfaceBlock.getBiome().equals(Biome.valueOf(materialName))) {
                    return false;
                }
            } else {
                //This is what happens when the block is not recognised.
                Bukkit.getLogger().log(Level.WARNING, "The block " + materialName + " was not recognised as a Minecraft block.");
            }

        }
        //If its not one of the blacklisted blocks, Continue and check for the regions.
        return !kingdomsClaim.kingdomClaimNearby(location) && !redProtect.redProtectClaimNearby(location) &&
                !fc.factionLandNearby(location) && !tc.townyClaimNearby(location) && !prc.areThereNearbyPlayers(location) &&
                !gpc.griefPrevNearby(location) && wbc.WorldBorderCheck(location) && worldGuardEnabled(location) && !isOutsideBorder(location) &&
                !residenceCheck.isChunkProtected(location);
    }


    /**
     * Is the location outside of the world border?
     * @param loc the location to check
     * @return False if not, True if so.
     */
    public boolean isOutsideBorder(final Location loc) {
        /**
         * Checks if we should check for VanillaBorders. Else return false.
         */
        if (RandomCoords.getPlugin().config.getString("VanillaBorder").equalsIgnoreCase("true")) {
            //Get the worlds world border.
            final WorldBorder wb = loc.getWorld().getWorldBorder();
            //Get the center of that border.
            final Location center = wb.getCenter();
            //Get the size of that border.
            final double size = wb.getSize();
            //Divide that size in half to get the radius of the border.
            final double radius = size / 2;
            //Get the X coord of the center.
            final int centerX = center.getBlockX();
            //Get the Z coord of the center.
            final int centerZ = center.getBlockZ();
            //Get the X coord of the player.
            final int playerX = loc.getBlockX();
            //Get the Z coord of the Player.
            final int playerZ = loc.getBlockZ();

            /**
             * If the player is outside the border, then return true.
             */
            if (playerX > (centerX + radius) || playerX < (centerX - radius) || playerZ > (centerZ + radius) || playerZ < (centerZ - radius)) {
                return true;
            }
        }
        return false;


    }


    /**
     * Grabs a safe random location. Tries as many times as max
     * @param world the world we want the random location for.
     * @param max the maximum coordinate to teleport to.
     * @param min the minimum coordinate to teleport to.
     * @return Returns a random location, Or null if it couldnt find a safe one.
     */
    public Location getSafeRandomLocation(World world, int max, int min) {
        Location randomLocation = null;
        boolean isItSafe = false;
        int attempts = 0;
        int maxAttempts = RandomCoords.getPlugin().config.getInt("MaxAttempts");


        while(!isItSafe) {
            if(attempts == maxAttempts) {
                return null;
            }

            //Get a random releative location with provided bounds.
            randomLocation = getRelativeRandomLocation(world, min, max);
            isItSafe = isTheLocationSafe(randomLocation);
            attempts++;

        }

        return randomLocation;
    }


    public boolean randomlyTeleportPlayer(Player player, World world, int max, int min, CoordType coordType, double cost) {


        if(inTimeBefore(player)) {
            messages.TeleportingIn(player, Cooldown.getTimeLeft(player.getUniqueId(), "TimeBefore"));
            return false;
        }



        if(inCooldown(player, coordType.getName())) {
            messages.cooldownMessage(player, Cooldown.getTimeLeft(player.getUniqueId(), coordType.getName()));
            return false;
        }

        boolean doesLimiterApply = false;
        if(!player.hasPermission("Random.Limiter.Byapss")) { doesLimiterApply = doesLimiterApply(coordType); }
        //Have they reached the limit, If limiter applys.
        if(doesLimiterApply && hasPlayerReachedLimit(player)) {
            messages.reachedLimit(player);
            return false; }


        switch (coordType) {
            case COMMAND:
                cost = config.getDouble("CommandCost");
                break;
            case PORTAL:
                cost = RandomCoords.getPlugin().config.getDouble("PortalCost");
                break;
        }

        if(!hasCorrectAmountOfMoney(player, cost)) {
            messages.cost(player, cost);
            return false;
        }

        //Get the time before
        int timeBefore = 0;
        if(!player.hasPermission("Random.TBT.Bypass") && !player.hasPermission("Random.Admin.*") && !player.hasPermission("Random.*")) { timeBefore = getTimeBefore(coordType);}

        //Get the cooldown time.
        int cooldownTime = 0;
        if(!player.hasPermission("Random.Cooldown.Bypass") && !player.hasPermission("Random.Admin.*") && !player.hasPermission("Random.*")) { cooldownTime = getCooldownTime(coordType); }

        //Does the limiter apply?
        if(timeBefore != 0) {
            messages.TeleportingIn(player, timeBefore);
        }

        //Get a new safe location with provided bounds.
        Location safeLocation;
        if(shouldWarp(coordType)) {
            safeLocation = getRandomWarp(player, world, coordType);
        } else {
            safeLocation = getSafeRandomLocation(world, max, min);
            if(!RandomCoords.getPlugin().skyBlockSave.getStringList("SkyBlockWorlds").contains(world.getName())) {
                double y = getSafeY(safeLocation);
                safeLocation.setY(y);
            }
        }

        //Is the location not actually safe?
        if(safeLocation == null) {
            messages.couldntFind(player);
            return false;
        }



        if(safeLocation.getWorld().getBiome(safeLocation.getBlockX(), safeLocation.getBlockZ()) == Biome.SKY ) {
            safeLocation = end.endCoord(safeLocation);
        }




        scheduleTeleport(player, safeLocation, coordType, timeBefore, cooldownTime, player.getLocation(), player.getHealth(), cost);

            /**
             * The loop that handles the loading of the chunk that they are teleporting into.
             * If the config options false, Exit the loop. If the chunks generated, exit the loop.
             */

        return true;

    }

    /**
     * Schedules a teleport to occur. Starts timeBefore cooldown first, then after delay the actual command cooldown.
     * @param player Player who we are teleporting
     * @param randomLocation The random location to send the player to
     * @param coordType The coordType
     * @param timeBefore the timebefore teleport
     * @param cooldownTime the cooldown time
     */
    public void scheduleTeleport(Player player, Location randomLocation, CoordType coordType, int timeBefore, int cooldownTime, Location start, double startHealth, double cost) {
        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        Cooldown timeBeforeCooldown = new Cooldown(player.getUniqueId(), "TimeBefore", timeBefore);

        Cooldown cooldown = new Cooldown(player.getUniqueId(), coordType.getName(), cooldownTime);
        timeBeforeCooldown.start();


        double timeBeforeTp = (double) timeBefore;
        //This is to ensure it teleports them, You need at least a tick delay to register the login.
        if(coordType == CoordType.JOIN || coordType == CoordType.JOINWORLD) {
            timeBeforeTp = 0.3;
        }


        scheduler.scheduleSyncDelayedTask(RandomCoords.getPlugin().getInstance(), () -> {
            RandomTeleportEvent event = new RandomTeleportEvent(player, randomLocation, coordType, cooldownTime);
            Bukkit.getServer().getPluginManager().callEvent(event);


            if(coordType != CoordType.JOIN && coordType != CoordType.JOINWORLD && coordType != CoordType.SIGN && coordType != CoordType.PORTAL) {
                if (hasThePlayerMoved(player, start)) {
                    messages.youMoved(player);
                    event.setCancelled(true);
                    return;
                }
            }

            if(isPlayerInCombat(player, startHealth)) {
                messages.tookDamage(player);
                event.setCancelled(true);
                return;
            }

            if (!event.isCancelled()) {
                if(!chargePlayer(player, cost)) { return;}
                cooldown.start();


                messages.teleportMessage(player, randomLocation);
                player.teleport(randomLocation);
                performOnJoinCommands(event);
                playEffectAtLocation(randomLocation);
                playSoundtLocation(player, randomLocation);
                addOneToLimiter(player, coordType);


                if(coordType == CoordType.JOIN){ bonusChests(player, randomLocation); }

                /**
                 * Start the suffocation timer.
                 */
                final Cooldown c = new Cooldown(player.getUniqueId(), "Invul", 30);
                c.start();
                /**
                 * Start the Invul timer, This is used if the users would like the players to be invul for a while after TP.
                 */
                final Cooldown cT = new Cooldown(player.getUniqueId(), "InvulTime", RandomCoords.getPlugin().config.getInt("InvulTime"));
                cT.start();

            }


            //My definition of a tick/second here is shorter. This is due to a double tp bug with portals.
        }, (long) (timeBeforeTp * 20) - 1);

    }


    /**
     * Does time before teleport apply for that specific coordType
     * @param coordType
     * @return the time before teleport time.
     */
    public int getTimeBefore(CoordType coordType) {
        if(config.getStringList("TimeBeforeApplys").contains(coordType.getName())) {
            return config.getInt("TimeBeforeTeleport");
        }
        return 0;
    }

    /**
     * Get the cooldown for the teleport method.
     * @param coordType
     * @return the cooldown time.
     */
    public int getCooldownTime(CoordType coordType) {
        if(config.getStringList("CooldownApplys").contains(coordType.getName())) {
            return config.getInt("CooldownTime");
        }
        return 0;
    }

    /**
     * Does the limiter apply for this teleport method.
     * @param coordType The method.
     * @return True if it does, False if not.
     */
    public boolean doesLimiterApply(CoordType coordType) {
        return config.getStringList("LimiterApplys").contains(coordType.getName());
    }

    /**
     * Has the player reached the limit.
     * @param player
     * @return
     */
    public boolean hasPlayerReachedLimit(Player player) {
        if(player.hasPermission("Random.Limiter.Bypass")) { return false; }
        if(RandomCoords.getPlugin().limiter.get(player.getUniqueId().toString() + ".Uses") == null) {
            return false;
        } else {
            int uses = RandomCoords.getPlugin().limiter.getInt(player.getUniqueId().toString() + ".Uses");
            int maximumUse = config.getInt("Limit");
            if(uses < maximumUse) {
                return false;
            }
        }
        return true;
    }


    /**
     * Charge the player for the cost of teleporting.
     * @param p The player we are taking money from.
     * @param cost The cost of teleporting
     * @return True if they have paid. False if they cannot pay.
     */
    public boolean chargePlayer(final Player p, final double cost) {
        if (!RandomCoords.getPlugin().setupEconomy() || cost == 0) {
            return true;
        } else {
            final EconomyResponse r = RandomCoords.getPlugin().econ.withdrawPlayer(p, cost);
            if (r.transactionSuccess()) {
                messages.charged(p, cost);
                return true;

            } else {
                messages.cost(p, cost);

                return false;
            }
        }
    }


    /**
     * Gets if the player is in the cooldown for that specific method.
     * @param player Player to check
     * @param cooldownName The cooldown we are checking.
     * @return True they are in a cooldown
     */
    public boolean inCooldown(Player player, String cooldownName) {
        return Cooldown.isInCooldown(player.getUniqueId(), cooldownName);
    }

    /**
     * Is the player in timeBefore?
     * @param player The player to check
     * @return True, They are in cooldown. False they are not.
     */
    public boolean inTimeBefore(Player player) {
        if(Cooldown.getTimeLeft(player.getUniqueId(), "TimeBefore") > 0) {
            return true;
        }
        return Cooldown.isInCooldown(player.getUniqueId(), "TimeBefore");
    }

    /**
     * Is the world banned?
     * @param world The world to check
     * @return True if banned, False if not.
     */
    private boolean isWorldBanned(final World world) {

        if (RandomCoords.getPlugin().config.getStringList("BannedWorlds").contains(world.getName())) {
            //Return true, the worlds banned
            return true;
        }
        //Return false, the world is not banned.
        return false;
    }

    /**
     * Checks if the player should warp, not teleport randomly.
     * @param coordType The coordType used.
     * @return True if they should warp, False if not
     */
    public boolean shouldWarp(CoordType coordType) {
        switch (coordType) {
            case WARPS:
                return true;
            case WARPWORLD:
                return true;
            case COMMAND:
                if(config.getString("Command").equalsIgnoreCase("warp")) { return true; }
                break;
            case ALL:
                if(config.getString("All").equalsIgnoreCase("warp")) { return true; }
                break;
            case PLAYER:
                if(config.getString("Others").equalsIgnoreCase("warp")) { return true; }
                break;
            case PORTAL:
                if(config.getString("Portals").equalsIgnoreCase("warp")) { return true; }
                break;
            case SIGN:
                if(config.getString("Signs").equalsIgnoreCase("warp")) { return true; }
                break;
            case JOIN:
            case JOINWORLD:
                if(config.getString("Join").equalsIgnoreCase("warp")) { return true; }
                break;

        }
        return false;
    }

    /**
     * This function handles Random Warps.
     * @param p The player we are teleporting
     * @param world The world we're Randomly Warping them in.
     * @param coordType The method in which they requested the warp.
     * @return Returns one of the Warps at random.
     */
    private Location getRandomWarp(final Player p, final World world, final CoordType coordType) {

        //Initiate the warp list.
        Set<String> list = null;

        /**
         * Checks if there is actually any warps set, If so set it to the list above.
         * Else return no warp message and set location as null.
         */
        if (RandomCoords.getPlugin().warps.get("Warps") != null) {
            //Set the list to the one from the config.
            list = RandomCoords.getPlugin().warps.getConfigurationSection("Warps.").getKeys(false);
        } else if(list == null) {
            messages.noWarps(p);
            return null;
        }


        //Copy the list to be able to edit in for loop.
        final Set<String> listCopy = new HashSet<>(list);

        /**
         * If the list size has been initiated but theres nothing in it, also return no warp message.
         */
        if (listCopy.size() == 0) {
            messages.noWarps(p);
            return null;
        } else if (listCopy.size() != 0) {

            //Starts teh attempt to grab the warps as 0. Adds 1 everytime the random number isnt equal to this.
            int i = 0;
            //Initiates the name of the warp
            String worldName;
            //Initiates the X coordinate to teleport to.
            double x;
            //Initiates the Y coordinate to teleport to.
            double y;
            //Initiates the Z coordinate to teleport to.
            double z;
            //Initiates the overall location of it.
            Location location;

            /**
             * Checks is warp cross world is disabled or if a world was specified.
             * If so, remove all the warps from other worlds.
             */
            if (RandomCoords.getPlugin().config.getString("WarpCrossWorld").equalsIgnoreCase("false") || coordType.equals(CoordType.WARPWORLD)) {

                //If not remove all from list that are not in the current players world
                for (final String obj : list) {
                    final World objWorld = Bukkit.getServer().getWorld(RandomCoords.getPlugin().warps.getString("Warps." + obj + ".World"));
                    if (objWorld != world) {
                        listCopy.remove(obj);
                    }
                }
                /**
                 * If the list is empty, just return the no warp message.
                 */
                if (listCopy.isEmpty()) {
                    messages.noWarpsWorld(p, world.getName());
                    return null;
                }

            }


            //Whats the size of the list, to know the max min of the random number.
            final int size = listCopy.size();

            //Grabs a random number between 0 and the size of the list
            final int myRandom = random.nextInt(size);

            /**
             * For every object in the list, Check if the number assigned to it is eqaul to the random number generated.
             * If not, Add 1 and repeat in the loop.
             */
            for (final Object obj : listCopy) {
                if (i == myRandom) {
                    //Get the location of the warp that Obj refers to.
                    worldName = RandomCoords.getPlugin().warps.getString("Warps." + obj.toString() + ".World");
                    x = RandomCoords.getPlugin().warps.getDouble("Warps." + obj.toString() + ".X");
                    y = RandomCoords.getPlugin().warps.getDouble("Warps." + obj.toString() + ".Y");
                    z = RandomCoords.getPlugin().warps.getDouble("Warps." + obj.toString() + ".Z");
                    location = new Location(Bukkit.getServer().getWorld(worldName), x, y, z);
                    return location;


                } else {
                    //If it wasnt, Add 1 to I and re-run.
                    i++;
                }


            }
        }

        return null;
    }


    /**
     * Gets a safe Y at the location.
     * @param location The location to get the Y for.
     * @return Safe y value, plus a buffer. (2.5 = max before fall damage)
     */
    private double getSafeY(Location location) {

        if(location.getWorld().getBiome(location.getBlockX(), location.getBlockZ()) == Biome.HELL) {
            return (nether.getSafeYNether(location));
        }
        if(RandomCoords.getPlugin().skyBlockSave.getStringList("SkyBlockWorlds").contains(location.getWorld().getName())) {
            return RandomCoords.getPlugin().skyBlockSave.getInt("DefaultY") + 3.5;

        }


        return location.getWorld().getHighestBlockYAt(location) + 2.5;
    }

    /**
     * Generates a random number from Random.org
     * @param min The minimum this number can be.
     * @param max The maximum this number can be.
     * @return Random number from Random.Org within range, Max Min.
     */
    private String getRandomFromOrg(final int min, final int max) {
        //Gets the site URL we're reading from.
        URL site;
        //Sets the string as null, until its changed when we read.
        String random = null;

        /**
         * Trys to grab the coorinates from the Random.Org site.
         * Catch if it cant, logs an error in the console.
         */
        try {
            //Website URL.
            final String web = "https://www.random.org/integers/?num=1&min=" + min + "&max=" + max + "&col=1&base=10&format=plain&rnd=new";
            //Sets it as the site to be read from.
            site = new URL(web);

            /**
             * Tries using buffered reader to read the line on the page.
             */
            try (BufferedReader in = new BufferedReader(new InputStreamReader(site.openStream()))) {
                //Set the line
                String line;
                /**
                 * If the line is not null, set Random as the line number.
                 */
                while ((line = in.readLine()) != null) {
                    //Set string random as line.
                    random = line;
                }
            }
        } catch (IOException ignored) {
            //Log if we couldnt read the line of the site.
            RandomCoords.logger.severe("Couldnt grab coordinates from Random.ORG!");
        }
        //Return the number as a string.
        return random;
    }

    /**
     * Checks to see if location is within a circular range of the center.
     * @param loc The location we're checking.
     * @param radius The radius of the circle to check.
     * @param center The center of the circle we're checking.
     * @return True or False, Is it within the circle.
     */
    private boolean isWithinCircle(final Location loc, final double radius, final Location center) {
        /**
         * Returns a boolean if the location is outside of the radius.
         * AND checks if the CircleRadius is default overall, or for the world.
         */
        return (RandomCoords.getPlugin().config.getString("CircleRadiusDefault").equalsIgnoreCase("true") || RandomCoords.getPlugin().config.getStringList("CircleRadiusWorlds").contains(loc.getWorld().getName())) && center.distanceSquared(loc) >= (radius * radius);
    }

    /**
     * Has the player moved?
     * @param player The player
     * @param start The start location
     * @return True if moved, False if not.
     */
    public boolean hasThePlayerMoved(Player player, Location start) {
        if(!config.getString("StopOnMove").equalsIgnoreCase("true")) {
            return false;
        }

        if(player.getLocation().getWorld() != start.getWorld()) {
            return false;
        }

        return player.getLocation().distance(start) >= 1;

    }

    /**
     * Is the player in combat?
     * @param player The player
     * @param startHealth Their starting health
     * @return True if in combat, false if not.
     */
    public boolean isPlayerInCombat(Player player, double startHealth) {
        if(!config.getString("StopOnCombat").equalsIgnoreCase("true")) {
            return false;
        }

        return player.getHealth() < startHealth;
    }

    /**
     * Performs the commands after teleport from config onJoinCommands.
     * @param event The random teleport event.
     */
    public void performOnJoinCommands(RandomTeleportEvent event) {
        if(event.coordType().equals(CoordType.JOIN) || event.coordType().equals(CoordType.JOINWORLD)) {
            if (RandomCoords.getPlugin().config.get("OnJoinCommand") instanceof String) {
                if (RandomCoords.getPlugin().config.getString("OnJoinCommand").equalsIgnoreCase("none")) {
                    return;
                }

                Bukkit.getServer().dispatchCommand(event.getPlayer(), RandomCoords.getPlugin().config.getString("OnJoinCommand"));

            } else if (RandomCoords.getPlugin().config.get("OnJoinCommand") instanceof List) {
                if (RandomCoords.getPlugin().config.getStringList("OnJoinCommand").contains("none")) {
                    return;
                }

                for (String n : RandomCoords.getPlugin().config.getStringList("OnJoinCommand")) {
                    Bukkit.getServer().dispatchCommand(event.getPlayer(), n);

                }
            }
        }
    }

    /**
     * Plays at sound at the location if turned on in config.
     * @param player The player to play the sound for
     * @param locationTP Where to play the sound
     */
    public void playSoundtLocation(Player player, Location locationTP) {
        if (!RandomCoords.getPlugin().config.getString("Sound").equalsIgnoreCase("false")) {
            //Get the name of the sound to be played.
            final String soundName = RandomCoords.getPlugin().config.getString("Sound");
            /**
             * Attempts to play the sound at the location they have teleported to.
             * Catches if the config is typed incorrectly and will log this.
             */
            try {
                //Play the sound.
                player.playSound(locationTP, Sound.valueOf(soundName), 1, 1);
            } catch (IllegalArgumentException e) {
                //Log if the sound is incorrect.
                Bukkit.getServer().getLogger().log(Level.WARNING, "Sound: " + soundName + " does not exist!");
            }


        }
    }

    /**
     * Plays an effect from config at the location.
     * @param locationTP The loction to play the effect.
     */
    public void playEffectAtLocation(Location locationTP) {
        if (!RandomCoords.getPlugin().config.getString("Effect").equalsIgnoreCase("false")) {
            //Get the effect name from the config.
            final String effectName = RandomCoords.getPlugin().config.getString("Effect");

            try {
                //Play the effect at the location
                locationTP.getWorld().playEffect(locationTP, Effect.valueOf(effectName), 1);
            } catch (IllegalArgumentException e) {
                //Log the fact that the config is incorrect.
                Bukkit.getServer().getLogger().log(Level.WARNING, "Effect: " + effectName + " does not exist!");
            }


        }
    }

    /**
     * Manages the bonus chest feature of the plugin. Whether or not we should spawn one for them.
     * @param player The player thats been teleported.
     * @param locationTP The location they were teleported to.
     */
    public void bonusChests(final Player player, final Location locationTP) {
        /**
         * Checks if the bonus chest feature is active. If not it does nothing.
         */
        if (RandomCoords.getPlugin().config.getString("BonusChest").equalsIgnoreCase("true")) {
            /**
             * If the player hasnt already recieved a chest, Spawn one in for them.
             * Else do nothing.
             */
            if (RandomCoords.getPlugin().limiter.getString(player.getUniqueId() + ".Chest") == null) {
               bonusChestManager.spawnBonusChest(player);
            }


        }
    }

    /**
     * Checks if they have the correct amount of money to pay for the teleport.
     * @param p The player that we are cheking.
     * @param cost the cost of teleport
     * @return True or False, do they have the money?
     */
    public boolean hasCorrectAmountOfMoney(final Player p, final double cost) {
        return !RandomCoords.getPlugin().setupEconomy() || cost == 0 || cost <= RandomCoords.getPlugin().econ.getBalance(p);
    }

    public void addOneToLimiter(Player player, CoordType coordType) {
        if(config.getInt("Limit") == 0) { return; }
        if(!doesLimiterApply(coordType)) {return; }

        if(RandomCoords.getPlugin().limiter.get(player.getUniqueId().toString() + ".Uses") == null) {
            RandomCoords.getPlugin().limiter.set(player.getUniqueId().toString() + ".Uses", 1);
            RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().limiter, RandomCoords.getPlugin().limiterFile);
            return;
        } else {
            int uses = RandomCoords.getPlugin().limiter.getInt(player.getUniqueId().toString() + ".Uses");
            uses++;
            RandomCoords.getPlugin().limiter.set(player.getUniqueId().toString() + ".Uses", uses);
            RandomCoords.getPlugin().saveFile(RandomCoords.getPlugin().limiter, RandomCoords.getPlugin().limiterFile);
            return;
        }

    }

    /**
     * Generates the chunk that the location is in. Seldom use.
     * @param location The location in which to load.
     * @return True or False, Is the chunk loaded yet?
     */
    private boolean generateChunk(final Location location) {
        //The world that we're loading the chunk in.
        final World world = location.getWorld();
        //The chunk itself that we are loading.
        final Chunk chunk = world.getChunkAt(location);
        /**
         * Checks if the chunk is loaded, if not an attempt is made to load.
         */
        if (chunk.isLoaded()) {
            return true;
        } else {
            chunk.load();
            return false;
        }
    }




    }
