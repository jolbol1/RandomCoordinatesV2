package com.jolbol1.RandomCoordinates;

import com.jolbol1.RandomCoordinates.commands.*;
import com.jolbol1.RandomCoordinates.commands.handler.CommandHandler;
import com.jolbol1.RandomCoordinates.listeners.*;
import com.jolbol1.RandomCoordinates.managers.ConstructTabCompleter;
import com.jolbol1.RandomCoordinates.managers.MessageManager;
import com.jolbol1.RandomCoordinates.managers.Metrics;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by James on 01/07/2016.
 */
public class RandomCoords extends JavaPlugin {


    public static Logger logger;
    public static Economy econ;
    private static Plugin plugin;

    /**
     * Used to put in the current /RC Wand selection, and who selected it.
     */
    public final Map<Player, Location> wandSelection = new ConcurrentHashMap<>();
    /**
     * Creates an instance of the messages folder, Which handles most messages within.
     */
    private final MessageManager messages = new MessageManager();

//Creates an instance of the config files.
    public FileConfiguration language;
    public FileConfiguration config;
    public FileConfiguration limiter;
    public FileConfiguration portals;
    public FileConfiguration warps;
    public FileConfiguration blacklist;
    private File languageFile;
    public File configFile;
    public File limiterFile;
    public File portalsFile;
    public File warpFile;
    public File blackFile;

    //Sets the number to 0, The counter for successful teleports and failed.
    public int successTeleports = 0;
    public int failedTeleports = 0;

    /**
     * Used to grab the plugin instance from this clas
     * @return The plugin instance
     */
    public static RandomCoords getPlugin() {
        return JavaPlugin.getPlugin(RandomCoords.class);
    }

    /**
     * What to do on start up.
     */
    public void onEnable() {
        //Setup bStats Metrics


        logger = Bukkit.getServer().getLogger();
        plugin = this;
        final PluginManager pm = getServer().getPluginManager();
        final CommandHandler handler = new CommandHandler();
        final PluginDescriptionFile pdf = getDescription();
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_BOLD = "\u001B[1m";
        final String ANSI_BLUE = "\u001B[34m";
        logger.log(Level.INFO, ANSI_BLUE + ANSI_BOLD + "[RandomCoords]" + ANSI_BLUE + ANSI_BOLD + pdf.getName() + ANSI_BLUE + ANSI_BOLD + " Version: " + ANSI_BLUE + ANSI_BOLD + pdf.getVersion() + ANSI_BLUE + ANSI_BOLD + " enabled." + ANSI_RESET);



        this.getDataFolder().mkdirs();
        //Setup Language File
        languageFile = new File(this.getDataFolder(), "language.yml");
        language = setupFile(languageFile);
        matchFile(language, languageFile, "language");

        configFile = new File(this.getDataFolder(), "config.yml");
        config = setupFile(configFile);
        matchFile(config, configFile, "config");


        limiterFile = new File(this.getDataFolder(), "limiter.yml");
        limiter = setupFile(limiterFile);

        portalsFile = new File(this.getDataFolder(), "portals.yml");
        portals = setupFile(portalsFile);

        warpFile = new File(this.getDataFolder(), "warps.yml");
        warps = setupFile(warpFile);

        blackFile = new File(this.getDataFolder(), "blacklist.yml");
        blacklist = setupFile(blackFile);
        if(blacklist.getStringList("Blacklisted") != null) { matchFile(blacklist, blackFile, "blacklist"); }

        pm.registerEvents(new Suffocation(), this);
        pm.registerEvents(new Invulnerable(), this);
        pm.registerEvents(new onJoin(), this);
        pm.registerEvents(new Signs(), this);
        pm.registerEvents(new SignClick(), this);
        pm.registerEvents(new BlockPortal(), this);
        pm.registerEvents(new InNetherPortal(), this);
        pm.registerEvents(new Wand(), this);
        handler.register("rc", new RandomCommand());
        handler.register("wand", new WandGive());
        handler.register("reload", new Reload());
        handler.register("all", new All());
        handler.register("set", new Center());
        handler.register("portal", new PortalCommand());
        handler.register("player", new Others());
        handler.register("warp", new Warp());
        handler.register("help", new HelpCommand());
        getCommand("rc").setExecutor(handler);
        getCommand("rc").setTabCompleter(new ConstructTabCompleter());
        if (RandomCoords.getPlugin().config.getString("Metrics").equalsIgnoreCase("true")) {
            Metrics metrics = new Metrics(this);
            setupCharts(metrics);
        }


        final PortalEnter pe = new PortalEnter();
        pe.runTaskTimer(this, 0L, 20L);

        if(config.getString("UpdateMessage").equalsIgnoreCase("true")) {
            updater();
        }


    }





