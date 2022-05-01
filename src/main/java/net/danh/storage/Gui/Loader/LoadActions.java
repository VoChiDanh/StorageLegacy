package net.danh.storage.Gui.Loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.danh.storage.Gui.Loader.LoadMenu.*;
import static net.danh.storage.Manager.Files.getguifile;

public class LoadActions {
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
}
