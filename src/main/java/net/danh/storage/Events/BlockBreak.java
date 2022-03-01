package net.danh.storage.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Files.getconfigfile;

public class BlockBreak implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreaking(@NotNull BlockBreakEvent e) {
        Player p = e.getPlayer();
        String blocks = e.getBlock().getType().toString();
        String items = null;
        for (String getBlockType : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
            if (blocks.equalsIgnoreCase(getBlockType)) {
                items = getconfigfile().getString("Blocks." + blocks + ".Name");
                break;
            }
        }
        if (items == null) {
            return;
        }
        e.setDropItems(false);
        e.getBlock().getDrops().clear();
        addStorage(p, items, 1);
        if (getMaxStorage(p, items) == 0) {
            setMaxStorage(p, items, getconfigfile().getInt("Default_Max_Storage"));
        }
    }
}