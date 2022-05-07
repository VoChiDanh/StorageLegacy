package net.danh.storage.Gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;

import static net.danh.storage.Gui.LoadMenu.LoadMenu;
import static net.danh.storage.Gui.LoadMenu.*;
import static net.danh.storage.Gui.OpenGui.SetItem;
import static net.danh.storage.Gui.OpenGui.gui;
import static net.danh.storage.Manager.Data.autoPick;
import static net.danh.storage.Manager.Data.autoSmelt;
import static net.danh.storage.Manager.Files.getguifile;
import static net.danh.storage.Storage.get;

public class Manager {
    public static HashMap<Player, BukkitTask> update_task = new HashMap<>();
    public static HashMap<Player, Long> cooldown = new HashMap<>();
    public static void UpdateI(Player p) {
        update_task.put(p, new  BukkitRunnable() {
            @Override
            public void run() {
                Inventory inv = p.getOpenInventory().getTopInventory();
                LoadMenu(p);
                if (player_gui.get(p).equals(inv)) {
                    HashMap<String, HashMap<Boolean, ItemStack>> ppick = pickup_buttons.get(p);
                    HashMap<Boolean, ItemStack> pick = ppick.get("Pickup");
                    if (update_buttons.get("Pickup")) {
                        inv.setItem(pickup_buttons_slot, pick.get(autoPick(p)));
                    }
                    HashMap<String, HashMap<Boolean, ItemStack>> psmelt = smelt_buttons.get(p);
                    HashMap<Boolean, ItemStack> smelt = psmelt.get("Smelt");
                    if (update_buttons.get("Smelt")) {
                        inv.setItem(smelt_buttons_slot, smelt.get(autoSmelt(p)));
                    }
                    int d = 0;
                    for (List<Integer> a : decorate_slot) {
                        if (update_decorate.get(d)) {
                            if (a == null) {
                                decorate.remove(d);
                            } else {
                                for (Integer integer : a) {
                                    inv.setItem(integer, decorate.get(d));
                                }
                                d++;
                            }
                        } else {
                            d++;
                        }
                    }
                    if (autoSmelt(p)) {
                        int i = 0;
                        try {
                            for (Boolean ct : converts_status) {
                                if (ct) {
                                    if (update_items_convert.get(i)) {
                                        if (converts_slot.get(i) == null) {
                                            converts_slot.remove(i);
                                        } else {
                                            inv.setItem(converts_slot.get(i), converts.get(i));
                                            i++;
                                        }
                                    } else {
                                        i++;
                                    }
                                } else {
                                    if (update_items_block.get(i)) {
                                        if (items_slot.get(i) == null) {
                                            items_slot.remove(i);
                                        } else {
                                            inv.setItem(items_slot.get(i), items.get(i));
                                            i++;
                                        }
                                    } else {
                                        i++;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            return;
                        }
                    } else {
                        int i = 0;
                        try {
                            for (Integer b : items_slot) {
                                if (update_items_block.get(i)) {
                                    if (items_slot.get(i) == null) {
                                        items_slot.remove(i);
                                    } else {
                                        inv.setItem(b, items.get(i));
                                        i++;
                                    }
                                } else {
                                    i++;
                                }
                            }
                        } catch (Exception e) {
                            Bukkit.getLogger().warning("[Storage] One or more items do not have block type so the items will not be loaded");
                        }
                    }
                    p.updateInventory();
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(get(), 20L * getguifile().getInt("UPDATE.TIME"), 20L * getguifile().getInt("UPDATE.TIME")));
    }

    public static void UpdateA(Player p) {
        update_task.put(p, new BukkitRunnable() {
            @Override
            public void run() {
                Inventory inv = p.getOpenInventory().getTopInventory();
                if (player_gui.get(p).equals(inv)) {
                    SetItem(p);
                    SaveMenu(p);
                    ReloadMenu();
                    p.openInventory(gui);
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(get(), 20L * getguifile().getInt("UPDATE.TIME"), 20L * getguifile().getInt("UPDATE.TIME")));
    }
}
