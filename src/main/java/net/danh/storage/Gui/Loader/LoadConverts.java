package net.danh.storage.Gui.Loader;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Set;

import static net.danh.storage.Gui.Loader.LoadMenu.*;
import static net.danh.storage.Manager.Files.*;

public class LoadConverts {
    public static void LoadItemsConvert(Player p) {
        Set<String> citems = getguifile().getConfigurationSection("ITEMS").getKeys(false);
        for (String key : citems) {
            ItemStack item;
            Set<String> type = getguifile().getConfigurationSection("ITEMS." + key + ".").getKeys(false);
            boolean status = getguifile().getBoolean("ITEMS." + key + ".Convert_Status");
            int i = 0;
            if (status) {
                if (type.contains("Convert")) {
                    Set<String> properties = getguifile().getConfigurationSection("ITEMS." + key + ".Convert.").getKeys(false);
                    if (properties.contains("material")) {
                        converts_status.add(true);
                        int d = getguifile().getInt("ITEMS." + key + ".Convert.data");
                        short data = (short) d;
                        if (properties.contains("amount")) {
                            if (properties.contains("data")) {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("ITEMS." + key + ".Convert.material")), getguifile().getInt("ITEMS." + key + ".Convert.amount"), data);
                            } else {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("ITEMS." + key + ".Convert.material")), getguifile().getInt("ITEMS." + key + ".Convert.amount"));
                            }
                        } else {
                            if (properties.contains("data")) {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("ITEMS." + key + ".Convert.material")), 1, data);
                            } else {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("ITEMS." + key + ".Convert.material")), 1);
                            }
                        }
                        ItemMeta meta = item.getItemMeta();
                        if (properties.contains("name")) {
                            meta.setDisplayName(colorize(papi(getguifile().getString("ITEMS." + key + ".Convert.name"), p)));
                            item.setItemMeta(meta);
                        }
                        if (properties.contains("lore")) {
                            List<String> lore = lorecolor(lorepapi(getguifile().getStringList("ITEMS." + key + ".Convert.lore"), p));
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                        }
                    } else {
                        item = new ItemStack(Material.AIR);
                        Bukkit.getLogger().warning("[Storage] The item (" + key + ") do not have material propertie");
                    }
                    converts.add(item);
                    if (type.contains("Slot")) {
                        converts_slot.add(getguifile().getInt("ITEMS." + key + ".Slot"));
                    } else {
                        converts_slot.add(null);
                        Bukkit.getLogger().warning("[Storage] The item (" + key + ") do not have slot propertie");
                    }
                    i++;
                } else {
                    converts.add(null);
                    converts_slot.add(null);
                    converts_status.add(null);
                    Bukkit.getLogger().warning("[Storage] The item (" + key + ") do not have convert type");
                }
            } else {
                converts_status.add(false);
                converts.add(items.get(i));
                converts_slot.add(items_slot.get(i));
                i++;
            }
        }
    }
}
