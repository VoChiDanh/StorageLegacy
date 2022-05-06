package net.danh.storage.Gui;

import org.bukkit.entity.Player;

import java.util.HashMap;

import static net.danh.storage.Gui.GuiEventListener.input;
import static net.danh.storage.Gui.GuiEventListener.input_result;
import static net.danh.storage.Manager.Files.colorize;
import static net.danh.storage.Manager.Files.getlanguagefile;

public class CatchInput {
    private static final HashMap<Player, String> type_input = new HashMap<>();
    public static HashMap<Player, String> block_input = new HashMap<>();

    public static void InputCatch(Player player, String type, String block) {
        type_input.put(player, type);
        block_input.put(player, block);
        if (!input.contains(player)) {
            input.add(player);
            player.sendMessage(colorize(getlanguagefile().getString("Input.Start")));
        }
    }

    public static void InputAction(Player p, String block) {
        String type = type_input.get(p);
        String input = (String) input_result.get(p);
        if (type.equalsIgnoreCase("take")) {
            p.performCommand("storage take " + block + " " + input);
        }
        if (type.equalsIgnoreCase("add")) {
            p.performCommand("storage add " + block + " " + input);
        }
        if (type.equalsIgnoreCase("sell")) {
            p.performCommand("storage sell " + block + " " + input);
        }
    }
}
