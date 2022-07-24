package net.danh.storage.Gui;

import net.danh.dcore.NMS.NMSAssistant;
import net.danh.dcore.Utils.Chat;
import net.danh.storage.Manager.Data;
import net.danh.storage.Manager.Files;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Objects;

import static net.danh.dcore.Utils.Chat.colorize;
import static net.danh.dcore.Utils.Player.sendPlayerMessage;
import static net.danh.storage.Manager.Data.getMaxStorage;
import static net.danh.storage.Manager.Data.getStorage;
import static net.danh.storage.Manager.Files.*;
import static net.danh.storage.Manager.Items.*;

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
                    if (e.getInventory().getItem(e.getSlot()) == null) {
                        return;
                    }
                    if (e.getSlot() < 0 || e.getSlot() > e.getInventory().getSize()) {
                        e.setCancelled(true);
                        return;
                    }
                    if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
                        e.setCancelled(true);
                        return;
                    }
                    String name = e.getInventory().getItem(e.getSlot()).getType().toString();
                    if (nms.isVersionLessThanOrEqualTo(12)) {
                        short i_data = e.getInventory().getItem(e.getSlot()).getDurability();
                        for (String f_name : getguifile().getConfigurationSection("CONVERT").getKeys(false)) {
                            if (f_name.equalsIgnoreCase(name)) {
                                short data = Short.parseShort(getguifile().getString("CONVERT." + name + ".DATA"));
                                if (data == i_data) {
                                    name = getguifile().getString("CONVERT." + name + ".MATERIAL");
                                    break;
                                }
                            }
                        }
                    } else {
                        for (String f_name : getguifile().getConfigurationSection("CONVERT").getKeys(false)) {
                            if (f_name.equalsIgnoreCase(name)) {
                                name = getguifile().getString("CONVERT." + name + ".MATERIAL");
                                break;
                            }
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
                            if (getStorage(Objects.requireNonNull(p), name) > 0) {
                                RemoveItems(p, name, Math.min(getStorage(Objects.requireNonNull(p), name), getAmountEmpty(Objects.requireNonNull(p), name)));
                            } else {
                                sendPlayerMessage(p, getlanguagefile().getString("User.Not_Have_Any_Item"));
                            }
                        }
                        if (e.getClick() == ClickType.SHIFT_RIGHT) {
                            if (getAmountItem((Objects.requireNonNull(p)), name) > 0) {
                                if (getMaxStorage(Objects.requireNonNull(p), name) >= getStorage(Objects.requireNonNull(p), name) + getAmountItem((Objects.requireNonNull(p)), name)) {
                                    AddItems((p), name, getAmountItem((Objects.requireNonNull(p)), name));
                                } else {
                                    if (getMaxStorage(Objects.requireNonNull(p), name) - getStorage(Objects.requireNonNull(p), name) > 0) {
                                        AddItems((p), name, getMaxStorage(Objects.requireNonNull(p), name) - getStorage(Objects.requireNonNull(p), name));
                                    } else {
                                        p.sendMessage(colorize(getlanguagefile().getString("User.Add_Full_Storage")));
                                    }
                                }
                            } else {
                                sendPlayerMessage(p, getlanguagefile().getString("User.Not_Have_Any_Item"));
                            }
                        }
                        if (e.getClick() == ClickType.CONTROL_DROP) {
                            if (getStorage(Objects.requireNonNull(p), name) > 0) {
                                SellItems(p, name, getStorage(Objects.requireNonNull(p), name));
                            } else {
                                sendPlayerMessage(p, getlanguagefile().getString("User.Not_Have_Any_Item"));
                            }
                        }
                    }
                }
            }
        }
    }
}