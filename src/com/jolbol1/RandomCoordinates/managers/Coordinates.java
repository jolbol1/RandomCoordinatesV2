package com.jolbol1.RandomCoordinates.managers;


import com.jolbol1.RandomCoordinates.RandomCoords;
import com.jolbol1.RandomCoordinates.checks.*;
import com.jolbol1.RandomCoordinates.cooldown.Cooldown;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.util.*;

/**
 * Created by James on 01/07/2016.
 */
public class Coordinates {

    private final SecureRandom random = new SecureRandom();
    private final MessageManager messages = new MessageManager();
    private final FactionChecker fc = new FactionChecker();
    private final GriefPreventionCheck gpc = new GriefPreventionCheck();
    private final PlayerRadCheck prc = new PlayerRadCheck();
    private final TownyChecker tc = new TownyChecker();
    private final WorldBorderChecker wbc = new WorldBorderChecker();
    private final WorldGuardCheck wgc = new WorldGuardCheck();
    private final Nether nether = new Nether();
    private final End end = new End();



    private Location getRandomCoordinates(final Player player, int max, final int min, final World world){
        int randomX;
        int randomZ;
        int spawnX;
        int spawnZ;

        //Grabs the center, Either of the world or the custom set on from /RC set
        if(RandomCoords.getPlugin().config.get(world.getName() + ".Center.X") != null ) {
            spawnX = RandomCoords.getPlugin().config.getInt(world.getName() + ".Center.X");
        } else {
            spawnX = world.getSpawnLocation().getBlockX();
        }
        if( RandomCoords.getPlugin().config.get(world.getName() + ".Center.Z") != null) {
            spawnZ = RandomCoords.getPlugin().config.getInt(world.getName() + ".Center.Z");
        } else {
            spawnZ = world.getSpawnLocation().getBlockZ();
        }

        //Is there a world border setting? If so, Max make center WB center (Vanilla World Border)
        if(RandomCoords.getPlugin().config.getString("VanillaBorder").equalsIgnoreCase("true")) {
            final WorldBorder border = world.getWorldBorder();
            final Location center = border.getCenter();
            spawnX = center.getBlockX();
            spawnZ = center.getBlockZ();
            final double size = border.getSize();
            final int radius = (int) size / 2;
            if(max > radius) {
                max = radius;
            }
        }

        //Should the coordinates be generated via the RandomOrg site? Its more "Random"
        if(RandomCoords.getPlugin().config.getString("RandomOrg").equalsIgnoreCase("true")) {
            randomX = Integer.valueOf(getRandomOrg(min, max)) * modulus();
            randomZ = Integer.valueOf(getRandomOrg(min, max)) * modulus();
        } else {
            //Grab Random Numbers From The Function Above. To make sure we get negative coords, multiply by modulus function, Which randomly selects 1 or -1
            randomX = getRandomNumberInRange(min, max, player) * modulus();
            randomZ = getRandomNumberInRange(min, max, player) * modulus();
        }

      //Just uses a Y = 90 as a deafult, Is always changed to highest block.
        final Location preRandom = new Location(world, spawnX + (randomX + 0.5), 90, spawnZ + (randomZ + 0.5));
        //Grabs the highest block at the Random Location, AKA spawn at highest point on map.
        int highestPoint = world.getHighestBlockYAt(preRandom);
       //Is the world an instance of the nether? If so use the Nether Y coordinate Generator (Nether class)
        if(world.getBiome(spawnX, spawnZ).equals(Biome.HELL)) {
            highestPoint = nether.netherY(preRandom);

        }
        return new Location(world, spawnX + (randomX + 0.5), highestPoint, spawnZ + (randomZ + 0.5));
    }

    private int getRandomNumberInRange(final int min, final int max, final CommandSender sender ) {
        //Function to makes sure the value is within the range that is in the config.
        if (min >= max) {
           messages.minTooLarge(sender);
        }

        return random.nextInt((max - min) + 1) + min;
    }

