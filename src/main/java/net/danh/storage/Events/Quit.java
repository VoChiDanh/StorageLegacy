package net.danh.storage.Events;

import net.danh.storage.Manager.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.danh.storage.Commands.TabCompleter.players;
import static net.danh.storage.Manager.Files.getconfigfile;

public class Quit implements Listener {

    @EventHandler
    public void onQuit(@NotNull PlayerQuitEvent e) {
        Player p = e.getPlayer();
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            Data.savePlayerData(p, item);
        }
        if (Objects.requireNonNull(players).contains(p.getName())) {
            players.remove(p.getName());
        }
    }
}
