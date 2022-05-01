package net.danh.storage.Gui.Loader;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.danh.storage.Gui.Loader.LoadActions.LoadItemActions;
import static net.danh.storage.Gui.Loader.LoadAutoPickup.LoadPickup;
import static net.danh.storage.Gui.Loader.LoadAutoSmelt.LoadSmelt;
import static net.danh.storage.Gui.Loader.LoadBlocks.LoadItemsBlock;
import static net.danh.storage.Gui.Loader.LoadConverts.LoadItemsConvert;
import static net.danh.storage.Gui.Loader.LoadDecorate.LoadDecorate;
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
}
