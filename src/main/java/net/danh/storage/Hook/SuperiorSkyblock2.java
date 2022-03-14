package net.danh.storage.Hook;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import com.bgsoftware.superiorskyblock.api.config.SettingsManager;
import com.bgsoftware.superiorskyblock.api.events.BlockUnstackEvent;
import com.bgsoftware.superiorskyblock.api.handlers.*;
import com.bgsoftware.superiorskyblock.api.scripts.IScriptEngine;
import com.bgsoftware.superiorskyblock.api.world.event.WorldEventsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SuperiorSkyblock2 implements Listener {

    @EventHandler
    public void onStackBlock(@NotNull BlockUnstackEvent e) {
        SuperiorSkyblock ss = new SuperiorSkyblock() {
            @Contract(pure = true)
            @Override
            public @Nullable GridManager getGrid() {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable StackedBlocksManager getStackedBlocks() {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable BlockValuesManager getBlockValues() {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable SchematicManager getSchematics() {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable PlayersManager getPlayers() {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable RolesManager getRoles() {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable MissionsManager getMissions() {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable MenusManager getMenus() {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable KeysManager getKeys() {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable ProvidersManager getProviders() {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable UpgradesManager getUpgrades() {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable CommandsManager getCommands() {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable SettingsManager getSettings() {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable FactoriesManager getFactory() {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable ModulesManager getModules() {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable IScriptEngine getScriptEngine() {
                return null;
            }

            @Override
            public void setScriptEngine(IScriptEngine iScriptEngine) {

            }

            @Contract(pure = true)
            @Override
            public @Nullable WorldEventsManager getWorldEventsManager() {
                return null;
            }

            @Override
            public void setWorldEventsManager(WorldEventsManager worldEventsManager) {

            }
        };
        ss.getStackedBlocks().setStackedBlock(e.getBlock(), -1);
        e.setCancelled(true);
    }
}
