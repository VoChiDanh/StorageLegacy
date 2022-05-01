package net.danh.storage.Gui.Loader;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static net.danh.storage.Gui.Loader.LoadMenu.smelt_buttons;
import static net.danh.storage.Gui.Loader.LoadMenu.smelt_buttons_slot;
import static net.danh.storage.Manager.Files.*;

public class LoadAutoSmelt {
    public static void LoadSmelt(Player p) {
        HashMap<Boolean, ItemStack> auto_smelt = new HashMap<>();
        HashMap<String, Integer> auto_smelt_slot = new HashMap<>();
        Set<String> a = getguifile().getConfigurationSection("BUTTONS.Auto_Smelt.").getKeys(false);
        for (String pickup : a) {
            if (!pickup.equalsIgnoreCase("Slot")) {
                ItemStack item;
                Set<String> properties = getguifile().getConfigurationSection("BUTTONS.Auto_Smelt." + pickup + ".").getKeys(false);
                if (properties.contains("material")) {
                    int d = getguifile().getInt("BUTTONS.Auto_Smelt." + pickup + ".data");
                    short data = (short) d;
                    if (properties.contains("amount")) {
                        if (properties.contains("data")) {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Smelt." + pickup + ".material")), getguifile().getInt("BUTTONS.Auto_Smelt." + pickup + ".amount"), data);
                        } else {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Smelt." + pickup + ".material")), getguifile().getInt("BUTTONS.Auto_Smelt." + pickup + ".amount"));
                        }
                    } else {
                        if (properties.contains("data")) {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Smelt." + pickup + ".material")), 1, data);
                        } else {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Smelt." + pickup + ".material")), 1);
                        }
                    }
                    ItemMeta meta = item.getItemMeta();
                    if (properties.contains("name")) {
                        meta.setDisplayName(colorize(papi(getguifile().getString("BUTTONS.Auto_Smelt." + pickup + ".name"), p)));
                        item.setItemMeta(meta);
                    }
                    if (properties.contains("lore")) {
                        List<String> lores = lorecolor(lorepapi(getguifile().getStringList("BUTTONS.Auto_Smelt." + pickup + ".lore"), p));
                        meta.setLore(lores);
                        item.setItemMeta(meta);
                    }
                } else {
                    item = new ItemStack(Material.AIR);
                    Bukkit.getLogger().warning("[Storage] The button auto_smelt (" + pickup + ") do not have material propertie");
                }
                auto_smelt.put(Boolean.parseBoolean(pickup), item);
                HashMap<String, HashMap<Boolean, ItemStack>> smelt = new HashMap<>();
                smelt.put("Smelt", auto_smelt);
                auto_smelt_slot.put("Smelt", getguifile().getInt("BUTTONS.Auto_Pickup.Slot"));
                smelt_buttons.put(p, smelt);
            } else {
                smelt_buttons_slot = getguifile().getInt("BUTTONS.Auto_Smelt.Slot");
            }
        }
    }
}
