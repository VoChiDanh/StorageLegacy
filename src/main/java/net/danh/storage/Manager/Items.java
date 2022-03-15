package net.danh.storage.Manager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Files.*;
import static net.danh.storage.Storage.economy;

public class Items {

    private static int price;
    private static String block;

    public static void RemoveItems(Player p, String name, Integer amount) {
        if (getStorage(p, name) >= amount) {
            for (String getBlockType : getconfigfile().getConfigurationSection("Blocks.").getKeys(false)) {
                if (name.equalsIgnoreCase(getBlockType)) {
                    block = getconfigfile().getString("Blocks." + name + ".Name");
                    break;
                }
            }
            removeStorage(p, name, amount);
            if (autoSmelt(p)) {
                if (name.contains("_ORE")) {
                    name = getconfigfile().getString("Blocks." + name + ".Convert");
                }
            }
            ItemStack items = new ItemStack(Material.getMaterial(name), amount);
            p.getInventory().addItem(items);
            p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.TAKE")),
                    new TranslatableComponent(colorize(getlanguagefile().getString("Take_Item")
                            .replaceAll("%blocks%", block.replaceAll("_", " "))
                            .replaceAll("%amount%", String.valueOf(amount)))));
        } else {
            p.sendMessage(colorize(getlanguagefile().getString("Not_Enough")));
        }
    }

    @Deprecated
    public static String getName(String name) {
        return getconfigfile().getString("Blocks." + name + ".Name");
    }

    public static void SellItems(Player p, String name, Integer amount) {
        if (getStorage(p, name) >= amount) {
            for (String getBlockType : getconfigfile().getConfigurationSection("Blocks.").getKeys(false)) {
                if (name.equalsIgnoreCase(getBlockType)) {
                    price = getconfigfile().getInt("Blocks." + name + ".Price");
                    block = getconfigfile().getString("Blocks." + name + ".Name");
                    break;
                }
            }
            removeStorage(p, name, amount);
            int money = price * amount;
            EconomyResponse r = economy.depositPlayer(p, money);
            if (r.transactionSuccess()) {
                p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.TAKE")),
                        new TranslatableComponent(colorize(getlanguagefile().getString("Sell")
                                .replaceAll("%money%", String.valueOf(money))
                                .replaceAll("%item%", block.replaceAll("_", " ")))));
            } else {
                p.sendMessage(colorize("&cError!"));
            }
        } else {
            p.sendMessage(colorize(getlanguagefile().getString("Not_Enough")));
        }
    }

    public static int getPrice(String m) {
        return Files.getconfigfile().getInt("Blocks." + m + ".Price");
    }
}
