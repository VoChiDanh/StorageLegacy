package net.danh.storage.Gui;

import net.danh.dcore.Utils.Chat;
import net.danh.dcore.Utils.Items;
import net.danh.storage.Manager.Data;
import net.danh.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.logging.Level;

import static net.danh.storage.Manager.Files.*;

public class OpenGui {

    public static Inventory Open(Player p) {
        Inventory gui = Bukkit.createInventory(p, getguifile().getInt("ROWS") * 9, Chat.colorize(getguifile().getString("TITLE").replaceAll("#player#", p.getName())));
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemDecorate(p, gui);
                if (!Data.autoSmelt(p)) {
                    ItemsBlock(p, gui);
                } else {
                    ItemsConvert(p, gui);
                }
            }
        }.runTaskTimer(Storage.get(), 20L, 20L);
        return gui;
    }

    private static void ItemsBlock(Player p, Inventory inv) {
        for (String string : getguifile().getConfigurationSection("ITEMS").getKeys(false)) {
            if (getguifile().contains("ITEMS." + string + ".Block.material")) {
                if (!getguifile().contains("ITEMS." + string + ".Block.data")) {
                    Material material = Material.getMaterial(getguifile().getString("ITEMS." + string + ".Block.material"));
                    String name = Chat.colorize(papi(getguifile().getString("ITEMS." + string + ".Block.name"), p));
                    int slot = getguifile().getInt("ITEMS." + string + ".Block.slot");
                    int amount = getguifile().getInt("ITEMS." + string + ".Block.amount");
                    List<String> lore = Items.Lore(lorepapi(getguifile().getStringList("ITEMS." + string + ".Block.lore"), p));
                    ItemStack item = new ItemStack(material, amount);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(name);
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    inv.setItem(slot, item);
                }
                if (getguifile().contains("ITEMS." + string + ".Block.data")) {
                    Material material = Material.getMaterial(getguifile().getString("ITEMS." + string + ".Block.material"));
                    String name = Chat.colorize(papi(getguifile().getString("ITEMS." + string + ".Block.name"), p));
                    int slot = getguifile().getInt("ITEMS." + string + ".Block.slot");
                    short data = Short.parseShort(getguifile().getString("ITEMS." + string + ".Block.data"));
                    int amount = getguifile().getInt("ITEMS." + string + ".Block.amount");
                    List<String> lore = Items.Lore(lorepapi(getguifile().getStringList("ITEMS." + string + ".Block.lore"), p));
                    ItemStack item = new ItemStack(material, amount, data);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(name);
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    inv.setItem(slot, item);
                }
            }
        }
    }

    private static void ItemsConvert(Player p, Inventory inv) {
        for (String string : getguifile().getConfigurationSection("ITEMS").getKeys(false)) {
            if (getguifile().contains("ITEMS." + string + ".Convert.material")) {
                if (!getguifile().contains("ITEMS." + string + ".Convert.data")) {
                    Material material = Material.getMaterial(getguifile().getString("ITEMS." + string + ".Convert.material"));
                    String name = Chat.colorize(papi(getguifile().getString("ITEMS." + string + ".Convert.name"), p));
                    int slot = getguifile().getInt("ITEMS." + string + ".Convert.slot");
                    int amount = getguifile().getInt("ITEMS." + string + ".Convert.amount");
                    List<String> lore = Items.Lore(lorepapi(getguifile().getStringList("ITEMS." + string + ".Convert.lore"), p));
                    ItemStack item = new ItemStack(material, amount);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(name);
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    inv.setItem(slot, item);
                }
                if (getguifile().contains("ITEMS." + string + ".Convert.data")) {
                    Material material = Material.getMaterial(getguifile().getString("ITEMS." + string + ".Convert.material"));
                    String name = Chat.colorize(papi(getguifile().getString("ITEMS." + string + ".Convert.name"), p));
                    int slot = getguifile().getInt("ITEMS." + string + ".Convert.slot");
                    short data = Short.parseShort(getguifile().getString("ITEMS." + string + ".Convert.data"));
                    int amount = getguifile().getInt("ITEMS." + string + ".Convert.amount");
                    List<String> lore = Items.Lore(lorepapi(getguifile().getStringList("ITEMS." + string + ".Convert.lore"), p));
                    ItemStack item = new ItemStack(material, amount, data);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(name);
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    inv.setItem(slot, item);
                }
            }
        }
    }

    private static void ItemDecorate(Player p, Inventory inv) {
        for (String string : getguifile().getConfigurationSection("DECORATES").getKeys(false)) {
            Material material = Material.getMaterial(getguifile().getString("DECORATES." + string + ".material"));
            if (material == null) {
                Storage.get().getLogger().log(Level.INFO, "Decorate Material is null !");
                return;
            }

            String name = Chat.colorize(papi(getguifile().getString("DECORATES." + string + ".name"), p));
            List<Integer> slots = getguifile().getIntegerList("DECORATES." + string + ".slots");
            int slot = getguifile().getInt("DECORATES." + string + ".slot");
            List<String> lore = Items.Lore(lorepapi(getguifile().getStringList("DECORATES." + string + ".lore"), p));
            if (getguifile().contains("DECORATES." + string + ".slot")) {
                if (!getguifile().contains("DECORATES." + string + ".data")) {
                    ItemStack item = new ItemStack(material, 1);
                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(lore);
                    meta.setDisplayName(name);
                    item.setItemMeta(meta);
                    inv.setItem(slot, item);
                }
                if (getguifile().contains("DECORATES." + string + ".data")) {
                    short data = Short.parseShort(getguifile().getString("DECORATES." + string + ".data"));
                    ItemStack item = new ItemStack(material, 1, data);
                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(lore);
                    meta.setDisplayName(name);
                    item.setItemMeta(meta);
                    inv.setItem(slot, item);
                }
            }
            if (getguifile().contains("DECORATES." + string + ".slots")) {
                if (!getguifile().contains("DECORATES." + string + ".data")) {
                    ItemStack item = new ItemStack(material, 1);
                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(lore);
                    meta.setDisplayName(name);
                    item.setItemMeta(meta);
                    for (Integer s : slots) {
                        inv.setItem(s, item);
                    }
                }
                if (getguifile().contains("DECORATES." + string + ".data")) {
                    short data = Short.parseShort(getguifile().getString("DECORATES." + string + ".data"));
                    ItemStack item = new ItemStack(material, 1, data);
                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(lore);
                    meta.setDisplayName(name);
                    item.setItemMeta(meta);
                    for (Integer s : slots) {
                        inv.setItem(s, item);
                    }
                }
            }
        }
    }
}
