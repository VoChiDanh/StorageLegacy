package net.danh.storage.Gui;

import net.danh.dcore.NMS.NMSAssistant;
import net.danh.dcore.Utils.Chat;
import net.danh.storage.Manager.Data;
import net.danh.storage.Manager.Files;
import net.danh.storage.Storage;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.danh.dcore.Utils.Player.sendPlayerMessage;
import static net.danh.storage.Manager.Files.*;

public class InventoryClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (p == null) {
                return;
            }
            if (e.getView().getTitle().equalsIgnoreCase(Chat.colorize(papi(Files.getguifile().getString("TITLE"), p)))) {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
                NMSAssistant nms = new NMSAssistant();
                if (e.isCancelled()) {
                    String name = e.getInventory().getItem(e.getSlot()).getType().toString();
                    for (String f_name : getguifile().getConfigurationSection("CONVERT").getKeys(false)) {
                        if (f_name.equalsIgnoreCase(name)) {
                            name = getguifile().getString("CONVERT." + name + ".MATERIAL");
                            break;
                        }
                    }
                    int c_s = e.getSlot();
                    List<Integer> d_s = getguifile().getIntegerList("DECORATES_SLOTS");
                    if (!d_s.contains(c_s)) {
                        if (e.getClick() == ClickType.LEFT) {
                            Data.click.put(p, ClickType.LEFT);
                            Data.action.add(p);
                            Data.item.put(p, name);
                            p.closeInventory();
                            sendPlayerMessage(p, getlanguagefile().getString("Input.Chat"));
                        }
                        if (e.getClick() == ClickType.RIGHT) {
                            Data.click.put(p, ClickType.RIGHT);
                            Data.action.add(p);
                            Data.item.put(p, name);
                            p.closeInventory();
                            sendPlayerMessage(p, getlanguagefile().getString("Input.Chat"));
                        }
                        if (e.getClick() == ClickType.DROP) {
                            Data.click.put(p, ClickType.DROP);
                            Data.action.add(p);
                            Data.item.put(p, name);
                            p.closeInventory();
                            sendPlayerMessage(p, getlanguagefile().getString("Input.Chat"));
                        }
                        if (e.getClick() == ClickType.SHIFT_LEFT) {
                            String finalName = name;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.performCommand("storage take " + finalName + " all");
                                }
                            }.runTask(Storage.get());
                        }
                        if (e.getClick() == ClickType.SHIFT_RIGHT) {
                            String finalName1 = name;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.performCommand("storage add " + finalName1 + " all");
                                }
                            }.runTask(Storage.get());
                        }
                        if (e.getClick() == ClickType.CONTROL_DROP) {
                            String finalName2 = name;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.performCommand("storage sell " + finalName2 + " all");
                                }
                            }.runTask(Storage.get());
                        }
                    }
                }
            }
        }
    }
}