    //Function to select 1 or -1 to generate positive and negative coordinates.
    private int modulus(){
        int modulus = random.nextInt((1) + 1);


        if(modulus == 0) {
            modulus = -1;
        }
        return modulus;
    }


    //This is the main coordinate function, It pieces together all the functions in this class and will ensure the location is safe.
    public void finalCoordinates(final Player player, int max, int min, final  World world, final CoordType type, final double cost) {
        for (final String worlds : RandomCoords.getPlugin().config.getStringList("BannedWorlds")) {
            if (player.getWorld().getName().equals(worlds)) {
                messages.worldBanned(player);
                return;

            }
        }

        //This is the secret key, It has been used among the code to save on checking the config for max min, and instead doing it here.
        if(max == 574272099) {
          if(RandomCoords.getPlugin().config.get(world.getName() + ".Max") != null) {
              max = RandomCoords.getPlugin().config.getInt(world.getName() + ".Max");
          } else {
              max = RandomCoords.getPlugin().config.getInt("MaxCoordinate");          }
       }
        if(min == 574272099) {
            if(RandomCoords.getPlugin().config.get(world.getName() + ".Max") != null) {
                min = RandomCoords.getPlugin().config.getInt(world.getName() + ".Min");
            } else {
                min = RandomCoords.getPlugin().config.getInt("MinCoordinate");
            }
        }

        //Starts the loop to check for a safe coordinates
        boolean exitLoop = false;
        //Sets attempts, If it reaches number defined in config, It will return no safe location
        int attempts = 0;

        //Starts the loop.
        while(!exitLoop) {
            final int maxAttempts = RandomCoords.getPlugin().config.getInt("MaxAttempts");

            //Checks have the attempts to look fro a safe location reached the maximum tries?
            if(attempts >= maxAttempts) {
                messages.couldntFind(player);
                return;
            }

            //Sets the location to be checked, Could be the final location.
            final Location location = getRandomCoordinates(player, max, min, world);

           //Is the location safe? If not, Re run loop and add to attempts.
            if (!isLocSafe(location)) {
                attempts++;
                exitLoop = false;
            } else {
                //Else if it is safe, Set exitLoop to stop, and teleport them 2.5 blocks above the highest point.
                exitLoop = true;
                Location locationTP = location.add(0, 2.5, 0);

                //Again is it hell? If so we do not want them to be high up as the custom Y function fins gaps of 2 blocks!
                if(locationTP.getWorld().getBiome(locationTP.getBlockX(), locationTP.getBlockZ()).equals(Biome.HELL)) {
                    locationTP = location.subtract(0, 1.5, 0);
                }

                // If its the end, We want to use an entirely different and inefficient coordinate method. This is why the end is banned by default.
                if(locationTP.getWorld().getBiome(locationTP.getBlockX(), locationTP.getBlockZ()).equals(Biome.SKY)) {
                    locationTP = end.endCoord(locationTP);
                    final int highest = locationTP.getWorld().getHighestBlockYAt(locationTP.getBlockX(), locationTP.getBlockZ());
                    locationTP = new Location(locationTP.getWorld(), locationTP.getBlockX(), highest, locationTP.getBlockZ());//NOPMD
                }

                //Sets limiter to true if custom function does not replace that.
                boolean limiter = true;
                //Sets this cost to equal the configured cost. This was mainly implemented to allow per sign costs.
                double thisCost = cost;

                //Sets the cooldown and timebefore counters to 0, Will be later changed if configured.
                int timeBefore = 0;
                int cooldown = 0;

                //What is their method of random teleporting?, Implemented to allow for more customisation of other methods.
                switch (type) {
                    //The /RC command
                    case COMMAND:
                        if (RandomCoords.getPlugin().config.getString("Command").equalsIgnoreCase("warps")) {
                            locationTP = warpTP(player, world);
                        }
                        if (RandomCoords.getPlugin().config.getStringList("LimiterApplys").contains("Command")) {
                            limiter = isLimiter(player);
                        }
                        if (RandomCoords.getPlugin().config.getDouble("CommandCost") != 0) {
                            thisCost = RandomCoords.getPlugin().config.getDouble("CommandCost");
                        }
                        if (RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport") != 0) {
                            if (RandomCoords.getPlugin().config.getStringList("TimeBeforeApplys").contains("Command")) {
                                timeBefore = RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport");
                            }
                        }
                        if (RandomCoords.getPlugin().config.getInt("CooldownTime") != 0) {
                            if (RandomCoords.getPlugin().config.getStringList("CooldownApplys").contains("Command")) {
                                cooldown = RandomCoords.getPlugin().config.getInt("CooldownTime");
                            }
                        }

                        break;
                    //Teleported by /RC all
                    case ALL:
                        if (RandomCoords.getPlugin().config.getString("All").equalsIgnoreCase("warps")) {
                            locationTP = warpTP(player, world);
                        }
                        if (RandomCoords.getPlugin().config.getStringList("LimiterApplys").contains("All")) {
                            limiter = isLimiter(player);
                        }
                        if (RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport") != 0) {
                            if (RandomCoords.getPlugin().config.getStringList("TimeBeforeApplys").contains("All")) {
                                timeBefore = RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport");
                            }
                        }
                        if (RandomCoords.getPlugin().config.getInt("CooldownTime") != 0) {
                            if (RandomCoords.getPlugin().config.getStringList("CooldownApplys").contains("All")) {
                                cooldown = RandomCoords.getPlugin().config.getInt("CooldownTime");
                            }
                        }
                        break;
                    //Teleported by /RC player
                    case PLAYER:
                        if (RandomCoords.getPlugin().config.getString("Others").equalsIgnoreCase("warps")) {
                            locationTP = warpTP(player, world);
                        }
                        if (RandomCoords.getPlugin().config.getStringList("LimiterApplys").contains("Others")) {
                            limiter = isLimiter(player);
                        }
                        if (RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport") != 0) {
                            if (RandomCoords.getPlugin().config.getStringList("TimeBeforeApplys").contains("Others")) {
                                timeBefore = RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport");
                            }
                        }
                        if (RandomCoords.getPlugin().config.getInt("CooldownTime") != 0) {
                            if (RandomCoords.getPlugin().config.getStringList("CooldownApplys").contains("Others")) {
                                cooldown = RandomCoords.getPlugin().config.getInt("CooldownTime");
                            }
                        }
                        break;
                    //Teleported by a RC Sign
                    case SIGN:
                        if (RandomCoords.getPlugin().config.getString("Signs").equalsIgnoreCase("warps")) {
                            locationTP = warpTP(player, world);
                        }

                            if(RandomCoords.getPlugin().config.getStringList("LimiterApplys").contains("Signs")) {
                                limiter = isLimiter(player);
                            }
                            if(RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport") != 0) {

                                if(RandomCoords.getPlugin().config.getStringList("TimeBeforeApplys").contains("Signs")) {
                                    timeBefore = RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport");

                                }
                            }
                            if(RandomCoords.getPlugin().config.getInt("CooldownTime") != 0) {
                                if(RandomCoords.getPlugin().config.getStringList("CooldownApplys").contains("Signs")) {
                                    cooldown = RandomCoords.getPlugin().config.getInt("CooldownTime");
                                }
                            }

                        break;
                    //Teleported on join
                    case JOIN:
                        if(RandomCoords.getPlugin().config.getString("Join").equalsIgnoreCase("warps")) {
                            locationTP = warpTP(player, world);
                        }
                        break;
                    //Teleported by a RC portal
                    case PORTAL:
                        if(RandomCoords.getPlugin().config.getString("Portals").equalsIgnoreCase("warps")) {
                            locationTP = warpTP(player, world);
                        }
                        if(RandomCoords.getPlugin().config.getStringList("LimiterApplys").contains("Portal")) {
                            limiter = isLimiter(player);
                        }
                        if(RandomCoords.getPlugin().config.getDouble("PortalCost") != 0) {
                            thisCost = RandomCoords.getPlugin().config.getDouble("CommandCost");
                        }

                        if(RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport") != 0) {
                            if(RandomCoords.getPlugin().config.getStringList("TimeBeforeApplys").contains("Portals")) {
                                timeBefore = RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport");
                            }
                        }
                        if(RandomCoords.getPlugin().config.getInt("CooldownTime") != 0) {
                            if(RandomCoords.getPlugin().config.getStringList("CooldownApplys").contains("Portals")) {
                                cooldown = RandomCoords.getPlugin().config.getInt("CooldownTime");
                            }
                        }

                        break;
                    //Teleported by the /RC warp command
                    case WARPS:
                        locationTP = warpTP(player, world);
                        if(RandomCoords.getPlugin().config.getStringList("LimiterApplys").contains("Warps")) {
                            limiter = isLimiter(player);
                        }
                        if(RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport") != 0) {
                            if(RandomCoords.getPlugin().config.getStringList("TimeBeforeApplys").contains("Warps")) {
                                timeBefore = RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport");
                            }
                        }
                        if(RandomCoords.getPlugin().config.getInt("CooldownTime") != 0) {
                            if(RandomCoords.getPlugin().config.getStringList("CooldownApplys").contains("Warps")) {
                                cooldown = RandomCoords.getPlugin().config.getInt("CooldownTime");
                            }
                        }

                        break;

                }

                //If they've reached the limit, Disallow it.
                if(!limiter){ return; }

                //Mainly added to check if any wars exist.
                if(locationTP == null) {
                    return;
                }
                //If they have the permission to bypass the Cooldown and/or time before set it back to 0 to do so.
                if(player.hasPermission("Random.Cooldown.Bypass") || player.hasPermission("Random.*")) {
                    cooldown = 0;
                }
                if(player.hasPermission("Random.TBT.Bypass") || player.hasPermission("Random.*")) {
                    timeBefore = 0;
                }


                //If they are already in
                if (Cooldown.isInCooldown(player.getUniqueId(), "TimeBefore")) {
                    messages.aboutTo(player, Cooldown.getTimeLeft(player.getUniqueId(), "TimeBefore"));
                    return;
                }

                //Grab their pre countdown health and their location on command
                final double health = player.getHealth();
                final Location start = player.getLocation();

                //Start the cooldowns with the cooldown class, and time set above
                final Cooldown cTb = new Cooldown(player.getUniqueId(), "TimeBefore", timeBefore);


                //If the cooldown isnt 0, and they are not in the timebefore cooldown, Start it!
                if (cooldown != 0) {
                    if (!Cooldown.isInCooldown(player.getUniqueId(), "Command")) {
                        if (!Cooldown.isInCooldown(player.getUniqueId(), "TimeBefore")) {
                            if(timeBefore != 0) {
                                cTb.start();
                                messages.TeleportingIn(player, timeBefore);
                            }
                        }


                       scheduleStuff(player, locationTP, thisCost, health, start, timeBefore, cooldown);
                    } else {
                        //Returns that they are in the cooldown, So cannot use the command.
                        final int secondsLeft = Cooldown.getTimeLeft(player.getUniqueId(), "Command");
                        messages.cooldownMessage(player, secondsLeft);
                        return;
                    }


                } else {
                    //If theres is no cooldown but is a time before

                    //Ensure they are not in TimeBefore
                    if (!Cooldown.isInCooldown(player.getUniqueId(), "TimeBefore")) {
                        //If its not 0
                        if(timeBefore != 0) {
                            cTb.start();
                            messages.TeleportingIn(player, timeBefore);
                        }
                        scheduleStuff(player, locationTP, thisCost, health, start, timeBefore, cooldown);
                    } else {
                        //Otherwise send them a message alerting them they are about to TP
                        messages.aboutTo(player, Cooldown.getTimeLeft(player.getUniqueId(), "TimeBefore"));
                        return;
                    }


                }



            }
        }
    }


