package net.danh.storage.Events;

import net.danh.storage.Manager.Data;
import net.danh.storage.Manager.Files;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import static net.danh.storage.Manager.Data.*;

public class Join implements Listener {

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!autoSmelt(p)) {
            if (p.hasPermission("storage.asmelt")) {
                setautoSmelt(p, true);
            } else {
                setautoPick(p, false);
            }
        }
        if (!autoPick(p)) {
            setautoPick(p, true);
        }
        if (Data.getMaxStorage(p) == 0) {
            Data.setMaxStorage(p, Files.getconfigfile().getInt("Default_Max_Storage"));
        }
    }
}
