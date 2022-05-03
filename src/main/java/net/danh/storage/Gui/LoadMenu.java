package net.danh.storage.Gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static net.danh.storage.Gui.OpenGui.gui;
import static net.danh.storage.Manager.Files.*;

public class LoadMenu {
    public static HashMap<Player, HashMap<String, HashMap<Boolean, ItemStack>>> pickup_buttons = new HashMap<>();
    public static int pickup_buttons_slot;
    public static HashMap<Player, HashMap<String, HashMap<Boolean, ItemStack>>> smelt_buttons = new HashMap<>();
    public static int smelt_buttons_slot;
    public static List<String> cancel = new ArrayList<>();
    public static HashMap<Player, Inventory> player_gui = new HashMap<Player, Inventory>();
    public static HashMap<Player, List<List<String>>> player_actions = new HashMap<Player, List<List<String>>>();
    public static HashMap<Player, List<Integer>> player_actions_slot = new HashMap<Player, List<Integer>>();
    public static HashMap<Player, List<String>> player_actions_block = new HashMap<Player, List<String>>();
    public static List<List<String>> actions = new ArrayList<List<String>>();
    public static List<String> actions_block = new ArrayList<String>();
    public static List<Integer> actions_slot = new ArrayList<Integer>();
    public static List<ItemStack> items = new ArrayList<ItemStack>();
    public static List<Integer> items_slot = new ArrayList<Integer>();
    public static List<ItemStack> converts = new ArrayList<ItemStack>();
    public static List<Integer> converts_slot = new ArrayList<Integer>();
    public static List<ItemStack> decorate = new ArrayList<ItemStack>();
    public static List<List<Integer>> decorate_slot = new ArrayList<List<Integer>>();
    public static List<Boolean> converts_status = new ArrayList<Boolean>();
    public static String tittle;
    public static int size;

    public static void LoadMenu(Player p) {
        LoadCancel();
        LoadItemActions();
        LoadDecorate(p);
        LoadItemsBlock(p);
        LoadItemsConvert(p);
        LoadPickup(p);
        LoadSmelt(p);
        try {
            tittle = colorize(papi(getguifile().getString("TITLE"), p));
        } catch (Exception e) {
            tittle = "Default tittle";
        }
        try {
            size = getguifile().getInt("ROWS") * 9;
        } catch (Exception e) {
            size = 54;
        }
    }

    public static void ReloadMenu() {
        decorate = new ArrayList<ItemStack>();
        decorate_slot = new ArrayList<List<Integer>>();
        items = new ArrayList<ItemStack>();
        items_slot = new ArrayList<Integer>();
        converts = new ArrayList<ItemStack>();
        converts_slot = new ArrayList<Integer>();
        converts_status = new ArrayList<Boolean>();
        actions = new ArrayList<List<String>>();
        actions_block = new ArrayList<String>();
        actions_slot = new ArrayList<Integer>();
    }

    public static void SaveMenu(Player p) {
        player_gui.put(p, gui);
        player_actions.put(p, actions);
        player_actions_slot.put(p, actions_slot);
        player_actions_block.put(p, actions_block);
    }

    public static void LoadCancel() {
        if (getconfigfile().getStringList("Input.Cancel_Character").isEmpty()) {
            cancel = new ArrayList<>();
            cancel.add(getconfigfile().getString("Input.Cancel_Character"));
        } else {
            cancel = getconfigfile().getStringList("Input.Cancel_Character");
        }
    }

    public static void LoadItemActions() {
        actions = new ArrayList<List<String>>();
        actions_block = new ArrayList<String>();
        actions_slot = new ArrayList<Integer>();
        Set<String> iactions = getguifile().getConfigurationSection("ITEMS.").getKeys(false);
        for (String key : iactions) {
            List<String> as = new ArrayList<String>();
            int slot = getguifile().getInt("ITEMS." + key + ".Slot");
            Set<String> action = getguifile().getConfigurationSection("ITEMS." + key + ".Actions.").getKeys(false);
            for (String type : action) {
                String a = getguifile().getString("ITEMS." + key + ".Actions." + type);
                if (a.equalsIgnoreCase("INPUT_TAKE") || a.equalsIgnoreCase("INPUT_ADD") || a.equalsIgnoreCase("INPUT_SELL") || a.equalsIgnoreCase("TAKE_ALL") || a.equalsIgnoreCase("ADD_ALL") || a.equalsIgnoreCase("SELL_ALL")) {
                    as.add(type + "`" + a);
                }
                if (type.equalsIgnoreCase("BLOCK")) {
                    actions_block.add(a);
                }
            }
            actions.add(as);
            actions_slot.add(slot);
        }
    }

    public static void LoadPickup(Player p) {
        HashMap<Boolean, ItemStack> auto_pickup = new HashMap<>();
        HashMap<String, Integer> auto_pickup_slot = new HashMap<>();
        Set<String> a = getguifile().getConfigurationSection("BUTTONS.Auto_Pickup.").getKeys(false);
        for (String pickup : a) {
            if (!pickup.equalsIgnoreCase("Slot")) {
                ItemStack item;
                Set<String> properties = getguifile().getConfigurationSection("BUTTONS.Auto_Pickup." + pickup + ".").getKeys(false);
                if (properties.contains("material")) {
                    int d = getguifile().getInt("BUTTONS.Auto_Pickup." + pickup + ".data");
                    short data = (short) d;
                    if (properties.contains("amount")) {
                        if (properties.contains("data")) {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".material")), getguifile().getInt("BUTTONS.Auto_Pickup." + pickup + ".amount"), data);
                        } else {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".material")), getguifile().getInt("BUTTONS.Auto_Pickup." + pickup + ".amount"));
                        }
                    } else {
                        if (properties.contains("data")) {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".material")), 1, data);
                        } else {
                            item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".material")), 1);
                        }
                    }
                    ItemMeta meta = item.getItemMeta();
                    if (properties.contains("name")) {
                        meta.setDisplayName(colorize(papi(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".name"), p)));
                        item.setItemMeta(meta);
                    }
                    if (properties.contains("lore")) {
                        List<String> lores = lorecolor(lorepapi(getguifile().getStringList("BUTTONS.Auto_Pickup." + pickup + ".lore"), p));
                        meta.setLore(lores);
                        item.setItemMeta(meta);
                    }
                } else {
                    item = new ItemStack(Material.AIR);
                    Bukkit.getLogger().warning("[Storage] The button auto_pickup (" + pickup + ") do not have material propertie");
                }
                auto_pickup.put(Boolean.parseBoolean(pickup), item);
                HashMap<String, HashMap<Boolean, ItemStack>> pick = new HashMap<>();
                pick.put("Pickup", auto_pickup);
                auto_pickup_slot.put("Pickup", getguifile().getInt("BUTTONS.Auto_Pickup.Slot"));
                pickup_buttons.put(p, pick);
            } else {
                pickup_buttons_slot = getguifile().getInt("BUTTONS.Auto_Pickup.Slot");
            }
        }
    }

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
