package net.danh.storage.Gui;

import net.danh.storage.Manager.Data;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

import static net.danh.dcore.Utils.Player.sendPlayerMessage;
import static net.danh.storage.Manager.Files.getlanguagefile;
import static net.danh.storage.Manager.Files.isInt;
import static net.danh.storage.Manager.Items.*;

public class Chat implements Listener {

    public static boolean isInteger(java.lang.String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = ChatColor.stripColor(e.getMessage());
        if (p == null) {
            return;
        }
        if (!isInteger(msg)) {
            return;
        }
        if (!Data.item.isEmpty() && Data.click.containsValue(ClickType.LEFT) && Data.click.containsKey(p) && Data.action.contains(p)) {
            if (!msg.equalsIgnoreCase("exit")) {
                try {
                    if (Integer.parseInt(msg) > 0) {
                        if (Integer.parseInt(msg) <= getAmountEmpty(Objects.requireNonNull(p), Data.item.get(p).toUpperCase())) {
                            RemoveItems(p, Data.item.get(p).toUpperCase(), Integer.parseInt(msg));
                            Data.item.remove(p);
                            Data.click.remove(p);
                            Data.action.remove(p);
                            e.setCancelled(true);
                        } else {
                            sendPlayerMessage(p, getlanguagefile().getString("User.Not_Enough_Inventory")
                                    .replaceAll("%space%", String.valueOf(getAmountEmpty(Objects.requireNonNull(p), Data.item.get(p).toUpperCase()))));
                        }
                    } else {
                        sendPlayerMessage(p, getlanguagefile().getString("Invaild_Number"));
                    }
                } catch (Exception error) {
                    sendPlayerMessage(p, getlanguagefile().getString("Number_To_Big"));
                }
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
                try {
                    if (Integer.parseInt(msg) > 0) {
                        AddItems((p), Data.item.get(p).toUpperCase(), Integer.parseInt(msg));
                        Data.item.remove(p);
                        Data.click.remove(p);
                        Data.action.remove(p);
                        e.setCancelled(true);
                    } else {
                        sendPlayerMessage(p, getlanguagefile().getString("Invaild_Number"));
                    }
                } catch (Exception error) {
                    sendPlayerMessage(p, getlanguagefile().getString("Number_To_Big"));
                }
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
                if (isInt(msg)) {
                    try {
                        if (Integer.parseInt(msg) > 0) {
                            SellItems(p, Data.item.get(p).toUpperCase(), Integer.parseInt(msg));
                            Data.item.remove(p);
                            Data.click.remove(p);
                            Data.action.remove(p);
                            e.setCancelled(true);
                        } else {
                            sendPlayerMessage(p, getlanguagefile().getString("Invaild_Number"));
                        }
                    } catch (Exception error) {
                        sendPlayerMessage(p, getlanguagefile().getString("Number_To_Big"));
                    }
                }
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
