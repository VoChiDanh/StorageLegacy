package net.danh.storage.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.danh.storage.Manager.Data.loadPlayerData;
import static net.danh.storage.Manager.Files.getconfigfile;

public class Join implements Listener {
    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {
        Player p = e.getPlayer();
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            loadPlayerData(p, item);
        }
    }
}