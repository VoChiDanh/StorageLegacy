package net.danh.storage;

import net.danh.storage.Commands.Commands;
import net.danh.storage.Commands.TabCompleter;
import net.danh.storage.Events.BlockBreak;
import net.danh.storage.Events.BlockExplode;
import net.danh.storage.Events.Join;
import net.danh.storage.Events.Quit;
import net.danh.storage.Gui.GuiEventListener;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import preponderous.ponder.minecraft.bukkit.abs.PonderBukkitPlugin;
import preponderous.ponder.minecraft.bukkit.nms.NMSAssistant;
import preponderous.ponder.minecraft.bukkit.tools.EventHandlerRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

import static net.danh.storage.Manager.Files.*;

public final class Storage extends PonderBukkitPlugin implements Listener {


    public static Economy economy;
    public static boolean ecostatus;
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
            ecostatus = false;
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else {
            ecostatus = true;
        }
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            getLogger().log(Level.INFO, "Successfully hooked with Vault!");
        }
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().log(Level.INFO, "Successfully hooked with PlaceholderAPI!");
            new PlaceholderAPI().register();
        }
        registerEventHandlers();
        Objects.requireNonNull(getCommand("Storage")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("Storage")).setTabCompleter(new TabCompleter());
        Objects.requireNonNull(getCommand("APick")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("APick")).setTabCompleter(new TabCompleter());
        Objects.requireNonNull(getCommand("ASmelt")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("ASmelt")).setTabCompleter(new TabCompleter());
        Files.createfiles();
        checkFilesVersion();
        (new BukkitRunnable() {
            public void run() {
                try {
                    SpigotUpdater updater = new SpigotUpdater(Storage.instance, 100516);
                    if (!updater.getLatestVersion().equals(getDescription().getVersion())) {
                        if (updater.checkForUpdates()) getLogger().info(Files.colorize("&6An update was found!"));
                        getLogger().info(Files.colorize("&aNew version: " + updater.getLatestVersion()));
                        getLogger().info(Files.colorize("&aYour version: " + Storage.get().getDescription().getVersion()));
                        getLogger().info(Files.colorize("&cDownload: " + updater.getResourceURL()));
                    }

                } catch (Exception e) {
                    getLogger().warning("Could not check for updates! Stacktrace:");
                    e.printStackTrace();
                }
            }
        }).runTaskTimer(this, 3600 * 20L, 3600 * 20L);
        (new BukkitRunnable() {
            public void run() {
                for (Player p : getServer().getOnlinePlayers()) {
                    for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
                        Data.savePlayerData(p, item);
                    }
                }
            }
        }).runTaskTimer(this, 1800 * 20L, 1800 * 20L);
    }

    @Override
    public void onDisable() {
        Files.saveconfig();
        Files.savelanguage();
        Files.savegui();
        for (Player p : getServer().getOnlinePlayers()) {
            for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
                Data.savePlayerData(p, item);
            }
        }
    }

    private void checkFilesVersion() {
        if (!Objects.requireNonNull(getconfigfile().getString("VERSION")).equalsIgnoreCase("1.0-B7") || getconfigfile().getString("VERSION") == null) {
            getLogger().log(Level.SEVERE, "You need update config.yml!");
        }
        if (!Objects.requireNonNull(getlanguagefile().getString("VERSION")).equalsIgnoreCase("1.0-B6") || getlanguagefile().getString("VERSION") == null) {
            getLogger().log(Level.SEVERE, "You need update language.yml!");
        }
        if (!Objects.requireNonNull(getguifile().getString("VERSION")).equalsIgnoreCase("1.0-B1") || getguifile().getString("VERSION") == null) {
            getLogger().log(Level.SEVERE, "You need update gui.yml!");
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
        return new ArrayList<>(Arrays.asList(new BlockBreak(), new Quit(), new Join(), new BlockExplode(), new GuiEventListener()));
    }

    /**
     * Registers the event handlers of the plugin using Ponder.
     */
    private void registerEventHandlers() {
        ArrayList<Listener> listeners = initializeListeners();
        EventHandlerRegistry eventHandlerRegistry = new EventHandlerRegistry();
        eventHandlerRegistry.registerEventHandlers(listeners, this);
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) economy = economyProvider.getProvider();
        return (economy != null);
    }
}
