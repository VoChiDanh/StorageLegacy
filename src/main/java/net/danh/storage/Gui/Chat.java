package net.danh.storage.Gui;

import net.danh.storage.Manager.Data;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static net.danh.storage.Manager.Items.*;

public class Chat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = ChatColor.stripColor(e.getMessage());
        if (p == null) {
            return;
        }
        if (!Data.item.isEmpty() && Data.click.containsValue(ClickType.LEFT) && Data.click.containsKey(p) && Data.action.contains(p)) {
            if (!msg.equalsIgnoreCase("exit")) {
                RemoveItems(p, Data.item.get(p).toUpperCase(), Integer.parseInt(msg));
                Data.item.remove(p);
                Data.click.remove(p);
                Data.action.remove(p);
                e.setCancelled(true);
            }
            if (msg.equalsIgnoreCase("exit")) {
                Data.item.remove(p);
                Data.click.remove(p);
                Data.action.remove(p);
                e.setCancelled(true);
                p.openInventory(OpenGui.Open(p));
            }
        }
        if (!Data.item.isEmpty() && Data.click.containsValue(ClickType.RIGHT) && Data.click.containsKey(p) && Data.action.contains(p)) {
            if (!msg.equalsIgnoreCase("exit")) {
                AddItems(p, Data.item.get(p).toUpperCase(), Integer.parseInt(msg));
                Data.item.remove(p);
                Data.click.remove(p);
                Data.action.remove(p);
                e.setCancelled(true);
            }
            if (msg.equalsIgnoreCase("exit")) {
                Data.item.remove(p);
                Data.click.remove(p);
                Data.action.remove(p);
                e.setCancelled(true);
                p.openInventory(OpenGui.Open(p));
            }
        }
        if (!Data.item.isEmpty() && Data.click.containsValue(ClickType.DROP) && Data.click.containsKey(p) && Data.action.contains(p)) {
            if (!msg.equalsIgnoreCase("exit")) {
                SellItems(p, Data.item.get(p).toUpperCase(), Integer.parseInt(msg));
                Data.item.remove(p);
                Data.click.remove(p);
                Data.action.remove(p);
                e.setCancelled(true);
            }
            if (msg.equalsIgnoreCase("exit")) {
                Data.item.remove(p);
                Data.click.remove(p);
                Data.action.remove(p);
                e.setCancelled(true);
                p.openInventory(OpenGui.Open(p));
            }
        }
    }
}
