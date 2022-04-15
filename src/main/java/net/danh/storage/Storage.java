package net.danh.storage;

import net.danh.storage.Commands.Commands;
import net.danh.storage.Events.BlockBreak;
import net.danh.storage.Events.Join;
import net.danh.storage.Events.Quit;
import net.danh.storage.Hook.PlaceholderAPI;
import net.danh.storage.Manager.Data;
import net.danh.storage.Manager.Files;
import net.danh.storage.Manager.SpigotUpdater;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import preponderous.ponder.minecraft.bukkit.abs.PonderBukkitPlugin;
import preponderous.ponder.minecraft.bukkit.tools.EventHandlerRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

import static net.danh.storage.Manager.Files.getconfigfile;

public final class Storage extends PonderBukkitPlugin implements Listener {


    public static Economy economy;
    private static Storage instance;

    public static Storage get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
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
            getLogger().log(Level.INFO, "&aSuccessfully hooked with PlaceholderAPI!");
            new PlaceholderAPI().register();
        }
        registerEventHandlers();
        Objects.requireNonNull(getCommand("Storage")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("APick")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("ASmelt")).setExecutor(new Commands());
        Files.createfiles();
/*        (new BukkitRunnable() {
            public void run() {
                try {
                    SpigotUpdater updater = new SpigotUpdater(Storage.instance, 100516);
                    if (updater.checkForUpdates())
                        getLogger().info(Files.colorize("&#f5602fAn update was found!"));
                    getLogger().info(Files.colorize("&#77ff73New version: " + updater.getLatestVersion()));
                    getLogger().info(Files.colorize("&#77ff73Your version: " + Storage.get().getDescription().getVersion()));
                    getLogger().info(Files.colorize("&#fffd73Download: " + updater.getResourceURL()));
                } catch (Exception e) {
                    getLogger().warning("Could not check for updates! Stacktrace:");
                    e.printStackTrace();
                }
            }
        }).runTaskTimer(this, 3600 * 20L, 3600 * 20L);*/
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


    @Contract(" -> new")
    private @NotNull ArrayList<Listener> initializeListeners() {
        return new ArrayList<Listener>(Arrays.asList(
                new BlockBreak(),
                new Quit(),
                new Join()
        ));
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
        if (economyProvider != null)
            economy = economyProvider.getProvider();
        return (economy != null);
    }
}
