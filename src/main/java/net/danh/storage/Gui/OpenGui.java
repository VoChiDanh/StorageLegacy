package net.danh.storage.Gui;

import net.danh.storage.Gui.Loader.LoadMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

import static net.danh.storage.Gui.Loader.LoadMenu.*;
import static net.danh.storage.Manager.Data.autoPick;
import static net.danh.storage.Manager.Data.autoSmelt;

public class OpenGui {
    public static Inventory gui;
    public static void OpenGui(Player p) {
        LoadMenu(p);
        gui = Bukkit.createInventory(null, size, tittle);
        int d = 0;
        for (List<Integer> a : decorate_slot) {
            if (a == null) {
                decorate.remove(d);
            } else {
                for (int j = 0; j < a.size(); j++) {
                    gui.setItem(a.get(j), decorate.get(d));
                }
                d++;
            }
        }
        HashMap<String, HashMap<Boolean, ItemStack>> ppick = pickup_buttons.get(p);
        HashMap<Boolean, ItemStack> pick = ppick.get("Pickup");
        gui.setItem(pickup_buttons_slot, pick.get(autoPick(p)));
        HashMap<String, HashMap<Boolean, ItemStack>> psmelt = smelt_buttons.get(p);
        HashMap<Boolean, ItemStack> smelt = psmelt.get("Smelt");
        gui.setItem(smelt_buttons_slot, smelt.get(autoSmelt(p)));
        if (autoSmelt(p)) {
            int i = 0;
            try {
                for (Boolean ct : converts_status) {
                    if (ct) {
                        if (converts_slot.get(i) == null) {
                            converts_slot.remove(i);
                        } else {
                            gui.setItem(converts_slot.get(i), converts.get(i));
                            i++;
                        }
                    } else {
                        if (items_slot.get(i) == null) {
                            items_slot.remove(i);
                        } else {
                            gui.setItem(items_slot.get(i), items.get(i));
                            i++;
                        }
                    }
                }
            } catch (Exception e) {
                Bukkit.getLogger().warning("[Storage] One or more items do not have block type or convert type so the items will not be loaded");
            }
        } else {
            int i = 0;
            try {
                for (Integer b : items_slot) {
                    if (items_slot.get(i) == null) {
                        items_slot.remove(i);
                    } else {
                        gui.setItem(b, items.get(i));
                        i++;
                    }
                }
            } catch (Exception e) {
                Bukkit.getLogger().warning("[Storage] One or more items do not have block type so the items will not be loaded");
            }
        }
        p.openInventory(gui);
    }
}
