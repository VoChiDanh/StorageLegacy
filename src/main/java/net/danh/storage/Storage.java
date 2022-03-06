package net.danh.storage;

import net.danh.storage.Commands.Commands;
import net.danh.storage.Events.BlockBreak;
import net.danh.storage.Hook.PlaceholderAPI;
import net.danh.storage.Manager.Files;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import preponderous.ponder.minecraft.bukkit.abs.PonderBukkitPlugin;
import preponderous.ponder.minecraft.bukkit.tools.EventHandlerRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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
    }

    @Override
    public void onDisable() {
        Files.saveconfig();
        Files.savelanguage();
        Files.savedata();
    }


    @Contract(" -> new")
    private @NotNull ArrayList<Listener> initializeListeners() {
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
