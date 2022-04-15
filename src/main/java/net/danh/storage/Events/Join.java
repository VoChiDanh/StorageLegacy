package net.danh.storage.Events;

import net.danh.storage.Manager.Data;
import net.danh.storage.Manager.Files;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Files.getconfigfile;

public class Join implements Listener {

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {
        Player p = e.getPlayer();
        setautoSmelt(p, autoSmeltData(p));
        setautoPick(p, autoPickData(p));
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            if (Data.getMaxStorage(p, item) == 0) {
                Data.setMaxStorage(p, item, Files.getconfigfile().getInt("Default_Max_Storage"));
            } else {
                Data.setMaxStorage(p, item, getMaxStorageData(p, item));
            }
            if (Data.getStorage(p, item) == 0) {
                Data.setStorage(p, item, 0);
            } else {
                Data.setStorage(p, item, getStorageData(p, item));
            }
        }
    }
}
