package net.danh.storage.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBreaking(@NotNull BlockBreakEvent e) {
    }
}