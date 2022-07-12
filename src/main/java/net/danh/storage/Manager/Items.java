package net.danh.storage.Manager;

import net.danh.dcore.NMS.NMSAssistant;
import net.danh.storage.Storage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.danh.dcore.Utils.Chat.colorize;
import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Files.getconfigfile;
import static net.danh.storage.Manager.Files.getlanguagefile;
import static net.danh.storage.Storage.economy;

public class Items {

    private static int price;
    private static String block;

    /**
     * @param p      Player
     * @param name   Material
     * @param amount Amount
     */
    public static void AddItems(Player p, String name, Integer amount) {
        if (Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks")).getKeys(false).contains(name)) {
            String convert_name = getconfigfile().getString("Blocks." + name.toUpperCase() + ".Convert").toUpperCase();
            NMSAssistant nmsAssistant = new NMSAssistant();
            ItemStack checkitems;
            if (Data.autoSmelt(p)) {
                if (nmsAssistant.isVersionLessThanOrEqualTo(12)) {
                    String[] iname = convert_name.split(";");
                    if (iname.length == 1) {
                        checkitems = new ItemStack(Objects.requireNonNull(Material.getMaterial(iname[0])), amount);
                    } else {
                        checkitems = new ItemStack(Objects.requireNonNull(Material.getMaterial(iname[0])), amount, Short.parseShort(iname[1]));
                    }
                } else {
                    checkitems = new ItemStack(Objects.requireNonNull(Material.getMaterial(convert_name)), amount);
                }
            } else {
                if (name.equalsIgnoreCase("STONE")) {
                    name = "COBBLESTONE";
                }
                if (nmsAssistant.isVersionLessThanOrEqualTo(12)) {
                    String[] iname = name.split(";");
                    if (iname.length > 1) {
                        checkitems = new ItemStack(Objects.requireNonNull(Material.getMaterial(iname[0])), amount, Short.parseShort(iname[1]));
                    } else {
                        checkitems = new ItemStack(Objects.requireNonNull(Material.getMaterial(name)), amount);
                    }
                } else {
                    checkitems = new ItemStack(Objects.requireNonNull(Material.getMaterial(name)), amount);
                }
            }
            if (getPlayerAmount(p, checkitems) >= amount) {
                removeItems(p, checkitems, amount);
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
                        if (nmsAssistant.isVersionGreaterThanOrEqualTo(11)) {
                            p.sendTitle(colorize(Objects.requireNonNull(getconfigfile().getString("Message.ADD.TITLE.TITLE"))
                                    .replaceAll("%item%", getName(name).replaceAll("_", " ").replaceAll("-", " "))
                                    .replaceAll("%amount%", String.valueOf(amount))
                                    .replaceAll("%storage%", String.format("%,d", getStorage(p, name))))
                                    .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name))), colorize(Objects.requireNonNull(getconfigfile().getString("Message.ADD.TITLE.SUBTITLE"))
                                    .replaceAll("%item%", Items.getName(name).replaceAll("_", " ").replaceAll("-", " "))
                                    .replaceAll("%amount%", name)
                                    .replaceAll("%storage%", String.format("%,d", getStorage(p, name)))
                                    .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name)))), getconfigfile().getInt("Message.ADD.TITLE.FADEIN"), getconfigfile().getInt("Message.ADD.TITLE.STAY"), getconfigfile().getInt("Message.ADD.TITLE.FADEOUT"));
                        } else {
                            p.sendTitle(colorize(Objects.requireNonNull(getconfigfile().getString("Message.ADD.TITLE.TITLE"))
                                    .replaceAll("%item%", getName(name).replaceAll("_", " ").replaceAll("-", " "))
                                    .replaceAll("%amount%", String.valueOf(amount))
                                    .replaceAll("%storage%", String.format("%,d", getStorage(p, name))))
                                    .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name))), colorize(Objects.requireNonNull(getconfigfile().getString("Message.ADD.TITLE.SUBTITLE"))
                                    .replaceAll("%item%", Items.getName(name).replaceAll("_", " ").replaceAll("-", " "))
                                    .replaceAll("%amount%", name)
                                    .replaceAll("%storage%", String.format("%,d", getStorage(p, name)))
                                    .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name)))));
                        }
                    }
                }
            } else {
                p.sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("User.Not_Enough"))
                        .replaceAll("%item%", String.valueOf(getAmountItem(p, name)))));
            }
        } else {
            p.sendMessage(colorize(Files.getlanguagefile().getString("User.Not_Correct_Item")));
        }
    }

    /**
     * @param p      Player
     * @param name   Material
     * @param amount Amount
     */
    public static void RemoveItems(Player p, String name, Integer amount) {
        name = name.toUpperCase();
        if (Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks")).getKeys(false).contains(name)) {
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
                        NMSAssistant nms = new NMSAssistant();
                        if (nms.isVersionGreaterThanOrEqualTo(11)) {
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
                        } else {
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
                                    .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name)))));
                        }
                    }
                }
                if (autoSmelt(p)) {
                    if (Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks." + name)).getKeys(false).contains("Convert")) {
                        name = getconfigfile().getString("Blocks." + name + ".Convert");
                    }
                } else {
                    name = getconfigfile().getString("Blocks." + name + ".Block");
                }
                NMSAssistant nmsAssistant = new NMSAssistant();
                if (nmsAssistant.isVersionLessThanOrEqualTo(12)) {
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
                    ItemStack items = new ItemStack(Objects.requireNonNull(Material.getMaterial(name)), amount);
                    p.getInventory().addItem(items);
                }
            } else {
                p.sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("User.Not_Enough"))
                        .replaceAll("%item%", String.valueOf(getPlayerAmount(p, new ItemStack(Material.getMaterial(name), amount))))));
            }
        } else {
            p.sendMessage(colorize(getlanguagefile().getString("User.Not_Correct_Item")));
        }
    }

    /**
     * @param name Material
     * @return Other name ?
     */

    public static String getName(@NotNull String name) {
        name = name.toUpperCase();
        return getconfigfile().getString("Blocks." + name + ".Name");
    }

    public static int getPlayerAmount(HumanEntity player, ItemStack item) {
        final PlayerInventory inv = player.getInventory();
        final ItemStack[] items = inv.getContents();
        int c = 0;
        for (final ItemStack is : items) {
            if (is != null) {
                if (is.isSimilar(item)) {
                    c += is.getAmount();
                }
            }
        }
        return c;
    }

    public static void removeItems(Player player, ItemStack item, long amount) {
        item = item.clone();
        final PlayerInventory inv = player.getInventory();
        final ItemStack[] items = inv.getContents();
        int c = 0;
        for (int i = 0; i < items.length; ++i) {
            final ItemStack is = items[i];
            if (is != null) {
                if (is.isSimilar(item)) {
                    if (c + is.getAmount() > amount) {
                        final long canDelete = amount - c;
                        is.setAmount((int) (is.getAmount() - canDelete));
                        items[i] = is;
                        break;
                    }
                    c += is.getAmount();
                    items[i] = null;
                }
            }
        }
        inv.setContents(items);
        player.updateInventory();
    }

    /**
     * @param p      Player
     * @param name   Material
     * @param amount Amount
     */
    public static void SellItems(Player p, String name, Integer amount) {
        name = name.toUpperCase();
        if (Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks")).getKeys(false).contains(name)) {
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
                if (Files.getconfigfile().getString("Economy").equalsIgnoreCase(Economy.Vault.name())) {
                    economy.depositPlayer(p, money);
                } else if (Files.getconfigfile().getString("Economy").equalsIgnoreCase(Economy.TokenManger.name())) {
                    Storage.get().getTmAPI().addTokens(p.getName(), money);
                } else if (Files.getconfigfile().getString("Economy").equalsIgnoreCase(Economy.PlayerPoints.name())) {
                    Storage.get().getPpAPI().give(p.getUniqueId(), money);
                }
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
                        NMSAssistant nms = new NMSAssistant();
                        if (nms.isVersionGreaterThanOrEqualTo(11)) {
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
                    } else {
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
                                .replaceAll("%max%", String.format("%,d", getMaxStorage(p, name)))));
                    }
                }
            } else {
                p.sendMessage(colorize(Objects.requireNonNull(getlanguagefile().getString("User.Not_Enough"))
                        .replaceAll("%item%", String.valueOf(getStorage(p, name)))));
            }
        } else {
            p.sendMessage(colorize(getlanguagefile().getString("User.Not_Correct_Item")));
        }
    }

    /**
     * @param m Material
     * @return Price of material
     */
    public static int getPrice(String m) {
        m = m.toUpperCase();
        return getconfigfile().getInt("Blocks." + m + ".Price");
    }

    /**
     * @param p    Player
     * @param name Material
     * @return Amount of item in player's storage
     */
    public static int getAmountItem(Player p, String name) {
        if (Data.autoSmelt(p)) {
            name = getconfigfile().getString("Blocks." + name.toUpperCase() + ".Convert").toUpperCase();
            int amount;
            String[] iname = name.split(";");
            if (iname.length == 1) {
                amount = getPlayerAmount(p, new ItemStack(Material.getMaterial(name)));
            } else {
                short data = Short.parseShort(iname[1]);
                ItemStack item = new ItemStack(Material.getMaterial(name));
                item.setDurability(data);
                amount = getPlayerAmount(p, item);
            }
            return amount;
        } else {
            name = name.toUpperCase();
            if (name.equalsIgnoreCase("STONE")) {
                name = "COBBLESTONE".toUpperCase();
            }
            int amount;
            String[] iname = name.split(";");
            if (iname.length == 1) {
                amount = getPlayerAmount(p, new ItemStack(Material.getMaterial(name)));
            } else {
                short data = Short.parseShort(iname[1]);
                ItemStack item = new ItemStack(Material.getMaterial(name));
                item.setDurability(data);
                amount = getPlayerAmount(p, item);
            }
            return amount;
        }
    }

    /**
     * @param p    Player
     * @param name Material
     * @return Amount of empty item in player's storage
     */
    public static int getAmountEmpty(Player p, String name) {
        if (Data.autoSmelt(p)) {
            name = getconfigfile().getString("Blocks." + name.toUpperCase() + ".Convert").toUpperCase();
            int EmptyAmount = 0;
            NMSAssistant nmsAssistant = new NMSAssistant();
            if (nmsAssistant.isVersionLessThanOrEqualTo(12)) {
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
        } else {
            name = name.toUpperCase();
            int EmptyAmount = 0;
            NMSAssistant nmsAssistant = new NMSAssistant();
            if (nmsAssistant.isVersionLessThanOrEqualTo(12)) {
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
}
