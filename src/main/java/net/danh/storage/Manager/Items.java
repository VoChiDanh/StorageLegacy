package net.danh.storage.Manager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import preponderous.ponder.minecraft.bukkit.nms.NMSAssistant;

import java.util.Objects;

import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Files.*;
import static net.danh.storage.Storage.economy;

public class Items {

    private static int price;
    private static String block;
    private static String material;

    public static void RemoveItems(Player p, String name, Integer amount) {
        if (getStorage(p, name) >= amount) {
            for (String getBlockType : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
                if (name.equalsIgnoreCase(getBlockType)) {
                    block = getconfigfile().getString("Blocks." + name + ".Name");
                    break;
                }
            }
            removeStorage(p, name, amount);
            if (autoSmelt(p)) {
                if (name.contains("_ORE")) {
                    material = getconfigfile().getString("Blocks." + name + ".Convert");
                }
            }
            NMSAssistant nmsAssistant = new NMSAssistant();
            if (nmsAssistant.isVersionLessThan(13)) {
                if (name.equalsIgnoreCase("LAPIS_ORE")) {
                    if (Objects.requireNonNull(material).equalsIgnoreCase("INK_SACK")) {
                        ItemStack items = new ItemStack(Material.LEGACY_INK_SACK, amount, (short) 4);
                        p.getInventory().addItem(items);
                    }
                } else {
                    ItemStack items = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(material))), amount);
                    p.getInventory().addItem(items);
                }
            } else {
                ItemStack items = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(material))), amount);
                p.getInventory().addItem(items);
            }
            p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.TAKE")),
                    new TranslatableComponent(colorize(Objects.requireNonNull(getlanguagefile().getString("Take_Item"))
                            .replaceAll("%blocks%", block.replaceAll("_", " "))
                            .replaceAll("%amount%", String.valueOf(amount))
                            .replaceAll("%storage%", String.format("%,d", getStorage(p, name)))
                            .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name))))));
        } else {
            p.sendMessage(colorize(getlanguagefile().getString("Not_Enough")));
        }
    }

    public static String getName(String name) {
        return getconfigfile().getString("Blocks." + name + ".Name");
    }

    public static void SellItems(Player p, String name, Integer amount) {
        if (getStorage(p, name) >= amount) {
            for (String getBlockType : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
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
                        new TranslatableComponent(colorize(Objects.requireNonNull(getlanguagefile().getString("Sell"))
                                .replaceAll("%money%", String.valueOf(money))
                                .replaceAll("%item%", block.replaceAll("_", " "))
                                .replaceAll("%storage%", String.format("%,d", getStorage(p, name)))
                                .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name))))));
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
