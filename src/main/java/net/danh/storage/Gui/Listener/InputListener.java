package net.danh.storage.Gui.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.danh.storage.Gui.CatchInput.InputAction;
import static net.danh.storage.Gui.CatchInput.block_input;
import static net.danh.storage.Gui.Loader.LoadMenu.cancel;
import static net.danh.storage.Manager.Files.*;

public class InputListener implements Listener {
    public static List<Player> input = new ArrayList<>();
    public static HashMap<Player, Object> input_result = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        if (input.contains(p)) {
            event.setCancelled(true);
            String message = event.getMessage();
            if (cancel.contains(message)) {
                input.remove(p);
                p.sendMessage(colorize(getlanguagefile().getString("Input.Cancel")));
            } else {
                if (getconfigfile().getString("Input.Type").equalsIgnoreCase("ONE")) {
                    if (message.contains(" ")) {
                        p.sendMessage(colorize(getlanguagefile().getString("Input.Space_Error")));
                        input.remove(p);
                    } else {
                        if (isInt(message)) {
                            if (Integer.parseInt(message) > 0) {
                                input_result.put(p, message);
                                InputAction(p, block_input.get(p));
                                input.remove(p);
                            } else {
                                p.sendMessage(colorize(getlanguagefile().getString("Input.Invaild_Number")));
                                input.remove(p);
                            }
                        } else {
                            p.sendMessage(colorize(getlanguagefile().getString("Input.Not_A_Number")));
                            input.remove(p);
                        }
                    }
                }
                if (getconfigfile().getString("Input.Type").equalsIgnoreCase("AGAIN")) {
                    if (message.contains(" ")) {
                        p.sendMessage(colorize(getlanguagefile().getString("Input.Space_Error") + " " + getlanguagefile().getString("Input.Again")));
                    } else {
                        if (isInt(message)) {
                            if (Integer.parseInt(message) > 0) {
                                input_result.put(p, message);
                                InputAction(p, block_input.get(p));
                                input.remove(p);
                            } else {
                                p.sendMessage(colorize(getlanguagefile().getString("Input.Invaild_Number") + " " + getlanguagefile().getString("Input.Again")));
                            }
                        } else {
                            p.sendMessage(colorize(getlanguagefile().getString("Input.Not_A_Number") + " " + getlanguagefile().getString("Input.Again")));
                        }
                    }
                }
            }
        }
    }
}
