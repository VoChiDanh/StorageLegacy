package net.danh.storage.Gui;

import net.danh.dcore.NMS.NMSAssistant;
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
    public static HashMap<String, Long> cooldown_time = new HashMap<>();
    public static HashMap<Player, HashMap<String, HashMap<Boolean, ItemStack>>> pickup_buttons_cooldown = new HashMap<>();
    public static HashMap<Player, HashMap<String, HashMap<Boolean, ItemStack>>> smelt_buttons_cooldown = new HashMap<>();
    public static HashMap<String, Boolean> update_buttons = new HashMap<>();
    public static List<Boolean> update_decorate = new ArrayList<>();
    public static List<Boolean> update_items_block = new ArrayList<>();
    public static List<Boolean> update_items_convert = new ArrayList<>();
    public static HashMap<Player, HashMap<String, HashMap<Boolean, ItemStack>>> pickup_buttons = new HashMap<>();
    public static int pickup_buttons_slot;
    public static HashMap<Player, HashMap<String, HashMap<Boolean, ItemStack>>> smelt_buttons = new HashMap<>();
    public static int smelt_buttons_slot;
    public static List<String> cancel = new ArrayList<>();
    public static HashMap<Player, Inventory> player_gui = new HashMap<>();
    public static HashMap<Player, List<List<String>>> player_actions = new HashMap<>();
    public static HashMap<Player, List<Integer>> player_actions_slot = new HashMap<>();
    public static HashMap<Player, List<String>> player_actions_block = new HashMap<>();
    public static List<List<String>> actions = new ArrayList<>();
    public static List<String> actions_block = new ArrayList<>();
    public static List<Integer> actions_slot = new ArrayList<>();
    public static List<ItemStack> items = new ArrayList<>();
    public static List<Integer> items_slot = new ArrayList<>();
    public static List<ItemStack> converts = new ArrayList<>();
    public static List<Integer> converts_slot = new ArrayList<>();
    public static List<ItemStack> decorate = new ArrayList<>();
    public static List<List<Integer>> decorate_slot = new ArrayList<>();
    public static List<Boolean> converts_status = new ArrayList<>();
    public static String tittle;
    public static int size;

    public static void LoadMenuGui(Player p) {
        LoadCancel();
        LoadItemActions();
        LoadDecorate(p);
        LoadItemsBlock(p);
        LoadItemsConvert(p);
        LoadPickup(p);
        LoadSmelt(p);
        LoadPickupCoolDown(p);
        LoadSmeltCoolDown(p);
        try {
            tittle = colorize(papi(getguifile().getString("TITLE"), p));
        } catch (Exception e) {
            tittle = "Default tittle";
        }
        try {
            if (getguifile().getInt("ROWS") > 0) {
                if (getguifile().getInt("ROWS") <= 6) {
                    size = getguifile().getInt("ROWS") * 9;
                } else {
                    size = 54;
                }
            } else {
                size = 54;
            }
        } catch (Exception e) {
            size = 54;
        }
    }

    public static void ReloadMenu() {
        pickup_buttons_cooldown = new HashMap<>();
        smelt_buttons_cooldown = new HashMap<>();
        update_decorate = new ArrayList<>();
        update_items_block = new ArrayList<>();
        update_items_convert = new ArrayList<>();
        decorate = new ArrayList<>();
        decorate_slot = new ArrayList<>();
        items = new ArrayList<>();
        items_slot = new ArrayList<>();
        converts = new ArrayList<>();
        converts_slot = new ArrayList<>();
        converts_status = new ArrayList<>();
        actions = new ArrayList<>();
        actions_block = new ArrayList<>();
        actions_slot = new ArrayList<>();
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
        actions = new ArrayList<>();
        actions_block = new ArrayList<>();
        actions_slot = new ArrayList<>();
        Set<String> iactions = getguifile().getConfigurationSection("ITEMS.").getKeys(false);
        for (String key : iactions) {
            List<String> as = new ArrayList<>();
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
        Set<String> a = getguifile().getConfigurationSection("BUTTONS.Auto_Pickup.").getKeys(false);
        for (String pickup : a) {
            if (a.contains("true") || a.contains("false")) {
                if (pickup.equalsIgnoreCase("true") || pickup.equalsIgnoreCase("false")) {
                    ItemStack item;
                    Set<String> properties = getguifile().getConfigurationSection("BUTTONS.Auto_Pickup." + pickup + ".Block.").getKeys(false);
                    if (properties.contains("material")) {
                        int d = getguifile().getInt("BUTTONS.Auto_Pickup." + pickup + ".Block.data");
                        short data = (short) d;
                        if (properties.contains("amount")) {
                            if (properties.contains("data")) {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".Block.material")), getguifile().getInt("BUTTONS.Auto_Pickup." + pickup + ".Block.amount"), data);
                            } else {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".Block.material")), getguifile().getInt("BUTTONS.Auto_Pickup." + pickup + ".Block.amount"));
                            }
                        } else {
                            if (properties.contains("data")) {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".Block.material")), 1, data);
                            } else {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".Block.material")), 1);
                            }
                        }
                        ItemMeta meta = item.getItemMeta();
                        if (properties.contains("name")) {
                            meta.setDisplayName(colorize(papi(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".Block.name"), p)));
                            item.setItemMeta(meta);
                        }
                        if (properties.contains("lore")) {
                            List<String> lores = lorecolor(lorepapi(getguifile().getStringList("BUTTONS.Auto_Pickup." + pickup + ".Block.lore"), p));
                            meta.setLore(lores);
                            item.setItemMeta(meta);
                        }
                    } else {
                        item = new ItemStack(Material.AIR);
                        Bukkit.getLogger().warning("[Storage] The button auto_pickup icon block (" + pickup + ") do not have material propertie");
                    }
                    auto_pickup.put(Boolean.parseBoolean(pickup), item);
                    HashMap<String, HashMap<Boolean, ItemStack>> pick = new HashMap<>();
                    pick.put("Pickup", auto_pickup);
                    pickup_buttons.put(p, pick);
                }
            }
            if (a.contains("Slot")) {
                if (pickup.equalsIgnoreCase("Slot")) {
                    pickup_buttons_slot = getguifile().getInt("BUTTONS.Auto_Pickup.Slot");
                }
            }
            if (a.contains("Update")) {
                if (pickup.equalsIgnoreCase("Update")) {
                    if (getguifile().getBoolean("BUTTONS.Auto_Pickup.Update")) {
                        update_buttons.put("Pickup", true);
                    } else {
                        update_buttons.put("Pickup", false);
                    }
                }
            }
            if (a.contains("Cooldown")) {
                cooldown_time.put("Pickup", getguifile().getLong("BUTTONS.Auto_Pickup.Cooldown"));
            }
        }
    }

    public static void LoadPickupCoolDown(Player p) {
        HashMap<Boolean, ItemStack> auto_pickup = new HashMap<>();
        Set<String> a = getguifile().getConfigurationSection("BUTTONS.Auto_Pickup.").getKeys(false);
        for (String pickup : a) {
            if (a.contains("true") || a.contains("false")) {
                if (pickup.equalsIgnoreCase("true") || pickup.equalsIgnoreCase("false")) {
                    ItemStack item;
                    Set<String> properties = getguifile().getConfigurationSection("BUTTONS.Auto_Pickup." + pickup + ".Cooldown.").getKeys(false);
                    if (properties.contains("material")) {
                        int d = getguifile().getInt("BUTTONS.Auto_Pickup." + pickup + ".Cooldown.data");
                        short data = (short) d;
                        if (properties.contains("amount")) {
                            if (properties.contains("data")) {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".Cooldown.material")), getguifile().getInt("BUTTONS.Auto_Pickup." + pickup + ".Cooldown.amount"), data);
                            } else {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".Cooldown.material")), getguifile().getInt("BUTTONS.Auto_Pickup." + pickup + ".Cooldown.amount"));
                            }
                        } else {
                            if (properties.contains("data")) {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".Cooldown.material")), 1, data);
                            } else {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".Cooldown.material")), 1);
                            }
                        }
                        ItemMeta meta = item.getItemMeta();
                        if (properties.contains("name")) {
                            meta.setDisplayName(colorize(papi(getguifile().getString("BUTTONS.Auto_Pickup." + pickup + ".Cooldown.name"), p)));
                            item.setItemMeta(meta);
                        }
                        if (properties.contains("lore")) {
                            List<String> lores = lorecolor(lorepapi(getguifile().getStringList("BUTTONS.Auto_Pickup." + pickup + ".Cooldown.lore"), p));
                            meta.setLore(lores);
                            item.setItemMeta(meta);
                        }
                    } else {
                        item = new ItemStack(Material.AIR);
                        Bukkit.getLogger().warning("[Storage] The button auto_pickup icon cooldown (" + pickup + ") do not have material propertie");
                    }
                    auto_pickup.put(Boolean.parseBoolean(pickup), item);
                    HashMap<String, HashMap<Boolean, ItemStack>> pick = new HashMap<>();
                    pick.put("Pickup", auto_pickup);
                    pickup_buttons_cooldown.put(p, pick);
                }
            }
        }
    }

    public static void LoadSmelt(Player p) {
        HashMap<Boolean, ItemStack> auto_smelt = new HashMap<>();
        Set<String> a = getguifile().getConfigurationSection("BUTTONS.Auto_Smelt.").getKeys(false);
        for (String pickup : a) {
            if (a.contains("true") || a.contains("false")) {
                if (pickup.equalsIgnoreCase("true") || pickup.equalsIgnoreCase("false")) {
                    ItemStack item;
                    Set<String> properties = getguifile().getConfigurationSection("BUTTONS.Auto_Smelt." + pickup + ".Block.").getKeys(false);
                    if (properties.contains("material")) {
                        int d = getguifile().getInt("BUTTONS.Auto_Smelt." + pickup + ".Block.data");
                        short data = (short) d;
                        if (properties.contains("amount")) {
                            if (properties.contains("data")) {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Smelt." + pickup + ".Block.material")), getguifile().getInt("BUTTONS.Auto_Smelt." + pickup + ".Block.amount"), data);
                            } else {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Smelt." + pickup + ".Block.material")), getguifile().getInt("BUTTONS.Auto_Smelt." + pickup + ".Block.amount"));
                            }
                        } else {
                            if (properties.contains("data")) {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Smelt." + pickup + ".Block.material")), 1, data);
                            } else {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Smelt." + pickup + ".Block.material")), 1);
                            }
                        }
                        ItemMeta meta = item.getItemMeta();
                        if (properties.contains("name")) {
                            meta.setDisplayName(colorize(papi(getguifile().getString("BUTTONS.Auto_Smelt." + pickup + ".Block.name"), p)));
                            item.setItemMeta(meta);
                        }
                        if (properties.contains("lore")) {
                            List<String> lores = lorecolor(lorepapi(getguifile().getStringList("BUTTONS.Auto_Smelt." + pickup + ".Block.lore"), p));
                            meta.setLore(lores);
                            item.setItemMeta(meta);
                        }
                    } else {
                        item = new ItemStack(Material.AIR);
                        Bukkit.getLogger().warning("[Storage] The button auto_smelt icon block (" + pickup + ") do not have material propertie");
                    }
                    auto_smelt.put(Boolean.parseBoolean(pickup), item);
                    HashMap<String, HashMap<Boolean, ItemStack>> smelt = new HashMap<>();
                    smelt.put("Smelt", auto_smelt);
                    smelt_buttons.put(p, smelt);
                }
            }
            if (a.contains("Slot")) {
                if (pickup.equalsIgnoreCase("Slot")) {
                    smelt_buttons_slot = getguifile().getInt("BUTTONS.Auto_Smelt.Slot");
                }
            }
            if (a.contains("Update")) {
                if (pickup.equalsIgnoreCase("Update")) {
                    if (getguifile().getBoolean("BUTTONS.Auto_Smelt.Update")) {
                        update_buttons.put("Smelt", true);
                    } else {
                        update_buttons.put("Smelt", false);
                    }
                }
            }
            if (a.contains("Cooldown")) {
                cooldown_time.put("Smelt", getguifile().getLong("BUTTONS.Auto_Smelt.Cooldown"));
            }
        }
    }

    public static void LoadSmeltCoolDown(Player p) {
        HashMap<Boolean, ItemStack> auto_smelt = new HashMap<>();
        Set<String> a = getguifile().getConfigurationSection("BUTTONS.Auto_Smelt.").getKeys(false);
        for (String pickup : a) {
            if (a.contains("true") || a.contains("false")) {
                if (pickup.equalsIgnoreCase("true") || pickup.equalsIgnoreCase("false")) {
                    ItemStack item;
                    Set<String> properties = getguifile().getConfigurationSection("BUTTONS.Auto_Smelt." + pickup + ".Cooldown.").getKeys(false);
                    if (properties.contains("material")) {
                        int d = getguifile().getInt("BUTTONS.Auto_Smelt." + pickup + ".Cooldown.data");
                        short data = (short) d;
                        if (properties.contains("amount")) {
                            if (properties.contains("data")) {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Smelt." + pickup + ".Cooldown.material")), getguifile().getInt("BUTTONS.Auto_Smelt." + pickup + ".Cooldown.amount"), data);
                            } else {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Smelt." + pickup + ".Cooldown.material")), getguifile().getInt("BUTTONS.Auto_Smelt." + pickup + ".Cooldown.amount"));
                            }
                        } else {
                            if (properties.contains("data")) {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Smelt." + pickup + ".Cooldown.material")), 1, data);
                            } else {
                                item = new ItemStack(Material.matchMaterial(getguifile().getString("BUTTONS.Auto_Smelt." + pickup + ".Cooldown.material")), 1);
                            }
                        }
                        ItemMeta meta = item.getItemMeta();
                        if (properties.contains("name")) {
                            meta.setDisplayName(colorize(papi(getguifile().getString("BUTTONS.Auto_Smelt." + pickup + ".Cooldown.name"), p)));
                            item.setItemMeta(meta);
                        }
                        if (properties.contains("lore")) {
                            List<String> lores = lorecolor(lorepapi(getguifile().getStringList("BUTTONS.Auto_Smelt." + pickup + ".Cooldown.lore"), p));
                            meta.setLore(lores);
                            item.setItemMeta(meta);
                        }
                    } else {
                        item = new ItemStack(Material.AIR);
                        Bukkit.getLogger().warning("[Storage] The button auto_smelt icon cooldown (" + pickup + ") do not have material propertie");
                    }
                    auto_smelt.put(Boolean.parseBoolean(pickup), item);
                    HashMap<String, HashMap<Boolean, ItemStack>> smelt = new HashMap<>();
                    smelt.put("Smelt", auto_smelt);
                    smelt_buttons_cooldown.put(p, smelt);
                }
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
                if (properties.contains("update")) {
                    if (getguifile().getBoolean("ITEMS." + key + ".Block.update")) {
                        update_items_block.add(true);
                    } else {
                        update_items_block.add(false);
                    }
                } else {
                    update_items_block.add(false);
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
                    if (properties.contains("update")) {
                        if (getguifile().getBoolean("ITEMS." + key + ".Convert.update")) {
                            update_items_convert.add(true);
                        } else {
                            update_items_convert.add(false);
                        }
                    } else {
                        update_items_convert.add(false);
                    }
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
            }
        }
    }

    public static void LoadDecorate(Player p) {
        Set<String> ditems = getguifile().getConfigurationSection("DECORATES.").getKeys(false);
        NMSAssistant nmsAssistant = new NMSAssistant();
        ItemStack item;
        for (String key : ditems) {
            Set<String> properties = getguifile().getConfigurationSection("DECORATES." + key + ".").getKeys(false);
            if (properties.contains("material")) {
                int d = getguifile().getInt("DECORATES." + key + ".data");
                short data = (short) d;
                if (nmsAssistant.isVersionLessThanOrEqualTo(12)) {
                    item = new ItemStack(Material.matchMaterial(getguifile().getString("DECORATES." + key + ".material")), getguifile().getInt("DECORATES." + key + ".amount"), data);
                } else {
                    item = new ItemStack(Material.matchMaterial(getguifile().getString("DECORATES." + key + ".material")), getguifile().getInt("DECORATES." + key + ".amount"));

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
                    List<Integer> input = new ArrayList<>();
                    input.add(getguifile().getInt("DECORATES." + key + ".slot"));
                    decorate_slot.add(input);
                } else {
                    decorate_slot.add(getguifile().getIntegerList("DECORATES." + key + ".slot"));
                }
            } else {
                decorate_slot.add(null);
                Bukkit.getLogger().warning("[Storage] The decorate item (" + key + ") do not have slot propertie");
            }
            if (properties.contains("update")) {
                if (getguifile().getBoolean("DECORATES." + key + ".update")) {
                    update_decorate.add(true);
                } else {
                    update_decorate.add(false);
                }
            } else {
                update_decorate.add(false);
            }
        }
    }
}
