package net.danh.storage.Events;

import net.danh.storage.Manager.Data;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

public class BlockExplode implements Listener {

    @EventHandler
    public void onBlockExplode(@NotNull EntityExplodeEvent e) {
        for (Block block : e.blockList()) {
            for (Player entity : e.getEntity().getWorld().getPlayers()) {
                if (entity.getLocation().distance(e.getLocation()) <= 7) {
                    Data.addStorage(entity, block.toString(), 1);
                }
            }
        }
        e.blockList().clear();
    }
}
