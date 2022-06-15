package net.danh.storage.Gui;

import net.danh.storage.Manager.Data;
import net.danh.storage.Storage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static net.danh.dcore.Random.Number.isInteger;

public class Chat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = ChatColor.stripColor(e.getMessage());
        if (isInteger(msg)) {
            if (!Data.item.isEmpty() && Data.click.containsValue(ClickType.LEFT) && Data.click.containsKey(p) && Data.action.contains(p)) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.performCommand("storage take " + Data.item.get(p) + " " + Integer.parseInt(msg));
                    }
                }.runTask(Storage.get());
                Data.item.remove(p);
                Data.click.remove(p);
                Data.action.remove(p);
                e.setCancelled(true);
            }
            if (!Data.item.isEmpty() && Data.click.containsValue(ClickType.RIGHT) && Data.click.containsKey(p) && Data.action.contains(p)) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.performCommand("storage add " + Data.item.get(p) + " " + Integer.parseInt(msg));
                    }
                }.runTask(Storage.get());
                Data.item.remove(p);
                Data.click.remove(p);
                Data.action.remove(p);
                e.setCancelled(true);
            }
            if (!Data.item.isEmpty() && Data.click.containsValue(ClickType.DROP) && Data.click.containsKey(p) && Data.action.contains(p)) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.performCommand("storage sell " + Data.item.get(p) + " " + Integer.parseInt(msg));
                    }
                }.runTask(Storage.get());
                Data.item.remove(p);
                Data.click.remove(p);
                Data.action.remove(p);
                e.setCancelled(true);
            }
        }
    }
}
