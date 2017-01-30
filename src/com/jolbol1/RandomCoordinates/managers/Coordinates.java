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
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

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
    private final int maxAttempts = RandomCoords.getPlugin().config.getInt("MaxAttempts");


    private Location getRandomCoordinates(final Player player, int max, final int min, final World world) {
        int randomX;
        int randomZ;
        int spawnX = getCenterX(world);
        int spawnZ = getCenterZ(world);
        int minOne = 0;
        int minTwo = 0;
        int highestPoint;

        //This is to fix the + bug, When using a minimum. This bug went on really long unnoticed... Rookie Lol
        if (min != 0) {
            if (modulus() == 1) {
                minOne = 0;
                minTwo = min;
            } else {
                minOne = min;
                minTwo = 0;
            }
        }

        if (RandomCoords.getPlugin().config.getString("RandomOrg").equalsIgnoreCase("true")) {
            randomX = Integer.valueOf(getRandomOrg(minOne, max)) * modulus();
            randomZ = Integer.valueOf(getRandomOrg(minTwo, max)) * modulus();
        } else {
            randomX = getRandomNumberInRange(minOne, max, player) * modulus();
            randomZ = getRandomNumberInRange(minTwo, max, player) * modulus();
        }

        //Just uses a Y = 90 as a default, Is always changed to highest block... I hope :)
        final Location preRandom = new Location(world, spawnX + (randomX + 0.5), 90, spawnZ + (randomZ + 0.5));

        //Is the world an instance of the nether? If so use the Nether Y coordinate Generator (Nether class)
        if (world.getBiome(spawnX, spawnZ).equals(Biome.HELL)) {
            highestPoint = nether.netherY(preRandom);

        } else {
            highestPoint = world.getHighestBlockYAt(preRandom);
        }

        return new Location(world, spawnX + (randomX + 0.5), highestPoint, spawnZ + (randomZ + 0.5));

    }

    private int getRandomNumberInRange(final int min, final int max, final CommandSender sender) {
        //Function to makes sure the value is within the range that is in the config.
        if (min >= max) {
            messages.minTooLarge(sender);
        }

        return random.nextInt((max - min) + 1) + min;
    }

    //Function to select 1 or -1 to generate positive and negative coordinates, Kinda a reverse modulus but whatever.
    private int modulus() {
        int modulus = random.nextInt((1) + 1);

        //As it gets either 0 or 1, change 0 to -1.
        if (modulus == 0) {
            modulus = -1;
        }
        return modulus;
    }


    //This is the main coordinate function, It pieces together all the functions in this class and will ensure the location is safe.
    public void finalCoordinates(final Player player, int max, int min, final World world, final CoordType type, final double cost) {
        if (isWorldBanned(player, world)) {
            return;
        }
        boolean exitLoop = false;
        int attempts = 0;
        if (max == 574272099) {
            max = getMax(world);
        }
        if (min == 574272099) {
            min = getMin(world);
        }

        while (!exitLoop) {

            if (attempts >= maxAttempts) {
                messages.couldntFind(player);
                RandomCoords.getPlugin().failedTeleports++;
                return;
            }

            Location locationTP = getRandomCoordinates(player, max, min, world);
            Location center = new Location(world, getCenterX(world), locationTP.getY(), getCenterZ(world));


            if (!isLocSafe(locationTP) || circleRadius(locationTP, max, center)) {
                attempts++;
                exitLoop = false;

            } else {
                exitLoop = true;
                locationTP = addBuffer(locationTP);
                boolean limiter;
                double thisCost = cost;
                int timeBefore = 0;
                int cooldown = 0;
                String name = "Command";
                String costName = "default";
                switch (type) {
                    case COMMAND:
                        name = "Command";
                        costName = "CommandCost";
                        break;
                    case ALL:
                        name = "All";
                        break;
                    case PLAYER:
                        name = "Others";
                        break;
                    case SIGN:
                        name = "Signs";
                        break;
                    case JOIN:
                        name = "Join";
                        break;
                    case PORTAL:
                        name = "Portals";
                        costName = "PortalCost";
                        break;
                    case WARPS:
                        name = "Warps";
                        break;
                    case WARPWORLD:
                        name = "Warps";
                        break;

                }

                locationTP = shouldWarp(player, world, locationTP, name, type);
                limiter = limiterApplys(player, true, name);
                thisCost = costApplys(thisCost, costName);
                timeBefore = timeBeforeApplys(player, timeBefore, name);
                cooldown = cooldownApplys(player, cooldown, name);
                if (!limiter) {
                    return;
                }
                if (locationTP == null) {
                    return;
                }
                if (inTimeBefore(player, timeBefore)) {
                    return;
                }
                final double health = player.getHealth();
                final Location start = player.getLocation();
                if (!inCooldown(player, cooldown)) {
                    scheduleStuff(player, locationTP, thisCost, health, start, timeBefore, cooldown);
                }

            }
        }
    }


    //Checks if the lcoation is safe
    private boolean isLocSafe(final Location location) {

        //If the biome is hell, and the coorindate = the key stop it.
        if (location.getWorld().getBiome(location.getBlockX(), location.getBlockZ()).equals(Biome.HELL)) {
            if (location.getY() == 574272099) {
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
        return !(material1 == Material.LAVA || material1 == Material.STATIONARY_LAVA || material1 == Material.FIRE || material1 == Material.CACTUS || material1 == Material.WATER || material1 == Material.STATIONARY_WATER || matt == Material.LAVA || matt == Material.STATIONARY_LAVA || matt == Material.WATER || matt == Material.STATIONARY_WATER || material == Material.FIRE || material == Material.CACTUS || matt2 == Material.LOG || matt == Material.LOG || matt2 == Material.LOG_2 || matt == Material.LOG_2 || material == Material.LAVA || material == Material.STATIONARY_LAVA || material == Material.WATER || material == Material.STATIONARY_WATER) && fc.FactionCheck(location) && gpc.griefPrevent(location) && prc.isPlayerNear(location) && tc.TownyCheck(location) && wbc.WorldBorderCheck(location) && wgc.WorldguardCheck(location) && !isOutsideBorder(location);
//If it is safe, then run it through the various plugin checks.
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
    private boolean generateChunk(final Location location) {
        final World world = location.getWorld();
        final Chunk chunk = world.getChunkAt(location);
        if (chunk.isLoaded()) {
            return true;
        } else {
            chunk.load();
            return false;
        }
    }


    //Chooses the location if the case / method is warps.
    private Location warpTP(final Player p, final World world, CoordType coordType) {

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
        if (RandomCoords.getPlugin().warps.get("Warps") != null) {
            list = RandomCoords.getPlugin().warps.getConfigurationSection("Warps.").getKeys(false);
        }

        //Is the warp list null, If so alert them.
        if (list == null) {
            messages.noWarps(p);
            return null;
        }

        //Copy the list to be able to edit in for loop.
        final Set<String> listCopy = new HashSet<>(list);
        //Checks if the size is 0, If so there are no warps.
        if (listCopy.size() == 0) {
            messages.noWarps(p);
            return null;
        } else if (listCopy.size() != 0) {


            int i = 0;
            String wName;
            double x;
            double y;
            double z;
            Location location;

            //Are they allowed to be teleported to a warp in another world?
            if (RandomCoords.getPlugin().config.getString("WarpCrossWorld").equalsIgnoreCase("false") || coordType.equals(CoordType.WARPWORLD)) {

                //If not remove all from list that are not in the current players world
                for (final String obj : list) {
                    final World objWorld = Bukkit.getServer().getWorld(RandomCoords.getPlugin().warps.getString("Warps." + obj + ".World"));
                    if (objWorld != world) {
                        //noinspection SuspiciousMethodCalls
                        listCopy.remove(obj);
                    }
                }

                if (listCopy.isEmpty()) {
                    messages.noWarpsWorld(p, world.getName());
                    return null;
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
        if (player.hasPermission("Random.Limiter.Byapss")) {
            return true;
        } else if (RandomCoords.getPlugin().config.getInt("Limit") != 0) {

            //Save as UUID in order to allow for name changes.
            final String uuid = player.getUniqueId().toString();

            final FileConfiguration limiter = RandomCoords.getPlugin().limiter;
            //If they are not stored in the file, Add them and set as one.
            if (limiter.get(uuid) == null) {
                limiter.set(uuid + ".Uses", 1);
                RandomCoords.getPlugin().saveLimiter();
                return true;
            }

            //If they are in the file, Grab the number of uses, and add one to that.
            final int used = limiter.getInt(uuid + ".Uses");
            final int limit = RandomCoords.getPlugin().config.getInt("Limit");

            //Checks that they have reached the limit.
            if (used < limit) {
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
        final Cooldown cool = new Cooldown(player.getUniqueId(), "Command", cooldown);
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
            while (!exit) {
                exit = RandomCoords.getPlugin().config.getString("ChunkLoader").equalsIgnoreCase("false") || generateChunk(locationTP);
                if (exit) {
                    if (RandomCoords.getPlugin().hasPayed(player, thisCost)) {
                        player.teleport(locationTP);
                        RandomCoords.getPlugin().successTeleports++;


                        cool.start();
                    } else {
                        return;
                    }

                }
            }

            //Bonus Chests?
            bonusChests(player, locationTP);
//
            //Check that they have paid
            // if(RandomCoords.getPlugin().hasPayed(player, thisCost)) {
            //    player.teleport(locationTP);
            //} else {
            //   return;
            // }

            //Play the sound
            if (!RandomCoords.getPlugin().config.getString("Sound").equalsIgnoreCase("false")) {
                final String soundName = RandomCoords.getPlugin().config.getString("Sound");
                //Added in null check in case its typed wrong.
                try {
                    player.playSound(locationTP, Sound.valueOf(soundName), 1, 1);
                } catch (IllegalArgumentException e) {
                    Bukkit.getServer().getLogger().log(Level.WARNING, "Sound: " + soundName + " does not exist!");
                }


            }

            //Play the effect
            if (!RandomCoords.getPlugin().config.getString("Effect").equalsIgnoreCase("false")) {
                final String effectName = RandomCoords.getPlugin().config.getString("Effect");
                try {
                    locationTP.getWorld().playEffect(locationTP, Effect.valueOf(effectName), 1);
                } catch (IllegalArgumentException e) {
                    Bukkit.getServer().getLogger().log(Level.WARNING, "Effect: " + effectName + " does not exist!");
                }


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
    private void bonusChests(Player player, Location locationTP) {
        if (RandomCoords.getPlugin().config.getString("BonusChest").equalsIgnoreCase("true")) {
            if (RandomCoords.getPlugin().limiter.getString(player.getUniqueId() + ".Chest") == null) {

                final Location chestLoc = new Location(locationTP.getWorld(), locationTP.getBlockX() + 1.0, locationTP.getBlockY() - 2, locationTP.getBlockZ() + 1.0);
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
                        if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
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
                        if (Material.getMaterial(material) != null) {
                            final ItemStack itemStack = new ItemStack(Material.getMaterial(material), 1);
                            chestInv.addItem(itemStack);
                            RandomCoords.getPlugin().limiter.set(player.getUniqueId() + ".Chest", "true");
                            RandomCoords.getPlugin().saveLimiter();
                        } else {
                            Bukkit.broadcastMessage(material + "  is not a material.");
                        }
                    }

                }
            }


        }
    }


    private boolean circleRadius(Location loc, double radius, Location center) {
        return (RandomCoords.getPlugin().config.getString("CircleRadiusDefault").equalsIgnoreCase("true") || RandomCoords.getPlugin().config.getStringList("CircleRadiusWorlds").contains(loc.getWorld().getName())) && center.distanceSquared(loc) >= (radius * radius);
    }

    private boolean isOutsideBorder(Location loc) {
        if (RandomCoords.getPlugin().config.getString("VanillaBorder").equalsIgnoreCase("true")) {
            WorldBorder wb = loc.getWorld().getWorldBorder();
            Location center = wb.getCenter();
            double size = wb.getSize();
            double radius = size / 2;
            int centerX = center.getBlockX();
            int centerZ = center.getBlockZ();
            int playerX = loc.getBlockX();
            int playerZ = loc.getBlockZ();


            if (playerX > (centerX + radius) || playerX < (centerX - radius) || playerZ > (centerZ + radius) || playerZ < (centerZ - radius)) {
                return true;
            }
        }
        return false;


    }

    private int getMax(World world) {
        int spawnX;
        int spawnZ;
        int max;
        if (RandomCoords.getPlugin().config.get(world.getName() + ".Center.X") != null) {
            spawnX = RandomCoords.getPlugin().config.getInt(world.getName() + ".Center.X");
        } else {
            spawnX = world.getSpawnLocation().getBlockX();
        }
        if (RandomCoords.getPlugin().config.get(world.getName() + ".Center.Z") != null) {
            spawnZ = RandomCoords.getPlugin().config.getInt(world.getName() + ".Center.Z");
        } else {
            spawnZ = world.getSpawnLocation().getBlockZ();
        }


        if (RandomCoords.getPlugin().config.get(world.getName() + ".Max") != null) {
            max = RandomCoords.getPlugin().config.getInt(world.getName() + ".Max");
        } else {
            max = RandomCoords.getPlugin().config.getInt("MaxCoordinate");
        }


        //Is there a world border setting? If so, Max make center WB center (Vanilla World Border)
        if (RandomCoords.getPlugin().config.getString("VanillaBorder").equalsIgnoreCase("true")) {
            final WorldBorder border = world.getWorldBorder();
            final Location center = border.getCenter();
            int borderX = center.getBlockX();
            int borderZ = center.getBlockZ();
            final double size = border.getSize();
            final double radius = size / 2;
            if (spawnX == borderX && spawnZ == borderZ) {
                if (radius < max) {
                    max = (int) radius;
                }
            }

        }

        return max;


    }

    private int getMin(World world) {
        int min;
        if (RandomCoords.getPlugin().config.get(world.getName() + ".Max") != null) {
            min = RandomCoords.getPlugin().config.getInt(world.getName() + ".Min");
        } else {
            min = RandomCoords.getPlugin().config.getInt("MinCoordinate");
        }

        return min;
    }

    private int getCenterX(World world) {
        int spawnX;
        if (RandomCoords.getPlugin().config.get(world.getName() + ".Center.X") != null) {
            spawnX = RandomCoords.getPlugin().config.getInt(world.getName() + ".Center.X");
        } else {
            spawnX = world.getSpawnLocation().getBlockX();
        }

        return spawnX;
    }

    private int getCenterZ(World world) {
        int spawnZ;
        if (RandomCoords.getPlugin().config.get(world.getName() + ".Center.Z") != null) {
            spawnZ = RandomCoords.getPlugin().config.getInt(world.getName() + ".Center.Z");
        } else {
            spawnZ = world.getSpawnLocation().getBlockZ();
        }

        return spawnZ;
    }

    private Location addBuffer(Location locationTP) {
        if (locationTP.getWorld().getBiome(locationTP.getBlockX(), locationTP.getBlockZ()).equals(Biome.HELL)) {

            locationTP = locationTP.add(0, 1, 0);

        } else if (locationTP.getWorld().getBiome(locationTP.getBlockX(), locationTP.getBlockZ()).equals(Biome.SKY)) {

            locationTP = end.endCoord(locationTP);
            int highest = locationTP.getWorld().getHighestBlockYAt(locationTP.getBlockX(), locationTP.getBlockZ());
            locationTP = new Location(locationTP.getWorld(), locationTP.getBlockX(), highest, locationTP.getBlockZ());


        } else {
            locationTP = locationTP.add(0, 2.5, 0);
        }

        return locationTP;

    }

    private Location shouldWarp(Player player, World world, Location locationTP, String name, CoordType coordType) {

        if (name.equalsIgnoreCase("Warps")) {
            locationTP = warpTP(player, world, coordType);
            return locationTP;
        }

        if (RandomCoords.getPlugin().config.getString(name).equalsIgnoreCase("warps")) {
            locationTP = warpTP(player, world, coordType);
            return locationTP;
        } else {
            return locationTP;
        }


    }

    private boolean limiterApplys(Player player, boolean limiter, String name) {

        if (name.equals("Join")) {
            return limiter;
        }

        if (RandomCoords.getPlugin().config.getStringList("LimiterApplys").contains(name)) {
            limiter = isLimiter(player);
        }

        return limiter;

    }

    private double costApplys(double thisCost, String name) {
        if (name.equals("default")) {
            return thisCost;
        }

        if (RandomCoords.getPlugin().config.getDouble(name) != 0) {
            thisCost = RandomCoords.getPlugin().config.getDouble(name);
        }

        return thisCost;

    }

    private int timeBeforeApplys(Player player, int timeBefore, String name) {


        if (name.equals("Join")) {
            return timeBefore;
        }

        if (RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport") != 0) {
            if (RandomCoords.getPlugin().config.getStringList("TimeBeforeApplys").contains(name)) {
                if (player.hasPermission("Random.TBT.Bypass") || player.hasPermission("Random.*")) {
                    timeBefore = 0;
                } else {

                    timeBefore = RandomCoords.getPlugin().config.getInt("TimeBeforeTeleport");
                }
            }
        }

        return timeBefore;

    }

    private int cooldownApplys(Player player, int cooldown, String name) {
        if (name.equals("Join")) {
            return cooldown;
        }


        if (RandomCoords.getPlugin().config.getInt("CooldownTime") != 0) {
            if (RandomCoords.getPlugin().config.getStringList("CooldownApplys").contains(name)) {
                if (player.hasPermission("Random.Cooldown.Bypass") || player.hasPermission("Random.*")) {
                    cooldown = 0;
                } else {
                    cooldown = RandomCoords.getPlugin().config.getInt("CooldownTime");
                }
            }
        }

        return cooldown;

    }

    private boolean isWorldBanned(Player player, World world) {
        if (RandomCoords.getPlugin().config.getStringList("BannedWorlds").contains(world.getName())) {
            messages.worldBanned(player);
            return true;
        }
        return false;
    }

    private boolean inTimeBefore(Player player, int timeBefore) {
        if (Cooldown.isInCooldown(player.getUniqueId(), "TimeBefore")) {
            messages.aboutTo(player, Cooldown.getTimeLeft(player.getUniqueId(), "TimeBefore"));
            return true;
        } else {

            final Cooldown cTb = new Cooldown(player.getUniqueId(), "TimeBefore", timeBefore);

            if (timeBefore != 0) {
                cTb.start();
                messages.TeleportingIn(player, timeBefore);
            }
        }

        return false;

    }


    private boolean inCooldown(Player player, int cooldown) {

        if (cooldown == 0) {
            return false;
        }

        if (!Cooldown.isInCooldown(player.getUniqueId(), "Command")) {

            return false;

        } else {
            //Returns that they are in the cooldown, So cannot use the command.
            final int secondsLeft = Cooldown.getTimeLeft(player.getUniqueId(), "Command");
            messages.cooldownMessage(player, secondsLeft);
            return true;
        }
    }


}
