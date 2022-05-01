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

public class LoadBlocks {
    public static void LoadItemsBlock(Player p) {
        Set<String> iitems = getguifile().getConfigurationSection("ITEMS").getKeys(false);
        for (String key : iitems) {
            ItemStack item;
            Set<String> type = getguifile().getConfigurationSection("ITEMS." + key + ".").getKeys(false);
            if (type.contains("Block")) {
                Set<String> properties = getguifile().getConfigurationSection("ITEMS." + key + ".Block.").getKeys(false);
                if (properties.contains("material")) {
                    int d = getguifile().getInt("ITEMS." + key + ".Block.data");
                    short data = (short) d;
                    if (properties.contains("amount")) {
                        if (properties.contains("data")) {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("ITEMS." + key + ".Block.material")), getguifile().getInt("ITEMS." + key + ".Block.amount"), data);
                        } else {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("ITEMS." + key + ".Block.material")), getguifile().getInt("ITEMS." + key + ".Block.amount"));
                        }
                    } else {
                        if (properties.contains("data")) {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("ITEMS." + key + ".Block.material")), 1, data);
                        } else {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("ITEMS." + key + ".Block.material")), 1);
                        }
                    }
                    ItemMeta meta = item.getItemMeta();
                    if (properties.contains("name")) {
                        meta.setDisplayName(colorize(papi(getguifile().getString("ITEMS." + key + ".Block.name"), p)));
                        item.setItemMeta(meta);
                    }
                    if (properties.contains("lore")) {
                        List<String> lore = lorecolor(lorepapi(getguifile().getStringList("ITEMS." + key + ".Block.lore"), p));
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                    }
                } else {
                    item = new ItemStack(Material.AIR);
                    Bukkit.getLogger().warning("[Storage] The item (" + key + ") do not have material propertie");
                }
                items.add(item);
                if (type.contains("Slot")) {
                    items_slot.add(getguifile().getInt("ITEMS." + key + ".Slot"));
                } else {
                    items_slot.add(null);
                    Bukkit.getLogger().warning("[Storage] The item (" + key + ") do not have slot type");
                }
            } else {
                items.add(null);
                items_slot.add(null);
                Bukkit.getLogger().warning("[Storage] The item (" + key + ") do not have block type");
            }
        }
    }
}
