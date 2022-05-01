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

    public static void AddItems(Player p, String name, Integer amount) {
        name = name.toUpperCase();
        if (Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks.")).getKeys(false).contains(name)) {
            NMSAssistant nmsAssistant = new NMSAssistant();
            ItemStack checkitems;
            if (nmsAssistant.isVersionLessThan(13)) {
                String[] iname = name.split(";");
                if (iname.length == 1) {
                    checkitems = new ItemStack(Objects.requireNonNull(Material.getMaterial(iname[0])));
                } else {
                    checkitems = new ItemStack(Objects.requireNonNull(Material.getMaterial(iname[0])));
                    checkitems.setDurability(Short.parseShort(iname[1]));
                }
            } else {
                checkitems = new ItemStack(Objects.requireNonNull(Material.getMaterial(name)));
            }
            if (p.getInventory().containsAtLeast(checkitems, amount)) {
                ItemStack items;
                if (nmsAssistant.isVersionLessThan(13)) {
                    String[] iname = name.split(";");
                    if (iname.length == 1) {
                        items = new ItemStack(Objects.requireNonNull(Material.getMaterial(iname[0])), amount);
                    } else {
                        items = new ItemStack(Objects.requireNonNull(Material.getMaterial(iname[0])), amount, Short.parseShort(iname[1]));
                    }
                } else {
                    items = new ItemStack(Objects.requireNonNull(Material.getMaterial(name)), amount);
                }
                p.getInventory().removeItem(items);
                Data.addStorage(p, name, amount);
                if (Objects.requireNonNull(getconfigfile().getString("Message.ADD.TYPE")).equalsIgnoreCase("ACTION_BAR")
                        || Objects.requireNonNull(getconfigfile().getString("Message.ADD.TYPE")).equalsIgnoreCase("CHAT")) {
                    if (Files.getconfigfile().getBoolean("Message.ADD.STATUS")) {
                        p.spigot().sendMessage(ChatMessageType.valueOf(Files.getconfigfile().getString("Message.ADD.TYPE")),
                                new TranslatableComponent(colorize(Objects.requireNonNull(getlanguagefile().getString("User.Add_Item"))
                                        .replaceAll("%item%", getName(name).replaceAll("_", " ").replaceAll("-", " "))
                                        .replaceAll("%amount%", String.valueOf(amount))
                                        .replaceAll("%storage%", String.format("%,d", getStorage(p, name)))
                                        .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name))))));
                    }
                } else {
                    if (Objects.requireNonNull(getconfigfile().getString("Message.ADD.TYPE")).equalsIgnoreCase("TITLE")) {
                        p.sendTitle(colorize(Objects.requireNonNull(getconfigfile().getString("Message.ADD.TITLE.TITLE"))
                                .replaceAll("%item%", getName(name).replaceAll("_", " ").replaceAll("-", " "))
                                .replaceAll("%amount%", String.valueOf(amount))
                                .replaceAll("%storage%", String.format("%,d", getStorage(p, name))))
                                .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name))), colorize(Objects.requireNonNull(getconfigfile().getString("Message.ADD.TITLE.SUBTITLE"))
                                .replaceAll("%item%", Items.getName(name).replaceAll("_", " ").replaceAll("-", " "))
                                .replaceAll("%amount%", name)
                                .replaceAll("%storage%", String.format("%,d", getStorage(p, name)))
                                .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name)))), getconfigfile().getInt("Message.ADD.TITLE.FADEIN"), getconfigfile().getInt("Message.ADD.TITLE.STAY"), getconfigfile().getInt("Message.ADD.TITLE.FADEOUT"));
                    }
                }
            } else {
                p.sendMessage(Files.colorize(Objects.requireNonNull(getlanguagefile().getString("User.Not_Enough"))
                        .replaceAll("%item%", String.valueOf(getAmountItem(p, name)))));
            }
        } else {
            p.sendMessage(Files.colorize(Files.getlanguagefile().getString("User.Not_Correct_Item")));
        }
    }

    public static void RemoveItems(Player p, String name, Integer amount) {
        name = name.toUpperCase();
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
                p.sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("User.Not_Enough"))
                        .replaceAll("%item%", String.valueOf(getStorage(p, name)))));
            }
        } else {
            p.sendMessage(colorize(getlanguagefile().getString("User.Not_Correct_Item")));
        }
    }

    public static String getName(@NotNull String name) {
        name = name.toUpperCase();
        return getconfigfile().getString("Blocks." + name + ".Name");
    }

    public static void SellItems(Player p, String name, Integer amount) {
        name = name.toUpperCase();
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
                p.sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("User.Not_Enough"))
                        .replaceAll("%item%", String.valueOf(getStorage(p, name)))));
            }
        } else {
            p.sendMessage(colorize(getlanguagefile().getString("User.Not_Correct_Item")));
        }
    }

    public static int getPrice(String m) {
        m = m.toUpperCase();
        return getconfigfile().getInt("Blocks." + m + ".Price");
    }

    public static int getAmountItem(Player p, String name) {
        name = name.toUpperCase();
        int amount = 0;
        NMSAssistant nmsAssistant = new NMSAssistant();
        if (!nmsAssistant.isVersionLessThan(13)) {
            for (ItemStack is : p.getInventory().getContents()) {
                if (is != null && is.getType().equals(Material.getMaterial(name)) && is.getDurability() == 0) {
                    amount = amount + is.getAmount();
                }
            }
        } else {
            String[] iname = name.split(";");
            short data = Short.parseShort(iname[1]);
            if (iname.length == 1) {
                for (ItemStack is : p.getInventory().getContents()) {
                    if (is != null && is.getType().equals(Material.getMaterial(name)) && is.getDurability() == 0) {
                        amount = amount + is.getAmount();
                    }
                }
            } else {
                for (ItemStack is : p.getInventory().getContents()) {
                    if (is != null && is.getType().equals(Material.getMaterial(iname[0])) && is.getDurability() == data) {
                        amount = amount + is.getAmount();
                    }
                }
            }
        }
        return amount;
    }

    public static int getAmountEmpty(Player p, String name) {
        name = name.toUpperCase();
        int EmptyAmount = 0;
        NMSAssistant nmsAssistant = new NMSAssistant();
        if (nmsAssistant.isVersionLessThan(13)) {
            String[] iname = name.split(";");
            if (iname.length == 1) {
                for (ItemStack i : p.getInventory().getStorageContents()) {
                    if (i != null) {
                        if (i.getType() == Material.getMaterial(name) && i.getAmount() != i.getMaxStackSize()) {
                            EmptyAmount += i.getMaxStackSize() - i.getAmount();
                        }
                    } else {
                        EmptyAmount += Objects.requireNonNull(Material.getMaterial(name)).getMaxStackSize();
                    }
                }
            } else {
                for (ItemStack i : p.getInventory().getStorageContents()) {
                    if (i != null) {
                        if (i.getType() == Material.getMaterial(iname[0]) && i.getDurability() == Short.parseShort(iname[1]) && i.getAmount() != i.getMaxStackSize()) {
                            EmptyAmount += i.getMaxStackSize() - i.getAmount();
                        }
                    } else {
                        ItemStack item = new ItemStack(Material.getMaterial(iname[0]));
                        item.setDurability(Short.parseShort(iname[1]));
                        EmptyAmount += item.getMaxStackSize();
                    }
                }
            }
        } else {
            for (ItemStack i : p.getInventory().getStorageContents()) {
                if (i != null) {
                    if (i.getType() == Material.getMaterial(name) && i.getAmount() != i.getMaxStackSize()) {
                        EmptyAmount += i.getMaxStackSize() - i.getAmount();
                    }
                } else {
                    EmptyAmount += Objects.requireNonNull(Material.getMaterial(name)).getMaxStackSize();
                }
            }
        }
        return EmptyAmount;
    }
}