    /**
     * Used to update the language file, used for new updates.
     */


    /**
     * Used to update the config file.
     */
    private void matchFile(FileConfiguration fileConfig, File file, String name) {

        final InputStream is = getResource(name + "-defaults.yml");
        if (is != null) {

            final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource(name + "-defaults.yml")));
            final File updateConfig = new File(getDataFolder(), name + ".yml");

            for (final String key : defConfig.getConfigurationSection("").getKeys(false)) {

                final YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(updateConfig);
                if (!newConfig.contains(key)) {
                    fileConfig.set(key, defConfig.get(key));
                    Bukkit.getLogger().log(Level.WARNING, "Set: " + defConfig.get(key));
                    try {
                        fileConfig.save(file);
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, " There was a problem matching the" + name + ".yml!");
                    }

                }
            }

        }
    }

    /**
     * Another method to grab an instance of plugin.
     * @return the plugin instance.
     */
    public Plugin getInstance() {
        return plugin;
    }

    /**
     * Used to save changes to the language file, and update.
     */
    public void reloadLanguageFile() {
        if (languageFile == null) {
            languageFile = new File(plugin.getDataFolder(), "language.yml");
        }
        language = YamlConfiguration.loadConfiguration(languageFile);
        final InputStream defLanguageStream = plugin.getResource("language.yml");
        if (defLanguageStream != null) {
            final YamlConfiguration defLanguage = YamlConfiguration.loadConfiguration(defLanguageStream);
            language.setDefaults(defLanguage);
        }

    }

    public void reloadFile(FileConfiguration fileConfig, String fileName) {
        File file = new File(this.getDataFolder(), fileName);
            try {
                fileConfig.load(file);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }


    /**
     * Saves the config file, to add changes.
     */
    public void saveFile(FileConfiguration fileConfig, File file) {
        if (fileConfig == null || file == null) {
            return;
        }
        try {
            fileConfig.save(file);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save to " + fileConfig.getName(), ex);
        }
    }


    /**
     * Grabs an instance of the WorldGuard plugin
     * @return WorldGuard plugin
     */
    public WorldGuardPlugin getWorldGuard() {
        final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (!(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }





    /**
     * Checks if the player has the specified permission.
     * @param sender Who are we checking
     * @param permission What permission are we checking
     * @return True or False, Do they have the permission?
     */
    public boolean hasPermission(final CommandSender sender, final String permission) {
        return sender.hasPermission(permission);
    }

    /**
     * Creates the economy envoronment for this plugin/
     * @return the instance of the economy, Using vault.
     */
    public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        final RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }



    /**
     * Generates the charts to send to metric.
     * @param metrics returns charts to send.
     */
    public void setupCharts(Metrics metrics) {
        metrics.addCustomChart(new Metrics.SingleLineChart("success") {
            @Override
            public int getValue() {
                // (This is useless as there is already a player chart by default.)
                return successTeleports;
            }
        });
        metrics.addCustomChart(new Metrics.SingleLineChart("failed") {
            @Override
            public int getValue() {
                // (This is useless as there is already a player chart by default.)
                return failedTeleports;
            }
        });
    }

    private FileConfiguration setupFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.severe(file.getName() + " may not have been created.");
            }
        }

        return YamlConfiguration.loadConfiguration(file);

    }

    private void updater() {
        //Gets the site URL we're reading from.
        URL site;
        //Sets the string as null, until its changed when we read.
        String versionOnFile = null;

        /**
         * Trys to grab the coorinates from the Random.Org site.
         * Catch if it cant, logs an error in the console.
         */
        try {
            //Website URL.
            final String web = "https://raw.githubusercontent.com/jolbol1/RandomCoordinatesV2/master/src/update.yml";
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
                    versionOnFile = line;
                }
            }
        } catch (IOException ignored) {
            //Log if we couldnt read the line of the site.
            RandomCoords.logger.severe("Couldnt grab the update.yml from the web.");
        }
        //Return the number as a string.
        if(!versionOnFile.equalsIgnoreCase(plugin.getDescription().getVersion())) {
            Bukkit.getLogger().log(Level.CONFIG, ChatColor.GOLD + "[RandomCoords] " + ChatColor.RED + "A new version: " + ChatColor.GREEN + versionOnFile + ChatColor.RED + " is now available on Bukkit.");
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(p.isOp()) {
                    p.sendMessage(ChatColor.GOLD + "[RandomCoords] " + ChatColor.RED + "A new version: " + ChatColor.GREEN + versionOnFile + ChatColor.RED + " is now available on Bukkit.");
                }
            }
        }




    }




}

