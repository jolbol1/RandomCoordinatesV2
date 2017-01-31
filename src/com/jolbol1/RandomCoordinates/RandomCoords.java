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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private static Economy econ;
    private static Plugin plugin;
    public final Map<Player, Location> wandSelection = new ConcurrentHashMap<>();
    private final MessageManager messages = new MessageManager();
    public FileConfiguration language;
    public FileConfiguration config;
    public FileConfiguration limiter;
    public FileConfiguration portals;
    public FileConfiguration warps;
    public int successTeleports = 0;
    public int failedTeleports = 0;
    private File languageFile;
    private File configFile;
    private File limiterFile;
    private File portalsFile;
    private File warpFile;

    public static RandomCoords getPlugin() {
        return JavaPlugin.getPlugin(RandomCoords.class);
    }

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


        //Setup Language File
        languageFile = new File(this.getDataFolder(), "language.yml");
        if (!languageFile.exists()) {
            try {
                languageFile.createNewFile();
            } catch (IOException e) {
                logger.severe("Could Not Create language.yml!");
            }
        }
        language = YamlConfiguration.loadConfiguration(languageFile);
        matchLanguage();

        configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                logger.severe("Could Not Create config.yml");
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        matchConfig();


        limiterFile = new File(this.getDataFolder(), "limiter.yml");
        if (!limiterFile.exists()) {
            try {
                limiterFile.createNewFile();
            } catch (IOException e) {
                logger.severe("Could Not Create limiter.yml");
            }
        }
        limiter = YamlConfiguration.loadConfiguration(limiterFile);

        portalsFile = new File(this.getDataFolder(), "portals.yml");
        if (!portalsFile.exists()) {
            try {
                portalsFile.createNewFile();
            } catch (IOException e) {
                logger.severe("Could Not Create portals.yml");
            }
        }
        portals = YamlConfiguration.loadConfiguration(portalsFile);

        warpFile = new File(this.getDataFolder(), "warps.yml");
        if (!warpFile.exists()) {
            try {
                warpFile.createNewFile();
            } catch (IOException e) {
                logger.severe("Could Not Create warps.yml");
            }
        }
        warps = YamlConfiguration.loadConfiguration(warpFile);

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


    }

    private void matchLanguage() {

        final InputStream is = getResource("language-defaults.yml");
        if (is != null) {

            final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource("language-defaults.yml")));
            final File updateConfig = new File(getDataFolder(), "language.yml");

            for (final String key : defConfig.getConfigurationSection("").getKeys(false)) {

                final YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(updateConfig);
                if (!newConfig.contains(key)) {
                    language.set(key, defConfig.get(key));
                    try {
                        language.save(languageFile);
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "There was an error matching the language.yml file!");
                    }

                }
            }

        }
    }

    private void matchConfig() {

        final InputStream is = getResource("config-defaults.yml");
        if (is != null) {

            final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource("config-defaults.yml")));
            final File updateConfig = new File(getDataFolder(), "config.yml");

            for (final String key : defConfig.getConfigurationSection("").getKeys(false)) {

                final YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(updateConfig);
                if (!newConfig.contains(key)) {
                    config.set(key, defConfig.get(key));
                    Bukkit.getLogger().log(Level.WARNING, "Set: " + defConfig.get(key));
                    try {
                        config.save(configFile);
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, " There was a problem matching the config.yml!");
                    }

                }
            }

        }
    }

    public Plugin getInstance() {
        return plugin;
    }

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

    public void reloadConfigFile() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        final InputStream defLanguageStream = plugin.getResource("config.yml");
        if (defLanguageStream != null) {
            final YamlConfiguration defLanguage = YamlConfiguration.loadConfiguration(defLanguageStream);
            config.setDefaults(defLanguage);
        }

    }

    public void saveCustomConfig() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            config.save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save to config.yml ", ex);
        }
    }

    public void saveLimiter() {
        if (limiter == null || limiterFile == null) {
            return;
        }
        try {
            limiter.save(limiterFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save to limiter.yml ", ex);
        }
    }

    public void savePortals() {
        if (portals == null || portalsFile == null) {
            return;
        }
        try {
            portals.save(portalsFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save to portals.yml ", ex);
        }
    }

    public void saveWarps() {
        if (warps == null || warpFile == null) {
            return;
        }
        try {
            warps.save(warpFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save to warps.yml ", ex);
        }
    }


    public WorldGuardPlugin getWorldGuard() {
        final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (!(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }


    public ItemStack wand() {
        final ItemStack wand = new ItemStack(Material.GOLD_AXE);
        final ItemMeta itemMeta = wand.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "Random Wand");
        final List<String> lore = new ArrayList<>();
        lore.add("Wand for portal creation!");
        itemMeta.setLore(lore);
        wand.setItemMeta(itemMeta);
        return wand;
    }

    public String PortalMap(final String position, final Player p) {
        return position + p.getName();
    }

    public boolean hasPermission(final CommandSender sender, final String permission) {
        return sender.hasPermission(permission);
    }

    private boolean setupEconomy() {
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

    public boolean hasPayed(final Player p, final double cost) {
        if (!setupEconomy() || cost == 0) {
            return true;
        } else {
            final EconomyResponse r = econ.withdrawPlayer(p, cost);
            if (r.transactionSuccess()) {
                messages.charged(p, cost);
                return true;

            } else {
                messages.cost(p, cost);

                return false;
            }
        }
    }

    public boolean hasMoney(final Player p, final double cost) {
        return !setupEconomy() || cost == 0 || cost > econ.getBalance(p);
    }


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


}

