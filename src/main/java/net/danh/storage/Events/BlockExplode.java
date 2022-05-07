package net.danh.storage.Events;

import net.danh.storage.Manager.Data;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlockExplode implements Listener {

    @EventHandler
    public void onBlockExplode(@NotNull EntityExplodeEvent e) {
        List<Block> blocks = e.blockList();
        for (Block block : blocks) {
            for (Player entity : e.getEntity().getWorld().getPlayers()) {
                if (entity.getLocation().distance(e.getLocation()) <= 7) {
                    Data.addStorage(entity, block.toString(), 1);
                }
            }
        }
        e.blockList().clear();
    }
}