    //Checks if the lcoation is safe
    private boolean isLocSafe(final Location location){

        //If the biome is hell, and the coorindate = the key stop it.
        if(location.getWorld().getBiome(location.getBlockX(), location.getBlockZ()).equals(Biome.HELL)) {
            if(location.getY() == 574272099) {
                return false;
            }
        }

        // Get the block and its material thats highest
        final Block block = location.getBlock();
        final Material material = block.getType();

        //Get the material of the top block, e.g Grass layer
        final Material material1 = block.getRelative(BlockFace.DOWN).getType();

        //Get the block thats 1 below the surface
        final Location logLoc = new Location(location.getWorld(), location.getBlockX(), location.getY() - 1, location.getBlockZ());
        final Block log = logLoc.subtract(0, 1, 0).getBlock();
        final Material matt = log.getType();

        //Get the block that is 2 below the surface.
        final Block log2 = logLoc.subtract(0, 1, 0).getBlock();
        final Material matt2 = log2.getType();

        //Checks if any of these blocks are Lava, or Water aswell as logs due to a bug in the get highest block.
        if(material1 == Material.LAVA || material1 == Material.STATIONARY_LAVA || material1 == Material.FIRE || material1 == Material.CACTUS || material1 == Material.WATER || material1 == Material.STATIONARY_WATER ||matt == Material.LAVA || matt == Material.STATIONARY_LAVA || matt == Material.WATER || matt == Material.STATIONARY_WATER || material == Material.FIRE || material == Material.CACTUS || matt2 == Material.LOG ||matt == Material.LOG || matt2 == Material.LOG_2 ||matt == Material.LOG_2 ||material ==  Material.LAVA || material == Material.STATIONARY_LAVA || material == Material.WATER || material == Material.STATIONARY_WATER) {
            return false;

        } else {
            //If it is safe, then run it through the various plugin checks.
            return fc.FactionCheck(location) && gpc.griefPrevent(location) && prc.isPlayerNear(location) && tc.TownyCheck(location) && wbc.WorldBorderCheck(location) && wgc.WorldguardCheck(location);
            }
        }

