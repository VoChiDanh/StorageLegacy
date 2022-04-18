package net.danh.storage;

import net.danh.storage.Commands.Commands;
import net.danh.storage.Events.BlockBreak;
import net.danh.storage.Events.IA_BlockBreak;
import net.danh.storage.Events.Join;
import net.danh.storage.Events.Quit;
import net.danh.storage.Hook.PlaceholderAPI;
import net.danh.storage.Manager.Data;
import net.danh.storage.Manager.Files;
import net.danh.storage.Manager.SpigotUpdater;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import preponderous.ponder.minecraft.bukkit.abs.PonderBukkitPlugin;
import preponderous.ponder.minecraft.bukkit.nms.NMSAssistant;
import preponderous.ponder.minecraft.bukkit.tools.EventHandlerRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.danh.storage.Manager.Files.*;

public final class Storage extends PonderBukkitPlugin implements Listener {


    public static Economy economy;
    private static Storage instance;

    public static Storage get() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        performNMSChecks();
    }

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 14622);
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            getLogger().log(Level.INFO, "Successfully hooked with Vault!");
        }
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().log(Level.INFO, "Successfully hooked with PlaceholderAPI!");
            new PlaceholderAPI().register();
        }
        if (getServer().getPluginManager().getPlugin("ItemAdder") != null) {
            getLogger().log(Level.INFO, "Successfully hooked with ItemAdder!");
            registerIAEventHandlers();
        }
        registerEventHandlers();
        Objects.requireNonNull(getCommand("Storage")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("APick")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("ASmelt")).setExecutor(new Commands());
        Files.createfiles();
        checkFilesVersion();
        plugin = this;
        logger = getLogger();
        autosave = (new BukkitRunnable() {
            public void run() {
                if (getconfigfile().getBoolean("Auto-Save.STATUS")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
                            Data.savePlayerData(p, item);
                        }
                    }
                    if (getconfigfile().getBoolean("Auto-Save.SEND_DEBUG_MESSAGE")) {
                        logger.info(consolecolor(getlanguagefile().getString("Auto_Save_Debug") + "%reset%"));
                    }
                }
            }
        }).runTaskTimer(plugin, 1200L * getconfigfile().getLong("Auto-Save.TIME_REPEAT"), 1200L * getconfigfile().getLong("Auto-Save.TIME_REPEAT"));
        (new BukkitRunnable() {
            public void run() {
                try {
                    SpigotUpdater updater = new SpigotUpdater(Storage.instance, 100516);
                    if (updater.checkForUpdates()) getLogger().info(Files.consolecolor("&cAn update was found!"  + "%reset%"));
                    getLogger().info(Files.consolecolor("&eNew version: " + updater.getLatestVersion() + "%reset%"));
                    getLogger().info(Files.consolecolor("&aYour version: " + Storage.get().getDescription().getVersion() + "%reset%"));
                    getLogger().info(Files.consolecolor("&9Download: " + "&9" + updater.getResourceURL() + "%reset%"));
                } catch (Exception e) {
                    getLogger().warning(consolecolor("&cCould not check for updates! Stacktrace:"));
                    e.printStackTrace();
                }
            }
        }).runTaskTimer(plugin, 3600 * 20L, 3600 * 20L);
    }

    @Override
    public void onDisable() {
        Files.saveconfig();
        Files.savelanguage();
        for (Player p : getServer().getOnlinePlayers()) {
            for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
                Data.savePlayerData(p, item);
            }
        }
    }

    private void checkFilesVersion() {
        if (!Objects.requireNonNull(getconfigfile().getString("VERSION")).equalsIgnoreCase("1.0-B4") || getconfigfile().getString("VERSION") == null) {
            getLogger().log(Level.SEVERE, "You need update config.yml!");
        }
        if (!Objects.requireNonNull(getlanguagefile().getString("VERSION")).equalsIgnoreCase("1.0-B4") || getconfigfile().getString("VERSION") == null) {
            getLogger().log(Level.SEVERE, "You need update language.yml!");
        }
    }

    private void performNMSChecks() {
        final NMSAssistant nmsAssistant = new NMSAssistant();
        if (nmsAssistant.isVersionGreaterThan(8)) {
            getLogger().log(Level.INFO, "Loading data matching server version " + nmsAssistant.getNMSVersion().toString());
        } else {
            getLogger().warning("The server version is not suitable to load the plugin");
            getLogger().warning("Support version 1.9.x - 1.18.x");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Contract(" -> new")
    private @NotNull ArrayList<Listener> initializeListeners() {
        return new ArrayList<Listener>(Arrays.asList(new BlockBreak(), new Quit(), new Join()));
    }

    /**
     * Registers the event handlers of the plugin using Ponder.
     */
    public static BukkitTask autosave;
    public static Logger logger;

    private void registerEventHandlers() {
        ArrayList<Listener> listeners = initializeListeners();
        EventHandlerRegistry eventHandlerRegistry = new EventHandlerRegistry();
        eventHandlerRegistry.registerEventHandlers(listeners, this);
    }

    @Contract(" -> new")
    private @NotNull ArrayList<Listener> initializeIAListeners() {
        return new ArrayList<Listener>(Collections.singletonList(new IA_BlockBreak()));
    }

    /**
     * Registers the event handlers of the plugin using Ponder.
     */
    private void registerIAEventHandlers() {
        ArrayList<Listener> listeners = initializeIAListeners();
        EventHandlerRegistry eventHandlerRegistry = new EventHandlerRegistry();
        eventHandlerRegistry.registerEventHandlers(listeners, this);
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) economy = economyProvider.getProvider();
        return (economy != null);
    }
    public static JavaPlugin plugin;
}
