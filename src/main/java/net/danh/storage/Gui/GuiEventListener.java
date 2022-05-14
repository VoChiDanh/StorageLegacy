package net.danh.storage.Gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.danh.dcore.Utils.Player.sendPlayerMessage;
import static net.danh.storage.Gui.CatchInput.*;
import static net.danh.storage.Gui.LoadMenu.*;
import static net.danh.storage.Gui.Manager.*;
import static net.danh.storage.Gui.OpenGui.gui;
import static net.danh.storage.Manager.Data.*;
import static net.danh.storage.Manager.Files.*;
import static net.danh.storage.Storage.get;

public class GuiEventListener implements Listener {
    public static Player gplayer;
    public static List<Player> input = new ArrayList<>();
    public static HashMap<Player, Object> input_result = new HashMap<>();
    private String block;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        if (input.contains(p)) {
            event.setCancelled(true);
            String message = event.getMessage();
            if (cancel.contains(message)) {
                input.remove(p);
                sendPlayerMessage(p, getlanguagefile().getString("Input.Cancel"));
            } else {
                if (getconfigfile().getString("Input.Type").equalsIgnoreCase("ONE")) {
                    if (message.contains(" ")) {
                        sendPlayerMessage(p, getlanguagefile().getString("Input.Space_Error"));
                        input.remove(p);
                    } else {
                        if (isInt(message)) {
                            try {
                                if (Integer.parseInt(message) > 0) {
                                    input_result.put(p, message);
                                    InputAction(p, block_input.get(p));
                                    input.remove(p);
                                } else {
                                    sendPlayerMessage(p, getlanguagefile().getString("Input.Invaild_Number"));
                                    input.remove(p);
                                }
                            } catch (Exception e) {
                                sendPlayerMessage(p, getlanguagefile().getString("Number_To_Big"));
                                input.remove(p);
                            }
                        } else {
                            sendPlayerMessage(p, getlanguagefile().getString("Input.Not_A_Number"));
                            input.remove(p);
                        }
                    }
                }
                if (getconfigfile().getString("Input.Type").equalsIgnoreCase("AGAIN")) {
                    if (message.contains(" ")) {
                        sendPlayerMessage(p, getlanguagefile().getString("Input.Space_Error") + " " + getlanguagefile().getString("Input.Again"));
                    } else {
                        if (isInt(message)) {
                            try {
                                if (Integer.parseInt(message) > 0) {
                                    input_result.put(p, message);
                                    InputAction(p, block_input.get(p));
                                    input.remove(p);
                                } else {
                                    sendPlayerMessage(p, getlanguagefile().getString("Input.Invaild_Number") + " " + getlanguagefile().getString("Input.Again"));
                                }
                            } catch (Exception e) {
                                sendPlayerMessage(p, getlanguagefile().getString("Number_To_Big"));
                            }
                        } else {
                            sendPlayerMessage(p, getlanguagefile().getString("Input.Not_A_Number") + " " + getlanguagefile().getString("Input.Again"));
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClickInv(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        if (!event.getInventory().equals(gui) || !event.getInventory().equals(player_gui.get(p))) return;
        if (event.getClickedInventory() == null) return;
        else {
            if (event.getClickedInventory().equals(player_gui.get(p))) {
                if (event.getSlot() == pickup_buttons_slot) {
                    if (pickup_cooldown.containsKey(p)) {
                        if (getpickupcooldown(p) == 0) {
                            startpickupcooldown(p, 1000L * cooldown_time.get("Pickup"));
                            setautoPick(p, !autoPick(p));
                            InstantUpdate(p);
                            p.updateInventory();
                        } else {
                            event.setCancelled(true);
                        }
                    } else {
                        startpickupcooldown(p, 1000L * cooldown_time.get("Pickup"));
                        setautoPick(p, !autoPick(p));
                        InstantUpdate(p);
                        p.updateInventory();
                    }
                }
                if (event.getSlot() == smelt_buttons_slot) {
                    if (smelt_cooldown.containsKey(p)) {
                        if (getsmeltcooldown(p) == 0) {
                            startsmeltcooldown(p, 1000L * cooldown_time.get("Smelt"));
                            setautoSmelt(p, !autoSmelt(p));
                            InstantUpdate(p);
                            p.updateInventory();
                        } else {
                            event.setCancelled(true);
                        }
                    } else {
                        startsmeltcooldown(p, 1000L * cooldown_time.get("Smelt"));
                        setautoSmelt(p, !autoSmelt(p));
                        InstantUpdate(p);
                        p.updateInventory();
                    }
                }
            }
            int i = 0;
            for (List<String> a : player_actions.get(p)) {
                if (event.getClickedInventory().equals(player_gui.get(p)) && event.getSlot() == player_actions_slot.get(p).get(i)) {
                    for (String s : a) {
                        String[] action = s.split("`");
                        String click = event.getClick().toString();
                        if (click.equalsIgnoreCase(action[0])) {
                            gplayer = p;
                            event.setCancelled(true);
                            List<String> ab = player_actions_block.get(p);
                            block = ab.get(i);
                            if (action[1].equalsIgnoreCase("TAKE_ALL")) {
                                new BukkitRunnable() {
                                    public void run() {
                                        p.closeInventory();
                                    }
                                }.runTaskLater(get(), 1);
                                new BukkitRunnable() {
                                    public void run() {
                                        p.performCommand("storage take " + block + " all");
                                    }
                                }.runTaskLater(get(), 2);
                            }
                            if (action[1].equalsIgnoreCase("ADD_ALL")) {
                                new BukkitRunnable() {
                                    public void run() {
                                        p.closeInventory();
                                    }
                                }.runTaskLater(get(), 1);
                                new BukkitRunnable() {
                                    public void run() {
                                        p.performCommand("storage add " + block + " all");
                                    }
                                }.runTaskLater(get(), 2);
                            }
                            if (action[1].equalsIgnoreCase("SELL_ALL")) {
                                new BukkitRunnable() {
                                    public void run() {
                                        p.closeInventory();
                                    }
                                }.runTaskLater(get(), 1);
                                new BukkitRunnable() {
                                    public void run() {
                                        p.performCommand("storage sell " + block + " all");
                                    }
                                }.runTaskLater(get(), 2);
                            }
                            if (action[1].equalsIgnoreCase("INPUT_TAKE")) {
                                new BukkitRunnable() {
                                    public void run() {
                                        p.closeInventory();
                                    }
                                }.runTaskLater(get(), 1);
                                new BukkitRunnable() {
                                    public void run() {
                                        InputCatch(p, "take", block);
                                    }
                                }.runTaskLater(get(), 2);
                            }
                            if (action[1].equalsIgnoreCase("INPUT_ADD")) {
                                new BukkitRunnable() {
                                    public void run() {
                                        p.closeInventory();
                                    }
                                }.runTaskLater(get(), 1);
                                new BukkitRunnable() {
                                    public void run() {
                                        InputCatch(p, "add", block);
                                    }
                                }.runTaskLater(get(), 2);
                            }
                            if (action[1].equalsIgnoreCase("INPUT_SELL")) {
                                new BukkitRunnable() {
                                    public void run() {
                                        p.closeInventory();
                                    }
                                }.runTaskLater(get(), 1);
                                new BukkitRunnable() {
                                    public void run() {
                                        InputCatch(p, "sell", block);
                                    }
                                }.runTaskLater(get(), 2);
                            }
                        }
                    }
                }
                i++;
            }
        }
        event.setCancelled(true);
    }
}