    //Gets the random numbers from Random.ORG
    private String getRandomOrg(final int min, final int max) {
        URL site;
        String random = null;

        try {
            final String web = "https://www.random.org/integers/?num=1&min=" + min + "&max=" + max + "&col=1&base=10&format=plain&rnd=new";
            site = new URL(web);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(site.openStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    random = line;
                }
            }
        } catch (IOException ignored) {
                 RandomCoords.logger.severe("Couldnt grab coordinates from Random.ORG!");
        }

        return random;
    }

    //Not to sure why I created this method, But why not? - Im sure I had a plan for it xD
    private boolean generateChunk(final Player p, final Location location) {
        final World world = location.getWorld();
        final Chunk chunk = world.getChunkAt(location);
        if(chunk.isLoaded()) {
            return true;
        } else {
            chunk.load();
            return false;
        }
    }




    //Chooses the location if the case / method is warps.
    private Location warpTP(final Player p, final World world) {

        //Is the world banned?
        for (final String worlds : RandomCoords.getPlugin().config.getStringList("BannedWorlds")) {
            if (world.getName().equals(worlds)) {
                messages.worldBanned(p);
                return null;

            }
        }

        //Initiate the warp list.
        Set<String> list = null;

        //Check that they have actually set some warps.
        if(RandomCoords.getPlugin().warps.get("Warps") != null) {
            list = RandomCoords.getPlugin().warps.getConfigurationSection("Warps.").getKeys(false);
        }

        //Is the warp list null, If so alert them.
        if(list == null) {
            messages.noWarps(p);
            return null;
        }

        //Copy the list to be able to edit in for loop.
        final Set<String> listCopy = new HashSet<>(list);
        //Checks if the size is 0, If so there are no warps.
        if(listCopy.size() == 0 || listCopy == null) {
            messages.noWarps(p);
            return null;
        } else if(listCopy.size() != 0) {



            int i = 0;
            String wName;
            double x;
            double y;
            double z;
            Location location;

            //Are they allowed to be teleported to a warp in another world?
            if(RandomCoords.getPlugin().config.getString("WarpCrossWorld").equalsIgnoreCase("false")) {

                //If not remove all from list that are not in the current players world
                for(final Object obj : list) {
                    final World objWorld = Bukkit.getServer().getWorld(RandomCoords.getPlugin().warps.getString("Warps." + obj.toString() + ".World"));
                    if (objWorld != world) {
                        listCopy.remove(obj);
                    }
                }
            }


            //Whats the size of the list, to know the max min of the random number.
            final int size = listCopy.size();

            //Grabs a random number between 0 and the size of the list
            final int myRandom = random.nextInt(size);

            //Runs through the list, when it finds the warp that is equal to the random number, grab the coorindates.
            for (final Object obj : listCopy) {
                if (i == myRandom) {
                    wName = RandomCoords.getPlugin().warps.getString("Warps." + obj.toString() + ".World");
                    x = RandomCoords.getPlugin().warps.getDouble("Warps." + obj.toString() + ".X");
                    y = RandomCoords.getPlugin().warps.getDouble("Warps." + obj.toString() + ".Y");
                    z = RandomCoords.getPlugin().warps.getDouble("Warps." + obj.toString() + ".Z");
                    location = new Location(Bukkit.getServer().getWorld(wName), x, y, z);
                    return location;



                } else {
                    //If it wasnt, Add 1 to I and re-run.
                    i = i + 1;
                }


            }
        }

        return null;
    }


    //Check to see if the player has reached the limit.
    private boolean isLimiter(final Player player) {

        //Can they bypass it?
        if(player.hasPermission("Random.Limiter.Byapss")) {
            return true;
        } else if(RandomCoords.getPlugin().config.getInt("Limit") != 0) {

            //Save as UUID in order to allow for name changes.
            final String uuid = player.getUniqueId().toString();

            final FileConfiguration limiter = RandomCoords.getPlugin().limiter;
            //If they are not stored in the file, Add them and set as one.
            if(limiter.get(uuid) == null) {
                limiter.set(uuid + ".Uses", 1);
                RandomCoords.getPlugin().saveLimiter();
                return true;
            }

            //If they are in the file, Grab the number of uses, and add one to that.
            final int used = limiter.getInt(uuid + ".Uses");
            final int limit = RandomCoords.getPlugin().config.getInt("Limit");

            //Checks that they have reached the limit.
            if(used < limit){
                limiter.set(uuid + ".Uses", used + 1);
                RandomCoords.getPlugin().saveLimiter();
                return true;

            } else {
                messages.reachedLimit(player);
                return false;
            }
        } else {
            //If the limit is 0, AKA Off, the return true always.
            return true;
        }
    }

    //Used to shcedule the main basis, to avoid code duplication in the coordinates class.
    private void scheduleStuff(final Player player, final Location locationTP, final double thisCost, final double health, final Location start, final int timeBefore, final int cooldown) {
        final BukkitScheduler s = RandomCoords.getPlugin().getServer().getScheduler();
        final Cooldown cool = new Cooldown(player.getUniqueId(), "Command", cooldown + timeBefore);
        //Start the scheduler :) (Lambda)
        s.scheduleSyncDelayedTask(RandomCoords.getPlugin().getInstance(), () -> {

            //Checks if they move, and if feature is enabled.
            if (RandomCoords.getPlugin().config.getString("StopOnMove").equalsIgnoreCase("true")) {
                if (start.distance(player.getLocation()) > 1) {
                    messages.youMoved(player);
                    return;
                }
            }

            //Checks if they have taken damage, and if the feature is enabled.
            if (RandomCoords.getPlugin().config.getString("StopOnCombat").equalsIgnoreCase("true")) {
                if (health > player.getHealth()) {
                    messages.tookDamage(player);
                    return;
                }
            }

            boolean exit = false;

            //The implmented but shouldnt really be used chunk loading method.
            while(!exit) {
                if(RandomCoords.getPlugin().config.getString("ChunkLoader").equalsIgnoreCase("false")) {
                    exit = true;
                } else {
                    exit = generateChunk(player, locationTP);
                }
                if(exit) {
                    if(RandomCoords.getPlugin().hasPayed(player, thisCost)) {
                        player.teleport(locationTP);
                        cool.start();
                    } else {
                        return;
                    }

                }
            }

            //Bonus Chests?
            bonusChests(player, locationTP);

            //Check that they have paid
           // if(RandomCoords.getPlugin().hasPayed(player, thisCost)) {
          //      player.teleport(locationTP);
          //  } else {
            //    return;
            //}

            //Play the sound
            if(!RandomCoords.getPlugin().config.getString("Sound").equalsIgnoreCase("false")) {
                final String soundName = RandomCoords.getPlugin().config.getString("Sound");
                player.playSound(locationTP, Sound.valueOf(soundName), 1, 1);


            }

            //Play the effect
            if(!RandomCoords.getPlugin().config.getString("Effect").equalsIgnoreCase("false")) {
                final String effectName = RandomCoords.getPlugin().config.getString("Effect");
                locationTP.getWorld().playEffect(locationTP, Effect.valueOf(effectName), 1);


            }

            // Start the Suffocation cooldown check
            final Cooldown c = new Cooldown(player.getUniqueId(), "Invul", 30);
            c.start();

            //Start the InvulnerableTime cooldown
            final Cooldown cT = new Cooldown(player.getUniqueId(), "InvulTime", RandomCoords.getPlugin().config.getInt("InvulTime"));
            cT.start();

            messages.teleportMessage(player, locationTP);

        }, timeBefore * 20L);
    }

    //Handles the bonus chest feature, also aided by the KitManager class to avoid needing essentials as a dependency
    public void bonusChests(Player player, Location locationTP){
        if(RandomCoords.getPlugin().config.getString("BonusChest").equalsIgnoreCase("true")) {
            if(RandomCoords.getPlugin().limiter.getString(player.getUniqueId() + ".Chest") == null) {

                final Location chestLoc = new Location(locationTP.getWorld(), locationTP.getBlockX() + 1.0, locationTP.getBlockY() -2 , locationTP.getBlockZ() + 1.0);
                final Location airLoc = new Location(chestLoc.getWorld(), chestLoc.getBlockX(), chestLoc.getBlockY() + 1, chestLoc.getBlockZ());
                // World chestWorld = chestLoc.getWorld();
                final Block chestBlock = chestLoc.getBlock();
                final Block airBlock = airLoc.getBlock();
                airBlock.setType(Material.AIR);
                chestBlock.setType(Material.CHEST);
                final Chest chest = (Chest) chestBlock.getState();
                final Inventory chestInv = chest.getInventory();
                for (final String material : RandomCoords.getPlugin().config.getStringList("BonusChestItems")) {
                    if (material.contains("Essentials,")) {
                        if(Bukkit.getPluginManager().getPlugin("Essentials") != null) {
                            final KitManager kitManager = new KitManager();
                            final String[] kits = material.split(",");
                            for (final String kit : kits) {
                                if (!kit.equals("Essentials")) {
                                    kitManager.getKit(player, chest, kit);
                                    RandomCoords.getPlugin().limiter.set(player.getUniqueId() + ".Chest", "true");
                                    RandomCoords.getPlugin().saveLimiter();

                                }
                            }
                        }
                    } else {
                        if(Material.getMaterial(material) !=null) {
                            final ItemStack itemStack = new ItemStack(Material.getMaterial(material), 1);
                            chestInv.addItem(itemStack);
                            RandomCoords.getPlugin().limiter.set(player.getUniqueId() + ".Chest", "true");
                            RandomCoords.getPlugin().saveLimiter();
                        } else {
                            Bukkit.broadcastMessage( material +"  is not a material.");
                        }
                    }

                }
            }



        }
    }






}








