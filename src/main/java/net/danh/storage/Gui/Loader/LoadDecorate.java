package net.danh.storage.Gui.Loader;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.danh.storage.Gui.Loader.LoadMenu.decorate;
import static net.danh.storage.Gui.Loader.LoadMenu.decorate_slot;
import static net.danh.storage.Manager.Files.*;

public  class LoadDecorate {
    public static void LoadDecorate(Player p) {
        Set<String> ditems = getguifile().getConfigurationSection("DECORATES.").getKeys(false);
        for (String key : ditems) {
            ItemStack item;
            Set<String> properties = getguifile().getConfigurationSection("DECORATES." + key + ".").getKeys(false);
            if (properties.contains("material")) {
                int d = getguifile().getInt("DECORATES." + key + ".data");
                short data = (short) d;
                if (properties.contains("amount")) {
                    if (properties.contains("data")) {
                        item = new ItemStack(Material.matchMaterial(getguifile().getString("DECORATES." + key + ".material")), getguifile().getInt("DECORATES." + key + ".amount"), data);
                    } else {
                        item = new ItemStack(Material.matchMaterial(getguifile().getString("DECORATES." + key + ".material")), getguifile().getInt("DECORATES." + key + ".amount"));
                    }
                } else {
                    if (properties.contains("data")) {
                        item = new ItemStack(Material.matchMaterial(getguifile().getString("DECORATES." + key + ".material")), 1, data);
                    } else {
                        item = new ItemStack(Material.matchMaterial(getguifile().getString("DECORATES." + key + ".material")), 1);
                    }
                }
                ItemMeta meta = item.getItemMeta();
                if (properties.contains("name")) {
                    meta.setDisplayName(colorize(papi(getguifile().getString("DECORATES." + key + ".name"), p)));
                    item.setItemMeta(meta);
                }
                if (properties.contains("lore")) {
                    List<String> lores = lorecolor(lorepapi(getguifile().getStringList("DECORATES." + key + ".lore"), p));
                    meta.setLore(lores);
                    item.setItemMeta(meta);
                }
            } else {
                item = new ItemStack(Material.AIR);
                Bukkit.getLogger().warning("[Storage] The decorate item (" + key + ") do not have material propertie");
            }
            decorate.add(item);
            if (properties.contains("slot")) {
                if (getguifile().getIntegerList("DECORATES." + key + ".slot").isEmpty()) {
                    List<Integer> input = new ArrayList<Integer>();
                    input.add(getguifile().getInt("DECORATES." + key + ".slot"));
                    decorate_slot.add(input);
                } else {
                    decorate_slot.add(getguifile().getIntegerList("DECORATES." + key + ".slot"));
                }
            } else {
                decorate_slot.add(null);
                Bukkit.getLogger().warning("[Storage] The decorate item (" + key + ") do not have slot propertie");
            }
        }
    }
}
