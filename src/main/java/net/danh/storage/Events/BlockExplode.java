package net.danh.storage.Events;

import net.danh.storage.Manager.Data;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

public class BlockExplode implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockExplode(@NotNull EntityExplodeEvent e) {
        if (e.getEntity() instanceof Player) {
            for (Block block : e.blockList()) {
                Data.addStorage((Player) e.getEntity(), block.toString(), 1);
                e.blockList().remove(block);
            }
        }
    }
}
