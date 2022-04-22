package net.danh.storage.Manager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import preponderous.ponder.minecraft.bukkit.nms.NMSAssistant;

import java.util.Objects;

import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Files.*;
import static net.danh.storage.Storage.economy;

public class Items {

    private static int price;
    private static String block;

    public  static void AddItems(Player p, String name, Integer amount) {
        if (Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false).contains(name)) {
            ItemStack checkitems = new ItemStack(Objects.requireNonNull(Material.getMaterial(name.toUpperCase())));
            if (Objects.requireNonNull(p.getInventory().containsAtLeast(checkitems, amount))) {
                ItemStack items = new ItemStack(Objects.requireNonNull(Material.getMaterial(name.toUpperCase())), amount);
                p.getInventory().removeItem(items);
                Data.addStorage(p, name.toUpperCase(), amount);
                if (Objects.requireNonNull(getconfigfile().getString("Message.ADD.TYPE")).equalsIgnoreCase("ACTION_BAR")
                        || Objects.requireNonNull(getconfigfile().getString("Message.ADD.TYPE")).equalsIgnoreCase("CHAT")) {
                    if (Files.getconfigfile().getBoolean("Message.ADD.STATUS")) {
                        p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.ADD.TYPE")),
                                new TranslatableComponent(colorize(Objects.requireNonNull(getlanguagefile().getString("User.Add_Item"))
                                        .replaceAll("%item%", getName(name.toUpperCase()).replaceAll("_", " ").replaceAll("-", " "))
                                        .replaceAll("%amount%", String.valueOf(amount))
                                        .replaceAll("%storage%", String.format("%,d", getStorage(p, name.toUpperCase())))
                                        .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name.toUpperCase()))))));
                    }
                } else {
                    if (Objects.requireNonNull(getconfigfile().getString("Message.ADD.TYPE")).equalsIgnoreCase("TITLE")) {
                        p.sendTitle(colorize(Objects.requireNonNull(getconfigfile().getString("Message.ADD.TITLE.TITLE"))
                                .replaceAll("%item%", getName(name.toUpperCase()).replaceAll("_", " ").replaceAll("-", " "))
                                .replaceAll("%amount%", String.valueOf(amount))
                                .replaceAll("%storage%", String.format("%,d", getStorage(p, name.toUpperCase()))))
                                .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name.toUpperCase()))), colorize(Objects.requireNonNull(getconfigfile().getString("Message.ADD.TITLE.SUBTITLE"))
                                .replaceAll("%item%", Items.getName(name.toUpperCase()).replaceAll("_", " ").replaceAll("-", " "))
                                .replaceAll("%amount%", name.toUpperCase())
                                .replaceAll("%storage%", String.format("%,d", getStorage(p, name.toUpperCase())))
                                .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name.toUpperCase())))), getconfigfile().getInt("Message.ADD.TITLE.FADEIN"), getconfigfile().getInt("Message.ADD.TITLE.STAY"), getconfigfile().getInt("Message.ADD.TITLE.FADEOUT"));
                    }
                }
            } else {
                p.sendMessage(Files.colorize(Files.getlanguagefile().getString("User.Not_Enough")));
            }
        } else {
            p.sendMessage(Files.colorize(Files.getlanguagefile().getString("User.Not_Correct_Item")));
        }
    }

    public static void RemoveItems(Player p, String name, Integer amount) {
        if (Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false).contains(name)) {
            if (getStorage(p, name) >= amount) {
                for (String getBlockType : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false)) {
                    if (name.equalsIgnoreCase(getBlockType)) {
                        block = getconfigfile().getString("Blocks." + name + ".Name");
                        break;
                    }
                }
                removeStorage(p, name, amount);
                if (Objects.requireNonNull(getconfigfile().getString("Message.TAKE.TYPE")).equalsIgnoreCase("ACTION_BAR")
                        || Objects.requireNonNull(getconfigfile().getString("Message.TAKE.TYPE")).equalsIgnoreCase("CHAT")) {
                    if (getconfigfile().getBoolean("Message.TAKE.STATUS")) {
                        p.spigot().sendMessage(ChatMessageType.valueOf(getconfigfile().getString("Message.TAKE.TYPE")),
                                new TranslatableComponent(colorize(Objects.requireNonNull(getlanguagefile().getString("User.Take_Item"))
                                        .replaceAll("%item%", block.replaceAll("_", " ")
                                                .replaceAll("-", " "))
                                        .replaceAll("%amount%", String.valueOf(amount))
                                        .replaceAll("%storage%", String.format("%,d", getStorage(p, name)))
                                        .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name))))));
                    }
                } else {
                    if (Objects.requireNonNull(getconfigfile().getString("Message.TAKE.TYPE")).equalsIgnoreCase("TITLE")) {
                        p.sendTitle(colorize(Objects.requireNonNull(getconfigfile().getString("Message.TAKE.TITLE.TITLE"))
                                .replaceAll("%item%", block.replaceAll("_", " ")
                                        .replaceAll("-", " "))
                                .replaceAll("%amount%", String.valueOf(amount))
                                .replaceAll("%storage%", String.format("%,d", getStorage(p, name))))
                                .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name))), colorize(Objects.requireNonNull(getconfigfile().getString("Message.TAKE.TITLE.SUBTITLE"))
                                .replaceAll("%item%", block.replaceAll("_", " ")
                                        .replaceAll("-", " "))
                                .replaceAll("%amount%", String.valueOf(amount))
                                .replaceAll("%storage%", String.format("%,d", getStorage(p, name)))
                                .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name)))), getconfigfile().getInt("Message.TAKE.TITLE.FADEIN"), getconfigfile().getInt("Message.TAKE.TITLE.STAY"), getconfigfile().getInt("Message.TAKE.TITLE.FADEOUT"));
                    }
                }
                if (autoSmelt(p)) {
                    if (Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks." + name)).getKeys(false).contains("Convert")) {
                        name = getconfigfile().getString("Blocks." + name + ".Convert");
                    }
                }
                NMSAssistant nmsAssistant = new NMSAssistant();
                if (nmsAssistant.isVersionLessThan(13)) {
                    String[] data = Objects.requireNonNull(name).split(";");
                    String material = data[0];
                    if (data.length == 1) {
                        ItemStack items = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(material))), amount);
                        p.getInventory().addItem(items);
                    } else {
                        short damaged = Short.parseShort(data[1]);
                        ItemStack items = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(material))), amount, damaged);
                        p.getInventory().addItem(items);
                    }
                } else {
                    ItemStack items = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(name))), amount);
                    p.getInventory().addItem(items);
                }
            } else {
                p.sendMessage(colorize(getlanguagefile().getString("User.Not_Enough")));
            }
        } else {
            p.sendMessage(colorize(getlanguagefile().getString("User.Not_Correct_Item")));
        }
    }

    public static String getName(@NotNull String name) {
        return getconfigfile().getString("Blocks." + name.toUpperCase() + ".Name");
    }

    public static void SellItems(Player p, String name, Integer amount) {
        if (Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false).contains(name)) {
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
                    if (Objects.requireNonNull(getconfigfile().getString("Message.SELL.TYPE")).equalsIgnoreCase("ACTION_BAR")
                            || Objects.requireNonNull(getconfigfile().getString("Message.SELL.TYPE")).equalsIgnoreCase("CHAT")) {
                        p.spigot().sendMessage(ChatMessageType.valueOf(getconfigfile().getString("Message.SELL.TYPE")),
                                new TranslatableComponent(colorize(Objects.requireNonNull(getlanguagefile().getString("User.Sell"))
                                        .replaceAll("%money%", String.valueOf(money))
                                        .replaceAll("%item%", block.replaceAll("_", " ")
                                                .replaceAll("-", " "))
                                        .replaceAll("%amount%", String.valueOf(amount))
                                        .replaceAll("%storage%", String.format("%,d", getStorage(p, name)))
                                        .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name))))));
                    } else {
                        if (Objects.requireNonNull(getconfigfile().getString("Message.SELL.TYPE")).equalsIgnoreCase("TITLE")) {
                            p.sendTitle(colorize(Objects.requireNonNull(getconfigfile().getString("Message.SELL.TITLE.TITLE"))
                                    .replaceAll("%money%", String.valueOf(money))
                                    .replaceAll("%item%", block.replaceAll("_", " ")
                                            .replaceAll("-", " "))
                                    .replaceAll("%amount%", String.valueOf(amount))
                                    .replaceAll("%storage%", String.format("%,d", getStorage(p, name))))
                                    .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name))), colorize(Objects.requireNonNull(getconfigfile().getString("Message.SELL.TITLE.SUBTITLE"))
                                    .replaceAll("%money%", String.valueOf(money))
                                    .replaceAll("%item%", block.replaceAll("_", " ")
                                            .replaceAll("-", " "))
                                    .replaceAll("%amount%", String.valueOf(amount))
                                    .replaceAll("%storage%", String.format("%,d", getStorage(p, name)))
                                    .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name)))), getconfigfile().getInt("Message.SELL.TITLE.FADEIN"), getconfigfile().getInt("Message.SELL.TITLE.STAY"), getconfigfile().getInt("Message.SELL.TITLE.FADEOUT"));
                        }
                    }
                } else {
                    p.sendMessage(colorize(getlanguagefile().getString("Errol")));
                }
            } else {
                p.sendMessage(colorize(getlanguagefile().getString("User.Not_Enough")));
            }
        } else {
            p.sendMessage(colorize(getlanguagefile().getString("User.Not_Correct_Item")));
        }
    }

    public static int getPrice(String m) {
        return getconfigfile().getInt("Blocks." + m + ".Price");
    }

    public static int getAmountItem (Player p, String name) {
        int amount = 0;
        for (ItemStack is : p.getInventory().getContents()) {
            if (is != null && is.getType().equals(Material.getMaterial(name))) {
                amount = amount + is.getAmount();
            }
        }
        return amount;
    }
}
