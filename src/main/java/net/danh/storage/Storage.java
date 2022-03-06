package net.danh.storage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.danh.storage.Commands.Commands;
import net.danh.storage.Events.BlockBreak;
import net.danh.storage.Hook.PlaceholderAPI;
import net.danh.storage.Manager.Files;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;
import preponderous.ponder.minecraft.bukkit.abs.PonderBukkitPlugin;
import preponderous.ponder.minecraft.bukkit.tools.EventHandlerRegistry;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

public final class Storage extends PonderBukkitPlugin implements Listener {


    public static Economy economy;
    private static Storage instance;

    public static Storage get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            getLogger().info(Files.colorize("&aSuccessfully hooked with Vault!"));
        }
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info(Files.colorize("&aSuccessfully hooked with PlaceholderAPI!"));
            new PlaceholderAPI().register();
        } else {
            getLogger().info(Files.colorize("&cUnsuccessfully hooked with PlaceholderAPI"));
        }
        registerEventHandlers();
        Objects.requireNonNull(getCommand("Storage")).setExecutor(new Commands());
        Files.createfiles();
        (new BukkitRunnable() {
            public void run() {
                try {
                    String tagname;
                    String version;
                    String body;
                    String name;
                    String plugins = getDescription().getName();
                    URL api = new URL("https://api.github.com/repos/VoChiDanh/" + plugins + "/releases/latest");
                    URLConnection con = api.openConnection();
                    con.setConnectTimeout(15000);
                    con.setReadTimeout(15000);

                    JsonObject json = new JsonParser().parse(new InputStreamReader(con.getInputStream())).getAsJsonObject();
                    tagname = json.get("tag_name").getAsString();
                    version = getDescription().getVersion();
                    body = json.get("body").getAsString();
                    name = json.get("name").getAsString();

                    String parsename = name.replaceAll("v", "");
                    String parsebody = body.replaceAll("## Commits", "");
                    if (!(parsename.equalsIgnoreCase(version))) {
                        URL download = new URL("https://github.com/VoChiDanh/" + plugins + "/releases/download/" + tagname + "/" + plugins + ".jar");
                        getLogger().log(Level.INFO, Files.colorize("&eDownloading " + name + "! &6Your version is &6v" + version));
                        getLogger().log(Level.INFO, Files.colorize("&aUpdate log: " + parsebody));
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                try {
                                    InputStream in = download.openStream();
                                    File temp = new File("plugins/" + plugins + "/update/");
                                    if (!temp.exists()) {
                                        temp.mkdir();
                                    }
                                    Path path = new File("plugins/" + plugins + "/update/" + File.separator + "" + plugins + ".jar").toPath();
                                    java.nio.file.Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
                                    getLogger().log(Level.INFO, Files.colorize("&6Download finished, stop the server and check new jar in " + plugins + " folder to get the new update"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.runTaskLaterAsynchronously(instance, 1);
                    } else {
                        getLogger().log(Level.INFO, Files.colorize("&aYou are on latest build"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).runTaskTimer(this, 20L, 20L);
    }

    @Override
    public void onDisable() {
        Files.saveconfig();
        Files.savelanguage();
        Files.savedata();
    }


    private ArrayList<Listener> initializeListeners() {
        return new ArrayList<>(Arrays.asList(
                new BlockBreak()
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
