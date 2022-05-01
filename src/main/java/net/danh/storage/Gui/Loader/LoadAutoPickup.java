package net.danh.storage.Gui.Loader;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static net.danh.storage.Gui.Loader.LoadMenu.pickup_buttons;
import static net.danh.storage.Gui.Loader.LoadMenu.pickup_buttons_slot;
import static net.danh.storage.Manager.Files.*;
import static net.danh.storage.Manager.Files.getguifile;

public class LoadAutoPickup {
    public static void LoadPickup(Player p) {
        HashMap<Boolean, ItemStack> auto_pickup = new HashMap<>();
        HashMap<String, Integer> auto_pickup_slot = new HashMap<>();
        Set<String> a = getguifile().getConfigurationSection("BUTTONS.Auto_Pickup.").getKeys(false);
        for (String pickup : a) {
            if (!pickup.equalsIgnoreCase("Slot")) {
                ItemStack item;
                Set<String> properties = getguifile().getConfigurationSection("BUTTONS.Auto_Pickup." + pickup + ".").getKeys(false);
                if (properties.contains("material")) {
                    int d = getguifile().getInt("BUTTONS.Auto_Pickup." + pickup + ".data");
                    short data = (short) d;
                    if (properties.contains("amount")) {
                        if (properties.contains("data")) {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".material")), getguifile().getInt("BUTTONS.Auto_Pickup." + pickup + ".amount"), data);
                        } else {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".material")), getguifile().getInt("BUTTONS.Auto_Pickup." + pickup + ".amount"));
                        }
                    } else {
                        if (properties.contains("data")) {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".material")), 1, data);
                        } else {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".material")), 1);
                        }
                    }
                    ItemMeta meta = item.getItemMeta();
                    if (properties.contains("name")) {
                        meta.setDisplayName(colorize(papi(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".name"), p)));
                        item.setItemMeta(meta);
                    }
                    if (properties.contains("lore")) {
                        List<String> lores = lorecolor(lorepapi(getguifile().getStringList("BUTTONS.Auto_Pickup." + pickup + ".lore"), p));
                        meta.setLore(lores);
                        item.setItemMeta(meta);
                    }
                } else {
                    item = new ItemStack(Material.AIR);
                    Bukkit.getLogger().warning("[Storage] The button auto_pickup (" + pickup + ") do not have material propertie");
                }
                auto_pickup.put(Boolean.parseBoolean(pickup), item);
                HashMap<String, HashMap<Boolean, ItemStack>> pick = new HashMap<>();
                pick.put("Pickup", auto_pickup);
                auto_pickup_slot.put("Pickup", getguifile().getInt("BUTTONS.Auto_Pickup.Slot"));
                pickup_buttons.put(p, pick);
            } else {
                pickup_buttons_slot = getguifile().getInt("BUTTONS.Auto_Pickup.Slot");
            }
        }
    }
}
