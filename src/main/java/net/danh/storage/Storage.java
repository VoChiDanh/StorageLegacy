package net.danh.storage;

import net.danh.dcore.DCore;
import net.danh.dcore.NMS.NMSAssistant;
import net.danh.dcore.Utils.File;
import net.danh.storage.Commands.AutoPickup;
import net.danh.storage.Commands.AutoSmelt;
import net.danh.storage.Commands.TabCompleter;
import net.danh.storage.Events.BlockBreak;
import net.danh.storage.Events.BlockExplode;
import net.danh.storage.Events.Join;
import net.danh.storage.Events.Quit;
import net.danh.storage.Gui.Chat;
import net.danh.storage.Gui.InventoryClick;
import net.danh.storage.Hook.PlaceholderAPI;
import net.danh.storage.Manager.Data;
import net.danh.storage.Manager.Files;
import net.danh.storage.Manager.PlayerData;
import net.danh.storage.Manager.SpigotUpdater;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.logging.Level;

import static net.danh.dcore.DCore.RegisterDCore;
import static net.danh.dcore.Utils.Chat.colorize;
import static net.danh.storage.Manager.Files.*;

public final class Storage extends JavaPlugin implements Listener {

    public static Economy economy;
    public static boolean ecostatus;
    public static boolean papistatus;
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
        if (getServer().getOnlinePlayers().size() > 0) {
            for (Player p : getServer().getOnlinePlayers()) {
                p.kickPlayer("Rejoin in few second");
            }
        }
        if (!setupEconomy()) {
            ecostatus = false;
            getLogger().severe(String.format("[%s] You need essentials and vault!", getDescription().getName()));
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
        getServer().getPluginManager().registerEvents(new BlockExplode(), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(), this);
        getServer().getPluginManager().registerEvents(new Chat(), this);
        getServer().getPluginManager().registerEvents(new Join(), this);
        getServer().getPluginManager().registerEvents(new Quit(), this);
        new net.danh.storage.Commands.Storage(this);
        Objects.requireNonNull(getCommand("Storage")).setTabCompleter(new TabCompleter());
        new AutoPickup(this);
        Objects.requireNonNull(getCommand("AutoPickup")).setTabCompleter(new TabCompleter());
        new AutoSmelt(this);
        Objects.requireNonNull(getCommand("AutoSmelt")).setTabCompleter(new TabCompleter());
        RegisterDCore(this);
        Files.createfiles();
        checkFilesVersion();
        NMSAssistant nms = new NMSAssistant();
        File.updateFile(Storage.get(), getlanguagefile(), "language.yml");
        if (nms.isVersionGreaterThanOrEqualTo(19)) {
            DCore.dCoreLog("&eYou are running the plugin in 1.19+, if you find any bugs/errors please report them to github or discord!");
            DCore.dCoreLog("&eDiscord: https://discord.gg/CWjaq5fZN9");
            DCore.dCoreLog("&eGithub: " + getDescription().getWebsite());
        }
        if (getRawDataFile().exists()) {
            if (Files.getdatafile().contains("players")) {
                for (String name : Files.getdatafile().getConfigurationSection("players").getKeys(false)) {
                    PlayerData playerData = new PlayerData(name);
                    playerData.load();
                    if (playerData.getConfig().getKeys(true).size() == 0) {
                        playerData.getConfig().set("players." + name + ".auto.Smelt", getdatafile().getBoolean("players." + name + ".auto.Smelt"));
                        playerData.getConfig().set("players." + name + ".auto.Pick", getdatafile().getBoolean("players." + name + ".auto.Pick"));
                        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks")).getKeys(false)) {
                            playerData.getConfig().set("players." + name + ".items." + item + ".max", getdatafile().getInt("players." + name + ".items." + item + ".max"));
                            playerData.getConfig().set("players." + name + ".items." + item + ".amount", getdatafile().getInt("players." + name + ".items." + item + ".amount"));
                        }
                        playerData.save();
                    }
                }
            }
        }
        (new BukkitRunnable() {
            public void run() {
                try {
                    SpigotUpdater updater = new SpigotUpdater(Storage.instance, 100516);
                    if (!updater.getLatestVersion().equals(getDescription().getVersion())) {
                        if (updater.checkForUpdates()) getLogger().info(colorize("&6An update was found!"));
                        getLogger().info(colorize("&aNew version: " + updater.getLatestVersion()));
                        getLogger().info(colorize("&aYour version: " + Storage.get().getDescription().getVersion()));
                        getLogger().info(colorize("&cDownload: " + updater.getResourceURL()));
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
        papistatus = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    @Override
    public void onDisable() {
        saveconfig();
        savelanguage();
        savegui();
        if (ecostatus) {
            Files.saveconfig();
            Files.savelanguage();
            Files.savegui();
            for (Player p : getServer().getOnlinePlayers()) {
                for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
                    Data.savePlayerData(p, item);
                }
            }
        }
    }

    private void checkFilesVersion() {
        if (getconfigfile().getString("VERSION") == null || !getconfigfile().getString("VERSION").equalsIgnoreCase("1.0-B7")) {
            getLogger().log(Level.SEVERE, "You need update config.yml!");
        }
        if (getlanguagefile().getString("VERSION") == null || !getlanguagefile().getString("VERSION").equalsIgnoreCase("1.0-B6")) {
            getLogger().log(Level.SEVERE, "You need update language.yml!");
        }
        if (getguifile().getString("VERSION") == null || !getguifile().getString("VERSION").equalsIgnoreCase("1.0-B1")) {
            getLogger().log(Level.SEVERE, "You need update gui.yml!");
        }
    }

    private void performNMSChecks() {
        NMSAssistant nmsAssistant = new NMSAssistant();
        if (nmsAssistant.isVersionGreaterThan(8)) {
            getLogger().log(Level.INFO, "Loading data matching server version " + nmsAssistant.getNMSVersion().toString());
        } else {
            getLogger().warning("The server version is not suitable to load the plugin");
            getLogger().warning("Support version 1.9.x - 1.18.x");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) economy = economyProvider.getProvider();
        return (economy != null);
    }
}
